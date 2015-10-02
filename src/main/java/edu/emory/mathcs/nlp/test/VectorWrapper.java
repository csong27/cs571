package edu.emory.mathcs.nlp.test;

import org.apache.commons.math3.ml.clustering.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Song on 10/1/2015.
 */
public class VectorWrapper implements Clusterable {
    private double[] points;

    public VectorWrapper(float[] points) {
        this.points = IntStream.range(0, points.length).mapToDouble(i -> points[i]).toArray();
    }

    public double[] getPoint() {
        return points;
    }

    public static void main(String[] args){
        VectorReader vr = new VectorReader();
        Map<String, Integer> indexMap = new HashMap<>();
        List<VectorWrapper> vectors = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, float[]> entry : vr.getMap().entrySet()) {
            vectors.add(new VectorWrapper(entry.getValue()));
            indexMap.put(entry.getKey(), i++);
        }
//        DBSCANClusterer<VectorWrapper> clusterer = new DBSCANClusterer<>(0.1, 3);
//        List<Cluster<VectorWrapper>> clusterResults = clusterer.cluster(vectors);

        KMeansPlusPlusClusterer<VectorWrapper> clusterer = new KMeansPlusPlusClusterer<>(10, 10000);
        MultiKMeansPlusPlusClusterer<VectorWrapper> multiClusterer =
                new MultiKMeansPlusPlusClusterer<>(clusterer, 10);
        List<CentroidCluster<VectorWrapper>> clusterResults = multiClusterer.cluster(vectors);
        // output the clusters
        for (i=0; i<clusterResults.size(); i++) {
            System.out.println("Cluster " + i);
            System.out.println(clusterResults.get(i).getPoints().size());
        }
    }
}
