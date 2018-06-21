/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author Owner
 */
public class IndicesBufferObject extends BufferObject {
    public int[][] indices;
    
    public IndicesBufferObject(int[][] indices, boolean normalized, int stride, int pointer) {
        super(GL_ELEMENT_ARRAY_BUFFER, GL_INT, indices[0].length, normalized, stride, pointer);
        this.indices = indices;
    }

    
    @Override
    protected void sendBufferData(int usage) {
        int[] flattened = flattenIndices();
        IntBuffer toBuffer = BufferUtils.createIntBuffer(flattened.length);
        toBuffer.put(flattened).flip();
        glBufferData(getTarget(), toBuffer, usage);
    }
    
    private int[] flattenIndices() {
        int[] flattened = new int[indices.length * indices[0].length];
        for(int i = 0; i < indices.length; i++) {
            for(int j = 0; j < indices[i].length; j++) {
                flattened[i*indices[i].length + j] = indices[i][j];
            }
        }
        return flattened;
    }
}
