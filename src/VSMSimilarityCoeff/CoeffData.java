/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VSMSimilarityCoeff;

/**
 *
 * @author 1
 */
public class CoeffData{
        int[] tf;
        double[] tfidf;
        int df;
        double idf;
        
        public CoeffData(int fileCount){
            tf = new int[fileCount];
            tfidf = new double[fileCount + 1];//+1 is to handle query values which will be refreshed on every new query
            df = 0;
            idf = 0;
        }
        
        public void AddTF(int index){
            tf[index]++;
        }
        
        public void countDF(){
            for(int i = 0; i < tf.length; i++){
                if(tf[i] > 0)
                    df++;
            }
        }
        
        public void countIDF(float d){
          //  Double val = Double.parseDouble((d /  df) + "" );
            idf = Math.log10( d/df );
        }
        
        public void countTFXIDF(){
            for(int i = 0; i < tf.length; i++){
                tfidf[i] = tf[i] * idf;
            }
        }
        
        public void calcQueryTFXIDF(int count){
            tfidf[tfidf.length - 1] = count * idf;
        }
        
        public int[] getTf() {
            return tf;
        }

        public int getDf() {
            return df;
        }

        public double getIdf() {
            return idf;
        }
    }
