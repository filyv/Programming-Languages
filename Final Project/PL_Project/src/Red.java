import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Red extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	int red;
	int green;
	int blue;
	
	public Red(BufferedImage img){
		this.img = img;
	}

	public Red(BufferedImage img, int start, int end, int r, int g, int b) {
		this.img = img;
		this.start = start;
		this.end = end;
		this.red = r;
		this.green = g;
		this.blue = b;		
	}

	@Override
	protected Void compute(){
		int numCols = img.getWidth();
		
		if(end-start < THRESHOLD){
			for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++) {
					Color c = new Color(img.getRGB(j, i));
    				//int red = (int)(c.getRed() * 0.299);
					
					
					double unos=red/(double)255;
					double doss=green/(double)255;
					double tress=blue/(double)255;
					
					int red = (int)(c.getRed()*unos);
    				int green = (int)(c.getGreen() * doss);
    				int blue = (int)(c.getBlue() * tress);
    				
					Color newColor = new Color(red, green, blue);

    				img.setRGB(j, i, newColor.getRGB());
                }
            }
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Red t1, t2;
        	t1 = new Red(img, start, mid, red, green, blue);
        	t2 = new Red(img, mid, end, red, green, blue);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name, int r, int g,int b) {
    	try{
	    	Red t = new Red(img, 0, img.getHeight(), r, g, b);
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
    public void sequential(String name,int r, int g,int b){
    	try {

    		int numCols = img.getWidth();
    		for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++) {
					Color c = new Color(img.getRGB(j, i));
    				//int red = (int)(c.getRed() * 0.299);
					
					
					double unos=r/(double)255;
					double doss=g/(double)255;
					double tress=b/(double)255;
					
					int red = (int)(c.getRed()*unos);
    				int green = (int)(c.getGreen() * doss);
    				int blue = (int)(c.getBlue() * tress);
    				
					Color newColor = new Color(red, green, blue);

    				img.setRGB(j, i, newColor.getRGB());
                }
            }
    		
	     	File ouptut = new File(name);
	    	ImageIO.write(img, "png", ouptut);
    	} catch (Exception e) {
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
    	obj.parallel(img, "rgb_lena.png", r, g, b); //apply the parallel approach
    	long end = System.currentTimeMillis(); 
    	long time = end - start;
      	System.out.printf("Parallel ended after %d ms.\n", time);

    	
    	start = System.currentTimeMillis(); //time the sequential approach
      	obj.sequential("rgb_sequential_lena.png", r, g, b); //apply the sequential approach
      	end = System.currentTimeMillis();
      	time = end - start;
      	System.out.printf("Sequential ended after %d ms.\n", time);
	}

}