/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VSMCosineSimilarity;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

/**
 *
 * @author 1
 */
public class Runtime {
    static VSMData vsm;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        vsm = new  VSMData();
        vsm.CreateIndex();
        
        Scanner sc = new Scanner(System.in);        
        
        boolean run = true;
        
        while(run){
             System.out.println("Enter Query(Type Exit to escape)");
             if(sc.hasNextLine()){
                String input= sc.nextLine();
                if(input.equalsIgnoreCase("Exit")){
                    run = false;
                }else{
                    //check for brackets
                    input = input.toLowerCase();
                    String[] keywords = input.split(" ");
                    
                    Dictionary<String,Integer> query = new Hashtable();
                    
                    for(int i = 0 ; i < keywords.length; i++){
                       if(query.get(keywords[i]) == null)
                           query.put(keywords[i], 1);
                       else
                           query.put(keywords[i], query.get(keywords[i]) + 1 ); 
                    }
                    
                    String fileNames = vsm.CalcCosineSimilarity(query);                   
                    
                    if(fileNames == "" )
                        System.out.println("No values present");
                    else
                        System.out.println(fileNames);
                    
                }
            }  
         }
    }
}