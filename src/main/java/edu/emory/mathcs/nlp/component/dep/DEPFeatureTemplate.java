package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.FeatureItem;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.feature.Field;
import edu.emory.mathcs.nlp.component.util.feature.Source;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPFeatureTemplate<N extends DEPNode> extends FeatureTemplate<N,DEPState<N>> {

    public DEPFeatureTemplate()
    {
        init();
    }

    protected void init()
    {
        // 1-gram features
        add(new FeatureItem<>(Source.i, Field.simplified_word_form));
        add(new FeatureItem<>(Source.j, Field.simplified_word_form));

        add(new FeatureItem<>(Source.i, Field.word_shape, 2));
        add(new FeatureItem<>(Source.j, Field.word_shape, 2));

        add(new FeatureItem<>(Source.i, Field.pos_tag));
        add(new FeatureItem<>(Source.j, Field.pos_tag));

        // 2-gram features
        add(new FeatureItem<>(Source.i, Field.uncapitalized_simplified_word_form), new FeatureItem<>(Source.j, Field.uncapitalized_simplified_word_form));
        add(new FeatureItem<>(Source.i, Field.pos_tag), new FeatureItem<>(Source.j, Field.pos_tag));
    }


    @Override
    protected String getFeature(FeatureItem<?> item)
    {
        N node = state.getNode(item.source);
        if (node == null) return null;
        switch (item.field)
        {
            case word_form: return node.getWordForm();
            case simplified_word_form: return node.getSimplifiedWordForm();
            case uncapitalized_simplified_word_form: return StringUtils.toLowerCase(node.getSimplifiedWordForm());
            case word_shape: return node.getWordShape((Integer)item.value);
            case lemma: return node.getLemma();
            case feats: return node.getFeat((String)item.value);
            case pos_tag: return node.getPOSTag();
//            case prefix: return getPrefix(node, (Integer)item.value);
//            case suffix: return getSuffix(node, (Integer)item.value);
            default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
        }
    }

    @Override
    protected String[] getFeatures(FeatureItem<?> item)
    {
        return null;
    }
}
