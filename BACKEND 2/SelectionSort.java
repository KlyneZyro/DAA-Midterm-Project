package application;

import java.util.List;

public class SelectionSort {
    public void sortRides(List<Ride> rides, int sortChoice) {
        // sortChoice:
        // 1 - Alphabetical (by name)
        // 2 - Popularity (Rating, descending)
        // 3 - Estimated Time (ascending)

        int n = rides.size();
        for (int i = 0; i < n - 1; i++) {
            int selectedIndex = i;
            for (int j = i + 1; j < n; j++) {
                boolean condition = false;
                if (sortChoice == 1) {
                    // Alphabetical: compare ride names (ignore case)
                    condition = rides.get(j).getName().compareToIgnoreCase(rides.get(selectedIndex).getName()) < 0; // checks if the String of the first one is before the second one then make the condition true 
                } else if (sortChoice == 2) {
                    // Popularity: higher rating should come first
                    condition = rides.get(j).getRating() > rides.get(selectedIndex).getRating();
                } else if (sortChoice == 3) {
                    // Estimated Time: lower time first
                    condition = rides.get(j).getEstimatedTime() < rides.get(selectedIndex).getEstimatedTime();
                }
                if (condition) { // If condition is true, updates selectedIndex to the current ride’s index (j)
                    selectedIndex = j;
                }
            }
            // Swap rides[i] and rides[selectedIndex] if needed after a whole iteration of the [j] for loop
            if (selectedIndex != i) {
                Ride temp = rides.get(i);
                rides.set(i, rides.get(selectedIndex));
                rides.set(selectedIndex, temp);
            }
        }
        System.out.println("Rides sorted successfully.");
    }
}
