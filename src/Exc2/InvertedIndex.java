/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exc2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1
 */
public class InvertedIndex {
    private SortedMap<String,List<Integer>> info;
    private SortedMap<String,List<Integer>> infoWithStopWords;
    
    private  FileReader fr;
    private  List<String> fileNamesref;
    private int fileCount;
    
    public List<String> getFileNamesref() {
        return fileNamesref;
    }

    public int getFileCount() {
        return fileCount;
    }
    
    public void CreateInvertedMatrix(){
        List<String> stopWords = new ArrayList<String>();
        
        fr = new FileReader();
        fr.ReadFile();
        
        info = new TreeMap<String, List<Integer>>();
        infoWithStopWords = new TreeMap<String, List<Integer>>();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        System.out.println("Total Files in directory : " + fileCount);
        
        try {
            stopWords = Files.readAllLines(folderPath.resolve("StopWords.txt"));
        } catch (IOException ex) {
            Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        for (String file : fileNamesref) {  
            try {
                if(!file.equals("StopWords.txt") && !file.equals("AllWords.txt") && !file.equals("WithStopWords.txt")){
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));
                    
                    for(int i =0; i < lines.size(); i++){
                         String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            if(!stopWords.contains(content[j].toLowerCase()) ){//Removed stopwords
                                AddEntry(info, content[j], file);
                            }
                            AddEntry(infoWithStopWords, content[j], file);//Stopwords included
                        }
                    }
                } 
            } catch (IOException ex) {
                  Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        //File Write
        String data = "";
        
        Set s = infoWithStopWords.entrySet();
        Iterator itr = s.iterator();
     
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            data += m.getKey() + " => ";
            List<Integer> rowValues = (List<Integer>)m.getValue();
            for(int i = 0; i < rowValues.size(); i++){
                data += rowValues.get(i) + " ";
            }
            data += "\n";
        }
        
        new FileWrite(folderPath.resolve("AllWords.txt").toString(),data );
        
        System.out.println("Completed Inverted Index");
        
        data = PrintTIM();
        
        new FileWrite(folderPath.resolve("WithStopWords.txt").toString(),data ); 
    }
    
    public void AddEntry( SortedMap<String,List<Integer>> info ,String entry, String fileName){
        if(info.get(entry) == null){
            info.put(entry, new ArrayList<Integer>()); 
        } 
        
        int index = fr.getFileNames().indexOf(fileName);
        info.get(entry).add(index);
    }
    
    public static String replaceChar(String str, char ch, int index) {
        StringBuilder myString = new StringBuilder(str);
        myString.setCharAt(index, ch);
        return myString.toString();
    }
    
    String PrintTIM(){
        
        String data = "";
        
        Set s = info.entrySet();
        Iterator itr = s.iterator();
         
        String column1Format = "%-15.15s";
        String alternateColFormat = "%-10.10s";
        String alternateColFormatC = "%-10.1s";
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf(column1Format, "WORD");
        System.out.printf(alternateColFormat, "COUNT");
        System.out.printf(alternateColFormat, "Doc ID's");
        
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
    
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            System.out.format(column1Format, m.getKey() + ":  ");
            
            data += m.getKey() + " => ";
            
            List<Integer> rowValues = (List<Integer>)m.getValue();
            
            System.out.printf(alternateColFormat, rowValues.size());
            
            for(int i = 0; i < rowValues.size(); i++){
                System.out.printf(alternateColFormatC, rowValues.get(i));
                data += rowValues.get(i) + " ";
            }
            
            data += "\n";
            System.out.println("");
        }
        
       
        
        System.out.println("-----------------------------------------------------------------------------");
        
        return data;
    }
    
    public List<Integer> returnPostingList(String keyword){
        if(info.get(keyword) == null)
            return null;
        else
            return info.get(keyword);
    }
}
