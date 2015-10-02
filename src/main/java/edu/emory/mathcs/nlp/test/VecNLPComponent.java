package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.component.dep.DEPState;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.state.NLPState;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.vector.DenseVector;
import edu.emory.mathcs.nlp.learn.vector.StringVector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Song on 9/29/2015.
 */
public abstract class VecNLPComponent<N,L,S extends NLPState<N,L>> {
    protected VecFeatureTemplate feature_template;
    protected DenseModel[] models;
    protected NLPFlag flag;
    protected Eval eval;

//	============================== CONSTRUCTORS ==============================

    public VecNLPComponent(DenseModel[] models)
    {
        setModels(models);
    }


//	============================== MODELS ==============================

    public DenseModel[] getModels()
    {
        return models;
    }

    public void setModels(DenseModel[] model)
    {
        this.models = model;
    }

//	============================== FEATURE ==============================

    public VecFeatureTemplate getFeatureTemplate()
    {
        return feature_template;
    }

    public void setFeatureTemplate(VecFeatureTemplate template)
    {
        feature_template = template;
    }

//	============================== FLAG ==============================

    public NLPFlag getFlag()
    {
        return flag;
    }

    public void setFlag(NLPFlag flag)
    {
        this.flag = flag;
    }

    public boolean isTrain()
    {
        return flag == NLPFlag.TRAIN;
    }

    public boolean isAggregate()
    {
        return flag == NLPFlag.AGGREGATE;
    }

    public boolean isTrainOrAggregate()
    {
        return isTrain() || isAggregate();
    }

    public boolean isDecode()
    {
        return flag == NLPFlag.DECODE;
    }

    public boolean isEvaluate()
    {
        return flag == NLPFlag.EVALUATE;
    }

//	============================== EVALUATOR ==============================

    public Eval getEval()
    {
        return eval;
    }

    public void setEval(Eval eval)
    {
        this.eval = eval;
    }

//	============================== PROCESS ==============================

    /** @return the processing state for the input nodes. */
    protected abstract S createState(N[] nodes);
    /** @return the gold-standard label for training; otherwise, the predicted label. */
    protected abstract L getLabel(S state, DenseVector vector);
    /** Adds a training instance (label, x) to the statistical model. */
    protected abstract void addInstance(L label, DenseVector vector);

    public void process(N[] nodes)
    {
        S state = createState(nodes);
        feature_template.setState((DEPState)state);
        if (!isDecode()) state.clearGoldLabels();

        while (!state.isTerminate())
        {
            DenseVector vector = extractFeatures(state);
            if (isTrainOrAggregate()) addInstance(state.getGoldLabel(), vector);
            L label = getLabel(state, vector);
            state.setLabel(label);
            state.next();
        }

        if (isEvaluate()) state.evaluate(eval);
    }

    /** @return the vector consisting of all features extracted from the state. */
    protected DenseVector extractFeatures(S state)
    {
        return feature_template.extractDenseVector();
    }
}
