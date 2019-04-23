
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.AcetateProvider;
import de.fhpotsdam.unfolding.providers.EsriProvider;
import de.fhpotsdam.unfolding.providers.GeoMapApp;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.ImmoScout;
import de.fhpotsdam.unfolding.providers.MapBox;
import de.fhpotsdam.unfolding.providers.MapQuestProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenMapSurferProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.OpenStreetMapProvider;
import de.fhpotsdam.unfolding.providers.OpenWeatherProvider;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.providers.ThunderforestProvider;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.ui.CompassUI;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PFont;
import controlP5.*;

import java.util.*;

public class Display extends PApplet {
	UnfoldingMap map;
	Map otherMap;
	
	ArrayList<LocationClass> list;
	ArrayList<LocationClass> displayed = new ArrayList<LocationClass>();
	ArrayList<SimplePointMarker> spmList = new ArrayList<SimplePointMarker>();

	LocationClass l;
	LocationClass current;
	
	String[] coordinates = {"30.26759", "-97.74299"};
	double rad = 5;
	
	SimplePointMarker m;
	ControlP5 cp5;
	Textfield searchBox, longitude, latitude, commentBox;
	Slider radius;
	Textlabel infoLabel, locationTitle, locationDescription, ratingLabel;
	RadioButton r1, r2;
	Button searchButton, commentButton;
	
	Knob rating;
	
	String searchString = "";
    float maxPanningDistance = 30;

