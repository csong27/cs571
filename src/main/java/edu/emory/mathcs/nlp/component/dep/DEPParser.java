package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.SparseVector;
import edu.emory.mathcs.nlp.learn.vector.StringVector;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by Song on 9/23/2015.
 */
public class DEPParser<N extends DEPNode> extends NLPComponent<N, String, DEPState<N>> {

    public DEPParser(StringModel model) {
        super(new StringModel[]{model});
    }

    @Override
    protected void readLexicons(ObjectInputStream in) throws IOException, ClassNotFoundException {
    }

    @Override
    protected void writeLexicons(ObjectOutputStream out) throws IOException {
    }

    @Override
    protected DEPState<N> createState(N[] nodes) {
        return new DEPState<>(nodes);
    }

    @Override
    protected String getLabel(DEPState<N> state, StringVector vector) {
        return isTrain() ? state.getGoldLabel() : models[0].predictBest(vector).getLabel();
    }

    @Override
    protected void addInstance(String label, StringVector vector) {
        models[0].addInstance(new StringInstance(label, vector));
    }

    @Override
    protected StringVector extractFeatures(DEPState<N> state) {
        feature_template.setState(state);
        return feature_template.extractFeatures();
    }

    @Override
    public void process(N[] nodes) {
        DEPState<N> state = createState(nodes);
        if (!isDecode()) state.clearGoldLabels();
        StringVector vector;
        String label;
        while (!state.isTerminate()) {
            vector = extractFeatures(state);
            if (isTrainOrAggregate()) addInstance(state.getGoldLabel(), vector);
            label = getLabel(state, vector);
//            if(isEvaluate())
//                System.out.println(label);
            state.setLabel(label);
            state.next2();
        }
        if (isEvaluate()) state.evaluate(eval);
    }
}
