import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Blur extends RecursiveTask<Void>{
//	public class Blur {
 
    static BufferedImage image;
    static boolean imageLoaded = false;
	public static final int THRESHOLD = 800;
 
    int start;
	int end;
    
	public Blur(BufferedImage img){
		this.image = img;
	}

	public Blur(BufferedImage img, int start, int end) {
		this.image = img;
		this.start = start;
		this.end = end;
		//this.img2 = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_INT_RGB);
	}
	
	

    /*************
     *  COMPUTE: *
     *************/
    @Override
	protected Void compute() {
    	 
    	float[] matrix = new float[100];
		for (int i = 0; i < 100; i++)
			matrix[i] = 1.0f/99.0f;
	  
	  Kernel kernel = new Kernel(10, 10, matrix);
	  
	  BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
 	 
	  
	  
  		if(end-start < THRESHOLD){
  		//APPLY THE FILTER TO THE IMAGE
  		  image = op.filter(image, null);
  		} else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
    		Blur t1, t2;
        	t1 = new Blur(image, start, mid);
        	t2 = new Blur(image, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
    	return null;
    	
	}
    
	
    /**************
     *  PARALLEL: *
     **************/
    private static void parallel(BufferedImage img, String name) {
    	try{
    		Blur t = new Blur(img, 0, img.getHeight());
	    	ForkJoinPool pool = new ForkJoinPool();
	    	pool.invoke(t);
	    	pool.shutdown();
	     	File ouptut = new File(name);
	    	ImageIO.write(image, "png", ouptut);
	    } catch(Exception e){
	    	System.out.println(e);
	    }
    }
    
    /****************
     *  SEQUENTIAL: *
     ****************/
    public void sequential(String name){
    	try {
    		float[] matrix = new float[100];
    		for (int i = 0; i < 100; i++)
    			matrix[i] = 1.0f/99.0f;
    	  
    		Kernel kernel = new Kernel(10, 10, matrix);
    	  
    		BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
     	 

    		  image = op.filter(image, null);
    	  
    		
	     	File ouptut = new File(name);
	    	ImageIO.write(image, "png", ouptut);
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }
    
    
    /**********
     *  MAIN: *
     **********/
    
   public static void main() throws IOException {
 
	   BufferedImage img = ImageIO.read(new File("src/lena.png"));
   	
	   Blur obj = new Blur(img);
	   
	   //image = ImageIO.read(new File("src/lena.png"));
	
	  long start = System.currentTimeMillis(); //time the sequential approach
  	obj.sequential("blurry_sequential_lena.png"); //apply the sequential approach
  	long end = System.currentTimeMillis();
  	long time = end - start;
  	System.out.printf("Sequential ended after %d ms.\n", time);

  	start = System.currentTimeMillis(); //time the parallel approach
		obj.parallel(img, "blurry_lena.png"); //apply the parallel approach
  	end = System.currentTimeMillis(); 
  	time = end - start;
  	System.out.printf("Parallel ended after %d ms.\n", time);
  
	 
	    }
	 
	
		
	 
    
 	
}