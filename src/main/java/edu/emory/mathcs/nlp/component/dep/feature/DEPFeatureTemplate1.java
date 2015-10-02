package edu.emory.mathcs.nlp.component.dep.feature;

import edu.emory.mathcs.nlp.component.dep.DEPFeatureTemplate;
import edu.emory.mathcs.nlp.component.util.feature.*;

/**
 * Created by Song on 9/29/2015.
 */
public class DEPFeatureTemplate1 extends DEPFeatureTemplate {
    protected void init(){
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
            add(new FeatureItem<>(Source.k,  2, Field.lemma));
            add(new FeatureItem<>(Source.k,  3, Field.lemma));

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
            add(new FeatureItem<>(Source.k,  3, Field.pos_tag));

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
            add(new FeatureItem<>(Source.i, Field.path));
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
            add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i,  Field.path));
            add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i,  Field.path));
            //S0wvr;S0pvr;S0wvl;S0pvl;N0wvl;N0pvl;
            add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.valency, Direction.right));
            add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.valency, Direction.right));
            add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.valency, Direction.left));
            add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.valency, Direction.left));
            add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.j, 0, Field.valency, Direction.left));
            add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.j, 0, Field.valency, Direction.left));
            //S0wd;S0pd;N0wd;N0pd;
            add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.path));
            add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.path));
            add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.path));
            add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.path));
            //S0wsr;S0psr;S0wsl;S0psl;N0wsl;N0psl;
//            add(new FeatureItem<>(Source.i,  0, Field.lemma),
//                    new FeatureItem<>(Source.i, Field.subcategorization, Direction.right));
//            add(new FeatureItem<>(Source.i,  0, Field.pos_tag),
//                    new FeatureItem<>(Source.i, Field.subcategorization, Direction.right));
//            add(new FeatureItem<>(Source.i,  0, Field.lemma),
//                    new FeatureItem<>(Source.i, Field.subcategorization, Direction.left));
//            add(new FeatureItem<>(Source.i,  0, Field.pos_tag),
//                    new FeatureItem<>(Source.i, Field.subcategorization, Direction.left));
//            add(new FeatureItem<>(Source.j,  0, Field.lemma),
//                    new FeatureItem<>(Source.j, Field.subcategorization, Direction.left));
//            add(new FeatureItem<>(Source.j,  0, Field.pos_tag),
//                    new FeatureItem<>(Source.j, Field.subcategorization, Direction.left));
//            //try this
//        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.length, true));
//        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.length, true));
//        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.length, false));
//        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.length, false));
//        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.length, true));
//        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.length, true));
//        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.length, false));
//        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.length, false));
//
//        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.num_of_operation, true));
//        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.num_of_operation, true));
//        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i, Field.num_of_operation, false));
//        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.i, Field.num_of_operation, false));
//        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.num_of_operation, true));
//        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.num_of_operation, true));
//        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.num_of_operation, false));
//        add(new FeatureItem<>(Source.j,  0, Field.pos_tag), new FeatureItem<>(Source.i, Field.num_of_operation, false));
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
}
