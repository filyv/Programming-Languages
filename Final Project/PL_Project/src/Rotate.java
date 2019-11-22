import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Rotate extends RecursiveTask<Void> {

	public static final int THRESHOLD = 300;

	BufferedImage img;
	BufferedImage img2;
	int originalImage[][]; //The image to which we'll apply the filter
	int filteredImage[][]; //The resulting image
	
	
	int start;
	int end;

	public Rotate(BufferedImage img){
		this.img = img;
		
	}

	public Rotate(BufferedImage img, int start, int end, BufferedImage img2) {
		this.img = img;
		this.start = start;
		this.end = end;
		this.img2 = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	
	
	//This synchronized method is the one that does the filtering itself by flipping the image dianonally
	public synchronized void flipImage(int originalImage[][], int filteredImage[][]){
		for(int i=start; i<end;i++){
			for(int j=0; j<originalImage.length; j++){
				filteredImage[i][j]=originalImage[j][i]; //Flip the image
			}
		}
	}
		
	
	@Override
	protected Void compute(){
		
		int numCols = img.getWidth();
		
		int[][] pixels = new int[numCols][end];
		int[][] filteredImage=new int[numCols][end];
		
		if(end-start < THRESHOLD){
            for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++) {
			      img2.setRGB(j, i, img.getRGB(i,j) );
                }
            }
            
            for (int i = start; i < end; i++) {
				for (int j = 0; j < numCols; j++) {
			      img.setRGB(j, i, img2.getRGB(j,i) );
                }
            }
			
        } else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
            invokeAll(new Rotate(img, start, mid, img2), new Rotate(img, mid, end, img2));
    		
            /*Rotate t1, t2;
        	t1 = new Rotate(img, start, mid, img2);
        	t2 = new Rotate(img, mid, end, img2);
            t1.fork(); // Call a new thread
            t1.join();
            t2.compute(); // Run over main*/
        }
        return null;
    }


    private static void parallel(BufferedImage img, String name, BufferedImage img2) {
    	try{
	    	Rotate t = new Rotate(img, 0, img.getHeight(), img2);
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
 // java Rotate.java [image]
 // Example: java Rotate.java lena.png
public static void main(BufferedImage img2) throws Exception {
    	
    	BufferedImage img = ImageIO.read(new File("src/lena.png"));
    	
    	Rotate obj = new Rotate(img);
    	
    	long start = System.currentTimeMillis(); //time the parallel approach
		
    	obj.parallel(img, "rotated_lena.png", img2); //apply the parallel approach
    	
		long end = System.currentTimeMillis(); 
    	
    	long time = end - start;
    	
    	System.out.printf("Parallel ended after %d ms.\n", time);
    }
}