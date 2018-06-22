/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.math;

/**
 *
 * @author Owner
 */
public class MatrixMath {
    
     public static float[][] multiply(float[][] leftMat, float[][] rightMat) {
        float[][] newMat = new float[leftMat.length][rightMat[0].length];
        for(int i = 0; i < newMat.length; i++) {
            for(int j = 0; j < newMat[i].length; j++) {
                for(int k = 0; k < leftMat[0].length; k++) {
                    newMat[i][j] += leftMat[i][k] * rightMat[k][j];
                }
            }
        }
        return newMat;
    }
     
    /*assumes all mats are square and of same dimension*/
    public static float[][] multiply(float[][]... mats) {
        float[][] mul = (float[][])mats[0].clone();
        for(int i = 1; i < mats.length; i++) {
            assert(mul.length == mats[i].length && mul[0].length == mats[i][0].length);
            mul = multiply(mul, mats[i]);
        }
        return mul;
    }
}
