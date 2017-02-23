package day10;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class MoreKernels extends Application {
    
    static Image inputImageJfx;
    static MyImage outputImageJfx;
    
    static List<MyImage> undoStack = new ArrayList<MyImage>();
    static List<MyImage> redoStack = new ArrayList<MyImage>();
    

    public static void main(String[] args) throws Exception{
        
        inputImageJfx = new Image(new FileInputStream(System.getProperty("user.home") + "/Desktop/photo.jpg"));;
        
        outputImageJfx = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());;
        
        outputImageJfx.copyFrom(inputImageJfx);
        
        
        
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        //ImageView inputImageView = new ImageView(inputImageJfx);
        ImageView outputImageView = new ImageView(outputImageJfx);
      
        VBox menuPane = new VBox();
        
        for(Method method : outputImageJfx.getClass().getDeclaredMethods())
        {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            
            
            Button button = new Button(method.getName());
            button.setOnAction(
                    new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        try {  
                            
                            MyImage newImage = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());
                            
                            newImage.copyFrom(outputImageJfx);
                            
                            undoStack.add(outputImageJfx);
                            
                            outputImageJfx = newImage;
                            
                            
                            
                            
                            method.invoke(outputImageJfx, new Object[]{1});
                            
                            outputImageView.setImage(outputImageJfx);
                            
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(MoreKernels.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            );
            
            
            menuPane.getChildren().add(button);
        }
        
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: DAE6F3;");
        root.setCenter(outputImageView);
        
        
        root.setLeft(menuPane);
        
        Scene scene = new Scene(root, inputImageJfx.getWidth() + 400, inputImageJfx.getHeight() + 20 );
        
        primaryStage.setTitle("Duplicates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
