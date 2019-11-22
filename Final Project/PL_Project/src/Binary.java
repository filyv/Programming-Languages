import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Binary extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	
	public Binary(BufferedImage img){
		this.img = img;
	}

	public Binary(BufferedImage img, int start, int end) {
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
					Color c = new Color(img.getRGB(j, i));
    				int red = (int)(c.getRed() * 0.299);
    				int green = (int)(c.getGreen() * 0.587);
    				int blue = (int)(c.getBlue() * 0.114);

    				int gray=red+green+blue;
    				
    				if(gray>127){
    					gray=255;
    				}
    				else{
    					gray=0;
    				}
    				
    				//System.out.println(gray);
    				
					Color newColor = new Color(gray, gray, gray);

    				img.setRGB(j, i, newColor.getRGB());
                }
            }
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Binary t1, t2;
        	t1 = new Binary(img, start, mid);
        	t2 = new Binary(img, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name) {
    	try{
	    	Binary t = new Binary(img, 0, img.getHeight());
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
 // java Binary.java [image]
 // Example: java Binary.java lena.png
public static void main(BufferedImage img2) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	Binary obj = new Binary(img);
    	
    	long start = System.currentTimeMillis(); //time the parallel approach
		
    	obj.parallel(img, "binary_lena.png"); //apply the parallel approach
    	
		long end = System.currentTimeMillis(); 
    	
    	long time = end - start;
    	
    	System.out.printf("Parallel ended after %d ms.\n", time);
    }
}