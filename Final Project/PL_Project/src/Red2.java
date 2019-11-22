import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Red extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	
	public Red(BufferedImage img){
		this.img = img;
	}

	public Red(BufferedImage img, int start, int end) {
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
	  
	                // set new RGB 
	                // keeping the r value same as in original 
	                // image and setting g and b as 0. 
	                p = (a<<24) | (r<<16) | (0<<8) | 0; 
	  
	                img.setRGB(j, i, p); 
                }
            }
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Red t1, t2;
        	t1 = new Red(img, start, mid);
        	t2 = new Red(img, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name) {
    	try{
	    	Red t = new Red(img, 0, img.getHeight());
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
 // java Red.java [image]
 // Example: java Red.java lena.png
public static void main(BufferedImage img2, int r, int g, int b) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	Red obj = new Red(img);
    	
    	long start = System.currentTimeMillis(); //time the parallel approach
		
    	obj.parallel(img, "red_lena.png"); //apply the parallel approach
    	
		long end = System.currentTimeMillis(); 
    	
    	long time = end - start;
    	
    	System.out.printf("Parallel ended after %d ms.\n", time);
    }
}