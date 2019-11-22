import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Sepia extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	
	public Sepia(BufferedImage img){
		this.img = img;
	}

	public Sepia(BufferedImage img, int start, int end) {
		this.img = img;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Void compute(){
		
		int numCols = img.getWidth();
		
		if(end-start < THRESHOLD){
			for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++)  {

					int p = img.getRGB(j,i); 
					  
	                int a = (p>>24)&0xff; 
	                int R = (p>>16)&0xff; 
	                int G = (p>>8)&0xff; 
	                int B = p&0xff; 
	  
	                //calculate newRed, newGreen, newBlue 
	                int newRed = (int)(0.393*R + 0.769*G + 0.189*B); 
	                int newGreen = (int)(0.349*R + 0.686*G + 0.168*B); 
	                int newBlue = (int)(0.272*R + 0.534*G + 0.131*B); 
	  
	                //check condition 
	                if (newRed > 255) 
	                    R = 255; 
	                else
	                    R = newRed; 
	  
	                if (newGreen > 255) 
	                    G = 255; 
	                else
	                    G = newGreen; 
	  
	                if (newBlue > 255) 
	                    B = 255; 
	                else
	                    B = newBlue; 
	  
	                //set new RGB value 
	                p = (a<<24) | (R<<16) | (G<<8) | B; 
	  
	                img.setRGB(j,i, p); 
				
			        
                }
            }
				
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Sepia t1, t2;
        	t1 = new Sepia(img, start, mid);
        	t2 = new Sepia(img, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name) {
    	try{
	    	Sepia t = new Sepia(img, 0, img.getHeight());
	    	ForkJoinPool pool = new ForkJoinPool();
	    	pool.invoke(t);
	    	pool.shutdown();
	     	File ouptut = new File(name);
	    	ImageIO.write(img, "jpg", ouptut);
	    } catch(Exception e){
	    	System.out.println(e);
	    }
    }


 // How to run (from terminal):
 // java Sepia.java [image]
 // Example: java Sepia.java lena.png
public static void main(BufferedImage img2) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	Sepia obj = new Sepia(img);
    	
    	long start = System.currentTimeMillis(); //time the parallel approach
		
    	obj.parallel(img, "sepia_lena.png"); //apply the parallel approach
    	
		long end = System.currentTimeMillis(); 
    	
    	long time = end - start;
    	
    	System.out.printf("Parallel ended after %d ms.\n", time);
    }
}