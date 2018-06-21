package lwjgl.testing.opengl;

import java.io.IOException;
import lwjgl.testing.util.FileUtil;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author Owner
 */
public class Shader implements Disposable {
    private final int SHADER_TYPE;
    private final int ID;
    private String shaderSource;
    
    public Shader(String shaderSource, int shaderType) {
        this.shaderSource = shaderSource;
        this.SHADER_TYPE = shaderType;
        ID = glCreateShader(this.SHADER_TYPE);
        compile();
    }
    
    public static Shader loadFromPath(String path, int shaderType) throws IOException {
        return new Shader(FileUtil.loadAsString(path), shaderType);
    }
    
    private void compile() {
        //links the shader ID to the corresponding shader code
        glShaderSource(ID, shaderSource);
        glCompileShader(ID);
        if (glGetShaderi(ID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating vertex shader \n" + glGetShaderInfoLog(ID, glGetShaderi(ID, GL_INFO_LOG_LENGTH)));
        }
    }
    
    public void attach(int programId) {
        glAttachShader(programId, this.ID);
    }
    
    public void detach(int programId) {
        glDetachShader(programId, this.ID);
    }
    
    @Override
    public void dispose() {
        glDeleteShader(this.ID);
    }
}
