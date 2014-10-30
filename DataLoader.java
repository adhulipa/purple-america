
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;

public class DataLoader {
    
    private String filename;
    private int TOTAL_POP, TOTAL_DEM, TOTAL_REP, TOTAL_OTH;
    private ArrayList<Region> regions;
    private Map<String, ArrayList<Region>> regionsMap = new HashMap<String, ArrayList<Region>>();
	private Map<String, Map<String, Region>> stateCounties= new HashMap<String, Map<String, Region>>();
    private double[] lowerLeft;
    private double[] upperRight;
    private int numOfRegions;
    private String year;
    private ServletContext context;
    
    public DataLoader() {}
    
    public DataLoader(ServletContext context) {
		this.context = context;
	}

	public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public void setElectionYear(String year) {
    	this.year = year;
    }
    
    public ArrayList<Region> getData() {
        ArrayList<Region> regions = new ArrayList<Region>();
        
        for (Entry<String, ArrayList<Region>> each : regionsMap.entrySet()) {
        	regions.addAll(each.getValue());
        }
        
        // Modify Color of region accroding to TOTAL_POP & Region pop
        for (Region eachRegion : regions) {
        	int dem = eachRegion.getDemocratic();
        	int rep = eachRegion.getRepublican();
        	int oth = eachRegion.getOther();
        	int tot = dem + rep + oth;
        	
        	float red = (float)rep / TOTAL_POP;
        	float gre = (float)oth / TOTAL_POP;
        	float blu = (float)dem / TOTAL_POP;
        	
        	System.out.println(red + " " + gre + " " + blu);
        	
        	Color color = new Color(red, gre, blu);
        	//eachRegion.setColor(color);

        	float R = (float)TOTAL_REP / (float)TOTAL_POP;
        	float G = (float)TOTAL_OTH / (float)TOTAL_POP;
        	float B = (float)TOTAL_DEM / (float)TOTAL_POP;
        	
        	if (eachRegion.getColor() != null ) {

            	int r = eachRegion.getColor().getRed();
            	int g = eachRegion.getColor().getGreen();
            	int b = eachRegion.getColor().getBlue();
            	
            	//System.out.println(R + " " + G + " " + B);
            	//System.out.println((r) + " " + (g/G) + " " + (b/B));
            	color = new Color(r, g, b);
            	//eachRegion.setColor(color);
        	}
        	
        }
        
        return regions;
    }
    
    
    public double[] getLowerLeft() {
    	return lowerLeft;
    }
    
    private void setLowerLeft(String coords) {
    	String xy[] = coords.split("   ");
    	
    	lowerLeft = new double[2];
    	lowerLeft[0] = new Double(xy[0]).doubleValue();
    	lowerLeft[1] = new Double(xy[1]).doubleValue();
    	
    }
    
    private void setUpperRight(String coords) {
    	String xy[] = coords.split("   ");
    	upperRight = new double[2];
    	upperRight[0] = new Double(xy[0]).doubleValue();
    	upperRight[1] = new Double(xy[1]).doubleValue();
    }
    
    private void setNumOfData(String num) {
    	numOfRegions = new Integer(num).intValue();
    }
    
    public double[] getUpperRight() {
    	return upperRight;
    }
    
    public int getNumOfData() {
    	return numOfRegions;
    }
    
    private Region readRegion(BufferedReader reader) throws IOException {
    	
    	Region region = new Region();
    	region.setName(reader.readLine());
    	region.setParent(reader.readLine());
    	region.setNumOfCoords(new Integer(reader.readLine()).intValue());
    	
    	//Read n Co-Ordinates
    	//Initialize variables
    	float x[] = new float[region.getNumOfCoords()];
    	float y[] = new float[region.getNumOfCoords()];
    	String coords[];
    	for (int i = 0; i < region.getNumOfCoords(); i += 1) {
    		coords = reader.readLine().split("   ");;
    		x[i] = new Float(coords[0]).floatValue();
    		y[i] = new Float(coords[1]).floatValue();
    		
    	}
    	region.setLongitudes(x);
    	region.setLatitudes(y);
    	
    	return region;
    }
    
    

