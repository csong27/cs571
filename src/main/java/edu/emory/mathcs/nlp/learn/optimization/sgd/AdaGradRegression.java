/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.learn.optimization.sgd;

import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AdaGradRegression extends StochasticGradientDescent
{

	protected final double epsilon = 0.00001;
	protected WeightVector diagonals;

	public AdaGradRegression(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
		diagonals = weightVector.createEmptyVector();

	}

	@Override
	protected void updateBinomial(Instance instance)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateMultinomial(Instance instance)
	{
		int      i, size = weight_vector.labelSize();
		Vector   x = instance.getVector();
		double[] d = weight_vector.scores(x);
		double   g;

		for (i=0; i<size; i++)
		{
			updateDiagonals(i, x);
			g = instance.isLabel(i) ? 1 - d[i] : -d[i];
			d[i] = g;
		}

		for (IndexValuePair xi : x)
		{
			for (i=0; i<size; i++)
			{
				g = d[i] * xi.getValue() * getGradient(i, xi.getIndex());
				weight_vector.add(i, xi.getIndex(), g);
				if (isAveraged()) average_vector.add(i, xi.getIndex(), g * steps);
			}
		}
		
	}

	private void updateDiagonals(int y, Vector x)
	{
		for (IndexValuePair p : x)
			diagonals.add(y, p.getIndex(), MathUtils.sq(p.getValue()));
	}

	protected double getGradient(int y, int xi)
	{
		return learning_rate / (epsilon + Math.sqrt(diagonals.get(y, xi)));
	}

	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "AdaGrad regression: "+join.toString();
	}

}
