package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.*;

import java.util.Arrays;

/**
 * Created by Song on 9/23/2015.
 */
public class DEPFeatureTemplate<N extends DEPNode> extends FeatureTemplate<N,DEPState<N>> {

    public DEPFeatureTemplate()
    {
        init();
    }

    protected void init() {
        // lemma features
        add(new FeatureItem<>(Source.i, -1, Field.lemma));
        add(new FeatureItem<>(Source.i,  0, Field.lemma));
        add(new FeatureItem<>(Source.i,  1, Field.lemma));

        add(new FeatureItem<>(Source.j, -2, Field.lemma));
        add(new FeatureItem<>(Source.j, -1, Field.lemma));
        add(new FeatureItem<>(Source.j,  0, Field.lemma));
        add(new FeatureItem<>(Source.j,  1, Field.lemma));
        add(new FeatureItem<>(Source.j,  2, Field.lemma));

        add(new FeatureItem<>(Source.k,  1, Field.lemma));

        // pos features
        add(new FeatureItem<>(Source.i, -2, Field.pos_tag));
        add(new FeatureItem<>(Source.i, -1, Field.pos_tag));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.i,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.i,  2, Field.pos_tag));

        add(new FeatureItem<>(Source.j, -2, Field.pos_tag));
        add(new FeatureItem<>(Source.j, -1, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  2, Field.pos_tag));

        add(new FeatureItem<>(Source.k,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.k,  2, Field.pos_tag));

        // valency features
        add(new FeatureItem<>(Source.i, 0, Field.valency, Direction.all));
        add(new FeatureItem<>(Source.j, 0, Field.valency, Direction.all));

        // 2nd-order features
        add(new FeatureItem<>(Source.i, Relation.h  , 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.lemma));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.lemma));

        add(new FeatureItem<>(Source.i, Relation.h  , 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.pos_tag));

        add(new FeatureItem<>(Source.i,               0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lns, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.dependency_label));

        // 3rd-order features
        add(new FeatureItem<>(Source.i, Relation.h2  , 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.lemma));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.lemma));

        add(new FeatureItem<>(Source.i, Relation.h2  , 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.pos_tag));

        add(new FeatureItem<>(Source.i, Relation.h   , 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lns2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.dependency_label));

        // boolean features
        addSet(new FeatureItem<>(Source.i, 0, Field.binary));
        addSet(new FeatureItem<>(Source.j, 0, Field.binary));

        /*
            more features,
            reference: Proceedings of the 49th Annual Meeting of the Association for Computational Linguistics:
            shortpapers, pages 188–193,Portland, Oregon, June 19-24, 2011.c©2011
            Transition-based Dependency Parsing with Rich Non-local Features
         */
        //distance features
        add(new FeatureItem<>(Source.i, Field.distance));
        //sub categorization
        add(new FeatureItem<>(Source.i, Field.subcategorization, Direction.all));
        add(new FeatureItem<>(Source.j, Field.subcategorization, Direction.all));
        /*bigram features*/
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  1, Field.lemma), new FeatureItem<>(Source.j,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  2, Field.lemma), new FeatureItem<>(Source.j,  2, Field.pos_tag));
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.j,  0, Field.lemma));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i,  Field.distance));
        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i,  Field.distance));
        //S0wvr;S0pvr;S0wvl;S0pvl;N0wvl;N0pvl;
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.valency, Direction.right));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.valency, Direction.right));
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.valency, Direction.left));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.valency, Direction.left));
        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.j, 0, Field.valency, Direction.left));
        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.j, 0, Field.valency, Direction.left));
        //S0wd;S0pd;N0wd;N0pd;
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.distance));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.distance));
        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.distance));
        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.distance));
        //S0wsr;S0psr;S0wsl;S0psl;N0wsl;N0psl;
        add(new FeatureItem<>(Source.i,  0, Field.lemma),
                new FeatureItem<>(Source.i, Field.subcategorization, Direction.right));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i, Field.subcategorization, Direction.right));
        add(new FeatureItem<>(Source.i,  0, Field.lemma),
                new FeatureItem<>(Source.i, Field.subcategorization, Direction.left));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i, Field.subcategorization, Direction.left));
        add(new FeatureItem<>(Source.j,  0, Field.lemma),
                new FeatureItem<>(Source.j, Field.subcategorization, Direction.left));
        add(new FeatureItem<>(Source.j,  0, Field.pos_tag),
                new FeatureItem<>(Source.j, Field.subcategorization, Direction.left));
        /*trigram features*/
        //S0wN0wp;S0wpN0p;S0pN0wp
        add(    new FeatureItem<>(Source.i,  0, Field.lemma),
                new FeatureItem<>(Source.j,  0, Field.lemma),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.lemma),
                new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.lemma),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));
        //N0pN1pN2p;S0pN0pN1p;S0hpS0pN0p;S0pS0lpN0p;S0pS0rpN0p;S0pN0pN0lp
        add(    new FeatureItem<>(Source.j,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  1, Field.pos_tag),
                new FeatureItem<>(Source.j,  2, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  1, Field.pos_tag));

        add(    new FeatureItem<>(Source.i, Relation.h, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.lmd, 0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.rmd, 0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  Relation.lmd, 0, Field.pos_tag));
        //S0pS0lpS0l2p;S0pS0rpS0r2p;S0pS0hpS0h2p;N0pN0lpN0l2p;
        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.lmd, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.lmd2, 0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.rmd, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.rmd2, 0, Field.pos_tag));

        add(    new FeatureItem<>(Source.i,  0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.h, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.h2, 0, Field.pos_tag));

        add(    new FeatureItem<>(Source.j,  0, Field.pos_tag),
                new FeatureItem<>(Source.j,  Relation.lmd, 0, Field.pos_tag),
                new FeatureItem<>(Source.j,  Relation.lmd2, 0, Field.pos_tag));


    }


    @Override
    protected String getFeature(FeatureItem<?> item) {
        DEPNode node = getNode(item);
        if (node == null) return null;
        switch (item.field)
        {
            case word_form: return node.getWordForm();
            case simplified_word_form: return node.getSimplifiedWordForm();
            case lemma: return node.getLemma();
            case pos_tag: return node.getPOSTag();
            case feats: return node.getFeat((String) item.value);
            case dependency_label: return node.getLabel();
            case valency: return node.getValency((Direction) item.value);
            case distance: return node.getPath(state.getInputWord(), Field.distance);
            case subcategorization: return node.getSubcategorization((Direction) item.value, Field.dependency_label);
            default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
        }
    }
    protected DEPNode getNode(FeatureItem<?> item) {
        DEPNode node = null;

        switch (item.source)
        {
            case i: node = state.getStack (item.window); break;
            case j: node = state.getInput (item.window); break;
            case k: node = state.peekStack(item.window); break;
        }

        return getNode(node, item);
    }

    protected DEPNode getNode(DEPNode node, FeatureItem<?> item) {
        if (node == null || item.relation == null)
            return node;

        switch (item.relation)
        {
            case h   : return node.getHead();
            case h2  : return node.getGrandHead();
            case lmd : return node.getLeftMostDependent();
            case lmd2: return node.getLeftMostDependent(1);
            case lnd : return node.getLeftNearestDependent();
            case lnd2: return node.getLeftNearestDependent(1);
            case lns : return node.getLeftNearestSibling();
            case lns2: return node.getLeftNearestSibling(1);
            case rmd : return node.getRightMostDependent();
            case rmd2: return node.getRightMostDependent(1);
            case rnd : return node.getRightNearestDependent();
            case rnd2: return node.getRightNearestDependent(1);
            case rns : return node.getRightNearestSibling();
            case rns2: return node.getRightNearestSibling(1);
        }

        return null;
    }
    @Override
    protected String[] getFeatures(FeatureItem<?> item) {
        DEPNode node = getNode(item);
        if (node == null) return null;

        switch (item.field)
        {
            case binary: return getBinaryFeatures(node);
            default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
        }
    }

    protected String[] getBinaryFeatures(DEPNode node) {
        String[] values = new String[2];
        int index = 0;

        if (state.isFirst(node)) values[index++] = "0";
        if (state.isLast (node)) values[index++] = "1";

        return (index == 0) ? null : (index == values.length) ? values : Arrays.copyOf(values, index);
    }

}
