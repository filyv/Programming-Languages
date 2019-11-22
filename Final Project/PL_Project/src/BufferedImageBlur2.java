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
 
public class BufferedImageBlur2 {
 
    static BufferedImage image;
    static boolean imageLoaded = false;
 
    public static void main(String[] args) throws IOException {
 
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
 
try {
 
    Thread.sleep(100);
 
} catch (InterruptedException e) {
 
}
 
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

  
  float[] matrix = new float[100];
	for (int i = 0; i < 100; i++)
		matrix[i] = 1.0f/99.0f;
  
  Kernel kernel = new Kernel(10, 10, matrix
 /*
		  new float[] {

				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,
				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,
				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,
				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,
				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,
				  1f/35f, 1f/35f, 1f/35f, 1f/35f, 1f/35f,1f/35f,}*/);
 
  BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
 
  image = op.filter(image, null);
  try {
      // retrieve image
     // BufferedImage bi = getMyImage();
      File outputfile = new File("saved.png");
      ImageIO.write(image, "png", outputfile);
  } catch (IOException e) {
      
  }
  
  
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