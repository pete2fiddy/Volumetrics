/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.input;

import java.util.HashSet;
import org.lwjgl.glfw.Callbacks;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Owner
 */
public class WindowInput {
    
    private final long WINDOW_ID;
    private double mouseX, mouseY, scrollX, scrollY;
    private HashSet<Integer> mousePresses = new HashSet<Integer>();
    private HashSet<Integer> pressedKeys = new HashSet<Integer>();
    
    public WindowInput(long windowId) {
        this.WINDOW_ID = windowId;
        setCallbacks();
    }
    
    private void setCallbacks() {
        glfwSetMouseButtonCallback(WINDOW_ID, (long window, int button, int action, int mods) ->{
            assert (window == WINDOW_ID);
            glfwMouseButtonCallback(button, action, mods);   
        });
        
        glfwSetCursorPosCallback(WINDOW_ID, (long window, double xpos, double ypos) -> {
            assert (window == WINDOW_ID);
            glfwCursorPosCallback(xpos, ypos);
        });
        
        glfwSetKeyCallback(WINDOW_ID, (long window, int key, int scancode, int action, int mods) -> {
            assert (window == WINDOW_ID);
            glfwKeyCallback(key, scancode, action, mods);
        });
        
        glfwSetScrollCallback(WINDOW_ID, (long window, double xOffset, double yOffset) -> {
            assert (window == WINDOW_ID);
            glfwScrollCallback(xOffset, yOffset);
        });
    }
    
    
    private void glfwCursorPosCallback(double xPos, double yPos) {
        this.mouseX = xPos;
        this.mouseY = yPos;
    }
    
    private void glfwScrollCallback(double xOffset, double yOffset) {
        this.scrollX = xOffset;
        this.scrollY = yOffset;
    }
    
    
    private void glfwMouseButtonCallback(int button, int action, int mods) {
        //not sure if this properly autoboxes button -- if messing up, change to new Integer(button)
        if(action == GLFW_PRESS) {
            mousePresses.add(button);
        } else {
            mousePresses.remove(button);
        }
    }
    
    private void glfwKeyCallback(int key, int scancode, int action, int mods) {
        if(action == GLFW_PRESS) {
            pressedKeys.add(key);
        } else {
            //handles both repeat and release (just to make sure no keys "linger" in cache)
            pressedKeys.remove(key);
            if (action == GLFW_REPEAT) {
                //handle repeat keys here
            }
        }
    }
    
}
