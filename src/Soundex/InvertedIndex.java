/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soundex;

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
    private Dictionary<String,List<String>> soundex;
    private SortedMap<String,List<Integer>> invertedIndex;
    
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
        
        soundex = new Hashtable<String, List<String>>();
        invertedIndex = new TreeMap<String, List<Integer>>();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        System.out.println("Total Files in directory : " + fileCount);

        
        
        for (String file : fileNamesref) {  
            try {
                if(!file.equals("Inverted.txt") && !file.equals("Soundex.txt")){
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));
                    
                    for(int i =0; i < lines.size(); i++){
                        String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            content[j] = content[j].toLowerCase();
                            AddEntry(soundex, GetSoundex(content[j]), content[j]);
                            AddEntry(invertedIndex, content[j], file);//Current words
                        }
                    }
                } 
            } catch (IOException ex) {
                  Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        //File Write
        String data = "";
        
        Set s = invertedIndex.entrySet();
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
        
        new FileWrite(folderPath.resolve("Inverted.txt").toString(),data );
        
        System.out.println("Completed Inverted Index");
        
        data = PrintSoundex();
        
        new FileWrite(folderPath.resolve("Soundex.txt").toString(),data ); 
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
    
    
    String PrintSoundex(){
        String data = "";
        
        String column1Format = "%-15.15s";
        String alternateColFormat = "%-10.20s";
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf(column1Format, "WORD");
        System.out.printf(alternateColFormat, "Mapped words");
        
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        
        String key = "";
        for (Enumeration en = soundex.keys(); en.hasMoreElements();) {
            key = (String)en.nextElement();
            System.out.format(column1Format, key + ":  ");
            
            data += key + " => ";
            
            List<String> rowValues = (List<String>)soundex.get(key);
            
            
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
        if(invertedIndex.get(keyword) == null)
            return null;
        else
            return invertedIndex.get(keyword);
    }
    
    public Set<Integer> returnLDocIDsForSoundex(String word){
        
        Set<Integer> docIdSet = new LinkedHashSet<>();
        
        String soundexWord = GetSoundex(word);
        List<String> words = soundex.get(soundexWord);
        
        if(words == null)
            return null;
        else
        {
            for(int i = 0; i < words.size(); i++){
                docIdSet.addAll(returnInvertedList(words.get(i)));
            }
        }
        
        return docIdSet;
    }
    
    public String GetSoundex(String word){
        word = word.toLowerCase();
        String codex = "";
        for(int i = 0; i < word.length(); i++){
            if(i == 0)
                codex += word.charAt(i);
            else{
                switch(word.charAt(i) ){
                    case 'a':
                    case 'e':
                    case 'i':
                    case 'o':
                    case 'u':
                    case 'h':
                    case 'w':
                    case 'y':
                       codex += "0"; 
                        break;
                        
                    case 'b':
                    case 'f':
                    case 'p':
                    case 'v':
                       codex += "1"; 
                        break; 
                        
                    case 'c':
                    case 'g':
                    case 'k':
                    case 'q':
                    case 's':
                    case 'x':
                    case 'z':
                       codex += "2"; 
                        break;
                        
                    case 'd':
                    case 't':
                       codex += "3"; 
                        break;
                        
                    case 'l':
                       codex += "4"; 
                        break;
                        
                    case 'm':
                    case 'n':
                       codex += "5"; 
                        break;
                        
                    case 'r':
                       codex += "6"; 
                        break;
                }
            }
        }
        
        String newCodex = "";
        char prevChar = 0;
        for(int i = 0; i < codex.length(); i++){
            if(i == 0)
                newCodex += codex.charAt(i);
            else{
                char c = codex.charAt(i);
                if(c != prevChar){
                    newCodex += c;//Replaces double consecutive digits to single one
                    prevChar = c;
                }else{
                    prevChar = 0;
                }
            }
        }
        
        codex = "";
        int zerosToPad = 0;
        for(int i = 0; i <  newCodex.length(); i++){
             if(i == 0)
                codex += newCodex.charAt(i);
             else
             {
                 if(newCodex.charAt(i) == '0')
                     zerosToPad++;
                 else
                     codex += newCodex.charAt(i);
             }
        }
        
        if(codex.length() < 4 && codex.length() + zerosToPad < 4){
            zerosToPad = Math.abs(4 - codex.length());
        }
        
        for(int i = 0; i < zerosToPad; i++)
            codex += '0';
        
        return codex.substring(0, 4);//Return result for codex for first 4 char
    }
}
