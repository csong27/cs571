package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.learn.vector.DenseVector;

/**
 * Created by Song on 9/29/2015.
 */
public class DenseInstance {
    private DenseVector vector;
    private String label;

    public DenseInstance(String label, DenseVector vector)
    {
        set(label, vector);
    }

    public String getLabel()
    {
        return label;
    }

    public DenseVector getVector()
    {
        return vector;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setVector(DenseVector vector)
    {
        this.vector = vector;
    }

    public void set(String label, DenseVector vector)
    {
        setLabel(label);
        setVector(vector);
    }

    public boolean isLabel(String label)
    {
        return label.equals(this.label);
    }

    @Override
    public String toString()
    {
        return label+" "+vector.toString();
    }
}
