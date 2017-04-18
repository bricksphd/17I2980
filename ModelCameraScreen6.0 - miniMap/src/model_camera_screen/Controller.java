/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_camera_screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author unouser
 */
public class Controller implements KeyListener{

    private Model model;
    
    List<Integer> keysDown = Collections.synchronizedList(new ArrayList<Integer>());
    

    void setView(Model model) {
        this.model = model;
        
        model.setKeys(keysDown);
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //if(keysDown.contains(e.getKeyCode()))
        //keysDown.remove(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
     
        if(!keysDown.contains(e.getKeyCode()))
            keysDown.add(e.getKeyCode());
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(keysDown.contains(e.getKeyCode()))
            keysDown.remove((Object)e.getKeyCode());
    }
    
    
    
}
