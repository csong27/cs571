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
package edu.emory.mathcs.nlp.component.util.config;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.InputStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.emory.mathcs.nlp.common.util.Language;
import edu.emory.mathcs.nlp.common.util.XMLUtils;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.optimization.OnlineOptimizer;
import edu.emory.mathcs.nlp.learn.optimization.minibatch.AdaDeltaMiniBatch;
import edu.emory.mathcs.nlp.learn.optimization.minibatch.AdaGradMiniBatch;
import edu.emory.mathcs.nlp.learn.optimization.sgd.AdaGrad;
import edu.emory.mathcs.nlp.learn.optimization.sgd.Perceptron;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class NLPConfig<N> implements ConfigXML
{
	protected Element xml;
	
//	=================================== CONSTRUCTORS ===================================
	
	public NLPConfig() {}
	
	public NLPConfig(InputStream in)
	{
		xml = XMLUtils.getDocumentElement(in);
	}
	
//	=================================== GETTERS & SETTERS ===================================  
	
	public Language getLanguage()
	{
		String language = XMLUtils.getTextContentFromFirstElementByTagName(xml, LANGUAGE);
		return Language.getType(language);
	}
	
	public Double getST4SPDelta()
	{
		Element e = XMLUtils.getFirstElementByTagName(xml, ST4SP);
		return (e == null) ? null : Double.parseDouble(XMLUtils.getTrimmedAttribute(e, DELTA)); 
	}

	public TSVReader<N> getTSVReader()
	{
		return new TSVReader<>(getTSVIndex());
	}
	
	/** Called by {@link #getTSVReader()}. */
	protected Object2IntMap<String> getFieldMap(Element eTSV)
	{
		NodeList list = eTSV.getElementsByTagName(COLUMN);
		int i, index, size = list.getLength();
		Element element;
		String field;
		
		Object2IntMap<String> map = new Object2IntOpenHashMap<>();
		
		for (i=0; i<size; i++)
		{
			element = (Element)list.item(i);
			field   = XMLUtils.getTrimmedAttribute(element, FIELD);
			index   = XMLUtils.getIntegerAttribute(element, INDEX);
			
			map.put(field, index);
		}
		
		return map;
	}
	
	protected abstract TSVIndex<N> getTSVIndex();
	
//	=================================== TRAINER ===================================
	
	public OnlineOptimizer[] getLearners(StringModel[] models)
	{
		OnlineOptimizer[] trainers = new OnlineOptimizer[models.length];
		
		for (int i=0; i<models.length; i++)
			trainers[i] = getTrainer(models[i], i);
		
		return trainers;
	}
	
	private OnlineOptimizer getTrainer(StringModel model, int index)
	{
		Element  eTrainer = XMLUtils.getElementByTagName(xml, TRAINER, index);
		String  algorithm = XMLUtils.getTrimmedAttribute(eTrainer, ALGORITHM);
		initTrainer(eTrainer, model);
		
		switch (algorithm)
		{
		case PERCEPTRON         : return getPerceptron(eTrainer, model);
		case ADAGRAD            : return getAdaGrad   (eTrainer, model);
		case ADAGRAD_MINI_BATCH : return getAdaGradMiniBatch (eTrainer, model);
		case ADADELTA_MINI_BATCH: return getAdaDeltaMiniBatch(eTrainer, model);
		}
		
		throw new IllegalArgumentException(algorithm+" is not a valid algorithm name.");
	}
	
	private void initTrainer(Element eTrainer, StringModel model)
	{
		int labelCutoff   = XMLUtils.getIntegerAttribute(eTrainer, LABEL_CUTOFF);
		int featureCutoff = XMLUtils.getIntegerAttribute(eTrainer, FEATURE_CUTOFF);
		float bias        = XMLUtils.getFloatAttribute  (eTrainer, BIAS);
		boolean reset     = XMLUtils.getBooleanAttribute(eTrainer, RESET);
		
		model.setBias(bias);
		model.vectorize(labelCutoff, featureCutoff, reset);
	}
	
	private Perceptron getPerceptron(Element eTrainer, StringModel model)
	{
		boolean average      = XMLUtils.getBooleanAttribute(eTrainer, AVERAGE);
		double  learningRate = XMLUtils.getDoubleAttribute (eTrainer, LEARNING_RATE);
		
		return new Perceptron(model.getWeightVector(), average, learningRate);
	}
	
	private AdaGrad getAdaGrad(Element eTrainer, StringModel model)
	{
		boolean average      = XMLUtils.getBooleanAttribute(eTrainer, AVERAGE);
		double  learningRate = XMLUtils.getDoubleAttribute (eTrainer, LEARNING_RATE);
		
		return new AdaGrad(model.getWeightVector(), average, learningRate);
	}
	
	private AdaGradMiniBatch getAdaGradMiniBatch(Element eTrainer, StringModel model)
	{
		double  batchRatio   = XMLUtils.getDoubleAttribute (eTrainer, BATCH_RATIO);
		boolean average      = XMLUtils.getBooleanAttribute(eTrainer, AVERAGE);
		double  learningRate = XMLUtils.getDoubleAttribute (eTrainer, LEARNING_RATE);
		
		return new AdaGradMiniBatch(model.getWeightVector(), batchRatio, average, learningRate);
	}
	
	private AdaDeltaMiniBatch getAdaDeltaMiniBatch(Element eTrainer, StringModel model)
	{
		double  batchRatio   = XMLUtils.getDoubleAttribute (eTrainer, BATCH_RATIO);
		boolean average      = XMLUtils.getBooleanAttribute(eTrainer, AVERAGE);
		double  learningRate = XMLUtils.getDoubleAttribute (eTrainer, LEARNING_RATE);
		double  decayingRate = XMLUtils.getDoubleAttribute (eTrainer, DECAYING_RATE);

		return new AdaDeltaMiniBatch(model.getWeightVector(), batchRatio, average, learningRate, decayingRate);
	}
}
