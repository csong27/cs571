package edu.emory.mathcs.nlp.component.dep;

import edu.emory.mathcs.nlp.component.util.eval.Eval;

/**
 * Created by Song on 9/25/2015.
 */
public class DEPAccuracyEval implements Eval {
    private int las;    //how many nodes found both correct heads and labels
    private int uas;    //how many nodes found correct heads
    private int total;
    private boolean evalLas;

    public DEPAccuracyEval(boolean evalLas){
        this.evalLas = evalLas;
    }

    public void setEvalLas(boolean evalLas){
        this.evalLas = evalLas;
    }

    public void add(int las, int uas,int total)
    {
        this.las += las;
        this.uas += uas;
        this.total += total;
    }

    public void clear(){
        total = 0;
        las   = 0;
        uas   = 0;
    }

    public double score(){
        if(evalLas)
            return 100d * las / total;
        else
            return 100d * uas / total;
    }

    public String scores(){
        double _uas = 100d * uas / total;
        double _las = 100d * las / total;
        return String.format("UAS is : %5.2f, LAS is :%5.2f", _uas, _las);
    }
}
