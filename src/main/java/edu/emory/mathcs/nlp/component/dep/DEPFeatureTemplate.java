package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.FeatureItem;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPFeatureTemplate<N extends DEPNode> extends FeatureTemplate<N,DEPState<N>> {

    @Override
    protected String getFeature(FeatureItem<?> item)
    {
        return null;
    }

    @Override
    protected String[] getFeatures(FeatureItem<?> item)
    {
        return null;
    }
}
