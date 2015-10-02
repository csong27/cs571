package edu.emory.mathcs.nlp.deeplearning.activation;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface ActivationFunction
{
    /** Transforms all values in the array according to this activation function. */
    public void transform(double[] scores);
}