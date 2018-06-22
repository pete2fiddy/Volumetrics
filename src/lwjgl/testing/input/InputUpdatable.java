/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.input;

/**
 *
 * @author Owner
 */
interface InputUpdatable {
    
    /*
    performs input-related updates, using the number of ms between frames if necessary
    */
    public void update(long frameNanoDelta);
    
}
