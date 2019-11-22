package net.codejava.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class ProperImageFrame
{
    public static void main(String args[])
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        tryToSetLookAndFeel();

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel();
        f.getContentPane().add(imagePanel, BorderLayout.CENTER);

        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> loadAndShowImage(imagePanel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(importButton);
        f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        f.setSize(1000, 700);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static void loadAndShowImage(ImagePanel imagePanel)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("/Users/filyvazquez/Documents/workspace/PL_Project/src/lena.png"));
            imagePanel.setImage(image);
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private static void tryToSetLookAndFeel()
    {
        try
        {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException
            | InstantiationException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    private static class ImagePanel extends JPanel
    {
        private BufferedImage image;

        void setImage(BufferedImage image)
        {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if (image != null)
            {
                g.drawImage(image, 0, 0, null);
            }
        }

    }
}