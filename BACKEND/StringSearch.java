package application;
import java.util.List;
public class StringSearch {
   public static void searchRide(List<Ride> rides, String query) { //passes the list of rides and the string that we are finding
       boolean found = false;
       System.out.println("\n🔍 Search Results for \"" + query + "\":");
       System.out.println("--------------------------------------------------");
       System.out.printf("%-5s %-20s %-10s %-10s\n", "Code", "Ride Name", "Time (min)", "Rating");
       System.out.println("--------------------------------------------------");
       for (Ride ride : rides) { //loops over each ride
           if (ride.getName().toLowerCase().contains(query.toLowerCase())) { //converts the ride's name to lower case and checks if it contains the string that we are finding in lower case
               System.out.printf("%-5d %-20s %-10d %-10s\n",
                   ride.getCode(), ride.getName(), ride.getEstimatedTime(), ride.getRating() + "★"); //prints the ride that matches the query
               found = true;
           }
       }
       if (!found) { // after the for each loop, if there are still no matches, it prints that there are no rides found matching the query
           System.out.println("❌ No rides found matching the query.");
       }
       System.out.println("--------------------------------------------------");
   }
}
