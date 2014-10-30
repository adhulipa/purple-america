
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;

public class Purple {
	
	private static Color MAP_COLOR = Color.BLACK;
	private static int MAP_WIDTH = 1320;
	private static int MAP_HEIGHT = 768;
	private static String region, year;
    private ServletContext context;
	
	Purple(ServletContext context) {
		this.context = context;
	}
    
	public static void main(String args[]) {
        
        int[] xs, ys;
        int n;
        
        try {
	        region = args[0];
	        year = args[1];
	        MAP_WIDTH = new Integer(args[2]).intValue();
	        MAP_HEIGHT = new Integer(args[3]).intValue();
        } catch (ArrayIndexOutOfBoundsException e){
        	//System.out.println("Please enter cmd line args for map-type and election year and widrth and height of image");
        	e.printStackTrace();
        	System.exit(1);;
        }
        DataLoader dataLoader = new DataLoader();
        dataLoader.setFilename("data/" + region + ".txt");
        dataLoader.setElectionYear(year);
        
        dataLoader.loadAllData();
        ArrayList<Region> data = dataLoader.getData();
        
        double[] lower = dataLoader.getLowerLeft();
        double[] upper = dataLoader.getUpperRight();
        int numOfData = dataLoader.getNumOfData();
        
        drawRegionList(data, lower, upper);
        //drawmyimage();
    }
	
	private static void drawRegionList(ArrayList<Region> data, double[] lower, double[] upper) {

		BufferedImage bi = new BufferedImage(MAP_WIDTH + 25, MAP_HEIGHT + 25, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D page = bi.createGraphics();
        page.setColor(MAP_COLOR);
        Polygon2D p;
        
        for (Region region : data) {
        	p = getPolygon(region, lower, upper);
        	page.setColor(region.getColor());
        	//System.out.println(region.getColor());
        	page.fill(p);
        	//Polygon pa = new Polygon();
        }
        
        try {
			//ImageIO.write(bi, "PNG", new File("C:\\Users\\aditya\\Desktop\\Purple "+ region + year + ".PNG"));
			ImageIO.write(bi, "PNG", new File("/Users/adhulipa/Desktop/" + region + year + ".PNG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Polygon2D getPolygon(Region region, double[] lower, double[] upper) {
		
    	int numOfPoints = region.getNumOfCoords();
    	float longs[] = region.getLongitudes();
    	float lats[] = region.getLatitudes();
    	
    	int scaleX = (int) ((MAP_WIDTH) / (upper[0] - lower[0])); 
    	int originX = (int) (-scaleX * lower[0]); 
    	
    	int scaleY = (int) ((MAP_HEIGHT) / (lower[1] - upper[1])); 
    	int originY = (int) (-scaleY * upper[1]); 
    		
    	for (int i = 0 ; i < numOfPoints; i += 1) {
    		longs[i] = scaleX * longs[i] + originX;
    		lats[i] = scaleY * lats[i] + originY;
    	}
    	        
        Polygon2D p = new Polygon2D(longs, lats, numOfPoints);
		return p;
	}
	
	
	/* Non-static equivalent function */
	public BufferedImage getImage(String region, String year, int width, int height) {
		
		MAP_WIDTH = width;
		MAP_HEIGHT = height;
		
	    DataLoader dataLoader = new DataLoader(context);
	    dataLoader.setFilename("/WEB-INF/data/" + region + ".txt");
	    dataLoader.setElectionYear(year);
	    
	    dataLoader.loadAllData();
	    ArrayList<Region> data = dataLoader.getData();
	    
	    double[] lower = dataLoader.getLowerLeft();
	    double[] upper = dataLoader.getUpperRight();
	    int numOfData = dataLoader.getNumOfData();
	    

		BufferedImage bi = new BufferedImage(MAP_WIDTH + 25, MAP_HEIGHT + 25, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D page = bi.createGraphics();
        page.setColor(MAP_COLOR);
        Polygon2D p;
        
        for (Region each_region : data) {
        	p = getPolygon(each_region, lower, upper);
        	page.setColor(each_region.getColor());
        	//System.out.println(region.getColor());
        	page.fill(p);
        	//Polygon pa = new Polygon();
        }
        
        return bi;
	}
}
	