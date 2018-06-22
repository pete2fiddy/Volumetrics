/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.input;

import java.util.HashMap;

/**
 *
 * @author Owner
 */
public class InputTokenizer <ActionEnum extends Enum> {
    //a hashmap of keycodes (or codes of some sort) to an action enum
    private HashMap<Integer, ActionEnum> inputToActionMap = new HashMap<Integer, ActionEnum>();
    
    public InputTokenizer(){}
    
    protected void addActionBinding(int code, ActionEnum action) {
        inputToActionMap.put(code, action);
    }
    
    protected ActionEnum getAction(int code) {
        return inputToActionMap.get(code);
    }
}
