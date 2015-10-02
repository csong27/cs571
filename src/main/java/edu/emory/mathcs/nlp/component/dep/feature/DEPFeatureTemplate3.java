package edu.emory.mathcs.nlp.component.dep.feature;

import edu.emory.mathcs.nlp.component.dep.DEPFeatureTemplate;
import edu.emory.mathcs.nlp.component.util.feature.*;

/**
 * Created by Song on 10/2/2015.
 */
public class DEPFeatureTemplate3 extends DEPFeatureTemplate {
    public void init(){
        // lemma features
        add(new FeatureItem<>(Source.i, -1, Field.lemma));
        add(new FeatureItem<>(Source.i, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, 1, Field.lemma));

        add(new FeatureItem<>(Source.j, -2, Field.lemma));
        add(new FeatureItem<>(Source.j, -1, Field.lemma));
        add(new FeatureItem<>(Source.j, 0, Field.lemma));
        add(new FeatureItem<>(Source.j, 1, Field.lemma));
        add(new FeatureItem<>(Source.j, 2, Field.lemma));

        add(new FeatureItem<>(Source.k, 1, Field.lemma));
        add(new FeatureItem<>(Source.k, 2, Field.lemma));
        add(new FeatureItem<>(Source.k, 3, Field.lemma));

        // pos features
        add(new FeatureItem<>(Source.i, -2, Field.pos_tag));
        add(new FeatureItem<>(Source.i, -1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 2, Field.pos_tag));

        add(new FeatureItem<>(Source.j, -2, Field.pos_tag));
        add(new FeatureItem<>(Source.j, -1, Field.pos_tag));
        add(new FeatureItem<>(Source.j, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.j, 2, Field.pos_tag));

        add(new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 2, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 3, Field.pos_tag));

        // valency features
        add(new FeatureItem<>(Source.i, 0, Field.valency, Direction.all));
        add(new FeatureItem<>(Source.j, 0, Field.valency, Direction.all));

        // 2nd-order features
        add(new FeatureItem<>(Source.i, Relation.h, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.lemma));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.lemma));

        add(new FeatureItem<>(Source.i, Relation.h, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.pos_tag));

        add(new FeatureItem<>(Source.i, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lns, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lmd, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.rmd, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.j, Relation.lmd, 0, Field.dependency_label));

        // 3rd-order features
        add(new FeatureItem<>(Source.i, Relation.h2, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.lemma));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.lemma));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.lemma));

        add(new FeatureItem<>(Source.i, Relation.h2, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.pos_tag));

        add(new FeatureItem<>(Source.i, Relation.h, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lns2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.lmd2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.i, Relation.rmd2, 0, Field.dependency_label));
        add(new FeatureItem<>(Source.j, Relation.lmd2, 0, Field.dependency_label));

        // boolean features
        addSet(new FeatureItem<>(Source.i, 0, Field.binary));
        addSet(new FeatureItem<>(Source.j, 0, Field.binary));

        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.i,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  0, Field.lemma), new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  1, Field.lemma), new FeatureItem<>(Source.j,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.k,  1, Field.lemma), new FeatureItem<>(Source.k,  1, Field.pos_tag));
        add(new FeatureItem<>(Source.j,  2, Field.lemma), new FeatureItem<>(Source.j,  2, Field.pos_tag));
        add(new FeatureItem<>(Source.i,  0, Field.lemma), new FeatureItem<>(Source.j,  0, Field.lemma));
        add(new FeatureItem<>(Source.i,  0, Field.pos_tag), new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.j, 0, Field.lemma), new FeatureItem<>(Source.i, Field.path));
        add(new FeatureItem<>(Source.j, 0, Field.pos_tag), new FeatureItem<>(Source.i, Field.path));

        //s1.wt◦s2.wt;s1.wt◦s2.w;s1.wts2.t;s1.w◦s2.wt;s1.t◦s2.wt;s1.w◦s2.w;s1.t◦s2.t;s1.t◦b1.
        add(new FeatureItem<>(Source.i, 0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k, 1, Field.lemma), new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k, 1, Field.lemma));
        add(new FeatureItem<>(Source.i, 0, Field.lemma), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 0, Field.lemma),
                new FeatureItem<>(Source.k, 1, Field.lemma), new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k, 1, Field.lemma), new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.i, 0, Field.lemma), new FeatureItem<>(Source.k, 1, Field.lemma));
        add(new FeatureItem<>(Source.i, 0, Field.pos_tag), new FeatureItem<>(Source.k, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.j,  0, Field.lemma));
        //s2.t◦s1.t◦b1.t; s2.t◦s1.t◦lc1(s1).t; s2.t◦s1.t◦rc1(s1).t; s2.t◦s1.t◦lc1(s2).t; s2.t◦s1.t◦rc1(s2).t;
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.j,  0, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.rmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.i,  Relation.lmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k,  Relation.rmd, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.pos_tag),
                new FeatureItem<>(Source.k,  Relation.lmd, 1, Field.pos_tag));
        //s2.t◦s1.w◦rc1(s2).t; s2.t◦s1.w◦lc1(s1).t; s2.t◦s1.w◦b1.t
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.lemma),
                new FeatureItem<>(Source.k,  Relation.rmd, 1, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.lemma),
                new FeatureItem<>(Source.i,  Relation.lmd, 0, Field.pos_tag));
        add(new FeatureItem<>(Source.k, 1, Field.pos_tag), new FeatureItem<>(Source.i, 0, Field.lemma),
                new FeatureItem<>(Source.j, 0, Field.pos_tag));
    }
}
