/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CAIIExc1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        
        //A hashmap of all int columns with integer
        HashMap<String, Integer> mideanCount = new HashMap<>();
        
        HashMap<String, Integer> nameCount = new HashMap<>();
        
        String readpath = "C:\\IR\\CAII\\Problem1_Data\\Employee_Details.xlsx";
        String writepath = "C:\\IR\\CAII\\Problem1_Data\\Employee_Details_Cleaned.xlsx";
        
        recNo = ExcelReaderWriter.ReadExcel(readpath, map, attribType, keyPosition, mideanCount);
        
        SetMedianForEmptyCell(map, attribType, keyPosition, mideanCount);

        CreateExtraColumnForID(map, keyPosition);
        
        ExcelReaderWriter.WriteExcel(writepath, map, attribType, keyPosition, recNo);
    } 

    //Replace missing values with mode
    public static void SetMedianForEmptyCell( HashMap<String, List<String>> map,HashMap<String, ColumnType> attribType , List<String> keyPosition, HashMap<String, Integer> mideanCount){
        for(int i = 0; i < keyPosition.size(); i++){
            String key = keyPosition.get(i);
            if(attribType.get(key) == ColumnType.NUMERIC){//If its a numeric column then start for loop, check for any missing values and add it to mean
                int mideanOfSelCol =  mideanCount.get(key);
                
                List<String> data = map.get(key);
                for(int j = 0; j < data.size(); j++){
                    if(data.get(j).equals("")){
                       data.set(j, mideanOfSelCol + "");
                    }
                }
            }
        }
    }
    
    ///Replace soem values where some conditions are satisfied
    public static void CreateExtraColumnForID( HashMap<String, List<String>> map, List<String> keyPosition){
        List<String> empCode = new  ArrayList<>();
        
        map.put("empCode", empCode);//Create emp code col
        keyPosition.add("empCode");
        
        HashMap<String, Integer> tempnameCount = new HashMap<>();
        
        for(int i = 0; i < keyPosition.size(); i++){                
            List<String> empName = map.get("empName");
            int size = empName.size();

            for(int j = 0; j < size; j++){
                String name = empName.get(j);
                
                if(tempnameCount.containsKey(name)){
                    tempnameCount.put(name, tempnameCount.get(name) + 1);
                }else{
                    tempnameCount.put(name,1);
                }
                //int length = String.valueOf(nameCount.get(name)).length();
                int index = tempnameCount.get(name);
                empCode.add(name + String.format("%03d", index));
            }
        }
    }
}