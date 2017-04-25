/*
 * Copyright Brian Ricks, PhD, 2016. bricks at unomaha.edu
 */
package model_camera_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author bricks
 */
public class MainFrame extends JFrame implements ActionListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        
        MainFrame andrew = new MainFrame();
    }
    
    Model model;
    
    MainPanel panel;
    
    Controller controller;

    public MainFrame()  {
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Car");
        
        model = new Model();
        controller = new Controller();
        panel = new MainPanel();
        
        model.setController(controller);
        panel.setModelAndController(model, controller);
        controller.setView(model);
        
        int x = 0;
        int y = 0;
        for(int inc = 0; inc < 30 /*or 3 */; inc++)
        {
            model.cars.add(new Car(x * 5, y * 5, Color.BLACK, "Sports Car"));
            x++;
            if(x >= 5)
            {
                x = 0;
                y++;
            }
        }
        model.cars.add(new HeroCar(0, 0, Color.YELLOW, "Sports Car"));
        
        
        
        
        
        
        this.add(panel, BorderLayout.CENTER);
        this.pack();
        
        
        Timer timer = new Timer(10, this);
        timer.setDelay(10);
        timer.start();
        
        this.addKeyListener(controller);
        
        this.add(new JTextField(), BorderLayout.SOUTH);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.update();
        panel.repaint();
    }
    
    
    
    
    
}
