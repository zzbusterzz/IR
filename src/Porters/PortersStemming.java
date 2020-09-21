/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Porters;

/**
 *
 * @author 1
 */
public class PortersStemming {
    public static String stemedWord(String word){
        String oldword = word;
        
        if(word.endsWith("sses")){
            int ind = word.lastIndexOf("sses");
            word = word.substring(0, ind);
            word += "ss";
        }
        
        if(word.endsWith("ies")){
            int ind = word.lastIndexOf("ies");
            word = word.substring(0, ind);
            word += "i";
        }
        
        if(word.endsWith("s")){
            int ind = word.lastIndexOf("s");
            word = word.substring(0, ind);
        }
        
//        SSES -> SS
//caresses -> caress
//
//IES -> I
//ponies -> poni
//
//SS -> SS
//caress -> caress
//
//S ->
//cats -> cat
//        
        return word;
    }
}
