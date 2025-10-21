package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RideSelector {
    private RideList rideList;
    private List<Ride> selectedRides;
    private Scanner scanner;
    private int availableTime;
    private int totalTime;


    public RideSelector(int availableTime) {
        this.rideList = new RideList(); // list of the 8 rides
        this.selectedRides = new ArrayList<>(); // list for the selected rides of the user
        this.scanner = new Scanner(System.in);
        this.availableTime = availableTime; // (user input) sets the available time
        this.totalTime = 0; //counter for checking if the selected rides time exceeds the limit
    }


    public List<Ride> selectRides() {
        rideList.displayRides(); //displays the rides


        while (true) { // while loop until user exits
            System.out.printf("\nCurrent Time Used: %d / %d minutes\n", totalTime, availableTime); //shows the current time over vs time in minutes ( 0 / 300 )


            if (totalTime >= availableTime) { // Warns user and stops if total time reaches or exceeds available time
                System.out.println("⚠️ You have reached your maximum time! No more rides can be added.");
                break;
            }


            System.out.print("Enter ride code to select (1-8) or 0 to finish: "); // asks the user to input the code of the ride that they want to pick
            int rideCode = scanner.nextInt();


            if (rideCode == 0) { // Ends ride selection if user enters 0
                break;
            }


            Ride selectedRide = rideList.getRideByCode(rideCode); // Retrieves the Ride object for the entered code


            if (selectedRide != null) { // If the ride is valid, calculates new total time with its estimated time
                int newTotalTime = totalTime + selectedRide.getEstimatedTime();


                if (newTotalTime > availableTime) { // checks if the ride that will be added exceeds the available time
                    System.out.println("❌ Adding this ride will exceed your time! Please choose a different ride.");
                } else { // if it does not exceed, it will add the selected ride to the list
                    selectedRides.add(selectedRide);
                    totalTime = newTotalTime; // Updates totalTime with the new total
                    System.out.println(selectedRide.getName() + " added! (New Time: " + totalTime + " / " + availableTime + " minutes)"); //prints the ride that was added and the time
                }
            } else { //if the user types anything that isn't 1-8
                System.out.println("Invalid code. Try again.");
            }
        }


        // Display the final ride selection
        displayFinalSelection();
        return selectedRides;
    }


    private void displayFinalSelection() {
        System.out.println("\n🎢 Final Ride Selection:");
        if (selectedRides.isEmpty()) { //if the list is empty 
            System.out.println("❌ No rides were selected.");
        } else { // Prints the selected rides and their details
            System.out.println("--------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-10s\n", "Code", "Ride Name", "Time (min)", "Rating");
            System.out.println("--------------------------------------------------");
            for (Ride ride : selectedRides) { // uses for each loop to iterate over each ride in the selectedRides list
                System.out.printf("%-5d %-20s %-10d %-10s\n", 
                    ride.getCode(), ride.getName(), ride.getEstimatedTime(), ride.getRating() + "★");
            }
            System.out.println("--------------------------------------------------");
            System.out.println("⏳ Total Time Used: " + totalTime + " / " + availableTime + " minutes"); //prints the total time / max time


            if (KnapsackCalculator.canFitWithinTime(selectedRides, availableTime)) { //calls the KnapsackCalculator to check if the selected rides fit within the available time
                System.out.println("✅ Your selected rides fit within your available time!");
            } else { // if not asks the user to adjust the choice
                System.out.println("❌ Your selected rides exceed your available time! Please adjust your choices.");
            }
        }
    }


}
