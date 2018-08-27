import java.awt.*;        // import statements to make necessary classes available
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 *  This class shows the setup for drawing static images using Java Graphics2D.
 *  The drawing code goes in the paintComponent() method.  When the program
 *  is run, the drawing is shown in a window on the screen.  See AnimationStarter.java
 *  for a somewhat more useful framework.
 */
public class GraphicsStarter extends JPanel {
	private static int IMAGE_WIDTH = 75;
	private static int IMAGE_HEIGHT = 75;
	
	private static int keyframe = 0;
    /**
     * This main() routine makes it possible to run the class GraphicsStarter
     * as an application.  It simply creates a window that contains a panel
     * of type GraphicsStarter.  The program ends when the user closed the
     * window by clicking its close box.
     */
    public static void main(String[] args) {
        JFrame window;
        window = new JFrame("Java Graphics");  // The parameter shows in the window title bar.
        window.setContentPane( new GraphicsStarter() ); // Show a Graphics starter in the window.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // End program when window closes.
        window.pack();  // Set window size based on the preferred sizes of its contents.
        window.setResizable(true); // Don't let user resize window.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( // Center window on screen.
                (screen.width - window.getWidth())/2, 
                (screen.height - window.getHeight())/2 );
        window.setVisible(true); // Open the window, making it visible on the screen.
        
