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
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1
 */
public class InvertedMatrix {
    private Dictionary<String,String> info;
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
        
        info = new Hashtable();
        
        fileNamesref = fr.getFileNames();        
        Path folderPath = Paths.get(fr.folderPath);
        fileCount = (int)fr.getFileCount();
        
        System.out.println("Total Files in directory : " + fileCount);
        
        try {
            stopWords = Files.readAllLines(folderPath.resolve("StopWords.txt"));
        } catch (IOException ex) {
            Logger.getLogger(InvertedMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        for (String file : fileNamesref) {   
            if(!file.equals("StopWords.txt")){
                  try {
                    List<String> lines = Files.readAllLines(folderPath.resolve(file));

                    for(int i =0; i < lines.size(); i++){
                        String[] content = lines.get(i).split(" "); //Split the string based on whitespace
                        for(int j = 0; j < content.length; j++){
                            if(!stopWords.contains(content[j].toLowerCase()) ){//Removed stopwords
                                AddEntry(content[j], file);
                            }
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(InvertedMatrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        System.out.println("Completed Inverted Index");
        
        PrintTIM();
    }
    
    public void AddEntry(String entry, String fileName){
        if(info.get(entry) == null){
            String rowIncidence = String.format("%0" + (fileCount - 1) + "d", 0).replace('0', '0');
            info.put(entry, rowIncidence); //assign row incidence of each word to doc in entry
        } 
        
        int index = fr.getFileNames().indexOf(fileName);
        String data = info.get(entry);
        info.put(entry, replaceChar(data, '1', index)) ;//Set etnry to 1 for the doc where it was found
                
    }
    
    public static String replaceChar(String str, char ch, int index) {
        StringBuilder myString = new StringBuilder(str);
        myString.setCharAt(index, ch);
        return myString.toString();
    }
    
    void PrintTIM(){
        Enumeration enu = info.keys();
         
        String column1Format = "%-15.15s";
        String alternateColFormat = "%-10.10s";
        String alternateColFormatC = "%-10.1s";
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf(column1Format, "WORD");
        
        for(int i = 0; i < fileNamesref.size(); i++){
            if(!fileNamesref.get(i).equals("StopWords.txt"))
                System.out.printf(alternateColFormat, fileNamesref.get(i));
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
    
        while(enu.hasMoreElements()){
            String key = (String)enu.nextElement();
            System.out.format(column1Format, key + ":  ");
            
            String rowValues = info.get(key);
            for(int i = 0; i < rowValues.length(); i++){
                System.out.printf(alternateColFormatC, rowValues.charAt(i));
            }
            
            System.out.println("");
        }
        
        System.out.println("-----------------------------------------------------------------------------");
    }
    
    public String returnVector(String keyword){
        if(info.get(keyword) == null)
            return null;
        else
            return info.get(keyword);
    }
}
