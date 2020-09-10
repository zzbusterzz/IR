/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationretrieval.Exc1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author 1
 */
public class Runtime {
    static TIM tdm;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        tdm = new  TIM();
        tdm.CreateTIM();
        
        Scanner sc = new Scanner(System.in);        
        
        boolean run = true;
        
        
        
        while(run){
             System.out.println("Enter Query(Type Exit to escape)");
             if(sc.hasNextLine()){
                String input= sc.nextLine();
                if(input == "Exit"){
                    run = false;
                }else{
                    //check for brackets
                    String[] keywords = input.split(" ");
                    
                    if(keywords.length > 2){
                    
                        boolean continueLoop = true;
                        String convertedString = "";
                        
                        List<String> operandArray = new ArrayList<String>();
                        for(int i = 0; i < keywords.length; i++){//convert words to vectors
                            if(!keywords[i].equals("AND") && !keywords[i].equals("NOT") && !keywords[i].equals("OR")){
                                
                                String finalKeyWord = "";
                                
                                operandArray.clear();
                                
                                if(keywords[i].contains("AND(") ||  keywords[i].contains("OR(") || keywords[i].contains("NOT(") ){///Split AND( and word
                                    int lastind = keywords[i].lastIndexOf("(");
                                    int firstind = keywords[i].indexOf("(");
                                    
                                    finalKeyWord = keywords[i].substring(lastind+1, keywords[i].length());//final keyword will be from last place of ["(" + 1] to end of length
                                    String bracketoperands = keywords[i].substring(firstind, lastind+1);//Operend will be located from first place occurancce of bracket "(" to last place ocurace of bracket "("
                                    operandArray.add(keywords[i].substring(0,firstind));//Any attached operand will be separated from 0 index to just before first occurance of "("
                                    
                                    for(int j = 0; j < bracketoperands.length(); j++){
                                        operandArray.add(bracketoperands.charAt(j) + "");
                                    }
                                    
                                }else if(keywords[i].contains("(")){//Split ( and word
                                    int lastind = keywords[i].lastIndexOf("(");
                                    int firstind = keywords[i].indexOf("(");
                                    
                                    finalKeyWord = keywords[i].substring(lastind+1, keywords[i].length());//final keyword will be from last place of ["(" + 1] to end of length
                                    String bracketoperands = keywords[i].substring(firstind, lastind+1);//Operend will be located from first place occurancce of bracket "(" to last place ocurace of bracket "("
                                    
                                    for(int j = 0; j < bracketoperands.length(); j++){
                                        operandArray.add(bracketoperands.charAt(j) + "");
                                    }
                                }else if(keywords[i].contains(")")){//Split keyword from it and bracket
                                    
                                    int lastind = keywords[i].lastIndexOf(")");
                                    int firstind = keywords[i].indexOf(")");
                                    
                                    finalKeyWord = keywords[i].substring(0, firstind);//final keyword will be from last place of ["(" + 1] to end of length
                                    String bracketoperands = keywords[i].substring(firstind, lastind+1);//Operend will be located from first place occurancce of bracket "(" to last place ocurace of bracket "("
                                   
                                    
                                    for(int j = 0; j < bracketoperands.length(); j++){
                                        operandArray.add(bracketoperands.charAt(j) + "");
                                    }
                                    
                                   
                                } else {
                                    finalKeyWord = keywords[i];
                                }
                                
                                String vword = tdm.returnVector(finalKeyWord);
                                if(vword == null)
                                    continueLoop = false;//undentified word detected break the loop

                                if(operandArray.size() > 0  && operandArray.get(operandArray.size() - 1).contains("(")) {
                                    for(int j = 0; j < operandArray.size(); j++){
                                      convertedString += operandArray.get(j) + " ";                                       
                                    }
                                    convertedString += vword + " ";
                                } else{
                                    convertedString += vword + " ";
                                    for(int j = 0; j < operandArray.size(); j++){
                                      convertedString += operandArray.get(j) + " ";                                       
                                    }
                                }
                            }else{
                                convertedString += keywords[i] + " ";
                            }
                        }

                        System.out.println("Converted string is : " + convertedString);
                        
                        if(continueLoop){
                            Stack<Integer> bracketPos = new Stack<Integer>();
                            HashMap<Integer,Integer> indexPosition = new HashMap<Integer,Integer>();

                            for(int i = 0; i < convertedString.length(); i++){
                                if(convertedString.charAt(i) == '(' ){
                                    bracketPos.add(i);
                                }
                                if(convertedString.charAt(i) == ')'){
                                    indexPosition.put(bracketPos.pop(), i);
                                }
                            }
                            
                            StringBuilder sb = new StringBuilder(convertedString);
                            
                            int prevStrLength = 0;
                            if(bracketPos.size() == 0){//Found all matching brackets so we can continue our query operation
                                
                                for (HashMap.Entry<Integer,Integer> entry : indexPosition.entrySet())  {
                                    
                                    
                                    String query = convertedString.substring(entry.getKey()+1, entry.getValue()-1-prevStrLength);
                                    System.out.println("query string is : " + query);
                                    String outVec = ProcessBoolean(query);//Returns the output of the vector of part of query

                                    if(outVec == null)
                                        break;

                                    sb.delete(entry.getKey(), entry.getValue()+1);
                                    
                                    prevStrLength = entry.getValue() - entry.getKey() + outVec.length()+ 2; 
                                    sb.insert(sb.length() -1 , outVec );//Two whitespaces
                                }
                                
                                
                                String outVec = ProcessBoolean(sb.toString());
                                
                                if(outVec == null)
                                        break;
                                
                                System.out.println("Result is :" + outVec);
                            }else{
                                System.out.println("In equal brackets query found, recheck and add new query");
                            }
                        } else{
                            System.out.println("Keyword not found");
                        }
                    } 
                    else{
                            System.out.println("Incorrect Query, Please enter a proper query");
                    }
                }
             }
        }        
    }
    
    public static String ProcessBoolean(String data){
        String[] words = data.split(" ");
        
        int notcounter = 0;
        
        for(int i = 0; i < words.length; i++){//To handle single or multiple nots
            if(!words[i].equals("")){
                String cWord = words[i];
                if(cWord.equals("NOT")){
                    words[i] = "";
                    notcounter++;
                }else{
                    if(notcounter > 0){
                         if(!cWord.equals("AND") &&  !cWord.equals("OR")){//Valid Query at this point atleast

                            if(notcounter > 0 && notcounter%2 == 1 ){//Get the no of nots, if its odd invert the number else dont invert
                                String invertedVector = "";
                                for(int j = 0; j < cWord.length(); j++){//Inverted vector
                                    if(cWord.charAt(j) == '0')
                                        invertedVector += "1";
                                    else
                                        invertedVector += "0";
                                }                        
                                cWord = invertedVector;
                                notcounter = 0;
                            }                    
                            words[i] = cWord;
                        } else{
                            System.out.println("Invalid Query, Please renter proper query");
                            return null;
                        }
                    }
                }
            }
        }
        
             
        String b1 = "";
        String b2 = "";
        
        String oper = "";
        
        for(int  i = 0; i < words.length; i++){
            if(!words[i].equals("")){
                String cWord = words[i];
                if(cWord.equals("AND")){
                    if(b1 == ""){//First time you encounter directly 
                        System.out.println("Invalid Query, Please renter proper query");
                        return null;
                    }
                    oper = "AND";
                    words[i] = "";
                } else {
                    if(b1 == "" || oper == ""){
                        b1 = cWord;
                        if(i < words.length -1)
                            words[i] = "";
                    }else{
                     
                        b2 = cWord;//No operand found between words then return result as null
                        words[i] = "";
                        
                        if(oper == ""){
                            System.out.println("Invalid Query, Please renter proper query");
                            return null;
                        }
                        
                        //Do the AND/OR operation                        
                        if(oper == "AND"){
                            String andString = "";
                            for(int  j = 0; j < b1.length(); j++){
                                if((b1.charAt(j) == b2.charAt(j)) && (b1.charAt(j) == '1')){
                                    andString += '1';
                                }else
                                    andString += '0';
                            }
                            
                            b1 = andString;
                            oper = "";
                            if(i < words.length - 1){
                                words[i-1] = b1;
                                words[i] = "AND";
                                i = i-2;//Start from this current index -1 as we have assigned current word with new vector
                            }else
                            {
                                words[i] = b1;
                                i--;//Start from this current index -1 as we have assigned current word with new vector
                            }
                            
                        }
                    }
                }
            }            
        }
        
        b1 = "";
        b2 = ""; 
        oper = "";
        
        for(int  i = 0; i < words.length; i++){
            if(!words[i].equals("")){
                String cWord = words[i];
                   if(cWord.equals("OR")){
                        if(b1 == ""){//First time you encounter directly 
                            System.out.println("Invalid Query, Please renter proper query");
                            return null;
                        }
                        oper = "OR";
                        words[i] = null;
                    } else{
                        if(b1 == "" || oper == ""){
                            b1 = cWord;
                            if(i < words.length -1)
                                words[i] = "";
                    }else{
                        b2 = cWord;//No operand found between words then return result as null
                        words[i] = "";

                        if(oper == ""){
                            System.out.println("Invalid Query, Please renter proper query");
                            return null;
                        }

                        //Do the AND/OR operation                        
                        if(oper == "OR"){
                            String orString = "";
                            for(int  j = 0; j < b1.length(); j++){
                                //For or condition to hold true we need both condition where both values of 0 should give back 0
                                if((b1.charAt(j) == b2.charAt(j)) && (b1.charAt(j) == '0')){
                                    orString += '0';
                                }else
                                    orString += '1';
                            }

                            b1 = orString;
                            oper = "";
                            
                            if(i < words.length - 1){
                                words[i-1] = b1;
                                words[i] = "OR";
                                i = i-2;//Start from this current index -1 as we have assigned current word with new vector
                            }else
                            {
                                words[i] = b1;
                                i--;//Start from this current index -1 as we have assigned current word with new vector
                            }
                        }
                    }
                }
            }
        }            
        return b1;
    }
}
//Following queries were used to test
//1) Goa AND state  -> 110000
//2) Goa AND is NOT state -> 001100
//3) Goa AND state AND (South AND western) -> 100000
//4) Goa AND NOT( state AND India)
//5) Goa AND NOT(state AND India) -> 011100
//6) Maharashtra AND NOT(state AND India) -> 010001
//7) 