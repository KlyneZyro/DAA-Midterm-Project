package application;

import java.util.ArrayList;
import java.util.List;

public class OptimalPathCalculator {
    private List<Integer> selectedRides;
    private int[][] distanceMatrix;
    private List<Integer> bestPath;
    private int minDistance = Integer.MAX_VALUE;

    public OptimalPathCalculator(List<Integer> selectedRides, int[][] distanceMatrix) {
        this.selectedRides = selectedRides;
        this.distanceMatrix = distanceMatrix;
        this.bestPath = new ArrayList<>();
    }

    public void findOptimalPath() {
        List<Integer> rides = new ArrayList<>(selectedRides);
        permute(new ArrayList<>(), rides, 0);
    }

    private void permute(List<Integer> currentPath, List<Integer> remainingRides, int currentDistance) {
        if (remainingRides.isEmpty()) {
            // Get the last visited ride
            int lastRide = currentPath.get(currentPath.size() - 1);
            // Add distance back to entrance (Ride 0)
            int totalDistance = currentDistance + distanceMatrix[lastRide][0];

            // Print path with return to entrance
            System.out.print("Path: Entrance → ");
            for (int ride : currentPath) {
                System.out.print("Ride " + ride + " → ");
            }
            System.out.println("Entrance | Total Distance: " + totalDistance + " meters");

            // Update best path if a shorter distance is found
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestPath = new ArrayList<>(currentPath);
            }
            return;
        }

        for (int i = 0; i < remainingRides.size(); i++) {
            int nextRide = remainingRides.get(i);
            int previousRide = currentPath.isEmpty() ? 0 : currentPath.get(currentPath.size() - 1);
            int newDistance = currentDistance + distanceMatrix[previousRide][nextRide];

            List<Integer> newPath = new ArrayList<>(currentPath);
            newPath.add(nextRide);
            List<Integer> newRemaining = new ArrayList<>(remainingRides);
            newRemaining.remove(i);

            permute(newPath, newRemaining, newDistance);
        }
    }

    public List<Integer> getBestPath() {
        return bestPath;
    }

    public int getMinDistance() {
        return minDistance;
    }
}

/* Nearest Neighbor (NN)(Greedy) algorithm. More optimized than brute force (Exhaustive search)
package application;

import java.util.ArrayList;
import java.util.List;

public class OptimalPathCalculator {
    private List<Integer> selectedRides;
    private int[][] distanceMatrix;
    private List<Integer> bestPath;
    private int totalDistance;

    public OptimalPathCalculator(List<Integer> selectedRides, int[][] distanceMatrix) {
        this.selectedRides = selectedRides;
        this.distanceMatrix = distanceMatrix;
        this.bestPath = new ArrayList<>();
        this.totalDistance = 0;
    }

    public void findOptimalPath() {
        List<Integer> remainingRides = new ArrayList<>(selectedRides);
        List<Integer> path = new ArrayList<>();
        int currentRide = 0; // Start at entrance
        int distance = 0;

        // Build path by choosing the nearest unvisited ride
        while (!remainingRides.isEmpty()) {
            int nearestRide = findNearestRide(currentRide, remainingRides);
            distance += distanceMatrix[currentRide][nearestRide];
            path.add(nearestRide);
            remainingRides.remove(Integer.valueOf(nearestRide));
            currentRide = nearestRide;
        }

        // Add return to entrance
        distance += distanceMatrix[currentRide][0];
        this.bestPath = path;
        this.totalDistance = distance;

        // Print the path
        System.out.print("Path: Entrance → ");
        for (int ride : path) {
            System.out.print("Ride " + ride + " → ");
        }
        System.out.println("Entrance | Total Distance: " + distance + " meters");
    }

    private int findNearestRide(int currentRide, List<Integer> remainingRides) {
        int nearestRide = remainingRides.get(0);
        int minDist = distanceMatrix[currentRide][nearestRide];

        for (int ride : remainingRides) {
            int dist = distanceMatrix[currentRide][ride];
            if (dist < minDist) {
                minDist = dist;
                nearestRide = ride;
            }
        }
        return nearestRide;
    }

    public List<Integer> getBestPath() {
        return new ArrayList<>(bestPath); // Return a copy for safety
    }

    public int getMinDistance() {
        return totalDistance;
    }
}
*/