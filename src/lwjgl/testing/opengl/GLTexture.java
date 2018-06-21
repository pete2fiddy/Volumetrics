/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.opengl;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import lwjgl.testing.util.FileUtil;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

/**
 *
 * @author Owner
 */
public class GLTexture implements Disposable {
    private static final int BYTES_PER_PIXEL = 4; //3 for RGB, 4 for RGBA
    private final int ID;
    private BufferedImage texture;
    
    /*
    using bufferedimages is supposedly to be avoided with opengl (loads it twice or someting, extra memory?)
    but am using this way for now -- allows for editing the texture in code.
    */
    public GLTexture(BufferedImage img) {
        this.texture = img;
        bind();
        this.ID = glGenTextures();
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureToBuffer());
        glBindTexture(GL_TEXTURE_2D, 0);
        //tells opengl more info about what is stored in GL_TEXTURE_2D (this texture), and buffers
        //the texture into it
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, 
                GL_UNSIGNED_BYTE, textureToBuffer());
        unbind();
    }
    
    public static GLTexture loadFromPath(String path) throws IOException {
        return new GLTexture(FileUtil.loadImage(path));
    }
    
    
    private ByteBuffer textureToBuffer() {
        int[] pixels = new int[texture.getWidth() * texture.getHeight()];
        texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0, texture.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(texture.getWidth() * texture.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
        
        for(int y = 0; y < texture.getHeight(); y++){
            for(int x = 0; x < texture.getWidth(); x++){
                int pixel = pixels[y * texture.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                if(isRGBA()) buffer.put((byte) ((pixel >> 24) & 0xFF));// Alpha component. Only for RGBA
            }
        }
        buffer.flip();
        return buffer;
    }
    
    
    private boolean isRGBA(){return BYTES_PER_PIXEL == 4;}
    @Override
    public void dispose() {
        unbind();
        glDeleteTextures(ID);
    }
    public void bind(){glBindTexture(GL_TEXTURE_2D, ID);}
    public void unbind(){glBindTexture(GL_TEXTURE_2D, 0);}
}
