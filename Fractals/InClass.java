package carpet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;
import javax.swing.*;

public class Carpet extends JFrame {

    public static void main(String[] args) {
        new Carpet();
    }
    
    private void recurse(List<Twig> inList, int startIndex)
    {
        List<Integer> toParse = new ArrayList<Integer>();
        
        toParse.add(0);
        
        while(toParse.size() != 0)
        {
            int parentIndex = toParse.get(0);
            toParse.remove(0);
            
            //Terminating condition
            Twig parentTwig = inList.get(parentIndex);
            if(parentTwig.length() < 4)
                continue;


            //body
            ///Add 2 twigs
            Twig left = new Twig();
            Twig right = new Twig();

            double newTwigLength = parentTwig.length() / 2.0;

            left.startX = parentTwig.endX;
            right.startX = parentTwig.endX;

            left.startY = parentTwig.endY;
            right.startY = parentTwig.endY;

            double parentAngle = parentTwig.getAngle();
            double leftAngle = parentAngle + Math.PI/2.0;
            double rightAngle = parentAngle - Math.PI/2.0;

            left.endX = (int) (left.startX + Math.cos(leftAngle) * newTwigLength);
            right.endX = (int) (right.startX + Math.cos(rightAngle) * newTwigLength);

            left.endY = (int) (left.startY + Math.sin(leftAngle) * newTwigLength);
            right.endY = (int) (right.startY + Math.sin(rightAngle) * newTwigLength);

            inList.add(left);
            int leftIndex = inList.size() - 1;
            inList.add(right);
            int rightIndex = inList.size() - 1;
            
            toParse.add(leftIndex);
            toParse.add(rightIndex);
            
        }
        
        
        
        
        
       
        
    }
    
    private void recurseOld(List<Twig> inList, int parentIndex)
    {
        //Terminating condition
        Twig parentTwig = inList.get(parentIndex);
        if(parentTwig.length() < 4)
            return;
        
        
        //body
        ///Add 2 twigs
        Twig left = new Twig();
        Twig right = new Twig();
        
        double newTwigLength = parentTwig.length() / 2.0;
        
        left.startX = parentTwig.endX;
        right.startX = parentTwig.endX;
        
        left.startY = parentTwig.endY;
        right.startY = parentTwig.endY;
        
        double parentAngle = parentTwig.getAngle();
        double leftAngle = parentAngle + Math.PI/2.0;
        double rightAngle = parentAngle - Math.PI/2.0;
        
        left.endX = (int) (left.startX + Math.cos(leftAngle) * newTwigLength);
        right.endX = (int) (right.startX + Math.cos(rightAngle) * newTwigLength);
        
        left.endY = (int) (left.startY + Math.sin(leftAngle) * newTwigLength);
        right.endY = (int) (right.startY + Math.sin(rightAngle) * newTwigLength);
        
        inList.add(left);
        int leftIndex = inList.size() - 1;
        inList.add(right);
        int rightIndex = inList.size() - 1;
        
        
        
        
        
        
        //recursive call
        recurse(inList, leftIndex);
        recurse(inList, rightIndex);
        
    }

    public Carpet(){
        
        
        List<Twig> twigs = new ArrayList<Twig>();
        
        
        
        
        Twig trunk = new Twig();
        trunk.startX = 300;
        trunk.startY = 650;
        trunk.endX = 300;
        trunk.endY = 300;
        
        twigs.add(trunk);
        
        //Base case
        recurse(twigs, 0);
        
        
        add(new JPanel(){

            @Override
            public void paint(Graphics g) {
                this.setPreferredSize(new Dimension(651, 651));

                for(Twig twig : twigs)
                {
                    g.setColor(Color.GREEN);
                    
                    g.drawLine( twig.startX,
                                twig.startY,
                                twig.endX,
                                twig.endY);
                }
                
                
            }



        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 750));
        pack();
        setVisible(true);
    }

    class Twig {
        public int startX;
        public int startY;
        public int endX;
        public int endY;

        private double length() {;
            
            return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(endY - startY, 2));
            
        }

        private double getAngle() {
            return -Math.atan2(Math.abs(endY - startY), Math.abs(endX - startX));
        }
        
    }
}
