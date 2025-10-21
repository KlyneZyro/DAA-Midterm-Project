package application;

import java.util.ArrayList;
import java.util.List;

public class KnapsackCalculator { // provides static methods to call methods directly with the class without creating objects. (Utility class)

    // Brute-force approach to find the best rides maximizing rating within available time
    public static List<Ride> recommendBestRides(List<Ride> rides, int timeLimit) { //method that returns a list for the recommended best rides
        List<Ride> bestRides = new ArrayList<>();
        int maxRating = 0; // tracks the rating
        int n = rides.size(); // initializes the number of rides 
        int bestTotalTime = 0; // Track total time for the best selection

        // Try all possible ride subsets (2^n possibilities) O(2^N) Exponential.  its for building a subset each iteration and calculates time and rating 
        for (int i = 0; i < (1 << n); i++) {
            List<Ride> currentSelection = new ArrayList<>(); // creates a list for the possible ride subsets
            int totalTime = 0; //resets the time	
            int totalRating = 0; //resets the rating

            for (int j = 0; j < n; j++) { //checks each ride in the list 
                if ((i & (1 << j)) != 0) { // If ride is included in the subset
                    Ride ride = rides.get(j);
                    if (totalTime + ride.getEstimatedTime() <= timeLimit) { // if the time doesn't exceeds
                        currentSelection.add(ride); //add the current ride to the current subset of rides
                        totalTime += ride.getEstimatedTime(); //add the estimated time of the ride
                        totalRating += ride.getRating(); // add the rating of the ride
                    }
                }
            }

            // If this combination has a higher rating, update best selection. it keeps track of the highest rated combination of rides that fit within the time limit 
            if (totalRating > maxRating) {
                maxRating = totalRating;
                bestRides = new ArrayList<>(currentSelection);
                bestTotalTime = totalTime; // Update total time for best selection
            }
        }

        // Display recommended rides and total time used
        System.out.println("\n🎢 Recommended Ride Selection (Max Popularity within " + timeLimit + " min):");
        if (bestRides.isEmpty()) {
            System.out.println("❌ No rides can fit within the given time.");
        } else {
            System.out.println("---------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-10s\n", "Code", "Ride Name", "Time (min)", "Rating");
            System.out.println("---------------------------------------------------------");
            for (Ride ride : bestRides) {
                System.out.printf("%-5d %-20s %-10d %-10s\n",
                        ride.getCode(), ride.getName(), ride.getEstimatedTime(), ride.getRating() + "★");
            }
            System.out.println("---------------------------------------------------------");
            System.out.println("✅ Total Time Used: " + bestTotalTime + " / " + timeLimit + " minutes");
        }

        return bestRides;
    }

    // Helper function to calculate total time of a ride list
    public static int getTotalTime(List<Ride> selectedRides) {
        int total = 0;
        for (Ride ride : selectedRides) {
            total += ride.getEstimatedTime();
        }
        return total;
    }

    //  Helper function to check if selected rides fit within the time limit
    public static boolean canFitWithinTime(List<Ride> selectedRides, int availableTime) {
        return getTotalTime(selectedRides) <= availableTime;
    }
}
