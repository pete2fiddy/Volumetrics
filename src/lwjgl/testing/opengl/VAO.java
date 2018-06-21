/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/*
wasn't sure where to draw the line on how "binds and unbinds" should be called (within class automatically, or outside).
landed on all things completely handled within class would call them for you, then set bind status to whatever was set
previously
*/
public class VAO implements Disposable {
    private final int ID;
    private boolean bound = false;
    //list of vbos indexed into by location in the shader NEEDS A WAY TO HANDLE VBOS THAT DON'T APPEAR IN GLSL CODE
    private BufferObject[] vbos;
    
    public VAO(BufferObject... vbos) {
        ID = glGenVertexArrays();
        this.vbos = vbos;
    }
    
    public VAO(int numLocs) {
        ID = glGenVertexArrays();
        vbos = new BufferObject[numLocs];
    }
    
    public void setBufferObject(int loc, BufferObject vbo){
        bind();
        setVertexAttribPointer(loc, vbo);
        vbos[loc] = vbo;
        unbind();
    }
    
    /*
    If enabled, the values in the generic vertex attribute array will be
    accessed and used for rendering when calls are made to vertex array commands
    such as glDrawArrays, glDrawElements, glDrawRangeElements, glMultiDrawElements,
    or glMultiDrawArrays.
    */
    public void enableVertexAttribArray(int loc) {
        boolean wasBound = bindAndGetBound();
        glEnableVertexAttribArray(loc);
    }
    
    public void enableAllVertexAttribArray() {
        for(int i = 0; i < vbos.length; i++) {
            enableVertexAttribArray(i);
        }
    }
    
    /*
    If enabled, the values in the generic vertex attribute array will be
    accessed and used for rendering when calls are made to vertex array commands
    such as glDrawArrays, glDrawElements, glDrawRangeElements, glMultiDrawElements,
    or glMultiDrawArrays.
    */
    public void disableVertexAttribArray(int loc) {
        glDisableVertexAttribArray(loc);
    }
    
    public void disableAllVertexAttribArray() {
        for(int i = 0; i < vbos.length; i++) {
            disableVertexAttribArray(i);
        }
    }
    
    private void setVertexAttribPointer(int loc, BufferObject vbo) {
        boolean wasBound = vbo.bindAndGetBound();
        glVertexAttribPointer(loc, vbo.getSize(), vbo.getGlDataType(), vbo.getNormalized(), vbo.getStride(), vbo.getPointer());
        if(!wasBound) vbo.unbind();
    }
   
    protected boolean unbindAndGetBound(){
        boolean wasBound = bound;
        unbind();
        return wasBound;
    }
    public void unbind(){
        bound = false;
        glBindVertexArray(0);
    }
    protected boolean bindAndGetBound(){
        boolean wasBound = bound;
        bind();
        return wasBound;
    }
    public void bind(){
        bound = true;
        glBindVertexArray(ID);
    }
    
    private void unbindAll(){
        unbind();
        for(BufferObject vbo : vbos) {
            vbo.unbind();
        }
    }
    
    @Override
    public void dispose(){
        unbindAll();
        glDeleteVertexArrays(ID);
        for(BufferObject vbo : vbos) {
            vbo.dispose();
        }
    }
}
