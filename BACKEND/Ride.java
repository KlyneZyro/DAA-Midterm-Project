package application;
	
	public class Ride {
	    private int code;
	    private String name;
	    private int estimatedTime; // Ride duration + wait time
	    private int rating;        // Star rating (1-5)
	  
	
	    //constructor
	    public Ride(int code, String name, int estimatedTime, int rating) {
	        this.code = code;
	        this.name = name;
	        this.estimatedTime = estimatedTime;
	        this.rating = rating;
	
	    }
	
	    // Getters
	    public int getCode() {
	        return code;
	    }
	
	    public String getName() {
	        return name;
	    }
	
	    public int getEstimatedTime() {
	        return estimatedTime;
	    }
	
	    public int getRating() {
	        return rating;
	    }
	
	
	}
