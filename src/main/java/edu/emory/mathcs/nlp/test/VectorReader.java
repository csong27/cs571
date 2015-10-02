package edu.emory.mathcs.nlp.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Song on 9/29/2015.
 */
public class VectorReader {
    private Map<String, float[]> wordVecMap;

    public VectorReader(){
        wordVecMap = new HashMap<>();
        try{
            read();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, float[]> getMap(){
        return wordVecMap;
    }

    public void read() throws IOException{
        String train_path = "trainVector.txt";
        System.out.println("Loading embedded word vector...");
        try (BufferedReader br = new BufferedReader(new FileReader(train_path))) {
            String line;
            String[] arr;
            float[] vector;
            while ((line = br.readLine()) != null) {
                // process the line.
                arr = line.split(" ");
                vector = new float[arr.length - 1];
                for(int i = 1; i < arr.length; i++)
                    vector[i - 1] = Float.parseFloat(arr[i]);
                wordVecMap.put(arr[0], vector);
            }
        }
    }

    public float[] getVector(String word){
        return wordVecMap.get(word);
    }

    public static void main(String[] args) throws Exception{
        VectorReader vr = new VectorReader();
        System.out.println(Arrays.toString(vr.getVector("null")));
    }
}
