package day10;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                
                if(undoStack.size() > 0)
                {
                    MyImage previousImage = undoStack.get(undoStack.size() - 1);
                    undoStack.remove(undoStack.size() - 1);
                    
                    redoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
                
            }
        });
        menuPane.getChildren().add(undoButton);
        
        Button redoButton = new Button("Redo");
        redoButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(redoStack.size() > 0)
                {
                    MyImage previousImage = redoStack.get(redoStack.size() - 1);
                    redoStack.remove(redoStack.size() - 1);
                    
                    undoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
            }
        });
        menuPane.getChildren().add(redoButton);
        
        menuPane.getChildren().add(new Separator());
        
        
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
                            
                            redoStack.clear();
                            
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
        
        menuPane.getChildren().add(new Separator());
        
        Button reduceButton = new Button("Reduce Colors");
        reduceButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                            
                MyImage newImage = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());

                newImage.copyFrom(outputImageJfx);

                undoStack.add(outputImageJfx);

                redoStack.clear();

                outputImageJfx = newImage;


                //outputImageJfx.reduceColors(4096);

                outputImageView.setImage(outputImageJfx);
                    
            }
            
        
        });
        
        
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
