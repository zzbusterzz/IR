/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Permuterm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
    private Dictionary<String,List<String>> permuterm;
    private SortedMap<String,List<Integer>> normalWords;
    
    private  FileReader fr;
    private  List<String> fileNamesref;
    private int fileCount;
    
    public List<String> getFileNamesref() {
        return fileNamesref;
    }

    public int getFileCount() {
        return fileCount;
    }
    
    public void CreateInvertedIndex(){
        
        fr = new FileReader();
        fr.ReadFile();
        
        permuterm = new Hashtable<String, List<String>>();
        normalWords = new TreeMap<String, List<Integer>>();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        System.out.println("Total Files in directory : " + fileCount);

        
        
        for (String file : fileNamesref) {  
            try {
                if(!file.equals("inverted.txt") && !file.equals("permuterm.txt")){
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));
                    
                    for(int i =0; i < lines.size(); i++){
                         String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            content[j] = content[j].toLowerCase();
                            String word =  content[j] + "$";
                            AddEntry(permuterm, word, content[j]);
                            for(int k = 0; k < word.length(); k++){
                                String word1 = word.substring(0, k);
                                String word2 = word.substring(k, word.length());
                                
                                String finalWord = word2 + word1;
                                
                                AddEntry(permuterm, finalWord, content[j]);
                            }
                                
                          
                            AddEntry(normalWords, content[j], file);//Current words
                        }
                    }
                } 
            } catch (IOException ex) {
                  Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        //File Write
        String data = "";
        
        Set s = normalWords.entrySet();
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
        
        new FileWrite(folderPath.resolve("inverted.txt").toString(),data );
        
        System.out.println("Completed Inverted Index");
        
        data = PrintPermuterm();
        
        new FileWrite(folderPath.resolve("permuterm.txt").toString(),data ); 
    }
    
    public void AddEntry( SortedMap<String,List<Integer>> info ,String entry, String fileName){
        if(info.get(entry) == null){
            info.put(entry, new ArrayList<Integer>()); 
        } 
        
        int index = fr.getFileNames().indexOf(fileName);
        
        if(!info.get(entry).contains(index))
            info.get(entry).add(index);
    }
    
    public void AddEntry( Dictionary<String,List<String>> info ,String entry, String value){
        if(info.get(entry) == null){
            info.put(entry, new ArrayList<String>()); 
        } 
        
        
        if(!info.get(entry).contains(value))
            info.get(entry).add(value);
    }
    
    
    String PrintPermuterm(){
        String data = "";
        
        String column1Format = "%-15.15s";
        String alternateColFormat = "%-10.20s";
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf(column1Format, "WORD");
        System.out.printf(alternateColFormat, "Mapped words");
        
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        
        String key = "";
        for (Enumeration en = permuterm.keys(); en.hasMoreElements();) {
            key = (String)en.nextElement();
            System.out.format(column1Format, key + ":  ");
            
            data += key + " => ";
            
            List<String> rowValues = (List<String>)permuterm.get(key);
            
            
            for(int i = 0; i < rowValues.size(); i++){
                System.out.printf(alternateColFormat, rowValues.get(i));
                data += rowValues.get(i) + " ";
            }
            
            data += "\n";
            System.out.println("");
        }
        
       
        
        System.out.println("-----------------------------------------------------------------------------");
        
        return data;
    }
    
    public List<Integer> returnInvertedList(String keyword){
        if(normalWords.get(keyword) == null)
            return null;
        else
            return normalWords.get(keyword);
    }
    
    public Set<Integer> returnLDocIDsForPermuterm(String word){
        word = word + "$";
        Set<Integer> docIdSet = new LinkedHashSet<>();
        
        Set<String> set = new LinkedHashSet<>(); //This will remove duplicate words
        if(!word.contains("*")){
            //Normal Word: Rotate and get all permuterm
            List<String> combWords = new ArrayList<String>();
            for(int k = 0; k < word.length(); k++){
                String word1 = word.substring(0, k);
                String word2 = word.substring(k, word.length());

                String finalWord = word2 + word1;

               combWords.add(finalWord);
            }
            
           
            for(int j = 0; j < combWords.size(); j++){
                List<String> ref = permuterm.get(combWords.get(j));
                if(ref != null)
                    set.addAll(ref);
            }
           
        }else{
            String[] starSplit = word.split("\\*");
            
            int star_pos = word.indexOf("*");
            int star_pos_W2 = word.lastIndexOf("*");
            
            String word1 = word.substring(0, star_pos + 1);
            String word2 = word.substring(star_pos_W2+1, word.length());
            String termRot = word2 + word1;
            
            Enumeration<String> keys =  permuterm.keys();
            
            int termSize = termRot.length() - 1;
            int j = 0;
            while(keys.hasMoreElements()){
               String key = keys.nextElement();
               int kl = key.length();
               
                if(kl >= termSize ){
                    String ss1 = termRot.substring(0, termSize);
                    String ss2 = key.substring(0, termSize);
                    if(ss1.equals(ss2))
                        set.addAll(permuterm.get(key));
                    j++;
                }
            }
            
            if(starSplit.length > 2)//Multi star query 
            {
                Iterator it = set.iterator();
                while(it.hasNext()){
                    String val = (String) it.next() + "$";
                    val = val.replaceFirst(starSplit[0], "");
                    int lastind = val.lastIndexOf(starSplit[starSplit.length - 1]);
                    if(lastind == -1) return null;
                    val = val.substring(0, lastind);
                    boolean remove = false;
                    for(int i = 1; i < starSplit.length - 1; i++)
                    {
                        String keyword = starSplit[i];
                        if(val.contains(keyword)){
                            val = val.replaceFirst(keyword, "");
                        } else{
                            remove = true;
                        }
                    }
                    
                    if(remove)
                        it.remove();
                }
            }
        }
        
        Iterator it = set.iterator();
        while(it.hasNext()){
            List<Integer> ref = returnInvertedList((String) it.next());
            if(ref != null)
                docIdSet.addAll(ref);
        }
        
        return docIdSet;
    }
}
