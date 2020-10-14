/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProbabalisticRetrieval;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1
 */
public class VSMData {

    private SortedMap<String, int[]> cosDataSet;//0 index is df and 1 index is idf

    private FileReader fr;
    private List<String> fileNamesref;
    private int fileCount;
    private int N;

    public List<String> getFileNamesref() {
        return fileNamesref;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void CreateIndex() {

        fr = new FileReader();
        fr.ReadFile();

        cosDataSet = new TreeMap<String, int[]>();

        fileNamesref = fr.getFileNames();
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int) fr.getFileCount();

        N = fr.getNDocs();

        System.out.println("Total Files in directory : " + fileCount);

        for (String file : fileNamesref) {
            try {
                if (!file.equals("DocWeight.txt") && !file.equals("TermWeight.txt")) {
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));

                    for (int i = 0; i < lines.size(); i++) {
                        String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for (int j = 0; j < content.length; j++) {
                            AddEntry(cosDataSet, content[j].toLowerCase(), file);//Current words
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(VSMData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Completed Till TF");
    }

    public void AddEntry(SortedMap<String, int[]> info, String word, String fileName) {
        if (info.get(word) == null) {
            info.put(word, new int[N]);
        }

        info.get(word)[fileNamesref.indexOf(fileName)]++;
    }

    public String CalcDocSimilarity(Dictionary<String, Integer> query, Integer[] relventDocIndex_R) {
        int R = relventDocIndex_R.length;

        Dictionary<String, ProbData> weights = new Hashtable();

        Enumeration en = query.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();

            if (cosDataSet.get(key) != null) {
                ProbData data = new ProbData();

                int[] TF_i = cosDataSet.get(key);
                for (int i = 0; i < TF_i.length; i++) {
                    if (TF_i[i] > 0) {
                        data.n++;
                    }
                }

                for (int i = 0; i < relventDocIndex_R.length; i++) {
                    if (TF_i[relventDocIndex_R[i]] > 0) {
                        data.r++;
                    }
                }
                weights.put(key, data);
            }

        }

        //Term weights
        String dataTermWeight = "";

        en = weights.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();

            dataTermWeight += key + " => ";

            weights.get(key).CalcTermWeight(R, N);

            dataTermWeight += weights.get(key).w[0] + " , " + weights.get(key).w[1] + " , "
                    + weights.get(key).w[2] + " , " + weights.get(key).w[2] + "\n";

        }

        new FileWrite(Paths.get(fr.folderPath).resolve("TermWeight.txt").toString(), dataTermWeight);

        String dataDocWeight = "";
        Dictionary<Integer, Double[]> documentWeight = new Hashtable();
        //Calculate document weights
        for (int wi = 0; wi < 4; wi++) {//For weights w1 - w4

            dataDocWeight += "W" + wi + " => ";
            for (int i = 0; i < N; i++) {//Iterate with N documents
                en = query.keys();//get the queried term for each document
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    if (cosDataSet.get(key) != null) {
                        int[] tf = cosDataSet.get(key);
                        double w = weights.get(key).w[wi];

                        if (tf[i] > 0) {
                            if (documentWeight.get(i) == null) {
                                Double[] d = new Double[]{0d, 0d, 0d, 0d};
                                documentWeight.put(i, d);
                            }

                            documentWeight.get(i)[wi] += w;//D1_t1 with W1,W2.....
                        }
                    }
                }

                dataDocWeight += documentWeight.get(i)[wi] + " , ";
            }
            dataDocWeight += "\n";
        }

        new FileWrite(Paths.get(fr.folderPath).resolve("DocWeight.txt").toString(), dataDocWeight);

        List<Integer> orderofDocs = new ArrayList();

        en = documentWeight.keys();
        while (en.hasMoreElements()) {
            Integer key = (Integer) en.nextElement();

            if (orderofDocs.size() == 0) {
                orderofDocs.add(key);
            } else {
                //for(int i = 0; i < orderofDocs.size(); i++){
                boolean placing = true;
                int indexToPlace = 0;
                while (placing) {
                    Integer dockey = orderofDocs.get(indexToPlace);
                    if (documentWeight.get(key)[0] > documentWeight.get(dockey)[0]) {
                        orderofDocs.add(0, key);
                        placing = false;
                    } else if (indexToPlace == orderofDocs.size() - 1)//if checked element is last then add the key after it
                    {
                        orderofDocs.add(indexToPlace + 1, key);
                        placing = false;
                    } else {
                        indexToPlace++;
                    }
                }

            }
        }

        String soln = "";
        for (int i = 0; i < orderofDocs.size(); i++) {
            if (i != 0) {
                soln += " > ";
            }
            soln += fileNamesref.get(orderofDocs.get(i));
        }

        return soln;
    }
}
