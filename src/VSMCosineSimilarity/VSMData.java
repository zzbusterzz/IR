/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VSMCosineSimilarity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1
 */
public class VSMData {
   
    //private Dictionary<String,Integer[]> tfAndidf;
    private SortedMap<String,CosineData> cosDataSet;//0 index is df and 1 index is idf
    
    private  FileReader fr;
    private  List<String> fileNamesref;
    private int fileCount;
    private int d;
    public List<String> getFileNamesref() {
        return fileNamesref;
    }

    public int getFileCount() {
        return fileCount;
    }
    
    public void CreateIndex(){
        
        fr = new FileReader();
        fr.ReadFile();
        
        cosDataSet = new TreeMap<String, CosineData>();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        d = fr.getFileCountForD();
        
        System.out.println("Total Files in directory : " + fileCount);
        
        for (String file : fileNamesref) {  
            try {
                if(!file.equals("TermFreq.txt") && !file.equals("Weights.txt") && !file.equals("Normalised.txt") ){
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));
                    
                    for(int i =0; i < lines.size(); i++){
                        String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            AddEntry(cosDataSet, content[j].toLowerCase(), file);//Current words
                        }
                    }
                } 
            } catch (IOException ex) {
                  Logger.getLogger(VSMData.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        //File Write
        //String data = "";
        
       
        
        double[] denom_D = new double[d];
        
        Iterator itr = cosDataSet.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData rowValues = (CosineData)m.getValue();
            rowValues.CalctfWeights();
            
            for(int i = 0; i < d; i++){
                double w = rowValues.returnWeightAtIndex(i);
                denom_D[i] += w * w;
            }
        }
        
         for(int i = 0; i < d; i++)
            denom_D[i] = Math.sqrt( denom_D[i]);
        
        itr = cosDataSet.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData rowValues = (CosineData)m.getValue();
            rowValues.NormaliseLength(denom_D);
        }
         
        System.out.println("Completed Till Normalisation");
        
      //  data = PrintNormalised();
        
     //   new FileWrite(folderPath.resolve("Normalised.txt").toString(),data ); 
    }
    
    public void AddEntry( SortedMap<String,CosineData> info ,String word, String fileName){
        if(info.get(word) == null){
            info.put(word, new CosineData(d) ); 
        } 
        info.get(word).AddTF( fileNamesref.indexOf(fileName) );
    }
    
//    String PrintNormalised(){
//        String data = "";
//        
//        String column1Format = "%-15.15s";
//        String alternateColFormat = "%-10.20s";
//        System.out.println("-----------------------------------------------------------------------------");
//        System.out.printf(column1Format, "WORD");
//        System.out.printf(alternateColFormat, "DF");
//        System.out.printf(alternateColFormat, "IDF");
//        System.out.println();
//        System.out.println("-----------------------------------------------------------------------------");
//        
//        String key = "";
//        
//        Iterator itr = cosDataSet.entrySet().iterator();
//     
//        while(itr.hasNext()){
//            
//            Map.Entry m  = (Map.Entry)itr.next();
//            key = (String)m.getKey();
//            System.out.format(column1Format, key + ":  ");
//            
//            data += key + " => ";
//            
//            CosineData rowValues = (CosineData)m.getValue();
//            
//         //   System.out.printf(alternateColFormat, rowValues.df);
//         //   System.out.printf(alternateColFormat, rowValues.idf);
//          //  data += rowValues.df + " " + rowValues.idf;
//            
//            data += "\n";
//            System.out.println("");
//        }
//        System.out.println("-----------------------------------------------------------------------------");
//        
//        return data;
//    }
    
    public String CalcCosineSimilarity(Dictionary<String, Integer> query){
        Iterator itr = cosDataSet.entrySet().iterator();
        
        String tf = "";
        String weights = "";
        String normalised = "";
        
        double denom_Q = 0;
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
             
            if( query.get(key) != null){
                int count = query.get(key);
                cdata.AddTFAtQ(count);
            }else
                cdata.AddTFAtQ(0);
            
            cdata.CalctfWeightsofQ();
            
            double w = cdata.returnWeightAtIndex(d);
            denom_Q += w*w;
           
            
            tf += key + "=>";
            weights += key + "=>";
            for(int i = 0; i < cdata.tf.length; i++){
                tf += cdata.tf[i] + ",";
                weights += cdata.tfWeights[i] + ",";
            }
            tf+= "\n";
            weights += "\n";
        }
        
        new FileWrite( Paths.get(fr.folderPath).resolve("TermFreq.txt").toString(),tf ); 
        new FileWrite( Paths.get(fr.folderPath).resolve("Weights.txt").toString(),weights ); 
        
        itr = cosDataSet.entrySet().iterator();
        denom_Q = Math.sqrt(denom_Q);
        
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
            cdata.NormaliseLengthOFQ(denom_Q);
            
            normalised += key + "=>";
            for(int i = 0; i < cdata.tfNormalised.length; i++){
                normalised += cdata.tfNormalised[i] + ",";
            }
            normalised+= "\n";
        }
        
        
        new FileWrite( Paths.get(fr.folderPath).resolve("Normalised.txt").toString(),normalised ); 
        
        Double[] D_Q = new Double[d];
        for(int  i = 0; i < d; i++){
            D_Q[i] = 0d;
        }
        
        itr = cosDataSet.entrySet().iterator();
        int lastIndex = d;
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData data = (CosineData) m.getValue();
            for(int i = 0; i < data.tfNormalised.length - 1; i++){
                D_Q[i] +=  data.tfNormalised[i] * data.tfNormalised[lastIndex];
            }
        }
        
        List<Double> indexMap = new ArrayList<>();
        for(int i = 0; i < D_Q.length; i++){
            indexMap.add(D_Q[i]);
            System.out.println("Similarity Coefficient for D"+i + ",Q = "+D_Q[i]);
        }
        
        Arrays.sort(D_Q, Collections.reverseOrder()); 
        
        String order = "";
        
        for(int i = 0; i < D_Q.length; i++){
            if(i != 0 )
                order += " > ";
            
            order += fileNamesref.get( indexMap.indexOf(D_Q[i]) );
        }
        
        return order;
    }
}