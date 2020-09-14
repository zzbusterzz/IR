/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exc2;

import java.util.Scanner;

/**
 *
 * @author 1
 */
public class Runtime {
    static InvertedMatrix invMat;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        invMat = new  InvertedMatrix();
        invMat.CreateInvertedMatrix();
        
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
                    String[] keywords = input.split(" ");
                    
                    String defoutVec = "";
                    for(int i = 0; i < invMat.getFileCount() - 1; i++){//max-1 As our file should not count stopwords file
                        defoutVec += "0";
                    }
                   
                    for(int i = 0; i < keywords.length; i++){
                        String outvec = invMat.returnVector( keywords[i] );
                        if(outvec == null)
                        {
                            System.out.println("Keyword not found");
                        }else{
                            String tempindex = "";
                            for( int j = 0; j < defoutVec.length(); j++){
                                if(defoutVec.charAt(j) == outvec.charAt(j) && outvec.charAt(j) == '0')
                                    tempindex += "0";
                                else
                                    tempindex += "1";
                            }
                            
                            defoutVec = tempindex;
                        }
                    }
                    
                    String fileNames = "";
                    for( int i = 0; i < defoutVec.length(); i++){
                        if(defoutVec.charAt(i) == '1' ){
                            fileNames += invMat.getFileNamesref().get(i) + " ";
                        }
                    }
                    
                    if(fileNames == "" )
                        System.out.println("No values present");
                    else
                        System.out.println(fileNames);
                }
            }  
         }
    }
}