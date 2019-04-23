import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LocationClass {
    private String name, address, category;
    private String[] coordinates;
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<Float> ratings = new ArrayList<Float>();

    public LocationClass(String name, String address, String category, String[] coordinates) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String[] getCoordinates() {
        // Stored as [Longitude, Latitude]
        return coordinates;
    }

    public String toString() {
        String toReturn = name + ", " + address + " ," +
                category + " ,[" + coordinates[0] + ", " + coordinates[1] + "]" + ", " + comments.toString();
        return toReturn;

    }
    
    public ArrayList<String> getComments() {
    	return comments;
    }
    
    public void addComments(String comment) {
    	comments.add(comment);
    }
    
    public String getAverageRatings() {
    	if(ratings.size() != 0) {
        	double sum = 0;
        	for(double rating: ratings) {
        		sum += rating;
        	}
        	return "Current rating: " + String.format("%.2f", sum/(double)ratings.size());
    	} else {
    		return "No ratings added.";
    	}

    }
    
    public void addRatings(String rating) {
    	System.out.println(rating);
    	ratings.add(Float.valueOf(rating));
    }
    
    public double distFrom(float lat2, float lng2) {
    	float lng1 = Float.valueOf(coordinates[0]);
    	float lat1 = Float.valueOf(coordinates[1]);

	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
	
	    System.out.println(dist*0.00062137);
	    return dist*0.00062137;
    }
}
