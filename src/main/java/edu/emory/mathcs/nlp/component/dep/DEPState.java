package edu.emory.mathcs.nlp.component.dep;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Joiner;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.Source;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.state.NLPState;
import java.util.Stack;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPState<N extends DEPNode> extends NLPState<N, String> {
    static final String REDUCE = "REDUCE";
    static final String SHIFT = "SHIFT";
    static final String LEFTARC = "LEFTARC";
    static final String RIGHTARC = "RIGHTARC";

    private DEPArc[] goldDepLabel;
    private Stack<N> nodeStack;
    private int inputIndex = 0, inputSize;
    //move and dependency label, combined as the training label
    private String move;
    private String depLabel;
    private String label;

    public DEPState(N[] nodes){
        super(nodes);
        inputSize = nodes.length;
        nodeStack = new Stack<>();
        //add root to stack to initialize
        nodeStack.push((N)DEPNode.ROOT);
        initGoldDepLabel();
    }


    public N getNode(Source s){
        switch (s){
            case i: return getStackWord();
            case j: return getInputWord();
            default: return null;
        }
    }

    private void initGoldDepLabel(){
        goldDepLabel = new DEPArc[inputSize];
        DEPNode node, head;
        for(int i = 0; i < inputSize; i++){
            node = nodes[i];
            head = node.getHead();
            if(head != null)
                goldDepLabel[i] = new DEPArc(head, node.getLabel());
            else
                goldDepLabel[i] = new DEPArc(DEPNode.ROOT, "root");
            //should clear dependencies before training
            node.clearDependencies();
        }

    }

    public void clearGoldLabels(){}

    /** Moves onto the next state */
    public void next(){
        if(move != null){
            DEPNode stackWord = getStackWord();
            DEPNode inputWord = getInputWord();
            switch (move){
                case LEFTARC:   stackWord.setHead(inputWord, depLabel); reduce(); break;
                case RIGHTARC:  inputWord.setHead(stackWord, depLabel); shift(); break;
                case REDUCE:    reduce(); break;
                case SHIFT:     shift(); break;
                default: break;
            }
        }
    }

    /** @return true if no more state can be processed; otherwise, false. */
    public boolean isTerminate(){
        return inputIndex >= inputSize;
    }

    // should return head arc in the gold tree, i.e. not the oracle function
    public String getGoldLabel(){
        return getGoldMoveLabel();
    }

    // Not sure if this is useful
    public String setLabel(String label){
        this.label = label;
        return label;
    }

    public DEPArc getHeadNodeByIndex(int index){
        return index >= 0 ? goldDepLabel[index] : null;
    }
    // the oracle function
    public String getGoldMoveLabel(){
        DEPNode stackWord = getStackWord(),
                inputWord = getInputWord();
        //the ground truth
        DEPArc  stackWordHeadLabel = getHeadNodeByIndex(stackWord.getID() - 1),
                inputWordHeadLabel = getHeadNodeByIndex(inputWord.getID() - 1);
        /*
            if (j,l,i) ∈ Agold then LeftArc i.e. stack is dependent of input, input(j) is head
            else if (i,l,j) ∈ Agold then RightArc i.e. stack(i) is head of input, input is dependent
            else if ∃k [k < i ∧ ∃ l[(k,l,j) ∈ Agold ∨(j,l,k) ∈ Agold]] then Reduce
            else Shift
        */
        //left arc
        if(stackWordHeadLabel != null && inputWord.equals(stackWordHeadLabel.getNode()))
            return getMoveLabel(LEFTARC, stackWordHeadLabel.getLabel());
        //right arc
        else if(stackWord.equals(inputWordHeadLabel.getNode()))
            return getMoveLabel(RIGHTARC, inputWordHeadLabel.getLabel());
        //reduce
        else if(reduceCondition(stackWord, inputWord, inputWordHeadLabel.getNode()))
            return getMoveLabel(REDUCE, null);
        //shift
        else
            return getMoveLabel(SHIFT, null);
    }

    private String getMoveLabel(String move, String depLabel){
        this.move = move;   //set move for this state
        this.depLabel = depLabel;   //set dependency label for this state
        return move + ":" + depLabel;
    }

    private boolean reduceCondition(DEPNode stackWord, DEPNode inputWord, DEPNode inputHead){
        //if ∃k [k < i ∧ ∃ l[(k,l,j) ∈ Agold ∨(j,l,k) ∈ Agold]] then Reduce
        DEPNode node;
        DEPNode nodeHead;
        for(int i = 0; i < stackWord.getID() - 1; i++){
            node = nodes[i];
            nodeHead = getHeadNodeByIndex(i).getNode();
            if(node.equals(inputHead) || inputWord.equals(nodeHead))
                return true;
        }
        return false;
    }


    public void evaluate(Eval eval){
        evaluateTokens((DEPAccuracyEval)eval);
    }

    public void evaluateTokens(DEPAccuracyEval eval)
    {
        int las = 0;
        int uas = 0;
        DEPNode node;
        DEPArc arc;
        for(int i = 0; i < inputSize; i++){
            node = nodes[i];
            arc = goldDepLabel[i];
            if(arc.isNode(node.getHead()))
                uas++;
            if(arc.equals(node.getHead(), node.getLabel()))
                las++;
        }
        eval.add(las, uas, inputSize);
    }

    private void shift(){
        nodeStack.push(nodes[inputIndex]);
        inputIndex++;
    }

    private void reduce(){
        nodeStack.pop();
    }

    public String getMove(){
        return move;
    }

    public N getStackWord(){
        return nodeStack.peek();
    }

    public N getInputWord(){
        return nodes[inputIndex];
    }

    //test oracle
    public static void main(String[] args) throws Exception{
        TSVIndex<DEPNode> index = new DEPIndex(1, 2, 3, 4, 5, 6);
        TSVReader<DEPNode> reader = new TSVReader<>(index);
        Eval eval = new DEPAccuracyEval(true);
        reader.open(IOUtils.createFileInputStream("src/main/resources/dat/wsj_0001.dep"));
        DEPNode[] nodes = reader.next();
        DEPState<DEPNode> state = new DEPState<>(nodes);
        while(!state.isTerminate()){
            state.getGoldMoveLabel();
            state.next();
        }
        state.evaluate(eval);
        System.out.println(eval.score());

    }

}
