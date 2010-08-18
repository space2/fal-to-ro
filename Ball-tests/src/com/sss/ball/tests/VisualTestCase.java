package com.sss.ball.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import junit.framework.TestCase;

public class VisualTestCase extends TestCase implements ActionListener {

    public static final int IMG_WIDTH = 640;
    public static final int IMG_HEIGHT = 480;

    private static final String CORRECT = "Correct";
    private static final String WRONG = "Wrong";

    private static BufferedImage mImg;
    private static Graphics mGfx;
    private static Object mUiLock = new Object();
    private static boolean mAnswer;
    private static JFrame mFrame;
    private static ImgViewer mImgViewer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        mGfx = mImg.getGraphics();
        mGfx.setColor(Color.WHITE);
        mGfx.fillRect(0, 0, IMG_WIDTH, IMG_WIDTH);
        mGfx.setColor(Color.BLACK);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mImg = null;
        mGfx = null;
    }

    protected void assertScreen(String id, String msg) {
        // Load reference image
        BufferedImage ref = loadRefImg(id);
        if (ref == null) {
            // reference image doesn't exists yet
            if (askIfImageIsCorrect(mImg, ref, id)) {
                saveRefImg(mImg, id);
            }
        } else {
            if (!compareImg(mImg, ref)) {
                if (askIfImageIsCorrect(mImg, ref, id)) {
                    saveRefImg(mImg, id);
                } else {
                    assertTrue(msg, false);
                }
            }
        }
    }

    protected Graphics getGraphics() {
        return mGfx;
    }

    private void saveRefImg(BufferedImage img, String id) {
        String fn = buildRefImgName(id);
        try {
            ImageIO.write(img, "PNG", new File(fn));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadRefImg(String id) {
        String fn = buildRefImgName(id);
        try {
            return ImageIO.read(new File(fn));
        } catch (IOException e) {
            return null;
        }
    }

    private String buildRefImgName(String id) {
        return "ref/" + getClass().getSimpleName() + "-" + getName() + "-" + id + ".png";
    }

    private boolean compareImg(BufferedImage img, BufferedImage ref) {
        if (img.getWidth() != ref.getWidth()) return false;
        if (img.getHeight() != ref.getHeight()) return false;

        int w = img.getWidth();
        int h = img.getHeight();
        int RGBE[] = new int[w*h];
        int RGBA[] = new int[w*h];
        img.getRGB(0, 0, w, h, RGBA, 0, w);
        ref.getRGB(0, 0, w, h, RGBE, 0, w);
        for (int i = 0; i < RGBE.length; i++) {
            if ((RGBA[i] & 0xf0f0f0f0) != (RGBE[i] & 0xf0f0f0f0)) {
                return false;
            }
        }
        return true;
    }

    private boolean askIfImageIsCorrect(BufferedImage img, BufferedImage ref, String id) {
        synchronized (mUiLock) {
            if (mFrame == null) {
                // Create the dialog if needed
                mFrame = new JFrame("Visual tests");
                mFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                // Add the main container
                JPanel panel = new JPanel();
                mFrame.setContentPane(panel);
                panel.setLayout(new BorderLayout());

                // Add the button row
                JPanel buttons = new JPanel(new FlowLayout());
                panel.add(buttons, BorderLayout.SOUTH);
                JButton btn;

                btn = new JButton(CORRECT);
                btn.addActionListener(this);
                buttons.add(btn);

                btn = new JButton(WRONG);
                btn.addActionListener(this);
                buttons.add(btn);

                // Add image viewer
                mImgViewer = new ImgViewer();
                panel.add(mImgViewer, BorderLayout.CENTER);

                // Add question
                panel.add(new JLabel("Is the image correct?"), BorderLayout.NORTH);
            }

            mFrame.setTitle(buildRefImgName(id));
            mImgViewer.setImgs(img, ref);

            // Finish setup window
            mFrame.pack();
            mFrame.setVisible(true);

            // Wait for answer
            try {
                mAnswer = false;
                mUiLock.wait();
                return mAnswer;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        synchronized (mUiLock) {
            JButton btn = (JButton) e.getSource();
            String label = btn.getText();
            if (label.equals(CORRECT)) {
                mAnswer = true;
            } else {
                mAnswer = false;
            }
            mUiLock.notifyAll();
        }
    }

    static class ImgViewer extends JComponent {

        private BufferedImage mImg;
        private BufferedImage mRef;

        private static final long serialVersionUID = 1L;

        public ImgViewer() {
        }

        public void setImgs(BufferedImage img, BufferedImage ref) {
            mImg = img;
            mRef = ref;
            setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int idx = (int) ((System.currentTimeMillis() / 1000) & 1);
            g.setColor(Color.RED);
            if (idx == 0 || mRef == null) {
                g.drawImage(mImg, 0, 0, null);
                g.drawString("Actual", 10, 20);
            } else {
                g.drawImage(mRef, 0, 0, null);
                g.drawString("Expected", 10, 20);
            }
            // request new repaint
            repaint(400);
        }

    }

}
