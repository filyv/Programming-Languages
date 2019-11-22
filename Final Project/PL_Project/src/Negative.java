import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Negative extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	
	public Negative(BufferedImage img){
		this.img = img;
	}

	public Negative(BufferedImage img, int start, int end) {
		this.img = img;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Void compute(){
		
		int numCols = img.getWidth();
		
		if(end-start < THRESHOLD){
			for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++) {
					int p = img.getRGB(j,i); 
	                int a = (p>>24)&0xff; 
	                int r = (p>>16)&0xff; 
	                int g = (p>>8)&0xff; 
	                int b = p&0xff; 
	  
	                //subtract RGB from 255 
	                r = 255 - r; 
	                g = 255 - g; 
	                b = 255 - b; 
	  
	                //set new RGB value 
	                p = (a<<24) | (r<<16) | (g<<8) | b; 
	                img.setRGB(j, i, p); 
				
                }
            }
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Negative t1, t2;
        	t1 = new Negative(img, start, mid);
        	t2 = new Negative(img, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name) {
    	try{
	    	Negative t = new Negative(img, 0, img.getHeight());
	    	ForkJoinPool pool = new ForkJoinPool();
	    	pool.invoke(t);
	    	pool.shutdown();
	     	File ouptut = new File(name);
	    	ImageIO.write(img, "png", ouptut);
	    } catch(Exception e){
	    	System.out.println(e);
	    }
    }


    /****************
     *  SEQUENTIAL: *
     ****************/
    public void sequential(String name){
    	try {
    		int numCols = img.getWidth();
    		
    			for (int i = start; i < end; i++) {
    				for (int j = 0; j < numCols; j++) {
    					int p = img.getRGB(j,i); 
    	                int a = (p>>24)&0xff; 
    	                int r = (p>>16)&0xff; 
    	                int g = (p>>8)&0xff; 
    	                int b = p&0xff; 
    	  
    	                //subtract RGB from 255 
    	                r = 255 - r; 
    	                g = 255 - g; 
    	                b = 255 - b; 
    	  
    	                //set new RGB value 
    	                p = (a<<24) | (r<<16) | (g<<8) | b; 
    	                img.setRGB(j, i, p); 
    				
                    }
                }
            
    		
	     	File ouptut = new File(name);
	    	ImageIO.write(img, "png", ouptut);
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }
    
// How to run (from terminal):
// java Negative.java [image]
// Example: java Negative.java lena.png
public static void main(BufferedImage img2) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	Negative obj = new Negative(img);
    	long start = System.currentTimeMillis(); //time the parallel approach
		obj.parallel(img, "negative_lena.png"); //apply the parallel approach
		long end = System.currentTimeMillis(); 
		long time = end - start;
  	System.out.printf("Parallel ended after %d ms.\n", time);
   
    	   start = System.currentTimeMillis(); //time the sequential approach
    	  	obj.sequential("negative_sequential_lena.png"); //apply the sequential approach
    	  	 end = System.currentTimeMillis();
    	  	 time = end - start;
    	  	System.out.printf("Sequential ended after %d ms.\n", time);
    	  	}
}