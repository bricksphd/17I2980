/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_camera_screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author unouser
 */
public class HeroCar extends Car{
    
    public float maxVelocity = 1;
    
    HeroCar(float x, float y, Color carColor, String carName) {
        super(x, y, carColor, carName);
    }
    
    @Override
    void move(float f, List<Integer> keysDown) {
        
        
        x += Math.cos(direction) * velocity * f;
        y += Math.sin(direction) * velocity * f;
        
        //Speed up the car
        if(keysDown.contains(KeyEvent.VK_UP))
            velocity+= f * 3;
        
        //Slow down the car
        if(keysDown.contains(KeyEvent.VK_DOWN))
            velocity+= f * -3;
        
        //Turn the car right
        if(keysDown.contains(KeyEvent.VK_RIGHT))
                direction += -1 * f * maxVelocity;
        
        //Turn the car left
        if(keysDown.contains(KeyEvent.VK_LEFT))
                direction += 1 * f * maxVelocity;
        
        //Constrain the velocity of the car.
       velocity = maxVelocity;
        
        
        
    }
    
}
