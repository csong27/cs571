package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.state.NLPState;

import java.util.*;

/**
 * Created by Song on 9/23/2015.
 * TODO: change next() function, dynamic oracle, non-projective, word2vec
 *
 */
public class DEPState<N extends DEPNode> extends NLPState<N, String> {
    static final String REDUCE = "REDUCE";
    static final String SHIFT = "SHIFT";
    static final String LEFTARC = "LEFTARC";
    static final String RIGHTARC = "RIGHTARC";
    static final String LEFTPASS = "LEFTPASS";
    static final String RIGHTPASS = "RIGHTPASS";
    static final String PASS = "PASS";

    static final String DELIM = ":";
    private int numOfReduce = 0;
    private int numOfShift = 0;

    private DEPArc[] goldDepLabel;
    private Stack<N> nodeStack;
    private Stack<N> passStack;
    private int inputIndex;
    private int inputSize;
    //move and dependency label, combined as the training label
    private String move;
    private String depLabel;

    public DEPState(N[] nodes){
        super(nodes);
        inputIndex = 0;
        inputSize = nodes.length;
        nodeStack = new Stack<>();
        passStack = new Stack<>();
        //add root to stack to initialize
        nodeStack.push((N)DEPNode.ROOT);
    }

    public N getStack(int window) {
        if(nodeStack.size() == 0)   return null;
        return getNode(getStackWord().getID() - 1, window);
    }

    public N getInput(int window) {
        return getNode(inputIndex, window);
    }

    public N peekStack(int window) {
        if(window < nodeStack.size()){
            return nodeStack.elementAt(nodeStack.size() - window - 1);
        }
        return null;
    }

    public void clearGoldLabels(){
        goldDepLabel = Arrays.stream(nodes).map(n -> n.clearDependencies()).toArray(DEPArc[]::new);
    }

    /** Moves onto the next state */
    public void next(){
        if(move != null){
            DEPNode stackWord = getStackWord();
            DEPNode inputWord = getInputWord();
            if(move.equals(LEFTARC)){
                if(stackWord != DEPNode.ROOT && !inputWord.isDependentOf(stackWord)) {
                    stackWord.setHead(inputWord, depLabel);
                    reduce();
                }
                else shift();
            }
            else if(move.equals(RIGHTARC)){
                if(!stackWord.isDependentOf(inputWord))
                    inputWord.setHead(stackWord, depLabel);
                shift();
            }
            else if(move.equals(REDUCE)){
                if(nodeStack.size() == 1) shift();
                else reduce();
            }
            else shift();
        }
    }