        for (; keyframe<5; keyframe++){
        	window.repaint();
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
        }
    }
    
    private float pixelSize;  // This is the measure of a pixel in the coordinate system
                              // set up by calling the applyLimits method.  It can be used
                              // for setting line widths, for example.
    
    /**
     * This constructor sets up a GraphicsStarter when it is created.  Here, it
     * sets the size of the drawing area.  (The size is set as a "preferred size,"
     * which will be used by the pack() command in the main() routine.)
     */
    public GraphicsStarter() {
        setPreferredSize( new Dimension(800,600) ); // Set size of drawing area, in pixels.
    }
    
    /**
     * The paintComponent method draws the content of the JPanel.  The parameter
     * is a graphics context that can be used for drawing on the panel.  Note that
     * it is declared to be of type Graphics but is actually of type Graphics2D,
     * which is a subclass of Graphics.
     */
    protected void paintComponent(Graphics g) {
        
        /* First, create a Graphics2D drawing context for drawing on the panel.
         * (g.create() makes a copy of g, which will draw to the same place as g,
         * but changes to the returned copy will not affect the original.)
         */
        Graphics2D g2 = (Graphics2D)g.create();
        
        /* Turn on antialiasing in this graphics context, for better drawing.
         */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        /* Fill in the entire drawing area with white.
         */

        g2.setPaint(Color.WHITE);
        g2.fillRect(0,0,getWidth(),getHeight()); // From the old graphics API!
        
        /* Here, I set up a new coordinate system on the drawing area, by calling
         * the applyLimits() method that is defined below.  Without this call, I
         * would be using regular pixel coordinates.  This function sets the global
         * variable pixelSize, which I need for stroke widths in the transformed
         * coordinate system.
         */
                
        /* Finish by drawing a few shapes as an example.  You can erase the rest of 
         * this subroutine and substitute your own drawing.
         */
        
        //// THE IMAGE WITH THE SQUARE
        
        // make a square      
        Color [][]square = createImageSquare();
        BufferedImage imageOfSquare = makeBufferedImage(square, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // transform the first image by rendering into a second image
        BufferedImage squareTransformer = new BufferedImage(IMAGE_WIDTH*2, IMAGE_HEIGHT*2, BufferedImage.TYPE_INT_RGB);
    
        Graphics gSquare = squareTransformer.getGraphics();
        Graphics2D g2Square = (Graphics2D)gSquare.create();
        g2Square.setPaint(Color.WHITE);
        g2Square.fillRect(0,0,IMAGE_WIDTH*2,IMAGE_HEIGHT*2);

        applyapplyWindowToViewportTransformation(g2Square, -100, 100, -100, 100, IMAGE_WIDTH*2, IMAGE_HEIGHT*2,true);
        
        animate(g2Square);
        
        // render first image into second image
        g2Square.drawImage(imageOfSquare, -IMAGE_WIDTH/2, -IMAGE_HEIGHT/2, null);
        
        // render transformed image onto screen
        g2.drawImage( squareTransformer, 110, 100, null );

    
    
        ///make a rectangle
        Color [][]rectangle = createImageRectangle();
        BufferedImage imageOfRectangle = makeBufferedImage(rectangle, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        //transform the first rectangle by rendering into a second rectangle
        BufferedImage rectTransformer = new BufferedImage(IMAGE_WIDTH*2, IMAGE_HEIGHT*2, BufferedImage.TYPE_INT_RGB);
        Graphics gRect = rectTransformer.getGraphics();
        Graphics2D g2Rect = (Graphics2D)gRect.create();
        g2Rect.setPaint(Color.WHITE);
        g2Rect.fillRect(0,0,IMAGE_WIDTH*2,IMAGE_HEIGHT*2);
        
        applyapplyWindowToViewportTransformation(g2Rect, -100, 100, -100, 100, IMAGE_WIDTH*2, IMAGE_HEIGHT*2,true);
        animate(g2Rect);

        // render first image into second image
        g2Rect.drawImage(imageOfRectangle, -IMAGE_WIDTH/2, -IMAGE_HEIGHT/2, null);
        
        // render transformed image onto screen
        g2.drawImage( rectTransformer, 310, 100, null );
        
        
        
        
        // make lines
        Color [][]lines = createImageLines();
        BufferedImage imageOfLines = makeBufferedImage(lines, IMAGE_WIDTH, IMAGE_HEIGHT);        
        
        //transform the first line by rendering to rectangle
        BufferedImage lineTransformer = new BufferedImage(IMAGE_WIDTH*2, IMAGE_HEIGHT*2, BufferedImage.TYPE_INT_RGB);
        Graphics gLine = lineTransformer.getGraphics();
        Graphics2D g2Line = (Graphics2D)gLine.create();
        g2Line.setPaint(Color.WHITE);
        g2Line.fillRect(0,0,IMAGE_WIDTH*2,IMAGE_HEIGHT*2);
        

        applyapplyWindowToViewportTransformation(g2Line, -100, 100, -100, 100, IMAGE_WIDTH*2, IMAGE_HEIGHT*2,true);
        animate(g2Line);   
        
        
        // render first image into second image
        g2Line.drawImage(imageOfLines, -IMAGE_WIDTH/2, -IMAGE_HEIGHT/2, null);
        
        // render transformed image onto screen
        g2.drawImage( lineTransformer, 510, 100, null );
    }
    
  
    private void applyapplyWindowToViewportTransformation(Graphics2D g2,
            double left, double right, double bottom, double top, 
            int width, int height, 
            boolean preserveAspect) {
        if (preserveAspect) {
            // Adjust the limits to match the aspect ratio of the drawing area.
            double displayAspect = Math.abs((double)height / width);
            double requestedAspect = Math.abs(( bottom-top ) / ( right-left ));
            if (displayAspect > requestedAspect) {
                // Expand the viewport vertically.
                double excess = (bottom-top) * (displayAspect/requestedAspect - 1);
                bottom += excess/2;
                top -= excess/2;
            }
            else if (displayAspect < requestedAspect) {
                // Expand the viewport vertically.
                double excess = (right-left) * (requestedAspect/displayAspect - 1);
                right += excess/2;
                left -= excess/2;
            }
        }
        g2.scale( width / (right-left), height / (bottom-top) );
        g2.translate( -left, -top );
        double pixelWidth = Math.abs(( right - left ) / width);
        double pixelHeight = Math.abs(( bottom - top ) / height);
        pixelSize = (float)Math.max(pixelWidth,pixelHeight);
    }
    
    private void animate(Graphics2D graphic) {
    	// the transforms
        switch(keyframe) {
        	case 1: 
        		graphic.translate(-5, 7);
        		break;
        	case 2:
        		graphic.translate(-5, 7);
        		graphic.rotate(-Math.PI/4.0, 0, 0);
        		break;
        	case 3:
        		graphic.translate(-5, 7);
        		graphic.rotate(-Math.PI/4.0, 0, 0);
        		graphic.rotate(Math.PI/2.0, 0, 0);
        		break;
        	case 4:
        		graphic.translate(-5, 7);
        		graphic.rotate(-Math.PI/4.0, 0, 0);
        		graphic.rotate(Math.PI/2.0, 0, 0);
        		graphic.scale(2.0, 0.5);
        		break;
        }
    }
    
    private Color[][] createImageSquare() {
    	Color[][] pixels = new Color[IMAGE_WIDTH][IMAGE_HEIGHT];
    	
    	BufferedImage bImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    	Graphics g = bImage.getGraphics();
    	Graphics2D g2 = (Graphics2D)g.create();
    	
    	// draw the stuff
        g2.setPaint(Color.BLACK);
        g2.fillRect(0,0,IMAGE_WIDTH,IMAGE_HEIGHT);

        g2.setPaint(Color.CYAN);
        g2.fill(new Rectangle2D.Double(IMAGE_WIDTH/10, IMAGE_HEIGHT/10, IMAGE_WIDTH/2, IMAGE_HEIGHT/2));
        // done drawing the stuff
        
    	for (int x=0; x<IMAGE_WIDTH; x++) {
    		for (int y=0; y<IMAGE_HEIGHT; y++) {
    			int color = bImage.getRGB(x, y);
    			pixels[x][y] = new Color(color);
    		}
    	}
    	
    	return pixels;
    }
    
    private Color[][] createImageRectangle() {
    	Color[][] pixels = new Color[IMAGE_WIDTH][IMAGE_HEIGHT];

    	BufferedImage bImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    	Graphics g = bImage.getGraphics();
    	Graphics2D g2 = (Graphics2D)g.create();
    	
    	// draw the stuff
    	g2.setPaint(Color.BLACK);
        g2.fillRect(0,0,IMAGE_WIDTH,IMAGE_HEIGHT);

        g2.setPaint(Color.RED);
        g2.fill(new Rectangle2D.Double(IMAGE_WIDTH/10, IMAGE_HEIGHT/10, IMAGE_WIDTH/2+25, IMAGE_HEIGHT/2));
    	// done drawing the stuff

    	for (int x=0; x<IMAGE_WIDTH; x++) {
    		for (int y=0; y<IMAGE_HEIGHT; y++) {
    			int color = bImage.getRGB(x, y);
    			pixels[x][y] = new Color(color);
    		}
    	}

    	return pixels;
    }
    
    private Color[][] createImageLines() {
    	Color[][] pixels = new Color[IMAGE_WIDTH][IMAGE_HEIGHT];
    
    	BufferedImage bImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    	Graphics g = bImage.getGraphics();
    	Graphics2D g2 = (Graphics2D)g.create();
    	
    	// draw the stuff
    	g2.setPaint(Color.BLACK);
    	g2.fillRect(0,0,IMAGE_WIDTH,IMAGE_HEIGHT);
    	
    	g2.setPaint(Color.MAGENTA);
    	g2.setStroke( new BasicStroke(5*pixelSize) );
    	g2.draw(new Line2D.Double(0, 0, IMAGE_WIDTH,IMAGE_HEIGHT));
//    	 done drawing the stuff
    	
    	for (int x=0; x<IMAGE_WIDTH; x++) {
    		for (int y=0; y<IMAGE_HEIGHT; y++) {
    			int color = bImage.getRGB(x, y);
    			pixels[x][y] = new Color(color);
    		}
    	}

    	return pixels;
    }
    	
    private BufferedImage makeBufferedImage(Color[][] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x=0; x<width; x++) {
        	for (int y=0; y<height; y++) {
        		int rgb = pixels[x][y].getRGB();
        		image.setRGB(x, y, rgb);
        	}
        }
        return image;
    }
}