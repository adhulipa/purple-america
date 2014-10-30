
import java.awt.Color;


public class Region {

    private String name;
    private String parent;
    private float[] longitudes;
    private float[] latitudes;
    private int n, democratic, republican, other;
    private Color color;
    
    public void setColor(Color color) {
    	this.color = color;
    }
    
    public Color getColor() {
    	return color;
    }
    
    public Region(String name, String parent, float[] x, float[] y) {
        this.name = name;
        this.parent = parent;
        this.longitudes = x;
        this.latitudes = y;
    }
    
    public Region() {

    }

	@Override
    public String toString() {
		String r;
		r = "\n";
		
		r += name;
		r += ", " + parent;
		
		r += "\n";
		for (float each : longitudes) {
			r += each + ", ";
		}
		r = r.substring(0, r.length() - 2);
		
		r += "\n";
		for (float each : latitudes) {
			r += each + ", ";
		}
		r = r.substring(0, r.length() - 2);
		return r;
	}
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setParent(String parent) {
        this.parent = parent;
    }
    
    public String getContainingState() {
        return parent;
    }
    
    public void setLongitudes(float[] longitudes) {
        this.longitudes = longitudes;
    }
    
    public float[] getLongitudes() {
        return longitudes;
    }
    
    public void setLatitudes(float[] latitudes) {
        this.latitudes = latitudes;
    }
    
    public float[] getLatitudes() {
        return latitudes;
    }
    
    public void setNumOfCoords(int n) {
        this.n = n;
    }
    
    public int getNumOfCoords() {
        return n;
    }
    
    public void setDemocratic(int democrstic) {
		this.democratic = democrstic;
	}
	
	public int getDemocratic() {
		return this.democratic;
	}
	
	public void setRepublican(int republican) {
		this.republican = republican;
	}
	
	public int getRepublican() {
		return this.republican;
	}
	
	public void setOther(int other) {
		this.other = other;
	}
	
	public int getOther() {
		return this.other;
	}
}