/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestExc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KillerMech
 */
//Dataset taken from https://www.openml.org/d/38
public class Runtime {
    //Defines columns type
    enum ColumnType{
        NUMERIC,
        STRING,
        UNKNOWN
    }    
    
    
    public static void main(String[] args) throws Exception  
    {   
        //Maps column type for a key
        //Attributes which are at begining 1st rouw age,sex will be added here as key while other rows in the columns are added to array list
        HashMap<String, List<String>> map = new HashMap<>();       
        
        //This hashmap saves column type ie Name will be saved as STRING while age or other attributes will be saved as NUMERIC
        HashMap<String, ColumnType> attribType = new HashMap<>();
        
        //This array list keeps track of indices allocated to each attributes so we can return key for a particular index
        //ie age is kept at 0 so while itertating and we are on 0 column we can directly get key for 0 columns using this
        List<String> keyPosition = new ArrayList<>();
                
        //Keeps record of no of lines in the collection
        //Also used to iterate other loop while writing
        int  recNo = 0;
        
        HashMap<String, HashMap<Double, Integer>> modeCount = new HashMap<>();
        
        String readpath = "C:\\IR\\TestExc\\Problem1_Data\\Employee_Details.xlsx";
        String writepath = "C:\\IR\\TestExc\\Problem1_Data\\Employee_Details_Cleaned.xlsx";
        
        recNo = ExcelReaderWriter.ReadExcel(readpath, map, attribType, keyPosition, modeCount);
        
        SetModeAsCountForEmptyCell(map, attribType, keyPosition, modeCount);

        ReplaceWithCapital(map, keyPosition, modeCount);
        
        ExcelReaderWriter.WriteExcel(writepath, map, attribType, keyPosition, recNo);
    } 
    
    
    // function to sort hashmap by values 
    public static Double sortByValue(HashMap<Double, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<Double, Integer> > list = 
               new LinkedList<Map.Entry<Double, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Double, Integer> >() { 
            public int compare(Map.Entry<Double, Integer> o1,  
                               Map.Entry<Double, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
        
        for (Map.Entry<Double, Integer> aa : list) { 
            return aa.getKey();
            //return new AbstractMap.SimpleEntry<>(aa.getKey(), aa.getValue());
        } 
        return null;
    } 
    
    //Replace missing values with mode
    public static void SetModeAsCountForEmptyCell( HashMap<String, List<String>> map,HashMap<String, ColumnType> attribType , List<String> keyPosition, HashMap<String, HashMap<Double, Integer>> modeCount){
        for(int i = 0; i < keyPosition.size(); i++){
            String key = keyPosition.get(i);
            if(attribType.get(key) == ColumnType.NUMERIC){//If its a numeric column then start for loop, check for any missing values and add it to mean
                Double mode = sortByValue( modeCount.get(key));
                
                List<String> data = map.get(key);
                for(int j = 0; j < data.size(); j++){
                    if(data.get(j).equals("")){
                       data.set(j, mode + "");
                    }
                }
            }
        }
    }
    
    ///Replace soem values where some conditions are satisfied
    public static void ReplaceWithCapital( HashMap<String, List<String>> map, List<String> keyPosition, HashMap<String, HashMap<Double, Integer>> modeCount){
        for(int i = 0; i < keyPosition.size(); i++){                
            List<String> empResidence = map.get("empResidence");
            List<String> empSalary = map.get("empSalary");
            int size = empResidence.size();

            for(int j = 0; j < size; j++){
                if(empResidence.get(j).equals("City_Centre") && Double.parseDouble(empSalary.get(j)) > 65000){
                   empResidence.set(j, "Capital");
                }
            }
        }
    }
}