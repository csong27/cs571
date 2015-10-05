package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.*;
import edu.emory.mathcs.nlp.test.VectorReader;

import java.util.Arrays;

/**
 * Created by Song on 9/23/2015.
 */
public abstract class DEPFeatureTemplate extends FeatureTemplate<DEPNode,DEPState<DEPNode>> {

    private VectorReader vectorReader;

    public DEPFeatureTemplate()
    {
        init();
        vectorReader = new VectorReader();
    }

    protected abstract void init();

    @Override
    protected String getFeature(FeatureItem<?> item) {
        DEPNode node = getNode(item);
        if (node == null) return null;
        switch (item.field)
        {
            case word_form: return node.getWordForm();
            case simplified_word_form: return node.getSimplifiedWordForm();
            case lemma: return node.getLemma();
            case length: return state.getLength((Boolean) item.value);
            case num_of_operation: return state.getNumOfOperation((Boolean) item.value);
            case pos_tag: return node.getPOSTag();
            case feats: return node.getFeat((String) item.value);
            case dependency_label: return node.getLabel();
            case valency: return node.getValency((Direction) item.value);
            case move_label: return state.getMoveLabel();
            case path:  return node.getPath(state.getInputWord(), Field.dependency_label);
            case distance: return getDistance(item.source, item.relation, (Boolean) item.value);
            case prefix: return getPrefix(node, (Integer) item.value);
            case suffix: return getSuffix(node, (Integer) item.value);
            case subcategorization: return node.getSubcategorization((Direction) item.value, Field.dependency_label);
            case subcategorization1: return node.getSubcategorization((Direction) item.value, Field.lemma);
            case subcategorization2: return node.getSubcategorization((Direction) item.value, Field.pos_tag);
            case norm: return getNorm(node);
            default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
        }
    }

    private String getNorm(DEPNode node) {
        float[] v1 = vectorReader.getVector(getKey(node));
        if(v1 == null) return null;
        else {
            double norm = euclideanDistance(v1, new float[v1.length]);
            return String.format("%5.1f", norm);
        }
    }


    protected String getPrefix(DEPNode node, int n) {
        String s = node.getSimplifiedWordForm();
        return (n < s.length()) ? StringUtils.toLowerCase(s.substring(0, n)) : null;
    }

    /** The suffix cannot be the entire word (e.g., getSuffix("abc", 3) -> null). */
    protected String getSuffix(DEPNode node, int n) {
        String s = node.getSimplifiedWordForm();
        return (n < s.length()) ? StringUtils.toLowerCase(s.substring(s.length()-n)) : null;
    }

    protected DEPNode getNode(FeatureItem<?> item) {
        DEPNode node = null;

        switch (item.source)
        {
            case i: node = state.getStack (item.window); break;
            case j: node = state.getInput (item.window); break;
            case k: node = state.peekStack(item.window); break;
        }

        return getNode(node, item);
    }

    protected DEPNode getNode(DEPNode node, FeatureItem<?> item) {
        if (node == null || item.relation == null)
            return node;

        switch (item.relation)
        {
            case h   : return node.getHead();
            case h2  : return node.getGrandHead();
            case lmd : return node.getLeftMostDependent();
            case lmd2: return node.getLeftMostDependent(1);
            case lnd : return node.getLeftNearestDependent();
            case lnd2: return node.getLeftNearestDependent(1);
            case lns : return node.getLeftNearestSibling();
            case lns2: return node.getLeftNearestSibling(1);
            case rmd : return node.getRightMostDependent();
            case rmd2: return node.getRightMostDependent(1);
            case rnd : return node.getRightNearestDependent();
            case rnd2: return node.getRightNearestDependent(1);
            case rns : return node.getRightNearestSibling();
            case rns2: return node.getRightNearestSibling(1);
        }

        return null;
    }

    @Override
    protected String[] getFeatures(FeatureItem<?> item) {
        DEPNode node = getNode(item);
        if (node == null) return null;

        switch (item.field)
        {
            case binary: return getBinaryFeatures(node);
            default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
        }
    }

    protected String[] getBinaryFeatures(DEPNode node) {
        String[] values = new String[2];
        int index = 0;

        if (state.isFirst(node)) values[index++] = "0";
        if (state.isLast (node)) values[index++] = "1";

        return (index == 0) ? null : (index == values.length) ? values : Arrays.copyOf(values, index);
    }

    protected String getDistance(Source source, Relation relation, boolean si){
        if(si) {
            DEPNode stack = state.getStackWord();
            DEPNode input = state.getInputWord();
            return getDistance(stack, input);
        }
        DEPNode node1, node2;
        switch(source){
            case i: node1 = state.getStackWord(); break;
            case j: node1 = state.getInputWord(); break;
            default: return null;
        }
        switch (relation){
            case h   : node2 = node1.getHead();break;
            case h2  : node2 = node1.getGrandHead();break;
            case lmd : node2 = node1.getLeftMostDependent();break;
            case lnd : node2 = node1.getLeftNearestDependent();break;
            case lns : node2 = node1.getLeftNearestSibling();break;
            case rmd : node2 = node1.getRightMostDependent();break;
            case rnd : node2 = node1.getRightNearestDependent();break;
            case rns : node2 = node1.getRightNearestSibling();break;
            case i1: node2 = state.getInput(1);break;
            case i2: node2 = state.getInput(2);break;
            case i3: node2 = state.getInput(3);break;
            case s1: node2 = state.getStack(1);break;
            case s2: node2 = state.getStack(2);break;
            case s3: node2 = state.getStack(3);break;
            case k1: node2 = state.peekStack(1);break;
            case k2: node2 = state.peekStack(2);break;
            case k3: node2 = state.peekStack(3);break;
            default: return null;
        }
        if(node2 != null)
            return getDistance(node1, node2);
        else
            return null;
    }

    protected String getDistance(DEPNode node1, DEPNode node2){
        float[] v1 = vectorReader.getVector(getKey(node1));
        float[] v2 = vectorReader.getVector(getKey(node2));
        if(v1 != null && v2 != null) {
//            System.out.println(cosineSimilarity(v1, v2));
            return String.format("%5.1f", cosineSimilarity(v1, v2));
        }
        else
            return null;
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private double euclideanDistance(float[] vectorA, float[] vectorB) {
        double sum = 0.0;
        for(int i = 0; i < vectorA.length; i++)
            sum = sum + Math.pow((vectorA[i] - vectorB[i]), 2.0);
        return Math.sqrt(sum);
    }

    protected String getKey(DEPNode node){
        return node.getWordForm();// + "_" + node.getPOSTag();
    }
}
