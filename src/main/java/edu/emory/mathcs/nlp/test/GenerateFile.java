package edu.emory.mathcs.nlp.test;

import edu.emory.mathcs.nlp.common.util.FileUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.pos.POSIndex;
import edu.emory.mathcs.nlp.component.pos.POSNode;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Song on 9/29/2015.
 */
public class GenerateFile {

    private void read() throws IOException{
        String train_path = "wsj/dep/trn";
        String dev_path = "wsj/dep/dev";
        String ext = "*";
        List<String> trainFiles = FileUtils.getFileList(train_path, ext);
        TSVReader<POSNode> reader = new TSVReader<>(new POSIndex(1,3));
        iterate(reader, trainFiles, "both", "train2.txt");
    }

    private void iterate(TSVReader<POSNode> reader, List<String> filenames, String type, String outputFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (String filename : filenames)
        {
            reader.open(IOUtils.createFileInputStream(filename));
            POSNode[] nodes;

            while ((nodes = reader.next()) != null){
                StringJoiner join = new StringJoiner(" ");

                int i = 0;
                for(POSNode node: nodes){
                    switch (type){
                        case "lemma":   join.add(node.getWordForm()); break;
                        case "pos_tag": join.add(node.getPOSTag()); break;
                        case "both":    join.add(node.getWordForm() + "_" + node.getPOSTag());
                    }
                }
                writer.write(join.toString());
                writer.newLine();
            }
            reader.close();
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException{
        GenerateFile gf = new GenerateFile();
        gf.read();
    }
}
