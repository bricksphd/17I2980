package fractals;

import java.awt.*;
import javax.swing.*;

public class Carpet extends JFrame {

    public static void main(String[] args) {
        new Carpet();
    }

    public Carpet(){
        
        
        boolean[][] on = new boolean[651][651];
        for(int y = 0; y < 651; y++)
        {
            for(int x = 0; x < 651; x++)
            {
                on[y][x] = true;
            }
        }
        
        recurse(on, 0, 0, 651, 651);
        
        add(new JPanel(){

            @Override
            public void paint(Graphics g) {
                this.setPreferredSize(new Dimension(651, 651));

                super.paint(g); //To change body of generated methods, choose Tools | Templates.

                for(int y = 0; y < 651; y++)
                {
                    for(int x = 0; x < 651; x++)
                    {
                        g.setColor(on[y][x] ? Color.BLACK : Color.WHITE);
                        g.fillRect(x, y, 1, 1);
                    }
                }


            }



        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 750));
        pack();
        setVisible(true);
    }
}
