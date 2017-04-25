package fractals;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Mountains extends JFrame {

    private void recurse(int iteration, int startPoint, int endPoint) {
        
        
    }
    
    public class Point{
        public int x; public int y;
        
        public Point(int inX, int inY){this.x = inX; this.y = inY;}
    }

    public static void main(String[] args) {
        new Mountains();
    }

    
    java.util.List<Point> points;
    
    final int maxRecurse = 1;
    
    final int w = getWidth();
    final int h = getHeight();
    
    public Mountains(){
        points = new ArrayList<Point>();
        
        
        
        points.add(new Point(0, h/2));
        points.add(new Point(w - 1, h/2));
        
        recurse(0, 0, 1 );
        
       
        
        add(new JPanel(){

            @Override
            public void paint(Graphics g) {
                this.setPreferredSize(new Dimension(651, 651));

                super.paint(g); //To change body of generated methods, choose Tools | Templates.

                


            }



        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 750));
        pack();
        setVisible(true);
    }

    
   
}
