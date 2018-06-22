/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.input;

import java.util.HashSet;
import java.util.Iterator;
import org.lwjgl.glfw.Callbacks;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Owner
 */
public abstract class WindowInput implements InputUpdatable {
    
    private final long WINDOW_ID;
    
    private double scrollX, scrollY, mouseDx, mouseDy;
    private float[] mousePos = new float[2];
    private HashSet<Integer> mousePresses = new HashSet<Integer>();
    private HashSet<Integer> pressedKeys = new HashSet<Integer>();
    
    
    
    public WindowInput(long windowId) {
        this.WINDOW_ID = windowId;
        setCallbacks();
    }
    
    private void setCallbacks() {
        glfwSetMouseButtonCallback(WINDOW_ID, (long window, int button, int action, int mods) ->{
            assert (window == WINDOW_ID);
            notifyMouseButton(button, action, mods);//glfwMouseButtonCallback(button, action, mods);   
        });
        
        glfwSetCursorPosCallback(WINDOW_ID, (long window, double xpos, double ypos) -> {
            assert (window == WINDOW_ID);
            notifyCursorPos(xpos, ypos);
        });
        
        glfwSetKeyCallback(WINDOW_ID, (long window, int key, int scancode, int action, int mods) -> {
            assert (window == WINDOW_ID);
            notifyKeyPress(key, scancode, action, mods);
        });
        
        glfwSetScrollCallback(WINDOW_ID, (long window, double xOffset, double yOffset) -> {
            assert (window == WINDOW_ID);
            notifyScroll(xOffset, yOffset);
        });
    }
    
    /*implementation handles cursor movement*/
    private void notifyCursorPos(double xPos, double yPos){
        mousePos[0] = (float)xPos;
        mousePos[1] = (float)yPos;
    }
    
    /* implementation handles mouse scrolling and what to do*/
    private void notifyScroll(double xOffset, double yOffset){
        this.scrollX = xOffset;
        this.scrollY = yOffset;
    }
    
    
    /*implementation handles mouse button input*/
    private void notifyMouseButton(int button, int action, int mods){
        if(action == GLFW_PRESS) {
            mousePresses.add(button);
        } else {
            mousePresses.remove(button);
        }
    }
        
    
    /*
    handles keypresses
    */
    private void notifyKeyPress(int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS) {
            pressedKeys.add(key);
        } else if (action == GLFW_RELEASE) {
            pressedKeys.remove(key);
        } else {
            //for repeat
        }
    }
    
    protected Iterator<Integer> getPressedKeyIterator() {return pressedKeys.iterator();}
    protected float[] getMousePos(){return (float[])mousePos.clone();}
    protected long getWindowId(){return WINDOW_ID;}
    
}
