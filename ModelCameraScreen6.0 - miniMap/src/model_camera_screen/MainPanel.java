package model_camera_screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    /**
    /* Default constructor
    */
    Model model;
   
    //Where the mouse last was. Helps us see how the user has moved the cursor
    Point lastMouseLocation = null;
    
    //Where in world space is the camera center?
    float cameraX = 0;
    float cameraY = 0;
    
    //How zoomed in the camera
    float cameraZoom = 10;
    
    //The controller for our model
    private Controller controller;  
    
    
    public MainPanel()
    {
        //The preferred size of the panel
        this.setPreferredSize(new Dimension(400, 400));

        //hook up the mouse events to be caught by this panel. 
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this); 
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        
        Graphics2D g2d = (Graphics2D)g;
        
        //Turn on antialiasing...
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        ///Fill the background
        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        mainView(g2d);
        
        miniMapView(g2d);
    }

    private void mainView(Graphics2D g2d) {
        //Get the original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        //Create the transform to recenter the camera
        //and scale and invert y (we want y up)
        AffineTransform worldToCamera = new AffineTransform();
        
        worldToCamera.translate(
                this.getWidth()/2, 
                this.getHeight()/2
        );
        
        worldToCamera.scale(cameraZoom, -cameraZoom);
        
        worldToCamera.translate(
                (-cameraX), 
                (-cameraY )
        );
        
        g2d.setTransform(worldToCamera);
        {
            for(Car car : model.cars)
            {
                g2d.setColor(car.getColor());
                
                
                AffineTransform rotatedTransform = new AffineTransform(worldToCamera);
                rotatedTransform.translate(car.getX(),car.getY());
                rotatedTransform.rotate(car.getDirection());
                rotatedTransform.translate(-car.getX(), -car.getY());
                
                
                g2d.setTransform(rotatedTransform);
                {

                    Shape carOutline = new Rectangle2D.Float(car.getX() - 1.5f, car.getY() - .75f, 3, 1.5f);

                    g2d.fill(carOutline);
                }
                g2d.setTransform(worldToCamera);
                
                
                if(car instanceof HeroCar)
                {
                    HeroCar heroCar = (HeroCar)car;
                    

                    
                    float diffX = Math.abs(heroCar.getX() - cameraX);
                    if( diffX > 10)
                    {
                        if(heroCar.getX() > cameraX)
                            cameraX += (diffX - 10);
                        if(heroCar.getX() < cameraX)
                            cameraX -= (diffX - 10);
                        
                        

                    }

                    float diffY = Math.abs(heroCar.getY() - cameraY);
                    if(diffY > 10)
                    {
                        if(heroCar.getY() > cameraY)
                            cameraY += (diffY - 10);
                        if(heroCar.getY() < cameraY)
                            cameraY -= (diffY - 10);
                    }
                }
            }
        }
        g2d.setTransform(originalTransform);        
    }
    
    private void miniMapView(Graphics2D g2d) {
        
        g2d.setColor( new Color(1, 1, 1, .5f));
        
        g2d.fillRect(this.getWidth() - 100, this.getHeight() - 100, 100, 100);
        
        g2d.setClip(this.getWidth() - 100, this.getHeight() - 100, 100, 100);
        
        //Get the original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        //Create the transform to recenter the camera
        //and scale and invert y (we want y up)
        AffineTransform worldToCamera = new AffineTransform();
        
        worldToCamera.translate(this.getWidth() - 50,this.getHeight()- 50);
        
        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        
        for(Car car : model.cars)
        {
            if(car.getX() < minX)
                minX = car.getX();
            if(car.getX() > maxX)
                maxX = car.getX();
            if(car.getY() < minY)
                minY = car.getY();
            if(car.getY() > maxY)
                maxY = car.getY();
        }
        
        float xDistance = maxX - minX;
        float yDistance = maxY - minY;
        
        
        
        xDistance *= 1.1f;
        yDistance *= 1.1f;
        
        xDistance = Math.max(xDistance, yDistance);
        yDistance = Math.max(xDistance, yDistance);
        
        
        float centerX = (maxX + minX)/2.0f;
        float centerY = (maxY + minY)/2.0f;
        
        
        
        //xDistnace in meters, add 100 pixels, I want to know meters/pixel
        
        worldToCamera.scale(100/xDistance, -100/yDistance);
        
        worldToCamera.translate(
                (-centerX), 
                (-centerY )
        );
        
        g2d.setTransform(worldToCamera);
        {
            for(Car car : model.cars)
            {
                g2d.setColor(car.getColor());
                
                
                AffineTransform rotatedTransform = new AffineTransform(worldToCamera);
                rotatedTransform.translate(car.getX(),car.getY());
                rotatedTransform.rotate(car.getDirection());
                rotatedTransform.translate(-car.getX(), -car.getY());
                
                
                g2d.setTransform(rotatedTransform);
                {

                    Shape carOutline = new Rectangle2D.Float(car.getX() - 1.5f, car.getY() - .75f, 3, 1.5f);

                    g2d.fill(carOutline);
                }
                g2d.setTransform(worldToCamera);
                
                
                if(car instanceof HeroCar)
                {
                    HeroCar heroCar = (HeroCar)car;
                    

                    
                    float diffX = Math.abs(heroCar.getX() - cameraX);
                    if( diffX > 10)
                    {
                        if(heroCar.getX() > cameraX)
                            cameraX += (diffX - 10);
                        if(heroCar.getX() < cameraX)
                            cameraX -= (diffX - 10);
                        
                        

                    }

                    float diffY = Math.abs(heroCar.getY() - cameraY);
                    if(diffY > 10)
                    {
                        if(heroCar.getY() > cameraY)
                            cameraY += (diffY - 10);
                        if(heroCar.getY() < cameraY)
                            cameraY -= (diffY - 10);
                    }
                }
            }
        }
        g2d.setTransform(originalTransform);        
    }

    
    @Override
    public void mousePressed(MouseEvent e) {
       
        lastMouseLocation = e.getPoint();
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
        int diffX = lastMouseLocation.x - e.getPoint().x;
        int diffY = lastMouseLocation.y - e.getPoint().y;
        
        cameraX += diffX/cameraZoom;
        cameraY -= diffY/cameraZoom;
        
        lastMouseLocation = e.getPoint();
        
        repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Don't add and subtract from zoom!!!!
        //Instead, multiply
        
        if(e.getPreciseWheelRotation() > 0)
        {
            cameraZoom /= 1.1;
        }
        else if(e.getPreciseWheelRotation() < 0)
        {
            cameraZoom *= 1.1;
        }        
        repaint();
    }

    void setModel(Model model) {
        this.model = model;
    }

    void setModelAndController(Model model, Controller controller) {
        setModel(model);
        this.controller = controller;
        this.addKeyListener(this.controller);
    }
    
    @Override public void mouseReleased(MouseEvent e) {}

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}    
}
