package edu.emory.mathcs.nlp.component.dep;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Song on 9/29/2015.
 */
public class DEPJunk {

    private String getAmbiguousLabel(String best, Set<String> zeroCostLabels){
        if(zeroCostLabels.contains(best))
            return best;
        else
            return randomElement(zeroCostLabels);
    }

    private String randomElement(Set<String> set){
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(String string : set) {
            if (i == item) return string;
            i = i + 1;
        }
        return null;
    }
//
//    private String getExploreLabel(Set<String> zeroCostMove, String best) {
//        if(Math.random() < 0.1)
//            return best;
//        else
//            return getAmbiguousLabel(best, zeroCostMove);
//    }
//
//    private boolean isLeftArcCostly(DEPNode inputWord, DEPNode stackWord, DEPNode stackWordHead){
//        if(inputWord.equals(stackWordHead)) return false;
//        else{
//            DEPNode node, nodeHead;
//            for(int i = inputIndex + 1; i < inputSize; i++){
//                node = nodes[i];
//                nodeHead = goldDepLabel[i].getNode();
//                if(node.equals(stackWordHead) || stackWord.equals(nodeHead))
//                    return true;
//            }
//            return false;
//        }
//    }
//
//    private boolean isRightArcCostly(DEPNode inputWord, DEPNode stackWord, DEPNode inputWordHead){
//        if(stackWord.equals(inputWordHead)) return false;
//        else{
//            DEPNode nodeHead;
//            for(DEPNode node: nodeStack){
//                if(node != stackWord && node.getID() != 0){
//                    nodeHead = goldDepLabel[node.getID() - 1].getNode();
//                    if(node.equals(inputWordHead) || nodeHead.equals(inputWord)) return true;
//                }
//                else if(node.getID() == 0 && node.equals(inputWordHead)) return true;
//            }
//            DEPNode node;
//            for(int i = inputIndex + 1; i < inputSize; i++){
//                node = nodes[i];
//                if(node.equals(inputWordHead)) return true;
//            }
//            return false;
//        }
//    }
//
//    private boolean isReduceCostly(DEPNode stackWord){
//        DEPNode nodeHead;
//        for(int i = inputIndex; i < inputSize; i++){
//            nodeHead = goldDepLabel[i].getNode();
//            if(stackWord.equals(nodeHead)) return true;
//        }
//        return false;
//    }
//
//    private boolean isShiftCostly(DEPNode inputWord, DEPNode inputWordHead){
//        DEPNode nodeHead;
//        for(DEPNode node: nodeStack){
//            if(node.getID() != 0) {
//                nodeHead = goldDepLabel[node.getID() - 1].getNode();
//                if (node.equals(inputWordHead) || nodeHead.equals(inputWord)) return true;
//            }
//            else{
//                if (node.equals(inputWordHead)) return true;
//            }
//        }
//        return false;
//    }
//
//
//    public Set<String> getZeroCostMove(){
//        DEPNode stackWord = getStackWord(),
//                inputWord = getInputWord();
//        //the ground truth
//        DEPArc  stackWordHeadLabel = getHeadNodeByIndex(stackWord.getID() - 1),
//                inputWordHeadLabel = getHeadNodeByIndex(inputWord.getID() - 1);
//        Set<String> zeroCostLabels = new HashSet<>();
//        if(stackWordHeadLabel != null && !isLeftArcCostly(inputWord, stackWord, stackWordHeadLabel.getNode()))
//            zeroCostLabels.add(getMoveLabel(LEFTARC, stackWordHeadLabel.getLabel()));
//        if(!isRightArcCostly(inputWord, stackWord, inputWordHeadLabel.getNode()))
//            zeroCostLabels.add(getMoveLabel(RIGHTARC, inputWordHeadLabel.getLabel()));
//        if(!isReduceCostly(stackWord))
//            zeroCostLabels.add(getMoveLabel(REDUCE, null));
//        if(!isShiftCostly(inputWord, inputWordHeadLabel.getNode()))
//            zeroCostLabels.add(getMoveLabel(SHIFT, null));
//
//        return zeroCostLabels;
//    }
}
