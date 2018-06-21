/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

/**
 *
 * @author Owner
 */
public class MatrixBuilder {
    public static GLFloatMatrix createOrthoMatrix(float left, float right, float far, float near, float bottom, float top) {
        GLFloatMatrix out = new GLFloatMatrix(4, 4);
        out.mat[0][0] = 2.0f/(right-left);
        out.mat[1][1] = 2.0f/(top-bottom);
        out.mat[2][2] = -2.0f/(far-near);
        out.mat[3][3] = 1.0f;
        out.mat[0][3] = -(right+left)/(right-left);
        out.mat[1][3] = -(top+bottom)/(top-bottom);
        out.mat[2][3] = -(far+near)/(far-near);
        return out;
    }
    
}
