/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.math;

import lwjgl.testing.opengl.GLFloatMatrix;

/**
 *
 * @author Owner
 */
public class MatrixBuilder {
    public static GLFloatMatrix createGLOrthoMatrix(float left, float right, float far, float near, float bottom, float top) {
        return new GLFloatMatrix(createOrthoMatrix(left, right, far, near, bottom, top));
    }
    
    public static float[][] createOrthoMatrix(float left, float right, float far, float near, float bottom, float top) {
        float[][] out = new float[4][4];
        out[0][0] = 2.0f/(right-left);
        out[1][1] = 2.0f/(top-bottom);
        out[2][2] = -2.0f/(far-near);
        out[3][3] = 1.0f;
        out[0][3] = -(right+left)/(right-left);
        out[1][3] = -(top+bottom)/(top-bottom);
        out[2][3] = -(far+near)/(far-near);
        return out;
    }
    
    public static GLFloatMatrix createGLIdentity(int size) {
        return new GLFloatMatrix(createIdentity(size));
    }
    
    public static float[][] createIdentity(int size) {
        float[][] out = new float[size][size];
        for(int i = 0; i < size; i++) {
            out[i][i] = 1.0f;
        }
        return out;
    }
    
    public static GLFloatMatrix createGLPerspectiveMatrix(float fovDeg, float far, float near) {
        return new GLFloatMatrix(createPerspectiveMatrix(fovDeg, far, near));
    }
    
    public static float[][] createPerspectiveMatrix(float fovDeg, float far, float near) {
        float[][] out = new float[4][4];
        float s = 1.0f/(float)(Math.tan((Math.PI/180.0)*(fovDeg/2.0f)));
        out[0][0] = s;
        out[1][1] = s;
        out[2][2] = far/(far-near);
        out[2][3] = 1f;
        out[3][2] = -(far*near)/(far-near);
        return out;
    }
    
    public static float[][] createTranslationMatrix(float[] translate) {
        float[][] out = createIdentity(translate.length + 1);
        for(int i = 0; i < translate.length; i++){
            out[i][out[i].length-1] = translate[i];
        }
        return out;
    }
    
    public static float[][] createRotationMatrix3d(float[] rots) {
        float[][] rotX = {{1f,0f,0f},
            {0f,(float)Math.cos(rots[0]),(float)-Math.sin(rots[0])},
            {0f,(float)Math.sin(rots[0]),(float)Math.cos(rots[0])}};
        float[][] rotY = {{(float)Math.cos(rots[1]),0f,(float)Math.sin(rots[1])},
            {0f,1f,0f},
            {(float)-Math.sin(rots[1]),0f,(float)Math.cos(rots[1])}};
        float[][] rotZ = {{(float)Math.cos(rots[2]),(float)-Math.sin(rots[2]),0f},
            {(float)Math.sin(rots[2]),(float)Math.cos(rots[2]),0f},
            {0f,0f,1f}};
        return MatrixMath.multiply(rotX,rotY,rotZ);
    }
    
    public static float[][] createAffineRotationMatrix3d(float[] rots) {
        float[][] rotMat = createRotationMatrix3d(rots);
        float[][] affRotMat = new float[4][4];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                affRotMat[i][j] = rotMat[i][j];
            }
        }
        affRotMat[3][3] = 1f;
        return affRotMat;
    }
    
}
