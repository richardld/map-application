import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import info.debatty.java.stringsimilarity.*;
import info.debatty.java.stringsimilarity.experimental.Sift4;

public class Map {
    ArrayList<LocationClass> locations = new ArrayList<LocationClass>();
    HashMap<String, String> comments = new HashMap<String, String>();
    HashMap<String, String> ratings = new HashMap<String, String>();

    public Map(String fileLocation) {
        try {
            Scanner kevin = new Scanner(new File(fileLocation));
            while(kevin.hasNextLine()) {
                String[] line = kevin.nextLine().trim().split("\t");
                /*for(String s: line) {
                    System.out.print(s + " ");
                }
                System.out.println();*/
                String[] coordinates = {line[4], line[3]};
                locations.add(new LocationClass(line[0],line[1], line[2], coordinates));
            }
            kevin.close();
        } catch(FileNotFoundException f) {

        }
        
        try {
        	// Comments are stored in the format
        	// locationName + \t + comment + \t ... etc.
            Scanner kevin = new Scanner(new File("comments.txt"));
            while(kevin.hasNextLine()) {
                String[] line = kevin.nextLine().trim().split("\t");
                String s = "";
                for(int i = 1; i < line.length; i++) {
                	s += line[i] + "\t";
                }
            	comments.put(line[0], s);
                LocationClass l = findLocationByName(line[0]);
                for(int i = 1; i < line.length; i++) {
                	l.addComments(line[i]);
                }
                
            }
            kevin.close();
        } catch(FileNotFoundException f) {

        }
        
        try {
        	// Ratings are stored in the format
        	// locationName + \t + rating1 + \t ... etc.
            Scanner kevin = new Scanner(new File("ratings.txt"));
            while(kevin.hasNextLine()) {
                String[] line = kevin.nextLine().trim().split("\t");
                String s = "";
                for(int i = 1; i < line.length; i++) {
                	s += line[i] + "\t";
                }
            	ratings.put(line[0], s);
                LocationClass l = findLocationByName(line[0]);
                if(line.length >= 1) {
                    for(int i = 1; i < line.length; i++) {
                    	l.addRatings(line[i]);
                    }
                }
                
            }
            kevin.close();
        } catch(FileNotFoundException f) {

        }
        sortLocations();
        System.out.println(Arrays.toString(locations.toArray()));

    }
    
    public void createComment(LocationClass l, String comment) {
    	if(comments.containsKey(l.getName())) {
    		comments.put(l.getName(), comments.get(l.getName()) + comment + "\t");
    	} else {
    		comments.put(l.getName(), comment + "\t");
    	}
    	l.addComments(comment);
    	try {
			PrintWriter notkevin = new PrintWriter(new File("comments.txt"));
			for(String s: comments.keySet()) {
				notkevin.println(s + "\t" + comments.get(s));
			}
			notkevin.flush();
			notkevin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public void createRating(LocationClass l, String rating) {
    	if(ratings.containsKey(l.getName())) {
    		ratings.put(l.getName(), comments.get(l.getName()) + rating + "\t");
    	} else {
    		ratings.put(l.getName(), rating + "\t");
    	}
    	l.addRatings(rating);
    	try {
			PrintWriter notkevin = new PrintWriter(new File("ratings.txt"));
			for(String s: ratings.keySet()) {
				notkevin.println(s + "\t" + ratings.get(s));
			}
			notkevin.flush();
			notkevin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public LocationClass findLocationByName(String id){    
        for (LocationClass lc : locations) {
            if (lc.getName().equals(id)) {
                return lc;
            }
        }
        return null; 
    }

    public void sortLocations() {
        long startTime = System.nanoTime();
        Collections.sort(locations, new Comparator<LocationClass>() {
            @Override
            public int compare(LocationClass l1, LocationClass l2) {
                return l1.getName().compareTo(l2.getName());
            }
        });
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
    
    public ArrayList<LocationClass> search(String name) {
    	TreeMap<Double, Integer> tm = new TreeMap<Double, Integer>();
    	ArrayList<LocationClass> results = new ArrayList<LocationClass>();
    	MetricLCS comp1 = new MetricLCS();
    	Cosine comp2 = new Cosine();
    	int i = 0;
    	for(LocationClass l: locations) {
    		double value = (comp1.distance(name, l.getName()) + comp2.distance(name,  l.getName())*9)/10;
    		tm.put(value, i);
    		i++;
    	}
    	for(int num: tm.values()) {
    		results.add(locations.get(num));
    	}
    	return results;
    }
    
}
