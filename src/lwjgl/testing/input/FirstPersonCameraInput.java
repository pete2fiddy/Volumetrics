/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.input;

import java.util.Arrays;
import java.util.Iterator;
import static lwjgl.testing.input.FirstPersonCameraInput.KeyAction.*;
import lwjgl.testing.math.TimeMath;
import lwjgl.testing.opengl.Camera;
import lwjgl.testing.math.MatrixBuilder;
import lwjgl.testing.math.MatrixMath;
import lwjgl.testing.math.VectorMath;
import static org.lwjgl.glfw.GLFW.*;


/*
All implementations contain a projection matrix that gets edited by input from the user (e.g. rotating camera, etc.)
*/
public class FirstPersonCameraInput extends WindowInput implements Camera {
    private InputTokenizer<KeyAction> keyTokenizer = new InputTokenizer<KeyAction>();
    private float[] position = new float[3];
    private float[] velocitiesPerSec = {1f, 1f, 1f};
    
    private float[] rotations = new float[3];
    private float[] rotationRadsPerPixelPerSec = {0.1f,0.1f,0.1f};
    //needs to be set more smartly (get info about window, then set to center)
    private float[] CURSOR_SET_POS = {500f, 500f};

    public FirstPersonCameraInput(long windowId) {
        super(windowId);
        initTokenizers();
    }
    
    private void initTokenizers(){
        keyTokenizer.addActionBinding(GLFW_KEY_W, KeyAction.MOVE_FORWARD);
        keyTokenizer.addActionBinding(GLFW_KEY_A, KeyAction.MOVE_LEFT);
        keyTokenizer.addActionBinding(GLFW_KEY_S, KeyAction.MOVE_BACKWARD);
        keyTokenizer.addActionBinding(GLFW_KEY_D, KeyAction.MOVE_RIGHT);
    }


    private void updatePosition(double frameSecDelta) {
        //crashes with nullpointer when nonspecified key pressed
        Iterator<Integer> pressedKeyIterator = getPressedKeyIterator();
        while(pressedKeyIterator.hasNext()) {
            switch(keyTokenizer.getAction((int)pressedKeyIterator.next())) {
                case MOVE_FORWARD:
                    position[2] -= frameSecDelta*velocitiesPerSec[2];
                    break;
                case MOVE_BACKWARD:
                    position[2] += frameSecDelta*velocitiesPerSec[2];
                    break;
                case MOVE_LEFT:
                    position[0] -= frameSecDelta*velocitiesPerSec[0];
                    break;
                case MOVE_RIGHT:
                    position[0] += frameSecDelta*velocitiesPerSec[0];
                    break;
            }
        }
    }
    
    private void updateRotations(double frameSecDelta) {
        //need to lock up and down rotations
        float[] mouseDelta = VectorMath.subtract(getMousePos(), CURSOR_SET_POS);
        float[] axisRotations = new float[]{mouseDelta[1], mouseDelta[0], 0};
        
        rotations = VectorMath.add(rotations, 
                VectorMath.hadamardProd(axisRotations, VectorMath.multiply((float)frameSecDelta, rotationRadsPerPixelPerSec)));
        glfwSetCursorPos(getWindowId(), (int)CURSOR_SET_POS[0], (int)CURSOR_SET_POS[1]);
    }
    
    @Override
    public void update(long frameNanoDelta) {
        double frameSecDelta = TimeMath.nanoToSeconds(frameNanoDelta);
        updatePosition(frameSecDelta);
        updateRotations(frameSecDelta);
    }
    
    @Override
    public float[][] getLookAtMat() {
        float[][] translationMat = MatrixBuilder.createTranslationMatrix(position);
        float[][] rotMat = MatrixBuilder.createAffineRotationMatrix3d(rotations);
        return MatrixMath.multiply(translationMat, rotMat);
    }
    
    enum KeyAction {
        MOVE_FORWARD, MOVE_BACKWARD, MOVE_LEFT, MOVE_RIGHT;
    }
    
}
