import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This program demonstrates how to use JPanel in Swing.
 * @author www.codejava.net
 */
public class Main extends JFrame {
   
	BufferedImage img;
	
	private ImageIcon[] image = new ImageIcon[10];
	JLabel ImageLabel;

	//FILTERS
	Gray gray;
	Negative negative;
	Red red;
	Sepia sepia;
	Rotate rotate;
	Blur blur;
	Binary binary;
	
	int r;
	int g;
	int b;

	String text1;
	String text2;
	String text3;
	

    private JLabel labelRed = new JLabel("RED:");
    private JLabel labelGreen = new JLabel("GREEN:");
    private JLabel labelBlue = new JLabel("BLUE:");
    private JTextField textRed  = new JTextField(20);
    private JTextField textGreen  = new JTextField(20);
    private JTextField textBlue  = new JTextField(20);
    private JButton buttonGray = new JButton("B&W");
    private JButton buttonReset = new JButton("Reset");
    private JButton buttonNegative = new JButton("Negative");
    private JButton buttonRed = new JButton("RGB");
    private JButton buttonSepia = new JButton("Sepia");
    private JButton buttonRotate = new JButton("Rotate");
    private JButton buttonBlur = new JButton("Blur");
    private JButton buttonBinary = new JButton("Binary");
     
    public Main() {
        super("JPanel Demo Program");
         
        image[0] = new ImageIcon("src/lena.png");
        image[1] = new ImageIcon("gray_lena.png");
        image[2] = new ImageIcon("negative_lena.png");
        image[3] = new ImageIcon("rgb_lena.png");
        image[4] = new ImageIcon("sepia_lena.png");
        image[5] = new ImageIcon("rotated_lena.png");
        image[6] = new ImageIcon("blurry_lena.png");
        image[7] = new ImageIcon("binary_lena.png");
        ImageLabel = new JLabel(image[0]);
      
        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());
         
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
         
        // add components to the panel
        
        //IMAGE SECTION:
        newPanel.add(ImageLabel);
        
        //TEXT SECTION:
//        constraints.gridx = 0;
        constraints.gridy = 0;     
  //      newPanel.add(labelRed, constraints);
        constraints.gridx = 1;
        newPanel.add(textRed, constraints);
        textRed.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            	    text1 = textRed.getText();
            	    System.out.println(text1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        constraints.gridx = 2;
        newPanel.add(textGreen, constraints);
        textGreen.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            	    text2 = textGreen.getText();
            	    System.out.println(text2);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        constraints.gridx = 3;
        newPanel.add(textBlue, constraints);
        textBlue.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            	    text3 = textBlue.getText();
            	    System.out.println(text3);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        //RED BUTTON:
        constraints.gridx = 4;
        constraints.gridwidth = 1;
        newPanel.add(buttonRed, constraints);
        buttonRed.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		r = Integer.parseInt(text1);
            		g = Integer.parseInt(text2);
            		b = Integer.parseInt(text3);
            		red.main(img,r,g,b);
            		ImageLabel.setIcon(image[3]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        
        //RESET BUTTON:
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonReset, constraints);
        buttonReset.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		ImageLabel.setIcon(image[0]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        
        //GRAY (B&W) BUTTON:
        constraints.gridx = 1;
        newPanel.add(buttonGray, constraints);
        buttonGray.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		gray.main(img);
            		ImageLabel.setIcon(image[1]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        
        //NEGATIVE BUTTON:
        constraints.gridx = 2;
        newPanel.add(buttonNegative, constraints);
        buttonNegative.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		negative.main(img);
            		ImageLabel.setIcon(image[2]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        
        
        //SEPIA BUTTON:
        constraints.gridx = 3;
        newPanel.add(buttonSepia, constraints);
        buttonSepia.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		sepia.main(img);
            		ImageLabel.setIcon(image[4]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        

        //ROTATE BUTTON:
        constraints.gridy = 2;
        constraints.gridx = 1;
        newPanel.add(buttonRotate, constraints);
        buttonRotate.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		rotate.main(img);
                    image[5] = new ImageIcon("rotated_lena.png");
            		ImageLabel.setIcon(image[5]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        

        //BLUR BUTTON:
        constraints.gridx = 2;
        newPanel.add(buttonBlur, constraints);
        buttonBlur.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		blur.main();
                    image[6] = new ImageIcon("blurry_lena.png");
            		ImageLabel.setIcon(image[6]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });

        
        //BINARY BUTTON:
        constraints.gridx = 3;
        newPanel.add(buttonBinary, constraints);
        buttonBinary.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	try {
            		binary.main(img);
                    image[7] = new ImageIcon("binary_lena.png");
            		ImageLabel.setIcon(image[7]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        
        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Photo Editor"));
         
        // add the panel to this frame
        add(newPanel);
        
        pack();
        setLocationRelativeTo(null);
        
        
    }
     
    
    public static void main(String[] args) {
        // set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}


