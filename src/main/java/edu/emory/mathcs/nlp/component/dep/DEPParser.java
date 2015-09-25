package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.StringVector;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPParser<N extends DEPNode> extends NLPComponent<N,String,DEPState<N>> {

    public DEPParser(StringModel model)
    {
        super(new StringModel[]{model});
    }

    @Override
    protected void readLexicons(ObjectInputStream in) throws IOException, ClassNotFoundException {}

    @Override
    protected void writeLexicons(ObjectOutputStream out) throws IOException {}

    @Override
    protected DEPState<N> createState(N[] nodes)
    {
        return new DEPState<>(nodes);
    }

    @Override
    protected String getLabel(DEPState<N> state, StringVector vector)
    {
        return isTrain() ? state.getGoldLabel() : models[0].predictBest(vector).getLabel();
    }

    @Override
    protected void addInstance(String label, StringVector vector)
    {
        models[0].addInstance(new StringInstance(label, vector));
    }

    @Override
    protected StringVector extractFeatures(DEPState<N> state)
    {
        feature_template.setState(state);
        return feature_template.extractFeatures();
    }

    @Override
    public void process(N[] nodes)
    {
        DEPState<N> state = createState(nodes);
        if (!isDecode()) state.clearGoldLabels();

        while (!state.isTerminate())
        {
            StringVector vector = extractFeatures(state);
            if (isTrainOrAggregate()) addInstance(state.getGoldLabel(), vector);
            String label = getLabel(state, vector);
            state.setLabel(label);
            state.next();
        }

        if (isEvaluate()) state.evaluate(eval);
    }

    public static void main(String[] args) throws Exception{
        TSVIndex<DEPNode> index = new DEPIndex(1, 2, 3, 4, 5, 6);
        TSVReader<DEPNode> reader = new TSVReader<>(index);
        reader.open(IOUtils.createFileInputStream("src/main/resources/dat/wsj_0001.dep"));
        DEPNode[] nodes = reader.next();
        DEPParser<DEPNode> parser = new DEPParser<>(new StringModel(new MultinomialWeightVector()));
        DEPFeatureTemplate<DEPNode> featureTemplate = new DEPFeatureTemplate<>();
        parser.setFeatureTemplate(featureTemplate);
        DEPState<DEPNode> state = new DEPState<>(nodes);
        StringVector vector;
        String label;
        while(!state.isTerminate()){
            label = state.getGoldMoveLabel();
            vector = parser.extractFeatures(state);
            StringInstance instance = new StringInstance(label, vector);
            System.out.println(instance);
            state.next();
        }
    }
}
