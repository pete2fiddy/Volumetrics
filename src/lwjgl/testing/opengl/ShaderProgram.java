package lwjgl.testing.opengl;
import java.io.IOException;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
/**
 *
 * @author Owner
 */
public class ShaderProgram implements Disposable {
    private final int ID;
    private boolean bound = false;
    private Shader[] shaders;
    private HashMap<String, Integer> uniformLocs = new HashMap<String, Integer>();
    
    public ShaderProgram(Shader... shaders) {
        this.shaders = shaders;
        this.ID = glCreateProgram();
        //assumes creator wants all shaders attached to this program
        attachShaders();
        //linking the program makes it ready to be used. Assumes user wants it linked automatically (easy to change later)
        link();
    }
    
    public static ShaderProgram loadFromPaths(String[] paths, int[] shaderTypes) throws IOException {
        Shader[] shaders = new Shader[paths.length];
        for(int i = 0; i < shaders.length; i++) {
            shaders[i] = Shader.loadFromPath(paths[i], shaderTypes[i]);
        }
        return new ShaderProgram(shaders);
    }
    
    
    private void attachShaders(){
        for(Shader s : shaders) {
            s.attach(this.ID);
        }
    }
    
    private void detachShaders(){
        for(Shader s : shaders) {
            s.detach(this.ID);
        }
    }
    
    private void disposeShaders(){
        for(Shader s : shaders) {
            s.dispose();
        }
    }
    
    public void setUniform(String varName, int val) {
        glUniform1i(getUniformLocAndCache(varName), val);
    }

    protected int getUniformLocAndCache(String varName) {
        Integer loc = uniformLocs.get(varName);
        if(loc == null) {
            loc = new Integer(glGetUniformLocation(ID, varName));
            uniformLocs.put(varName, loc);
            return (int)loc;
        }
        return (int)loc;
    }
    
    private void link(){
        glLinkProgram(this.ID);
        if (glGetProgrami(ID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Unable to link shader program");
        }
    }
    
    public void bind() {
        bound = true;
        glUseProgram(ID);
    }
    
    public void unbind() {
        bound = false;
        glUseProgram(0);
    }
    
    @Override
    public void dispose(){
        unbind();
        detachShaders();
        disposeShaders();
        glDeleteProgram(this.ID);
    }
    
    protected boolean getBound(){return bound;}
    protected boolean bindAndGetBound(){
        boolean wasBound = bound;
        bind();
        return wasBound;
    }
    protected int getID(){return ID;}
}
