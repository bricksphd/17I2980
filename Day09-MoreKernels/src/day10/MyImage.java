package day10;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public class MyImage extends WritableImage{

    public MyImage(int i, int i0) {
        super(i, i0);
    }
    
    public void copyFrom(Image inImage){
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                this.getPixelWriter().setColor(x, y,  inImage.getPixelReader().getColor(x, y));
            }
        }
    }

    private void applyKernel(float[][] kernel, int w)
    {
        Color[][] newColors = new Color[(int)this.getHeight()][(int)this.getWidth()];
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                
                Color myColor = this.getPixelReader().getColor(x, y );
                if(myColor.getOpacity() == 0)
                {
                    newColors[y][x] = Color.TRANSPARENT;
                    continue;
                }
                
                
                float sumR= 0;
                float sumG= 0;
                float sumB= 0;
                
                float power = 0;
                
                for(int i = -1 * w; i <= 1 * w; i++)
                {
                    for(int j = -1 * w; j <= 1 * w; j++)
                    {
                        if(this.doesPixelExist(x + i, y + j))
                        {
                            Color currentPixel = this.getPixelReader().getColor(x + i, y + j);
                            float kernelValue = kernel[j + w][i + w];
                            sumR += currentPixel.getRed() * kernelValue;
                            sumG += currentPixel.getGreen()* kernelValue;
                            sumB += currentPixel.getBlue()* kernelValue;
                            power +=kernelValue;
                        }
                        
                    }
                }
                
                if(power != 0){
                
                    sumR /= power;
                    sumG /= power;
                    sumB /= power;
                
                }
                
                newColors[y][x] = new Color(
                        clampAbs(sumR), 
                        clampAbs(sumG), 
                        clampAbs(sumB), 
                        1.0f );
            }
        }
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                this.getPixelWriter().setColor(x, y, newColors[y][x]);
            }
        }
    }
    
    public void blur(int w) {
        
        float[][] blurKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                blurKernel[y][x] = 1;
            }
        }
        
        this.applyKernel(blurKernel, w);
        
    }
    
    /*public void doNothing(int w) {
        
        float[][] doNothingKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                doNothingKernel[y][x] = 0;
                if(y == w && x == w)
                    doNothingKernel[y][x] = 1;
            }
        }
        
        this.applyKernel(doNothingKernel, w);
    } */
    
        
    
    
    public void edgeDetector(int i) {
        
        int w = 1; ///3x3 kernel
        float[][] magicKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                magicKernel[y][x] = 0;
               
            }
        }
        
        magicKernel[w][w] = -4;
        magicKernel[w][0] = 1;
        magicKernel[0][w] = 1;
        magicKernel[w][2] = 1;
        magicKernel[2][w] = 1;
        
        
        
        this.applyKernel(magicKernel, w);
        
    }
    
    public void sharpen(int i) {
        
        int w = 1; ///3x3 kernel
        float[][] magicKernel = new float[2*w + 1][2 * w + 1];
        
        for(int y = 0; y < 2 * w + 1 ;  y++)
        {
            for(int x = 0; x < 2 * w+ 1 ; x++)
            {
                magicKernel[y][x] = 0;
               
            }
        }
        
        magicKernel[w][w] = 5;
        magicKernel[w][0] = -1;
        magicKernel[0][w] = -1;
        magicKernel[w][2] = -1;
        magicKernel[2][w] = -1;
        
        
        
        this.applyKernel(magicKernel, w);
        
    }

    private boolean doesPixelExist(int x, int y) {
        return x < this.getWidth() && x >= 0 && y < this.getHeight() && y >= 0;
    }
    
    private float clampAbs(float f){
        float toReturn = Math.abs(f);
        
        if(toReturn < 0)
            return 0;
        if(toReturn > 1)
            return 1;
        
        return toReturn;
    }
    
    public void reduceColors(int finalColorCount)
    {
        if(finalColorCount <= 0) return; //Doesn't make sense without a positive number
        
        List<Color> finalColors = new ArrayList<Color>();
        
        for(int i = 0; i < finalColorCount; i++)
        {
            double red = 0;
            double green = 0;
            double blue = 0;
            
            //Now assign values based on some heuristic
            red = Math.random();
            green = Math.random();
            blue = Math.random();
            
            Color randomColor = new Color(red,green,blue,1);
            finalColors.add(randomColor);
        }
        
        
        for(int pass = 0; pass < 8; pass++)
        {
            List<List<Color>> groupVotes = new ArrayList<List<Color>>();

            for(int i = 0; i < finalColorCount; i++)
            {
                groupVotes.add(new ArrayList<Color>());
            }

            for(int y = 0; y < this.getHeight() ;  y++)
            {
                for(int x = 0; x < this.getWidth() ; x++)
                {
                    double closestDistance = Double.MAX_VALUE;
                    int closestColorIndex = -1;
                    Color thisColor = this.getPixelReader().getColor(x, y);

                    for(int guess = 0; guess < finalColorCount; guess++)
                    {
                        Color possibleColor = finalColors.get(guess);
                        double currentDistance = colorDistance(thisColor, possibleColor);

                        if(currentDistance >= closestDistance) continue;

                        closestColorIndex= guess;
                        closestDistance = currentDistance;
                    }

                    groupVotes.get(closestColorIndex).add(thisColor);
                }
            }


            ///Calculate the new median value

            for(int i = 0; i < finalColorCount; i++)
            {
                double sumR = 0;
                double sumG = 0;
                double sumB = 0;
                
                if(groupVotes.get(i).size() == 0)
                {
                    sumR = Math.random();
                    sumG = Math.random();
                    sumB = Math.random();
                }
                else{
                    for(Color color : groupVotes.get(i))
                    {
                        sumR += color.getRed();
                        sumG += color.getGreen();
                        sumB += color.getBlue();
                    }

                    sumR /= groupVotes.get(i).size();
                    sumG /= groupVotes.get(i).size();
                    sumB /= groupVotes.get(i).size();
                }


                finalColors.set(i, new Color(sumR, sumG, sumB, 1));
               
            }
        }
        
        
        
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                double closestDistance = Double.MAX_VALUE;
                Color closestColor = null;
                Color thisColor = this.getPixelReader().getColor(x, y);
                
                for(Color possibleColor : finalColors)
                {
                    double currentDistance = colorDistance(thisColor, possibleColor);
                    
                    if(currentDistance >= closestDistance) continue;
                    
                    closestColor = possibleColor;
                    closestDistance = currentDistance;
                }
                
                this.getPixelWriter().setColor(x, y, closestColor);
            }
        }
    }
    
    public void reduceColorsRandom(int finalColorCount)
    {
        if(finalColorCount <= 0) return; //Doesn't make sense without a positive number
        
        List<Color> finalColors = new ArrayList<Color>();
        
        for(int i = 0; i < finalColorCount; i++)
        {
            double red = 0;
            double green = 0;
            double blue = 0;
            
            //Now assign values based on some heuristic
            red = Math.random();
            green = Math.random();
            blue = Math.random();
            
            Color randomColor = new Color(red,green,blue,1);
            finalColors.add(randomColor);
        }
        
        for(int y = 0; y < this.getHeight() ;  y++)
        {
            for(int x = 0; x < this.getWidth() ; x++)
            {
                double closestDistance = Double.MAX_VALUE;
                Color closestColor = null;
                Color thisColor = this.getPixelReader().getColor(x, y);
                
                for(Color possibleColor : finalColors)
                {
                    double currentDistance = colorDistance(thisColor, possibleColor);
                    
                    if(currentDistance >= closestDistance) continue;
                    
                    closestColor = possibleColor;
                    closestDistance = currentDistance;
                }
                
                this.getPixelWriter().setColor(x, y, closestColor);
            }
        }
    }
    
    private double colorDistance(Color one, Color two)
    {
        return Math.abs(one.getRed() - two.getRed()) + Math.abs(one.getGreen() - two.getGreen()) + Math.abs(one.getBlue() - two.getBlue());
    }
    
    
}
