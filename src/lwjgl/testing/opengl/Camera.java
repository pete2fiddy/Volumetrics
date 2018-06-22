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
public interface Camera {
    //returns the matrix that shifts position, rotation, etc. (so cameras don't have to only work with one type of 
    //projection matrix)
    public float[][] getLookAtMat();
}
