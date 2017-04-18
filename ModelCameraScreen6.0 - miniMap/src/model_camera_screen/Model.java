/*
 * Copyright Brian Ricks, PhD, 2016. bricks at unomaha.edu
 */

package model_camera_screen;

import java.util.ArrayList;
import java.util.List;

/**
/* The class Model
*/
public class Model
{
    
    List<Car> cars = new ArrayList<Car>();
    private Controller controller;
    private List<Integer> keysDown;

    void update() {
        
        HeroCar myHeroCar = null;
        
        for(Car car : cars)
        {
            car.move(.01f, keysDown);
            if(car instanceof HeroCar)
                myHeroCar = (HeroCar)car;
        }
        
        for(Car car : cars)
        {
            if(!(car instanceof HeroCar))
            {
                if(car.collidesWith(myHeroCar))
                {
                    car.dead = true;
                    myHeroCar.maxVelocity++;
                }
            }
        }
        
        cars.removeIf(car -> car.dead);
    }

    void setController(Controller controller) {
        
        this.controller = controller;
    }

    void setKeys(List<Integer> keysDown) {
        this.keysDown = keysDown;
    }
    
    
}
