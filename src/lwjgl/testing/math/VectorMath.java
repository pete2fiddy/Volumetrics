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
public class VectorMath {
    
    public static float[] add(float[] v1, float[] v2) {
        assert(v1.length == v2.length);
        float[] out = new float[v1.length];
        for(int i = 0; i < out.length; i++) {
            out[i] = v1[i] + v2[i];
        }
        return out;
    }
    
    public static float[] subtract(float[] v1, float[] v2) {
        assert(v1.length == v2.length);
        float[] out = new float[v1.length];
        for(int i = 0; i < out.length; i++) {
            out[i] = v1[i] - v2[i];
        }
        return out;
    }
    
    /*
    public static float[] hadamardProd(float[] v1, float[] v2) {
        assert(v1.length == v2.length);
        float[] out = new float[v1.length];
        for(int i = 0; i < out.length; i++) {
            out[i] = v1[i] * v2[i];
        }
        return out;
    }
    */
    
    /*
    performs element-wise vector multiplication
    */
    public static float[] hadamardProd(float[]... vecs) {
        float[] out = (float[])vecs[0].clone();
        for(int i = 1; i < vecs.length; i++) {
            assert(out.length == vecs[i].length);
            for(int j = 0; j < out.length; j++) {
                out[j] *= vecs[i][j];
            }
        }
        return out;
    }
    
    public static float[] multiply(float scalar, float[] vec) {
        float[] out = new float[vec.length];
        for(int i = 0; i < out.length; i++) {
            out[i] = scalar * vec[i];
        }
        return out;
    }
}
