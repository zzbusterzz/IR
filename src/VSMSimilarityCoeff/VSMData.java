/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VSMSimilarityCoeff;

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
    private SortedMap<String,CoeffData> tfAndidf;//0 index is df and 1 index is idf
    
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
        
        tfAndidf = new TreeMap<String, CoeffData>();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        d = fr.getFileCountForD();
        
        System.out.println("Total Files in directory : " + fileCount);
        
        for (String file : fileNamesref) {  
            try {
                if(!file.equals("DFIDF.txt") && !file.equals("TFXIDF.txt")){
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));
                    
                    for(int i =0; i < lines.size(); i++){
                        String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            AddEntry(tfAndidf, content[j].toLowerCase(), file);//Current words
                        }
                    }
                } 
            } catch (IOException ex) {
                  Logger.getLogger(VSMData.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        //File Write
        String data = "";
        
        Iterator itr = tfAndidf.entrySet().iterator();
     
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CoeffData rowValues = (CoeffData)m.getValue();
            rowValues.countDF();
            rowValues.countIDF(d);
            
            rowValues.countTFXIDF();
        }
        
        System.out.println("Completed DFIDF");
        
        data = PrintDF_IDF();
        
        new FileWrite(folderPath.resolve("DFIDF.txt").toString(),data ); 
    }
    
    public void AddEntry( SortedMap<String,CoeffData> info ,String word, String fileName){
        if(info.get(word) == null){
            info.put(word, new CoeffData(d) ); 
        } 
        info.get(word).AddTF( fileNamesref.indexOf(fileName) );
    }
    
    String PrintDF_IDF(){
        String data = "";
        
        String column1Format = "%-15.15s";
        String alternateColFormat = "%-10.20s";
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf(column1Format, "WORD");
        System.out.printf(alternateColFormat, "DF");
        System.out.printf(alternateColFormat, "IDF");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        
        String key = "";
        
        Iterator itr = tfAndidf.entrySet().iterator();
     
        while(itr.hasNext()){
            
            Map.Entry m  = (Map.Entry)itr.next();
            key = (String)m.getKey();
            System.out.format(column1Format, key + ":  ");
            
            data += key + " => ";
            
            CoeffData rowValues = (CoeffData)m.getValue();
            
            System.out.printf(alternateColFormat, rowValues.df);
            System.out.printf(alternateColFormat, rowValues.idf);
            data += rowValues.df + " " + rowValues.idf;
            
            data += "\n";
            System.out.println("");
        }
        System.out.println("-----------------------------------------------------------------------------");
        
        return data;
    }
    
    public String CalcQueryTF_IDF_ReturnOrder(Dictionary<String, Integer> query){
        Iterator itr = tfAndidf.entrySet().iterator();
        String tfidfData = "";
        
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CoeffData cdata = (CoeffData) m.getValue();
             
            if( query.get(key) != null){
                int count = query.get(key);
                cdata.calcQueryTFXIDF(count);
            }else
                cdata.calcQueryTFXIDF(0);
            
            
            tfidfData += key + "=>";
            for(int i = 0; i < cdata.tfidf.length; i++){
                tfidfData += cdata.tfidf[i] + ",";
            }
            tfidfData+= "\n";
        }
        
        new FileWrite( Paths.get(fr.folderPath).resolve("TFXIDF.txt").toString(),tfidfData ); 
        
        
        Double[] D_Q = new Double[d];
        for(int  i = 0; i < d; i++){
            D_Q[i] = 0d;
        }
        
        itr = tfAndidf.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CoeffData data = (CoeffData) m.getValue();
            for(int i = 0; i < data.tfidf.length - 1; i++){
                D_Q[i] +=  data.tfidf[i] * data.tfidf[data.tfidf.length - 1];
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