	public void setup() {
        size(850, 600);
        smooth(200000000);
        otherMap = new Map("locations.txt");
        l = otherMap.search("HEB").get(0);
        displayed.add(l);
        cp5 = new ControlP5(this);
        
        current = new LocationClass("current", "hello", "hello", coordinates);
        
        AbstractMapProvider provider = new OpenStreetMap.OpenStreetMapProvider();
        //AbstractMapProvider provider = new Microsoft.RoadProvider();


        map = new UnfoldingMap(this, provider);
        
        Location austin = new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1]));
        map.zoomAndPanTo(15, austin);
        map.setPanningRestriction(austin, maxPanningDistance);
        map.setZoomRange(11, 17);
        map.setTweening(true);

        m = new SimplePointMarker(new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1])));
        map.addMarker(m);
        spmList.add(m);
        
        PFont font = createFont("arial",20);
        
        searchBox = cp5.addTextfield("")
        .setPosition(25, 20)
        .setSize(550,35)
        .setFont(font)
        .setFocus(true)
        .setAutoClear(false)
        .setColor(color(0,0,0))
        .setColorBackground(color(255,255,255));
        
        latitude = cp5.addTextfield("Latitude")
        .setPosition(25, 60)
        .setSize(75, 30)
        .setFont(font)
        .setFocus(false)
	    .setColorCaptionLabel(color(0,0,0))
        .setAutoClear(false)
        .setColor(color(0,0,0))
        .setColorBackground(color(255,255,255));
        
        longitude = cp5.addTextfield("Longitude")
	    .setPosition(108, 60)
	    .setSize(75, 30)
	    .setFont(font)
	    .setFocus(false)
	    .setColorCaptionLabel(color(0,0,0))
	    .setAutoClear(false)
	    .setColor(color(0,0,0))
	    .setColorBackground(color(255,255,255));
        
        commentBox = cp5.addTextfield("CommentBox")
	    .setPosition(598, 450)
	    .setSize(230, 100)
	    .setFont(font)
	    .setFocus(false)
	    .setColorCaptionLabel(color(0,0,0))
	    .setAutoClear(false)
	    .setColor(color(0,0,0))
	    .setColorBackground(color(255,255,255))
	    .hide();
        
        radius = cp5.addSlider("Radius")
	    .setPosition(465, 60)
	    .setMax(25)
	    .setSize(75, 30)
	    .setDefaultValue(5)
	    .setColorActive(color(0,0,0))
	    .setColorValue(color(125,160,165))
	    .setColorBackground(color(255,255,255))
	    
	    .setColorForeground(color(0,0,0))
	    .setColorLabel(color(0,0,0));

        searchButton = cp5.addButton("Search")
        .setValue(0)
        .setPosition(530,25)
        .setSize(40,25);
        
        commentButton = cp5.addButton("Comment")
        .setValue(0)
        .setPosition(785,555)
        .setSize(40,25)
        .hide();
        
        infoLabel = cp5.addTextlabel("label")
        .setText("Search results")
        .setPosition(645,20)
        .setColorValue(color(0))
        .setFont(createFont("Arial",20));
        
        locationTitle = cp5.addTextlabel("label2")
        .setText("")
        .setPosition(605,55)
        .setWidth(100)
        .setColorValue(color(0))
        .setFont(createFont("Arial",14));
        
        ratingLabel = cp5.addTextlabel("ratingLabel")
                .setText("Current Rating: 5.0/5.0")
                .setPosition(660,395)
                .setColorValue(color(0))
                .setFont(createFont("Arial",14))
                .hide();
        
        rating = cp5.addKnob("rating")
        		.setRange(0, 5)
        		.setResolution(65)
        		.setPosition(605, 385)
        		.setColorCaptionLabel(255)
        		.setColorBackground(color(120,120,120))
        		.setColorForeground(color(255,255,255))
        		.setColorActive(color(255,255,125));
        
        searchButton.addCallback(new CallbackListener() {
        	public void controlEvent(CallbackEvent theEvent) {
        		switch(theEvent.getAction()) {
                	case(ControlP5.ACTION_CLICK):
                		if(!"".equals(searchBox.getText())) {
                			if(!"".equals(longitude.getText()) && !"".equals(latitude.getText())) {
                				coordinates[0] = longitude.getText();
                				coordinates[1] = latitude.getText();
                				current = new LocationClass("current", "", "", coordinates);
                			}
                			rad = Double.valueOf(radius.getValue());
	                		searchString = searchBox.getText();
	                    	list = otherMap.search(searchString);
	                    	map.getLastMarkerManager().clearMarkers();
	                    	displayed.clear();
	                    	spmList.clear();
	                    	for(int i = 5; i > 0; i--) {
	                    		l = list.get(i);
	                    		System.out.println(l.getName());
	                    		if(current.distFrom(Float.valueOf(l.getCoordinates()[1]), Float.valueOf(l.getCoordinates()[0])) < rad) {
	                    			displayed.add(l);
		                    		
		                    		//Location place = new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1]));
		                            //map.zoomAndPanTo(15, place);
		                            
		                            m = new SimplePointMarker(new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1])));
		                            map.addMarker(m);
		                            spmList.add(m);
	                    		}
	                    		else {
	                    			if(list.size() > 6) {
		                    			list.remove(i);
		                    			i++;
	                    			} else {
	                    				//infoLabel.show();
	                    				//infoLabel.setText("Few results found...");
	                    				for(int j = 5; j > 0; j--) {
	        	                    		l = list.get(j);
	        	                    		System.out.println(l.getName());
        	                    			displayed.add(l);
        		                    		//Location place = new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1]));
        		                            //map.zoomAndPanTo(15, place);
        		                            
        		                            m = new SimplePointMarker(new Location(Float.parseFloat((l.getCoordinates()[0])), Float.parseFloat(l.getCoordinates()[1])));
        		                            String id = l.getName() + "\n" + l.getAddress() + "\n\nLocation type: " + l.getCategory().substring(0, 1).toUpperCase()
        		                            		+ l.getCategory().substring(1) + "\n\nLatitude: " + l.getCoordinates()[1] + "\nLongitude: " + l.getCoordinates()[0] + "\n\nComments: ";
        		                            for(String s: l.getComments()) {
        		                            	id += "\n" + s;
        		                            	System.out.println(s);
        		                            }
        		                            if(l.getComments().isEmpty()) {
        		                            	id += "\nNo comments added.\nBe the first to add a comment!";
        		                            }
        		          
        		                            m.setId(id);
        		                            map.addMarker(m);
        		                            spmList.add(m);
	                    				}
	                    				i = 0;
	                    			}

	                    		}
	                    		
	                    	}
	                        Location place = new Location(Float.parseFloat((displayed.get(0).getCoordinates()[0])), Float.parseFloat(displayed.get(0).getCoordinates()[1]));
	                        map.zoomAndPanTo(15, place);
	                    	
	                        
	                		System.out.println(searchString);
                		}
	                	break;
	                	
        		}
            }
        });
        commentButton.addCallback(new CallbackListener() {
        	public void controlEvent(CallbackEvent theEvent) {
        		switch(theEvent.getAction()) {
                	case(ControlP5.ACTION_CLICK):
                		if(!"".equals(commentBox.getText())) {
	                        otherMap.createComment(current, commentBox.getText());
	                		System.out.println(commentBox.getText());
	                        commentBox.clear();
                		}
	                	break;
	                	
        		}
            }
        });
        MapUtils.createDefaultEventDispatcher(this, map);
        
	}
	
	public void mouseClicked() {
		
	    Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
	    if (hitMarker != null) {
	        // Select current marker 
			// Deselect all other markers
	        for (Marker marker : map.getMarkers()) {
	            marker.setSelected(false);
	        }
	        hitMarker.setSelected(true);
	        if(rating.getValue() != 0) {
		        otherMap.createRating(current, Float.toString(rating.getValue()));
		        rating.setValue(0);
	        }
	        current = otherMap.findLocationByName(hitMarker.getId().split("\n")[0]);
	        System.out.println(current.toString());
	        locationTitle.setText(hitMarker.getId());
	        ratingLabel.setText(current.getAverageRatings());
	        
	        commentButton.show();
	        commentBox.show();
	        rating.show();
	        ratingLabel.show();
	        
	    }
        
	    
	}
	 
	public void draw() {
		background(153);
		noClip();
	    map.draw();
	    
	    fill(255, 160);
	    stroke(125);
	    strokeWeight(2);
	   	rect(590, 15, 245, 570);;
	   	
	   	
        fill(0);
        for(int i = 0; i < displayed.size(); i++) {
        	LocationClass lc = displayed.get(i);
    	    ScreenPosition sp = spmList.get(i).getScreenPosition(map);
            text(lc.getName(), sp.x - textWidth(lc.getName()) / 2, sp.y + 20);
            text(lc.getAddress(), sp.x - textWidth(lc.getAddress()) / 2, sp.y + 35);
        }

	    
	}
 
}