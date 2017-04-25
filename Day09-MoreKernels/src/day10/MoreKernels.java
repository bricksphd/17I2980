package day10;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MoreKernels extends Application {
    
    static Image inputImageJfx;
    static MyImage outputImageJfx;
    
    static List<MyImage> undoStack = new ArrayList<MyImage>();
    static List<MyImage> redoStack = new ArrayList<MyImage>();
    

    public static void main(String[] args) throws Exception{
        
         System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        
        inputImageJfx = new Image(new FileInputStream("DogSprinkler.jpeg"));
        
        outputImageJfx = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());;
        
        outputImageJfx.copyFrom(inputImageJfx);
        
        
        
        launch(args);
    }
    ImageView outputImageView;
            
    @Override
    public void start(Stage primaryStage) {
        
        outputImageView = new ImageView(outputImageJfx);
      
        VBox menuPane = new VBox();
        
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(a ->
            {
              
                if(undoStack.size() > 0)
                {
                    MyImage previousImage = undoStack.get(undoStack.size() - 1);
                    undoStack.remove(undoStack.size() - 1);
                    
                    redoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
                
            }
        );
        menuPane.getChildren().add(undoButton);
        
        Button redoButton = new Button("Redo");
        redoButton.setOnAction(a -> {
                if(redoStack.size() > 0)
                {
                    MyImage previousImage = redoStack.get(redoStack.size() - 1);
                    redoStack.remove(redoStack.size() - 1);
                    
                    undoStack.add(outputImageJfx);
                    
                    outputImageJfx = previousImage;
                            
                            
                    outputImageView.setImage(outputImageJfx);
                    
                    
                }
            }
        );
        menuPane.getChildren().add(redoButton);
        
        menuPane.getChildren().add(new Separator());
        
        
        for(Method method : outputImageJfx.getClass().getDeclaredMethods())
        {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            
            
            Button button = new Button(method.getName());
            button.setOnAction(
                a -> 
                doWithUndo(() -> {
                    try {
                            method.invoke(outputImageJfx, new Object[]{1});
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(MoreKernels.class.getName()).log(Level.SEVERE, null, ex);
                        }}
                        )
            );
            ///we have to surround the expression with a try catch.Thank you checked exceptions.
            
            
            menuPane.getChildren().add(button);
        }
        
        menuPane.getChildren().add(new Separator());
        
        Button reduceButton = new Button("Reduce Colors");
        reduceButton.setOnAction(a -> doWithUndo(() -> outputImageJfx.reduceColors(8)));
        //Pass lamda expression to setOnAction. That expression sets up the undo infrastructure and calls the second lambda.
        //It is beautiful.
        
        menuPane.getChildren().add(reduceButton);
        
        reduceButton = new Button("Reduce Colors 16");
        reduceButton.setOnAction(a -> doWithUndo(() -> outputImageJfx.reduceColors(16)));
        menuPane.getChildren().add(reduceButton);
        
        
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: DAE6F3;");
        root.setCenter(outputImageView);
        
        Canvas canvas = new Canvas(100, 256);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawHistogram(gc);
        menuPane.getChildren().add(canvas);
        
        
        root.setLeft(menuPane);
        
        Scene scene = new Scene(root, inputImageJfx.getWidth() + 400, inputImageJfx.getHeight() + 20 );
        
        primaryStage.setTitle("Duplicates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void doWithUndo(Runnable lambda)
    {
        MyImage newImage = new MyImage((int)inputImageJfx.getWidth(), (int)inputImageJfx.getHeight());

        newImage.copyFrom(outputImageJfx);

        undoStack.add(outputImageJfx);

        redoStack.clear();

        outputImageJfx = newImage;


        lambda.run();

        outputImageView.setImage(outputImageJfx);
    
    }

    private void drawHistogram(GraphicsContext gc) {
        //JavaFX uses the same style as js canvas.
        //every component has a fill color and a stroke color
        //fill is the inside color.
        //stroke is the "pen"
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 100, 256);
        
        //Create the bins
        int[] buckets = new int[256]; //Our array of bins.
        
        
        //Same code that can be auto parallelized.
        
        IntStream.range(0,(int)outputImageJfx.getHeight()).forEach((y) -> {
            IntStream.range(0, (int)outputImageJfx.getWidth()).forEach((x)->{
                Color myColor = outputImageJfx.getPixelReader().getColor(x, y );
                
                double grayscale = myColor.grayscale().getBlue(); //Convert to grayscale and grad any component
                buckets[(int)(grayscale * 255)]++;
            });
        });
        
        for(int y = 0; y < outputImageJfx.getHeight() ;  y++)
        {
            for(int x = 0; x < outputImageJfx.getWidth() ; x++)
            {
                
                Color myColor = outputImageJfx.getPixelReader().getColor(x, y );
                
                double grayscale = myColor.grayscale().getBlue(); //Convert to grayscale and grad any component
                buckets[(int)(grayscale * 255)]++;
            }
        }
        
        gc.setStroke(Color.BLACK);
        
        
        
        
        int max = Arrays.stream(buckets).max().getAsInt();
        

        
        for(int i = 0; i < 256; i++)
        {
            gc.strokeLine(0, i, buckets[i]/(float)max*100, i);
            //We take the number of entries in a bucket, divide by the max to get a number between 0 and 1, then multiply by the component width
        }
        
    }
}