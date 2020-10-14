/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProbabalisticRetrieval;

/**
 *
 * @author 1
 */
public class ProbData {

    int n;
    int r;

    double[] w;

    public ProbData() {
        n = 0;
        r = 0;
        w = new double[4];
    }

    public void CalcTermWeight(int R, int N) {
        double t1 = r + 0.5;
        double t2 = R + 1;
        double t3 = n + 1;
        double t4 = n - r + 0.5;
        double t5 = R - r + 0.5;

        w[0] = Math.log10(
                (t1 / t2)
                / (t3 / (N + 2))
        );
        w[1] = Math.log10(
                (t1 / t2)
                / (t4 / (N - R + 1))
        );
        w[2] = Math.log10(
                (t1 / t5)
                / (t3 / (N - n + 1))
        );
        w[3] = Math.log10(
                (t1 / t5)
                / (t4 / (N - n - (R - r) + 0.5))
        );
    }
}
