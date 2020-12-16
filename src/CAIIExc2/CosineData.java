/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CAIIExc2;



/**
 *
 * @author 1
 */
public class CosineData{
        int[] tf;
        double[] tfWeights;
        double[] tfNormalised;
        
        public CosineData(int[] tf){
            this.tf = tf;
            tfWeights = new double[tf.length];
            tfNormalised = new double[tf.length];
            
//            for(int i = 0; i < tfTemp.length; i++){
//                tf[i] = tfTemp[i];
//            }
        }
        
        public void AddTF(int index){
            tf[index]++;
        }
        
        public void AddTFAtQ(int count){
            tf[tf.length - 1] = count;
        }
        
        public void CalctfWeights(){
            for(int i = 0 ; i < tf.length -1; i++){
                if(tf[i] == 0)
                    tfWeights[i] = 0;
                else
                    tfWeights[i] = 1 + Math.log10((double)tf[i]);
            }
        }
        
        public void CalctfWeightsofQ(){
            int lastIndex = tfNormalised.length - 1;
            if(tf[lastIndex] == 0)
                tfWeights[lastIndex] = 0;
            else
                tfWeights[lastIndex] = 1 + Math.log10((double)tf[lastIndex]);
        }
        
        public void NormaliseLength(double[] denom){
            for(int i = 0 ; i < tfWeights.length - 1; i++){
                tfNormalised[i] = tfWeights[i] / denom[i];
            }
        }
        
        public void NormaliseLengthOFQ(double denom){
            int lastIndex = tfNormalised.length - 1;
            tfNormalised[lastIndex] = tfWeights[lastIndex] / denom;
        }
        
        public int[] getTf() {
            return tf;
        }
        
        public double returnWeightAtIndex(int index){
            return tfWeights[index];
        }
    }
