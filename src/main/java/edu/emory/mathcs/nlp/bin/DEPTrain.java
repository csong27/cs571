package edu.emory.mathcs.nlp.bin;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.dep.*;
import edu.emory.mathcs.nlp.component.dep.feature.DEPFeatureTemplateExperiment;
import edu.emory.mathcs.nlp.component.dep.feature.DEPFeatureTemplateZhang;
import edu.emory.mathcs.nlp.component.dep.feature.DEPFeatureTemplateZhangAndStanford;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.config.NLPConfig;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.train.NLPTrain;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;
import edu.emory.mathcs.nlp.test.VecFeatureTemplate;

import java.util.List;

/**
 * Created by Song on 9/18/2015.
 */
public class DEPTrain extends NLPTrain<DEPNode,String,DEPState<DEPNode>> {

    public DEPTrain(String[] args)
    {
        super(args);
    }


    public void collect(TSVReader<DEPNode> reader, List<String> inputFiles,
                        NLPComponent<DEPNode,String,DEPState<DEPNode>> component, NLPConfig<DEPNode> configuration){
//        DEPParser<DEPNode> parser = (DEPParser<DEPNode>)component;
//        DEPConfig config = (DEPConfig) configuration;

        //Seems like unnecessary for dependency parsing
    }

    protected NLPConfig<DEPNode> createConfiguration(String filename){
        return new DEPConfig(IOUtils.createFileInputStream(filename));
    }

    protected FeatureTemplate<DEPNode,DEPState<DEPNode>> createFeatureTemplate(){
        return new DEPFeatureTemplateExperiment();
//        return new VecFeatureTemplate();
    }

    protected NLPComponent<DEPNode,String,DEPState<DEPNode>> createComponent(){
        return new DEPParser<>(new StringModel(new MultinomialWeightVector()));
    }

    protected Eval createEvaluator(){
        return new DEPAccuracyEval(true);
    }

    public static void main(String[] args){
        new DEPTrain(args).train();
    }

}
