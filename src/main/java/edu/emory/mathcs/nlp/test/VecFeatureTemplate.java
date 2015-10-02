package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.component.dep.DEPNode;
import edu.emory.mathcs.nlp.component.dep.DEPState;
import edu.emory.mathcs.nlp.component.util.feature.FeatureItem;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.feature.Field;
import edu.emory.mathcs.nlp.component.util.feature.Source;
import edu.emory.mathcs.nlp.learn.vector.DenseVector;
import edu.emory.mathcs.nlp.learn.vector.StringVector;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Song on 9/29/2015.
 */
public class VecFeatureTemplate extends FeatureTemplate<DEPNode, DEPState<DEPNode>>{

    static final int vector_size = 100;
    private VectorReader vectorReader;

    public VecFeatureTemplate() {
        init();
        vectorReader = new VectorReader();
    }

    protected void init(){
        add(new FeatureItem<>(Source.i, Field.lemma));
        add(new FeatureItem<>(Source.j, Field.lemma));
    }

    protected String getFeature(FeatureItem<?> item){
        return null;
    }

    protected String[] getFeatures(FeatureItem<?> item){
        return null;
    }

    protected float[] getVector(FeatureItem<?> item){
        DEPNode node = getNode(item);
        if(node == null){
            return new float[vector_size];
        }
        else{
            float[] vector = vectorReader.getVector(node.getLemma());
            return vector != null? vector: new float[vector_size];
        }
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

    private float[] getFeature(FeatureItem<?>... items) {
        return getVector(items[0]);
    }

//    @Override
    public StringVector extractFeatures()
    {
        StringVector x = new StringVector();
        int i, j, type = 0;
        float[] vector;

        for (i=0; i<feature_list.size(); i++,type++) {
            vector = getFeature(feature_list.get(i));
            for(j=0; j < vector.length; j++)
                x.add(type, j+"", vector[j]);
        }
        return x;
    }

    public DenseVector extractDenseVector()
    {
        DenseVector x = new DenseVector(1);

        return x;
    }
}
