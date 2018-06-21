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
public interface Uniformable {
    /*
    sets the uniform variable "name" in program to the value this Uniformable contains
    */
    public void setUniform(ShaderProgram program, String name);
}
