/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;

public class GLFloatMatrix implements Uniformable {
    public float[][] mat;
    
    public GLFloatMatrix(float[][] mat) {
        this.mat = mat;
    }
    
    public GLFloatMatrix(int rows, int cols) {
        this.mat = new float[rows][cols];
    }
    
    @Override
    public void setUniform(ShaderProgram program, String varName) {
        //reflection used as a small ack to get around typing out a double switch statement
        String methodName = getUniformMethodName();
        Method method = null;
        try {
            System.out.println("method name: " + methodName);
            method = GL20.class.getMethod(methodName, int.class, boolean.class, FloatBuffer.class);
            //count is 1 since there is one matrix
            //not sure if transpose should be GL_FALSE or GL_TRUE
            System.out.println("loc; " + Integer.toString(program.getUniformLocAndCache(varName)));
            method.invoke(null, program.getUniformLocAndCache(varName), false, matToBuffer());
        } catch (Exception e) {
            //error handling a little sloppy here because what this accomplishes is very ridgid and fixed -- it
            //either works or it doesn't. ALso doesn't make more sense to catch it anywhere else.
            System.err.println("Error in GLFloatMatrix setUniform during reflection");
            e.printStackTrace();
        }
    }
    
    private String getUniformMethodName() {
        String methodName = "glUniformMatrix";
        if(mat.length == mat[0].length) {
            methodName += Integer.toString(mat.length) + "fv";
            return methodName;
        }
        methodName += Integer.toString(mat.length) + "x" + Integer.toString(mat[0].length) + "fv";
        return methodName;
    }
    
    private FloatBuffer matToBuffer(){
        float[] flatMat = flattenMat();
        FloatBuffer toBuffer = BufferUtils.createFloatBuffer(flatMat.length);
        toBuffer.put(flatMat).flip();
        return toBuffer;
    }
    
    private float[] flattenMat(){
        float[] flat = new float[mat.length * mat[0].length];
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat[i].length; j++) {
                flat[i*mat[i].length + j] = mat[i][j];
            }
        }
        return flat;
    }
}