	private void readRegionList(int numOfRegions, BufferedReader reader) throws IOException {
        regions = new ArrayList<Region>();
        Region region = new Region();
        
        for (int i = 0; i < numOfRegions; i += 1) {
        	
        	//Read and throw away the blank line at the beginning of every region
        	reader.readLine();
        	
        	//Read a region and put it into arraylist of regions
        	region = readRegion(reader);
        	
        	//////////
        	String state = region.getContainingState();
        	ArrayList<Region> counties;
        	if (!regionsMap.containsKey(state)) {
        		counties = new ArrayList<Region>();
        	}
        	
        	else {
        		counties = regionsMap.get(state);
        	}
        	counties.add(region);
    		regionsMap.put(state, counties);
        	regions.add(region);
        }
	}
    
    public void loadAllData() {    	
    	loadRegionData();
    	loadElectionData();
    }
    
    private void loadElectionData() {
    	for (Entry<String, ArrayList<Region>> each : regionsMap.entrySet()){
    		String state = (String) each.getKey();
    		ArrayList<Region> countyList = regionsMap.get(state);
    		    		
    		try {
				
    			
    			String fn = "/WEB-INF/data/" + state + year +".txt";
    			
    			InputStream is = context.getResourceAsStream(fn);
    			InputStreamReader isr = new InputStreamReader(is);
    			BufferedReader reader = new BufferedReader(isr);
				
				
				reader.readLine();
				Region county;
				String line;
				for (int i = 0; i < countyList.size(); i += 1 ) {
					line = reader.readLine();
					//System.out.println(line);
					if(line != null) {
						String votes[] = line.split(",");
						String countyName = votes[0]; 
						
						//Update TOTAL_POP
						TOTAL_POP += new Integer(votes[1]).intValue() +
									 new Integer(votes[2]).intValue() +
									 new Integer(votes[3]).intValue();
						
						TOTAL_DEM += new Integer(votes[2]).intValue();
						TOTAL_REP += new Integer(votes[1]).intValue();
						TOTAL_OTH += new Integer(votes[3]).intValue();
						
						int[] indices = getRegionsWithName(countyName, countyList);
						for (int j : indices) {
							county = countyList.get(j);
							county.setRepublican(new Integer(votes[1]).intValue());
							county.setDemocratic(new Integer(votes[2]).intValue());
							county.setOther(new Integer(votes[3]).intValue());
							assignColor(county);
						}
					}
				}
				reader.close();				
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private int[] getRegionsWithName(String countyName,
			ArrayList<Region> countyList) {
		
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	int[] result;
    	Region county;
    	
    	for (int  i = 0; i < countyList.size(); i += 1) {
    		county = countyList.get(i);
    		if (county.getName().equalsIgnoreCase(countyName)) {
    			temp.add(i);
    		}
    	}
    	
    	result = new int[temp.size()];
    	int i = 0;
    	for (Integer each : temp) {
    		result[i++] = each.intValue();
    	}
		return result;
	}

	private void assignColor(Region region) {
    	
    	float dem, rep, oth, tot;
   
    	dem = region.getDemocratic();
    	rep = region.getRepublican();
    	oth = region.getOther();
    	tot = dem + rep + oth;
    	
    	float mult =  1.0f;
    	
    	if (tot != 0) {
    		region.setColor(new Color((rep/tot) * mult, (oth/tot) * mult, (dem/tot) * mult));
    	}		
	}

	public void loadRegionData() {
        System.out.print("Loading data...");
        BufferedReader reader = null;        
        try {
        	
    		InputStream is = context.getResourceAsStream(filename);
    		InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            
            setLowerLeft(reader.readLine());
            setUpperRight(reader.readLine());
            setNumOfData(reader.readLine());
            
            readRegionList(numOfRegions, reader);
            
            reader.close();
            
            ////////////
           
            //loadElectionData();
            
            /////////
            
            System.out.println("DONE!");
            
        } catch(IOException e) {
            System.out.println("Check if filename is specified correctly");
            e.printStackTrace();
        }
    }
}