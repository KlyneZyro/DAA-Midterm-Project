package application;


import java.util.ArrayList;
import java.util.List;


public class RideList {
    private List<Ride> rides; // creates a Ride class list


    public RideList() {
        rides = new ArrayList<>();
        // Adding 8 rides with increasing distances
        rides.add(new Ride(1, "Roller Coaster", 50, 5));
        rides.add(new Ride(2, "Ferris Wheel", 80, 4));
        rides.add(new Ride(3, "Bumper Cars", 60, 3));
        rides.add(new Ride(4, "Haunted House", 40, 4));
        rides.add(new Ride(5, "Water Slide", 70, 5));
        rides.add(new Ride(6, "Swing Ride", 50, 3));
        rides.add(new Ride(7, "Drop Tower", 90, 5));
        rides.add(new Ride(8, "Pirate Ship", 60, 4));
        // rides.add(new Ride(9, "Go-Karts", 80, 4));
    }


    	//displays the rides
    public void displayRides() {
        System.out.println("\n🎢 Available Rides:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-10s\n", "Code", "Ride Name", "Time (min)", "Rating");
        System.out.println("--------------------------------------------------");
        for (Ride ride : rides) { //for each ride in the rides list
            System.out.printf("%-5d %-20s %-10d %-10s\n",
                    ride.getCode(), ride.getName(), ride.getEstimatedTime(), ride.getRating() + "★"); //prints the rides
        }
        System.out.println("--------------------------------------------------");
    }


    // retrieves a ride by its code
    public Ride getRideByCode(int code) {
        for (Ride ride : rides) {
            if (ride.getCode() == code) {
                return ride;
            }
        }
        return null;
    }
   
    // gets the rides
    public List<Ride> getRides() {
        return rides;
    }
}
