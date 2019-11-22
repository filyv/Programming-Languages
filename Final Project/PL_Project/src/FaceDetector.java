import org.opencv.core.Core; 
import org.opencv.core.Mat; 
import org.opencv.core.MatOfRect; 
import org.opencv.core.Point; 
import org.opencv.core.Rect; 
import org.opencv.core.Scalar; 
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.imgproc.Imgproc; 
import org.opencv.objdetect.CascadeClassifier; 
  
public class FaceDetector 
{ 
    public static void main(String[] args) 
    { 
  
         System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
  
         CascadeClassifier faceDetector = new CascadeClassifier(); 
        faceDetector.load("haarcascade_frontalface_alt.xml"); 
  
        // Input image 
        Mat image = Imgcodecs.imread("src/lena.png"); 
  
        // Detecting faces 
        MatOfRect faceDetections = new MatOfRect(); 
        faceDetector.detectMultiScale(image, faceDetections); 
  
        // Creating a rectangular box showing faces detected 
        for (Rect rect : faceDetections.toArray()) 
        { 
            Imgproc.rectangle(image, new Point(rect.x, rect.y), 
             new Point(rect.x + rect.width, rect.y + rect.height), 
                                           new Scalar(0, 255, 0)); 
        } 
  
        // Saving the output image 
        String filename = "Ouput.jpg"; 
        Imgcodecs.imwrite("E:\\"+filename, image); 
    } 
} 