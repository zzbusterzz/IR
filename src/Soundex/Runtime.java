/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soundex;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author 1
 */
public class Runtime {
    static InvertedIndex invMat;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        invMat = new  InvertedIndex();
        invMat.CreateInvertedIndex();
        
        Scanner sc = new Scanner(System.in);        
        
        boolean run = true;
        
        while(run){
             System.out.println("Enter Query(Type Exit to escape)");
             if(sc.hasNextLine()){
                String input= sc.nextLine();
                input= input.toLowerCase();
                
                if(input.equalsIgnoreCase("Exit")){
                    run = false;
                }else{
                    //check for brackets
                    String[] keywords = input.split(" ");
                    
                    Set<Integer> docID = new HashSet<>();
                    
                    for(int i = 0 ; i < keywords.length; i++){
                       Set<Integer> temp = invMat.returnLDocIDsForSoundex(keywords[i]);
                       if(temp != null)
                            docID.addAll( temp);
                    }
                    
                    String fileNames = "";
                    Iterator it = docID.iterator();
                    while(it.hasNext()){
                        int val = (int) it.next();
                        fileNames += invMat.getFileNamesref().get( val ) + " ";
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