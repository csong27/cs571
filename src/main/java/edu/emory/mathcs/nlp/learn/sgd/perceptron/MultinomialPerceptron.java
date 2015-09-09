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
package edu.emory.mathcs.nlp.learn.sgd.perceptron;

import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultinomialPerceptron extends StochasticGradientDescent
{
	public MultinomialPerceptron(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}

	@Override
	protected void updateWeightVector(Instance instance, int steps)
	{
		Vector x = instance.getVector();
		int yp = instance.getLabel(), yn = weight_vector.predictBest(x).getLabel();
		double lambda = 0.5;

		if (yp != yn)
		{
//			weight_vector.update(x, yp,  learning_rate);
//			weight_vector.update(x, yn, -learning_rate);
			weight_vector.update(x, yp, (i,j) ->  getGradientL2(i, j, lambda));
			weight_vector.update(x, yn, (i,j) -> -getGradientL2(i,j, lambda));


			if (isAveraged())
			{
				average_vector.update(x, yp,  learning_rate * steps);
				average_vector.update(x, yn, -learning_rate * steps);
			}
		}
	}

	private double getGradientL2(int label, int featureIndex, double lambda)
	{
		return learning_rate * (1 - lambda * weight_vector.get(label, featureIndex));
	}
}
