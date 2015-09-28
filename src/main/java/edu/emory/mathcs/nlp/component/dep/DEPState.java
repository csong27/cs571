package edu.emory.mathcs.nlp.component.dep;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.Source;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.state.NLPState;

import java.util.*;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPState<N extends DEPNode> extends NLPState<N, String> {
    static final String REDUCE = "REDUCE";
    static final String SHIFT = "SHIFT";
    static final String LEFTARC = "LEFTARC";
    static final String RIGHTARC = "RIGHTARC";
    static final String DELIM = ":";

    private DEPArc[] goldDepLabel;
    private Stack<N> nodeStack;
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
        //add root to stack to initialize
        nodeStack.push((N)DEPNode.ROOT);
        initGoldDepLabel();
    }


    public N getStack(int window) {
        return getNode(getStackWord().getID() - 1, window);
    }

    public N getInput(int window) {
        return getNode(inputIndex, window);
    }

    public N peekStack(int window)
    {
        if(window < nodeStack.size()){
            return nodeStack.elementAt(nodeStack.size() - window - 1);
        }
        return null;
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
        return move + DELIM + depLabel;
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

    public Set<String> getDynamicGoldMoveLabel(){
        DEPNode stackWord = getStackWord(),
                inputWord = getInputWord();
        //the ground truth
        DEPArc  stackWordHeadLabel = getHeadNodeByIndex(stackWord.getID() - 1),
                inputWordHeadLabel = getHeadNodeByIndex(inputWord.getID() - 1);
        Set<String> zeroCostLabels = new HashSet<>();
        if(stackWordHeadLabel != null && !isLeftArcCostly(inputWord, stackWord, stackWordHeadLabel.getNode()))
            zeroCostLabels.add(getMoveLabel(LEFTARC, stackWordHeadLabel.getLabel()));
        if(!isRightArcCostly(inputWord, stackWord, inputWordHeadLabel.getNode()))
            zeroCostLabels.add(getMoveLabel(RIGHTARC, inputWordHeadLabel.getLabel()));
        if(!isReduceCostly(stackWord))
            zeroCostLabels.add(getMoveLabel(REDUCE, null));
        if(!isShiftCostly(inputWord, inputWordHeadLabel.getNode()))
            zeroCostLabels.add(getMoveLabel(SHIFT, null));

        return zeroCostLabels;
    }

    private boolean isLeftArcCostly(DEPNode inputWord, DEPNode stackWord, DEPNode stackWordHead){
        if(inputWord.equals(stackWordHead)) return false;
        else{
            DEPNode node, nodeHead;
            for(int i = inputIndex + 1; i < inputSize; i++){
                node = nodes[i];
                nodeHead = goldDepLabel[i].getNode();
                if(node.equals(stackWordHead) || stackWord.equals(nodeHead))
                    return true;
            }
            return false;
        }
    }

    private boolean isRightArcCostly(DEPNode inputWord, DEPNode stackWord, DEPNode inputWordHead){
        if(stackWord.equals(inputWordHead)) return false;
        else{
            DEPNode nodeHead;
            for(DEPNode node: nodeStack){
                if(node != stackWord && node.getID() != 0){
                    nodeHead = goldDepLabel[node.getID() - 1].getNode();
                    if(node.equals(inputWordHead) || nodeHead.equals(inputWord)) return true;
                }
                else if(node.getID() == 0 && node.equals(inputWordHead)) return true;
            }
            DEPNode node;
            for(int i = inputIndex + 1; i < inputSize; i++){
                node = nodes[i];
                if(node.equals(inputWordHead)) return true;
            }
            return false;
        }
    }

    private boolean isReduceCostly(DEPNode stackWord){
        DEPNode nodeHead;
        for(int i = inputIndex; i < inputSize; i++){
            nodeHead = goldDepLabel[i].getNode();
            if(stackWord.equals(nodeHead)) return true;
        }
        return false;
    }

    private boolean isShiftCostly(DEPNode inputWord, DEPNode inputWordHead){
        DEPNode nodeHead;
        for(DEPNode node: nodeStack){
            if(node.getID() != 0) {
                nodeHead = goldDepLabel[node.getID() - 1].getNode();
                if (node.equals(inputWordHead) || nodeHead.equals(inputWord)) return true;
            }
            else{
                if (node.equals(inputWordHead)) return true;
            }
        }
        return false;
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

    public boolean isFirst(DEPNode node)
    {
        return nodes[0] == node;
    }

    public boolean isLast(DEPNode node)
    {
        return nodes[nodes.length-1] == node;
    }

    public static void main(String[] args){
        Stack<Integer> stack = new Stack<>();
    }


}
