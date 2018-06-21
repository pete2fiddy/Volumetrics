/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Owner
 */
public class GLDebug {
    public static boolean printIfError(String msg) {
        int err = glGetError();
        if(err != GL_NO_ERROR) {
            System.out.println("GL ERROR: " + Integer.toString(err));
            System.out.println("Passed message: " + msg);
            return true;
        }
        return false;
    }
}
