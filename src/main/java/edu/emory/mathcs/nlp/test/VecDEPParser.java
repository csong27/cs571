package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.component.dep.DEPNode;
import edu.emory.mathcs.nlp.component.dep.DEPState;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.DenseVector;
import edu.emory.mathcs.nlp.learn.vector.StringVector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Song on 9/29/2015.
 */
public class VecDEPParser<N extends DEPNode> extends VecNLPComponent<N, String, DEPState<N>> {
    public VecDEPParser(DenseModel model) {
        super(new DenseModel[]{model});
    }

    @Override
    protected DEPState<N> createState(N[] nodes) {
        return new DEPState<>(nodes);
    }

    @Override
    protected String getLabel(DEPState<N> state, DenseVector vector) {
        return isTrain() ? state.getGoldLabel() : models[0].predictBest(vector).getLabel();
    }

    @Override
    protected void addInstance(String label, DenseVector vector) {
        models[0].addInstance(new DenseInstance(label, vector));
    }

    protected DenseVector extractFeatures(DEPState<DEPNode> state) {
        feature_template.setState(state);
        return feature_template.extractDenseVector();
    }

    @Override
    public void process(N[] nodes) {
        DEPState<N> state = createState(nodes);
        if (!isDecode()) state.clearGoldLabels();
        DenseVector vector;
        String label;
        while (!state.isTerminate())
        {
            vector = extractFeatures(state);
            if (isTrainOrAggregate()) addInstance(state.getGoldLabel(), vector);
            label = getLabel(state, vector);
            state.setLabel(label);
            state.next();
        }

        if (isEvaluate()) state.evaluate(eval);
    }
}
