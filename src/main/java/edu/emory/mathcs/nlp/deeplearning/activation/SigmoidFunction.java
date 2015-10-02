package edu.emory.mathcs.nlp.deeplearning.activation;

import edu.emory.mathcs.nlp.common.util.Sigmoid;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SigmoidFunction implements ActivationFunction
{
    private Sigmoid table;

    /** Calls {@link #SigmoidFunction(int, float, float)}, where size = 3500, floor = -6, ceiling = 6. */
    public SigmoidFunction()
    {
        table = new Sigmoid();
    }

    /**
     * @param size size of the sigmoid table (10,000 being the highest recommendation).
     * @param floor lower convergence.
     * @param ceiling upper convergence.
     */
    public SigmoidFunction(int size, float floor, float ceiling)
    {
        table = new Sigmoid(size, floor, ceiling);
    }

    public final double get(double d)
    {
        return table.get(d);
    }

    @Override
    public void transform(double[] scores)
    {
        for (int i=0; i<scores.length; i++)
            scores[i] = get(scores[i]);
    }
}