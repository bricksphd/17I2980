/*
 * Copyright Brian Ricks, PhD, 2016. bricks at unomaha.edu
 */

package model_camera_screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
/* The class Car
*/
class Car
{
    /**
    /* Default constructor
    */
    
    
    protected float x;
    protected float y;
    protected Color color;
    protected String name;
    protected float direction; //heading of the car
    protected float velocity = 0;
    
    public boolean dead = false;

    Car(float x, float y, Color carColor, String carName) {
        setX(x);
        setY(y);
        setColor(carColor);
        setName(carName);
        
        direction = (float)(Math.random() * 2 * Math.PI);
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the direction
     */
    public float getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(float direction) {
        this.direction = direction;
    }

    /**
     * @return the velocity
     */
    public float getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    void move(float f, List<Integer> keysDown) {
        
        
        
        x += Math.cos(direction) * f * 2;
        y += Math.sin(direction) * f * 2;
        
       
        
        
        
    }

    boolean collidesWith(HeroCar myHeroCar) {
        
        List<CollisionSphere> myColliders = new ArrayList<CollisionSphere>();
        List<CollisionSphere> theirColliders = new ArrayList<CollisionSphere>();
        
        myColliders.addAll(this.getColliders());
        theirColliders.addAll(myHeroCar.getColliders());
        
        
        
        for(CollisionSphere mySphere : myColliders)
        {
            for(CollisionSphere theirSphere : theirColliders)
            {
                if(mySphere.collidesWith(theirSphere))
                    return true;
            }
        }
        
        return false;
        
    }

    
    //? extends CollisionSphere
    //Check to make sure that whatever we return, the genric type is or extends CollisionSphere
    protected List<? extends CollisionSphere> getColliders() {
        
        
        List<CollisionSphere> toReturn = new ArrayList<CollisionSphere>();
        
        CollisionSphere a = new CollisionSphere();
        CollisionSphere b = new CollisionSphere();
        
        a.setCenterX(getX() -(float)Math.cos(this.direction) * .75f);
        a.setCenterY(getY() - (float)Math.sin(this.direction) * .75f);
        a.setRadius(.75f);
        
        b.setCenterX(getX()+ (float)Math.cos(this.direction) * .75f);
        b.setCenterY(getY()+ (float)Math.sin(this.direction) * .75f);
        b.setRadius(.75f);
        
        
        toReturn.add(a);
        toReturn.add(b);
        
        
        
        return toReturn;
    }
    
    
}
