/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCleaningMean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author KillerMech
 */
public class Runtime {
    //Defines
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
        
        //This hashmap is to calcuate mean in a attribute/column
        //It is mapped using key and a instance of MeanCalc class
        //Only attrib where they are numeric are stored here
        HashMap<String, MeanCalc> meanMap =  new HashMap<>();
                
        //Keeps record of no of lines in the collection
        //Also used to iterate other loop while writing
        int  recNo = 0;
        String line = "";  
        String splitBy = ",";  
        try   
        {  
            //Reads the csv file using buffer reader
            BufferedReader br = new BufferedReader(new FileReader("C:\\IR\\New folder\\dataset_38_sick.csv"));  
            while ((line = br.readLine()) != null)   //Read each line
            {  
                String[] val = line.split(splitBy);    // use comma as separator  
                if(recNo == 0){//If first line is read then save all the values we recieved as keys and initialise arrays
                    for(int j = 0; j < val.length; j++){//Iterate all columns/attrib
                        map.put(val[j], new ArrayList<String>());
                        attribType.put(val[j], ColumnType.UNKNOWN);//init mapped attrib type to unknown
                        meanMap.put(val[j], new MeanCalc());//init mean map
                        keyPosition.add(val[j]);
                    }
                }else{
                    for(int j = 0; j < val.length; j++){//From next loop onwards we have rows with each column values so need to save them
                        String key = keyPosition.get(j);//Get key for a particular index position
                        
                        if(val[j].equals("?") ){//If you encounter ? then replace it with ""
                            map.get( key ).add("");
                        }else{
                            //NumberUtils is apache commons lib, use it as it is faster 
                            if(NumberUtils.isParsable(val[j])){//check for each column in the row if it is Numeric 
                                if(attribType.get(key) == ColumnType.UNKNOWN)///If attribType of column is Unknown make it numeric
                                    attribType.put(key, ColumnType.NUMERIC);
                                
                                if(attribType.get(key) == ColumnType.NUMERIC){//Create mean only for numeric columns
                                    MeanCalc mean = meanMap.get(key);
                                    mean.no += NumberUtils.createFloat(val[j]);
                                    mean.count++;
                                }
                            }else{
                                attribType.put(key, ColumnType.STRING);//If value is string then change column type here
                            }
                             map.get( key ).add(val[j]);
                        }
                    }
                }
                recNo++;
            }  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        
        //Iterate each column
        for(int i = 0; i < keyPosition.size(); i++){
            String key = keyPosition.get(i);
            if(attribType.get(key) == ColumnType.NUMERIC){//If its a numeric column then start for loop, check for any missing values and add it to mean
                List<String> data = map.get(key);
                for(int j = 0; j < data.size(); j++){
                    if(data.get(j).equals("")){
                        data.set(j, meanMap.get(key).getMean() + "");
                    }
                }
            }
        }
        
        String data = "";
        //recNo stores count of all the lines in orig doc
        for(int i = 0; i < recNo; i++){
        //For first iteration add attrib/column names here which are stored as keyPositions
            if( i == 0){
                for(int j =0; j < keyPosition.size(); j++){
                    data += keyPosition.get(j);
                    if(j < keyPosition.size() - 1)//if reached last column then dont add comma
                        data += ",";
                }
            }else{
                
                //Rest of the iteration append array list from the map and iterate over it as column change
                for(int j =0; j < keyPosition.size(); j++){
                    String key = keyPosition.get(j);
                    
                    data += map.get(key).get(i - 1);
                    if(j < keyPosition.size() - 1)
                        data += ",";
                }
            }
           
            data +="\n";//Add new line after every iteration
        }
        
        new FileWrite("C:\\IR\\New folder\\CleanedData.csv",data );//Use the file writer class to write data to file
    }  
} 
