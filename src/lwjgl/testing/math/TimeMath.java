/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.math;

/**
 *
 * @author Owner
 */
public class TimeMath {
    
    public static double nanoToSeconds(long nano) {
        return (double)nano / 1000000000.0;
    }
}
