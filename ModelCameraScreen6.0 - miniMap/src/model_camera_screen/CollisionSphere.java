/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_camera_screen;

/**
 *
 * @author unouser
 */
class CollisionSphere {
    
    private float centerX;
    private float centerY;
    private float radius;

    /**
     * @return the centerX
     */
    public float getCenterX() {
        return centerX;
    }

    /**
     * @param centerX the centerX to set
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * @return the centerY
     */
    public float getCenterY() {
        return centerY;
    }

    /**
     * @param centerY the centerY to set
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    boolean collidesWith(CollisionSphere theirSphere) {
        
        
        float actualDistance = (float)Math.sqrt(
                Math.pow((float)(this.centerX - theirSphere.centerX), 2)+
                Math.pow((float)this.centerY - theirSphere.centerY, 2)
        );
        
        float minimumDistance = this.radius + theirSphere.radius;
        
        return actualDistance < minimumDistance;
        
    }
    
    
    
}
