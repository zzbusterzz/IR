/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSmoothingByMean;

import DataCleaningMeanofClassifier.*;

/**
 *
 * @author KillerMech
 */
public class MeanCalc {

    public float no;
    public int count;

    public MeanCalc() {
        no = 0;
        count = 0;
    }

    public float getMean() {
        return no / count;
    }
}