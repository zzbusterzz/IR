/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CAIIExc2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author 1
 */
public class VSMData {
   
    //private Dictionary<String,Integer[]> tfAndidf;
    private SortedMap<String,CosineData> cosDataSet;//0 index is df and 1 index is idf
    
   // private  FileReader fr;
    private  List<String> fileNamesref;
    private int fileCount;
    private int d;
    public List<String> getFileNamesref() {
        return fileNamesref;
    }

    public int getFileCount() {
        return fileCount;
    }
    
    public void CreateIndex(){
        
        
        cosDataSet = new TreeMap<String, CosineData>();
        
        fileNamesref = new ArrayList<>(){
            {
                add("Doc1"); 
                add("Doc2"); 
                add("Doc3"); 
            }
        };
        
        d = fileNamesref.size();

        
            cosDataSet.put("elvis", new CosineData( new int[]{3,4,5,0 }));
            cosDataSet.put("presley", new CosineData( new int[]{4,0,3,0 } ));
            cosDataSet.put("mississippi", new CosineData( new int[]{0,4,0,0 }));
            cosDataSet.put("pop", new CosineData( new int[]{6,0,4,0 }));
            cosDataSet.put("music", new CosineData( new int[]{0,0,4,0 }));            
            cosDataSet.put("life", new CosineData( new int[]{0,3,0,0 }));
            
       
        
        double[] denom_D = new double[d];
        
        Iterator itr = cosDataSet.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData rowValues = (CosineData)m.getValue();
            rowValues.CalctfWeights();
            
            for(int i = 0; i < d; i++){
                double w = rowValues.returnWeightAtIndex(i);
                denom_D[i] += w * w;
            }
        }
        
         for(int i = 0; i < d; i++)
            denom_D[i] = Math.sqrt( denom_D[i]);
        
        itr = cosDataSet.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData rowValues = (CosineData)m.getValue();
            rowValues.NormaliseLength(denom_D);
        }
         
        System.out.println("Completed Till Normalisation");
        
    }
    

    
    public String CalcCosineSimilarity(Dictionary<String, Integer> query){
        Iterator itr = cosDataSet.entrySet().iterator();
        
//        String tf = "";
//        String weights = "";
//        String normalised = "";
        
        double denom_Q = 0;
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
             
            if( query.get(key) != null){
                int count = query.get(key);
                cdata.AddTFAtQ(count);
            }else
                cdata.AddTFAtQ(0);
            
            cdata.CalctfWeightsofQ();
            
            double w = cdata.returnWeightAtIndex(d);
            denom_Q += w*w;
           
            
//            tf += key + "=>";
//            weights += key + "=>";
//            for(int i = 0; i < cdata.tf.length; i++){
//                tf += cdata.tf[i] + ",";
//                weights += cdata.tfWeights[i] + ",";
//            }
//            tf+= "\n";
//            weights += "\n";
        }
        
        System.out.println("TF");
        System.out.println("\tD1,D2,D3,Q");        
 
        itr = cosDataSet.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
            System.out.println(key +" = " + cdata.tf[0] + ","+ cdata.tf[1] + ","+ cdata.tf[2]+","+ cdata.tf[3]);
        }
        
        System.out.println();
        System.out.println("Weights");
        System.out.println("\tD1,D2,D3,Q");
        itr = cosDataSet.entrySet().iterator();

        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
            System.out.println(key +" = " + cdata.tfWeights[0] + ","+ cdata.tfWeights[1] + ","+ cdata.tfWeights[2]+","+ cdata.tfWeights[3]);
        }
        
       // new FileWrite( Paths.get(fr.folderPath).resolve("TermFreq.txt").toString(),tf ); 
      // new FileWrite( "C:\\IR\\CAII\\TermFreq.txt",tf ); 
       // new FileWrite( Paths.get(fr.folderPath).resolve("Weights.txt").toString(),weights ); 
     //  new FileWrite( "C:\\IR\\CAII\\Weights.txt",weights );  
       
        itr = cosDataSet.entrySet().iterator();
        denom_Q = Math.sqrt(denom_Q);
        
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
            cdata.NormaliseLengthOFQ(denom_Q);
            
//            normalised += key + "=>";
//            for(int i = 0; i < cdata.tfNormalised.length; i++){
//                normalised += cdata.tfNormalised[i] + ",";
//            }
//            normalised+= "\n";
        }
        
        System.out.println();
        System.out.println("Normalised Weights");
        System.out.println("\tD1,D2,D3,Q");
        itr = cosDataSet.entrySet().iterator();

        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            String key = (String) m.getKey();
            CosineData cdata = (CosineData) m.getValue();
            System.out.println(key +" = " + cdata.tfWeights[0] + ","+ cdata.tfWeights[1] + ","+ cdata.tfWeights[2]+","+ cdata.tfWeights[3]);
        }
       // new FileWrite( Paths.get(fr.folderPath).resolve("Normalised.txt").toString(),normalised ); 
        //new FileWrite( "C:\\IR\\CAII\\Normalised.txt",normalised );
        
        
        Double[] D_Q = new Double[d];
        for(int  i = 0; i < d; i++){
            D_Q[i] = 0d;
        }
        
        itr = cosDataSet.entrySet().iterator();
        int lastIndex = d;
        while(itr.hasNext()){
            Map.Entry m  = (Map.Entry)itr.next();
            CosineData data = (CosineData) m.getValue();
            for(int i = 0; i < data.tfNormalised.length - 1; i++){
                D_Q[i] +=  data.tfNormalised[i] * data.tfNormalised[lastIndex] ;
            }
        }
        
        System.out.println();
        List<Double> indexMap = new ArrayList<>();
        for(int i = 0; i < D_Q.length; i++){
            D_Q[i] = Math.toDegrees( Math.acos(D_Q[i]) );
            indexMap.add( D_Q[i]);
            System.out.println("Similarity Coefficient for D"+(i+1) + ",Q = "+D_Q[i]);
        }
        
        Arrays.sort(D_Q); 
        
        String order = "";
        
        for(int i = 0; i < D_Q.length; i++){
            if(i != 0 )
                order += " > ";
            
            order += fileNamesref.get( indexMap.indexOf(D_Q[i]) );
        }
        
        return order;
    }
}