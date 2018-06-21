/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;


public abstract class BufferObject implements Disposable {
    private final int ID;
    private boolean bound = false;
    
    //making these final requires extensions to instantiate them
    
   
    private int size, stride, pointer, glDataType, target;
    private boolean normalized;
    
    
    public BufferObject(int target, int glDataType, int size, boolean normalized, int stride, int pointer) {
        ID = glGenBuffers();
        //(target, such as GL_ELEMENT_ARRAY_BUFFER or GL_ARRAY_BUFFER)
        this.target = target;
        //GL_FLOAT, GL_INT, etc.
        this.glDataType = glDataType;
        //Size of a single vector in the vbo
        this.size = size;
        //below not so sure about, haven't needed to use yet
        this.stride = stride;
        this.pointer = pointer;
        this.normalized = normalized;
    }
    
    public void bufferData(int usage) {
        boolean wasBound = bindAndGetBound();
        sendBufferData(usage);
        if(!wasBound) unbind();
    }
    
    /*
    Sends the data in the buffer object into the uniform variable "name" in ShaderProgram program
    */
    //public abstract void setUniform(ShaderProgram program, String name);
    
    /*
    assumes that binding is already correctly implemented and purely buffers the data
    */
    abstract protected void sendBufferData(int usage);
    
    
    public void bind(){
        if(bound) return;
        bound = true;
        glBindBuffer(getTarget(), ID);
    }
    
    /*
    frequently need to track whether VBO was bound before a bind call, so binds VBO and returns whether the
    VBO was already bound
    */
    protected boolean bindAndGetBound() {
        boolean wasBound = bound;
        bind();
        return wasBound;
    }
    
    public void unbind(){
        if(!bound) return;
        bound = false;
        glBindBuffer(getTarget(), 0);
    }
    
    protected boolean unbindAndGetBound() {
        boolean wasBound = bound;
        unbind();
        return wasBound;
    }
    
    @Override
    public void dispose(){glDeleteBuffers(ID);}
    protected int getGlDataType() {return glDataType;}
    protected int getSize() {return size;}
    protected int getStride() {return stride;}
    protected int getPointer() {return pointer;}
    protected boolean getNormalized() {return normalized;}
    protected int getTarget() {return target;}
}




