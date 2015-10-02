package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.learn.model.LabelMap;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.util.StringPrediction;
import edu.emory.mathcs.nlp.learn.vector.DenseVector;
import edu.emory.mathcs.nlp.learn.vector.StringVector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by Song on 9/29/2015.
 */
public class DenseModel{
    private List<Instance>        instance_list;
    private LabelMap              label_map;
    private WeightVector          weight_vector;
    private Deque<DenseInstance> instance_deque;

    private float                 bias;

    public DenseModel(WeightVector vector)
    {
        label_map      = new LabelMap();
        weight_vector  = vector;
    }

    public float getBias()
    {
        return bias;
    }

    public void setBias(float bias)
    {
        this.bias = bias;
    }

    public void addInstance(DenseInstance instance)
    {
        label_map.add(instance.getLabel());
        instance_deque.add(instance);
    }
    public List<Instance> getInstanceList()
    {
        return instance_list;
    }

    public void vectorize(int labelCutoff, boolean reset)
    {
        instance_list = new ArrayList<>();
        DenseInstance instance = instance_deque.peek();
        int labelIndex;

        // filtering
        if (reset)
        {
            label_map  .initIndices();
        }

        label_map  .expand(labelCutoff);

        if (reset)	weight_vector.init  (label_map.size(), instance.getVector().size());
        else		weight_vector.expand(label_map.size(), instance.getVector().size());

        // vectorizing
        while (!instance_deque.isEmpty())
        {
            instance   = instance_deque.poll();
            labelIndex = label_map.indexOf(instance.getLabel());

            if (labelIndex >= 0)
                instance_list.add(new Instance(labelIndex, instance.getVector()));
        }

        instance_deque = new ArrayDeque<>();
    }

    public StringPrediction predictBest(DenseVector x)
    {
        Prediction p = weight_vector.predictBest(x);
        return new StringPrediction(label_map.getLabel(p.getLabel()), p.getScore());
    }
}
