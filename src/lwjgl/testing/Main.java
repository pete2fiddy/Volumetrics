package lwjgl.testing;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import lwjgl.testing.input.FirstPersonCameraInput;
import lwjgl.testing.input.WindowInput;
import lwjgl.testing.math.MatrixMath;
import lwjgl.testing.opengl.IndicesBufferObject;
import lwjgl.testing.opengl.CoordsBufferObject;
import lwjgl.testing.opengl.BufferObject;
import lwjgl.testing.opengl.GLDebug;
import lwjgl.testing.opengl.GLFloatMatrix;
import lwjgl.testing.opengl.GLTexture;
import lwjgl.testing.math.MatrixBuilder;
import lwjgl.testing.opengl.ShaderProgram;
import lwjgl.testing.opengl.VAO;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL43.*;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author Owner
 */
public class Main {

    static {
        //sets System.err to be the console output for errors
        GLFWErrorCallback.createPrint(System.err).set();
        initGLFW();
        setWindowHints();
        
    }
    private static GLFloatMatrix projMat;
    private static GLTexture texture;
    private static CoordsBufferObject vbo;
    private static IndicesBufferObject ibo;
    private static CoordsBufferObject tbo;
    private static VAO vao;
    //private static int vaoId;//, vboId;
    private static long windowId;
    private static ShaderProgram shaderProgram;
    private static FirstPersonCameraInput fpInput;
    private static long lastFrameNanoTime = System.nanoTime();
    
    
    /*
    Following this tutorial series: https://goharsha.com/lwjgl-tutorial-series/
    
    Did not really implement any of this (don't have a use for it yet), but if needed: https://goharsha.com/lwjgl-tutorial-series/window-callbacks/
    (contains stuff like window focus notifying, etc.)
    
    http://wiki.lwjgl.org/wiki/Using_Vertex_Buffer_Objects_(VBO).html: gives use of buffer types
    
    General tip: if a method requires something that is not specified in the method body, it means that thing must be bound (e.g. 
    binding ibo before calling drawElements)
     */
    public static void main(String[] args) {
        //NOTES: I THINK you can pass indices to a vertex shader by just calling glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, loc)
        //since it specifies how "elements" are segmented from the vertices
        
        //make sure I correctly dispose/cleanup everything as I go -- try to think of a cleaner way of doing so
        
        createWindow();
        doRenderPrereqs();
        fpInput = new FirstPersonCameraInput(windowId);
        initProgram();
        prepareProgram();
        startRendering();
        dispose();
        glfwTerminate();
    }

    private static void doRenderPrereqs() {
        glfwMakeContextCurrent(windowId);
        
        
        //links lwjgl OpenGL with . OpenGL calls MUST go after this is called.
        GL.createCapabilities();
        
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_ALWAYS); 
        glDisable(GL_BLEND);
        
        glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

        //context should refresh immediately when the buffers are swapped.
        glfwSwapInterval(1);
    }

    private static void createWindow() {
        windowId = glfwCreateWindow(1024, 768, "hello", NULL, NULL);//first null specifies monitor doesn't matter, second says no shared context between windows (windows don't talk to each other/mirror each other)
        glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        if (windowId == NULL) {
            System.err.println("Error creating window");
            System.exit(1);
        }
    }

    private static void setWindowHints() {
        //specifies OpenGL should use new OpenGL stuff using window hints
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        // before context creation
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    private static void initGLFW() {
        //glfw must be initialized first. calling glfwInit() returns whether the initialization was successful
        if (!glfwInit()) {
            System.err.println("Error initializing GLFW");
            System.exit(1);
        }
    }
    
    private static void initProgram() {
        String basePath = "C:\\Users\\Owner\\Documents\\NetBeansProjects\\LWJGL Testing\\build\\classes\\lwjgl\\testing\\opengl\\shaders\\";
        String[] paths = {basePath + "2DTriangleVertexShader.vert", basePath + "2DTriangleFragmentShader.frag"};
        int[] shaderTypes = {GL_VERTEX_SHADER, GL_FRAGMENT_SHADER };
        try {
            shaderProgram = ShaderProgram.loadFromPaths(paths, shaderTypes);
        } catch (IOException ex) {
            System.err.println("Error in loading shaders in initProgram()");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            shaderProgram = null;
        }
    }
    
    private static void prepareProgram(){
        vao = new VAO(3);
        
        float[][] vertices = new float[][] {
            {-0.5f, 0.5f, 3f},
            {-0.5f, -0.5f, 3f},
            {0.5f, -0.5f, 3f},
            {0.5f, 0.5f, 3f}
        };
        
        float[][] texCoords = new float[][] {
            {0,0},
            {0,1},
            {1,1},
            {1,0}
        };
        
        int[][] indices = new int[][] {
            {0,1,3},
            {3,1,2}
        };
        
        projMat = MatrixBuilder.createGLPerspectiveMatrix(90f, 10f, 1f);//MatrixBuilder.createGLOrthoMatrix(-1f, 1f, -1f, 1f, -1f, 1f);////
        projMat.setUniform(shaderProgram, "projMat");
        vbo = new CoordsBufferObject(vertices, false, 0, 0);
        tbo = new CoordsBufferObject(texCoords, false, 0, 0);
        ibo = new IndicesBufferObject(indices, false, 0, 0);
        
        try {
            
            texture = GLTexture.loadFromPath("C:\\Users\\Owner\\Documents\\NetBeansProjects\\LWJGL Testing\\build\\classes\\lwjgl\\testing\\textures\\brick_texture.jpg");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Error in loading texture image");
            System.exit(1);
        }
        
        vao.setBufferObject(0, vbo);
        vao.setBufferObject(1, tbo);
        vao.setBufferObject(2, ibo);
        vbo.bufferData(GL_STATIC_DRAW);
        tbo.bufferData(GL_STATIC_DRAW);
        ibo.bufferData(GL_STATIC_DRAW);
    }
    
    private static void startRendering() {
        
        while(!glfwWindowShouldClose(windowId)){
            //updates events like input (mouse, keyboard, etc.)
            glfwPollEvents();
            render();
            glfwSwapBuffers(windowId);
        }
        
        glfwDestroyWindow(windowId);
        
    }
    
    private static void render() {
        
        //clears both the color buffer and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        shaderProgram.bind();
        glActiveTexture(GL_TEXTURE0);
        texture.bind();
        shaderProgram.setUniform("textureSampler", GL_TEXTURE0);
        vao.bind();
        
        fpInput.update(System.nanoTime() - lastFrameNanoTime);
        
        lastFrameNanoTime = System.nanoTime();
        projMat.mat = MatrixMath.multiply(MatrixBuilder.createPerspectiveMatrix(90, 1f, 10f), fpInput.getLookAtMat());
        projMat.setUniform(shaderProgram, "projMat");
        
        vbo.bufferData(GL_STATIC_DRAW);
        
        vao.enableAllVertexAttribArray();
        ibo.bind();
        glDrawElements(GL_TRIANGLES, ibo.indices.length * ibo.indices[0].length, GL_UNSIGNED_INT, 0);
        ibo.unbind();
        vao.disableAllVertexAttribArray();
        vao.unbind();
        texture.unbind();
        shaderProgram.unbind();
    }
    
    private static void dispose(){
        shaderProgram.dispose();
        vao.dispose();
        
        texture.dispose();
    }
}
