import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Gray extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	int start;
	int end;
	
	public Gray(BufferedImage img){
		this.img = img;
	}

	public Gray(BufferedImage img, int start, int end) {
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
    				
					Color newColor = new Color(gray, gray, gray);

    				img.setRGB(j, i, newColor.getRGB());
                }
            }
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
        	Gray t1, t2;
        	t1 = new Gray(img, start, mid);
        	t2 = new Gray(img, mid, end);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main
        }
        return null;
    }

	public void sequential(String name){
    	try {
    		int width = img.getWidth();
    		int height = img.getHeight();

    		for(int i=0; i<height; i++) {

    			for(int j=0; j<width; j++) {

    				Color c = new Color(img.getRGB(j, i));
    				int red = (int)(c.getRed() * 0.299);
    				int green = (int)(c.getGreen() * 0.587);
    				int blue = (int)(c.getBlue() *0.114);
    				Color newColor = new Color(red+green+blue, red+green+blue, red+green+blue);

    				img.setRGB(j, i, newColor.getRGB());
    			}
    		}
    		
	     	File ouptut = new File(name);
	    	ImageIO.write(img, "jpg", ouptut);
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }

    private static void parallel(BufferedImage img, String name) {
    	try{
	    	Gray t = new Gray(img, 0, img.getHeight());
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
 // java Gray.java [image]
 // Example: java Gray.java lena.png
public static void main(BufferedImage img2) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	//Gray obj = new Gray(img);
    	
    	
    	Gray obj = new Gray(img);
    	
    	long start = System.currentTimeMillis(); //time the sequential approach
    	obj.sequential("gray_sequential_lena.png"); //apply the sequential approach
    	long end = System.currentTimeMillis();
    	long time = end - start;
    	System.out.printf("Sequential ended after %d ms.\n", time);

    	start = System.currentTimeMillis(); //time the parallel approach
		obj.parallel(img, "gray_lena.png"); //apply the parallel approach
    	end = System.currentTimeMillis(); 
    	time = end - start;
    	System.out.printf("Parallel ended after %d ms.\n", time);
    }
}