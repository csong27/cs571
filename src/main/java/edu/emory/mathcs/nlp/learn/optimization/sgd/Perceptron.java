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

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Perceptron extends StochasticGradientDescent
{
	public Perceptron(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}
	
	@Override
	protected void updateBinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int yp = instance.getLabel();
		int yn = weight_vector.predictBest(x).getLabel();
		
		if (yp != yn)
		{
			double gradient = learning_rate * yp;
			weight_vector.update(x, yp, gradient);
			if (isAveraged()) average_vector.update(x, yp, gradient * steps);
		}
	}
	
	@Override
	protected void updateMultinomial(Instance instance)
	{
		Vector x = instance.getVector();
<<<<<<< HEAD:src/main/java/edu/emory/mathcs/nlp/learn/sgd/perceptron/MultinomialPerceptron.java
		int yp = instance.getLabel(), yn = weight_vector.predictBest(x).getLabel();
		double lambda = 0.5;

=======
		int yp = instance.getLabel();
		int yn = weight_vector.predictBest(x).getLabel();
		
>>>>>>> upstream/master:src/main/java/edu/emory/mathcs/nlp/learn/optimization/sgd/Perceptron.java
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

<<<<<<< HEAD:src/main/java/edu/emory/mathcs/nlp/learn/sgd/perceptron/MultinomialPerceptron.java
	private double getGradientL2(int label, int featureIndex, double lambda)
	{
		return learning_rate * (1 - lambda * weight_vector.get(label, featureIndex));
=======
	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "Perceptron: "+join.toString();
>>>>>>> upstream/master:src/main/java/edu/emory/mathcs/nlp/learn/optimization/sgd/Perceptron.java
	}
}
