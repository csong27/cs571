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
package edu.emory.mathcs.nlp.component.ner;

import edu.emory.mathcs.nlp.component.pos.POSNode;
import edu.emory.mathcs.nlp.component.util.node.FeatMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NERNode extends POSNode
{
    private static final long serialVersionUID = -2055782375001848433L;
    protected String named_entity_tag;

//	====================================== Constructors ======================================

    public NERNode(int id, String form, String lemma, String posTag, FeatMap feats)
    {
        super(id, form, lemma, posTag, feats);
    }

    public NERNode(int id, String form, String lemma, String posTag, FeatMap feats, String namedEntityTag)
    {
        super(id, form, lemma, posTag, feats);
    }

//	============================== POS TAG ==============================

    public String getNamedEntityTag()
    {
        return named_entity_tag;
    }

    /** @return the previous named entity tag. */
    public String setNamedEntityTag(String tag)
    {
        String t = named_entity_tag;
        named_entity_tag = tag;
        return t;
    }

    public boolean isNamedEntityTag(String tag)
    {
        return tag.equals(named_entity_tag);
    }

    public double getF1(NERNode[] gold, NERNode[] sys)
    {
        int len = gold.length;
        Map<String, Integer> tp_map = new HashMap<>();
        Map<String, Integer> fp_map = new HashMap<>();
        Map<String, Integer> fn_map = new HashMap<>();
        Set<String> label_set = new HashSet<>();
        String trueLabel, predLabel;

        for(int i = 0; i < len; i++){
            trueLabel = gold[i].getNamedEntityTag();
            predLabel = sys[i].getNamedEntityTag();
            label_set.add(trueLabel);
            label_set.add(predLabel);
            if(trueLabel.equals(predLabel)){    //true positive for true label
                if(tp_map.containsKey(trueLabel))   tp_map.put(trueLabel, tp_map.get(trueLabel) + 1);
                else    tp_map.put(trueLabel, 1);
            }
            else{
                //false negative for true label
                if(fn_map.containsKey(trueLabel))   fn_map.put(trueLabel, fn_map.get(trueLabel) + 1);
                else    fn_map.put(trueLabel, 1);
                //false positive for predicted label
                if(fp_map.containsKey(predLabel))   fp_map.put(predLabel, fp_map.get(predLabel) + 1);
                else    fp_map.put(predLabel, 1);
            }
        }
        double precision, recall, f1_score = 0;
        int tp, fn, fp;
        //sum up f1 score for each label
        for(String label: label_set){
            tp = tp_map.get(label);
            fn = fn_map.get(label);
            fp = fp_map.get(label);
            precision = tp / (tp + fp);
            recall = tp / (tp + fn);
            f1_score += 2 * (precision * recall) / (precision + recall);
        }
        //return averaged f1 score
        return f1_score / label_set.size();
    }
}