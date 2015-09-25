package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.XMLUtils;
import edu.emory.mathcs.nlp.component.util.config.NLPConfig;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.w3c.dom.Element;

import java.io.InputStream;

/**
 * Created by Song on 9/25/2015.
 */
public class DEPConfig extends NLPConfig<DEPNode> {

    public DEPConfig(InputStream in) {
        super(in);
    }

    @Override
    public TSVIndex<DEPNode> getTSVIndex() {
        Element eReader = XMLUtils.getFirstElementByTagName(xml, TSV);
        Object2IntMap<String> map = getFieldMap(eReader);

        int form  = map.get(FIELD_FORM);
        int pos   = map.get(FIELD_POS);
        int lemma = map.get(FIELD_LEMMA);
        int feats = map.get(FIELD_FEATS);
        int headID = map.get(FIELD_HEADID);
        int deprel = map.get(FIELD_DEPREL);

        return new DEPIndex(form, lemma, pos, feats, headID, deprel);
    }
}
