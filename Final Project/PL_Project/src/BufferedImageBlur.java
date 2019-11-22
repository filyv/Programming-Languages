import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageBlur extends RecursiveTask<Void>{
//	public class BufferedImageBlur {
 
    static BufferedImage image;
    static boolean imageLoaded = false;
	public static final int THRESHOLD = 800;
 
    int start;
	int end;
    
	public BufferedImageBlur(BufferedImage img){
		this.image = img;
	}

	public BufferedImageBlur(BufferedImage img, int start, int end/*, BufferedImage img2*/) {
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
	  
	  BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
 	 
	  //APPLY THE FILTER TO THE IMAGE
	  image = op.filter(image, null);
	  
  		if(end-start < THRESHOLD){
  			
  		} else {
        	int mid = (start + end) >>> 1; // Divide in 2. Invoke separately.
    		BufferedImageBlur t1, t2;
        	t1 = new BufferedImageBlur(image, start, mid);
        	t2 = new BufferedImageBlur(image, mid, end);
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
    		BufferedImageBlur t = new BufferedImageBlur(img, 0, img.getHeight());
	    	ForkJoinPool pool = new ForkJoinPool();
	    	pool.invoke(t);
	    	pool.shutdown();
	     	File ouptut = new File("leblurry.png");
	    	ImageIO.write(image, "jpg", ouptut);
	    } catch(Exception e){
	    	System.out.println(e);
	    }
    }
    
    
    /**********
     *  MAIN: *
     **********/
    
   public static void main(String[] args) throws IOException {
 
	   BufferedImageBlur obj = new BufferedImageBlur(image);
	   
	   
	  // The ImageObserver implementation to observe loading of the image
	  ImageObserver myImageObserver = new ImageObserver() {
	 
	    public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height) {
	 
	if ((flags & ALLBITS) != 0) {
	 
	  imageLoaded = true;
	 
	  System.out.println("Image loading finished!");
	 
	  return false;
	 
	}
	 
	return true;
	 
	    }
	 
	  };
	 
	  // The image URL - change to where your image file is located!
	  String imageURL = "src/lena.png";
	 
	  /**
	   * This call returns immediately and pixels are loaded in the background
	   * We use an ImageObserver to be notified when the loading of the image
	   * is complete
	   */
	 
	  Image sourceImage = Toolkit.getDefaultToolkit().getImage(imageURL);
	  sourceImage.getWidth(myImageObserver);
	 
	  // We wait until the image is fully loaded
	 
	  while (!imageLoaded) {
		try {Thread.sleep(100);} catch (InterruptedException e) {} 
	  }
		 
	  // Create a buffered image from the source image with a format that's compatible with the screen
	  GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	  GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
	  GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
	 
	  // If the source image has no alpha info use Transparency.OPAQUE instead
	  image = graphicsConfiguration.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.BITMASK);
	 
	  // Copy image to buffered image
	  Graphics graphics = image.createGraphics();
	 
	  // Paint the image onto the buffered image
	  graphics.drawImage(sourceImage, 0, 0, null);
	  graphics.dispose();
	 
	  // A 3x3 kernel that blurs an image
	  image = ImageIO.read(new File("src/lena.png"));
	
	  obj.parallel(image, "blurry_lena.png"); //apply the parallel approach
  	
	  
	  /*
	  float[] matrix = new float[100];
		for (int i = 0; i < 100; i++)
			matrix[i] = 1.0f/99.0f;
	  
	  Kernel kernel = new Kernel(10, 10, matrix);
	 
	  BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
	 
	  
	  //APPLY THE FILTER TO THE IMAGE
	  image = op.filter(image, null);
	  
	  
	  //SAVE THE IMAGE
	  //
	  //
	  //
	 try {
	      // retrieve image
	     // BufferedImage bi = getMyImage();
	      File outputfile = new File("blurry.png");
	      ImageIO.write(image, "png", outputfile);
	  } catch (IOException e) {
	      
	  }
	  */
	  
	  //
	  //
	  //
	  
	  
	  // Create frame with specific title
	 
	  Frame frame = new Frame("Example Frame");
	 
	  // Add a component with a custom paint method
	 
	  frame.add(new CustomPaintComponent());
	 
	  // Display the frame
	 
	  int frameWidth = 300;
	 
	  int frameHeight = 300;
	 
	  frame.setSize(frameWidth, frameHeight);
	 
	  frame.setVisible(true);
	 
	    }
	 
	    /**
	     * To draw on the screen, it is first necessary to subclass a Component and
	     * override its paint() method. The paint() method is automatically called
	     * by the windowing system whenever component's area needs to be repainted.
	     */
	    
	    
	    static class CustomPaintComponent extends Component {
	 
	    	
	    	
	    	
	  public void paint(Graphics g) {
	 
	// Retrieve the graphics context; this object is used to paint
	 
	// shapes
	 
	Graphics2D g2d = (Graphics2D) g;
	 
	/**
	 
	 * Draw an Image object The coordinate system of a graphics context
	 
	 * is such that the origin is at the northwest corner and x-axis
	 
	 * increases toward the right while the y-axis increases toward the
	 
	 * bottom.
	 
	 */
	 
	int x = 0;
	 
	int y = 0;
	 
	g2d.drawImage(image, x, y, this);
	 
	  }
	 
	    }

		
	
		
	 
    
 	
}