    public void next2(){
        if(move != null){
            DEPNode stackWord = getStackWord();
            DEPNode inputWord = getInputWord();
            if(move.equals(LEFTARC)){
                if(stackWord != DEPNode.ROOT && !inputWord.isDescendantOf(stackWord)) {
                    stackWord.setHead(inputWord, depLabel);
                    reduce();
                }
                else shift();
            }
            else if(move.equals(LEFTPASS)){
                if(stackWord != DEPNode.ROOT && !inputWord.isDescendantOf(stackWord)) {
                    stackWord.setHead(inputWord, depLabel);
                    pass();
                }
                else
                    shift();
            }
            else if(move.equals(RIGHTARC)){
                if(!stackWord.isDescendantOf(inputWord))
                  inputWord.setHead(stackWord, depLabel);
                shift();
            }
            else if (move.equals(RIGHTPASS)){
                if(!stackWord.isDescendantOf(inputWord))
                    inputWord.setHead(stackWord, depLabel);
                if(nodeStack.size() == 1) shift();
                else pass();
            }
            else if(move.equals(REDUCE)){
                if(nodeStack.size() == 1) shift();
                else reduce();
            }
            else if(move.equals(SHIFT)) shift();
            else{
                if(nodeStack.size() == 1) shift();
                else pass();
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

    public String setLabel(String label){
        String[] moveLabel = label.split(DELIM);
        this.move = moveLabel[0];
        this.depLabel = moveLabel[1];
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
        //left arc
        if(stackWordHeadLabel != null && inputWord.equals(stackWordHeadLabel.getNode())) {
            if(reduceCondition(stackWord, inputWord, inputWordHeadLabel.getNode()))
                return getMoveLabel(LEFTARC, stackWordHeadLabel.getLabel());
            else
                return getMoveLabel(LEFTPASS, stackWordHeadLabel.getLabel());
        }
        //right arc
        else if(stackWord.equals(inputWordHeadLabel.getNode())) {
            if (shiftCondition(stackWord, inputWord, inputWordHeadLabel.getNode()))
                return getMoveLabel(RIGHTARC, inputWordHeadLabel.getLabel());
            else
                return getMoveLabel(RIGHTPASS, inputWordHeadLabel.getLabel());
        }
        //reduce
        else if(reduceCondition(stackWord, inputWord, inputWordHeadLabel.getNode()))
            return getMoveLabel(REDUCE, null);
        //shift
        else if (shiftCondition(stackWord, inputWord, inputWordHeadLabel.getNode()))
            return getMoveLabel(SHIFT, null);
        else
            return getMoveLabel(PASS, null);
    }

    public String getGoldMoveLabel2(){
        DEPNode stackWord = getStackWord(),
                inputWord = getInputWord();
        //the ground truth
        DEPArc  stackWordHeadLabel = getHeadNodeByIndex(stackWord.getID() - 1),
                inputWordHeadLabel = getHeadNodeByIndex(inputWord.getID() - 1);
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
        return move + DELIM + depLabel;
    }

    public String getMoveLabel(){
        return move + DELIM + depLabel;
    }

    private boolean reduceCondition(DEPNode stackWord, DEPNode inputWord, DEPNode inputHead){
        DEPNode node;
        DEPNode nodeHead;
        //if ∃k [k < i ∧ ∃ l[(k,l,j) ∈ A gold ∨(j,l,k) ∈ A gold]] then Reduce
        for(int i = 0; i < stackWord.getID() - 1; i++){
            node = nodes[i];
            nodeHead = getHeadNodeByIndex(i).getNode();
            if(node.equals(inputHead) || inputWord.equals(nodeHead))
                return true;
        }
        return false;
    }

    private boolean shiftCondition(DEPNode stackWord, DEPNode inputWord, DEPNode inputHead){
        //¬[∃k∈σ.(k!=i)∧((k←j)∨(k→j))]
//        if(inputHead.compareTo(stackWord) < 0)
//            return false;
        DEPNode nodeHead;
        for(DEPNode node: nodeStack){
            if(!node.equals(stackWord)){
                if(inputHead.equals(node))
                    return false;
                nodeHead = node.getID() > 0 ? getHeadNodeByIndex(node.getID() - 1).getNode(): null;
                if(nodeHead != null && nodeHead.equals(inputWord))
                    return false;
            }
        }
        return true;
    }

    private boolean reduceCondition2(DEPNode stackWord, DEPNode inputWord, DEPNode inputHead){
        if(!stackWord.hasHead())
            return false;
        for (int i=inputIndex+1; i<nodes.length; i++)
            if (goldDepLabel[i].isNode(stackWord))
                return false;
        return true;
    }

    public void evaluate(Eval eval) {
        evaluateTokens((DEPAccuracyEval) eval);
    }

    public void evaluateTokens(DEPAccuracyEval eval) {
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
        //( [σ|i],δ, [j|β],A)⇒( [σ|i|δ|j], [ ],β,A)
        if(!passStack.isEmpty())
            for(N node: passStack)
                nodeStack.push(node);
        passStack.clear();
        nodeStack.push(nodes[inputIndex]);
        inputIndex++;
        numOfShift++;
    }

    private void reduce(){
        nodeStack.pop();
        numOfReduce++;
    }

    private void pass(){
        //( [σ|i],δ, [j|β],A)⇒(σ, [i|δ], [j|β],A)
        passStack.push(nodeStack.pop());
    }

    public String getMove(){
        return move;
    }

    public N getStackWord(){
        return nodeStack.size() > 0 ? nodeStack.peek() : null;
    }

    public N getInputWord(){
        return nodes[inputIndex];
    }

    public boolean isFirst(DEPNode node)
    {
        return nodes[0] == node;
    }

    public boolean isLast(DEPNode node)
    {
        return nodes[nodes.length-1] == node;
    }

    public String getStackLength(){
        return nodeStack.size() + "";
    }

    public String getInputLength(){
        return inputSize - inputIndex + "";
    }

    public String getDistance(){
        return "" + (getStackWord().getID() - getInputWord().getID());
    }

    public String getLength(Boolean stack){
        return stack ? getStackLength(): getInputLength();
    }

    public String getNumOfReduce(){
        return numOfReduce + "";
    }

    public String getNumOfShift(){
        return numOfShift + "";
    }

    public String getNumOfOperation(Boolean reduce){
        return reduce? getNumOfReduce(): getNumOfShift();
    }

}