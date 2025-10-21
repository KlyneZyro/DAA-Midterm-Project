package application;

public class DistanceMatrix {
    private final int[][] matrix;

    public DistanceMatrix() {
        matrix = new int[][] {
            //  Entrance, Ride 1, Ride 2, Ride 3, Ride 4, Ride 5, Ride 6, Ride 7, Ride 8
            { 0, 100, 120, 150, 110, 130, 140, 160, 180 }, // Entrance distances
            { 100, 0, 80, 60, 90, 100, 110, 120, 140 }, // Ride 1
            { 120, 80, 0, 90, 70, 95, 105, 115, 135 }, // Ride 2
            { 150, 60, 90, 0, 85, 95, 115, 125, 145 }, // Ride 3
            { 110, 90, 70, 85, 0, 75, 95, 105, 125 }, // Ride 4
            { 130, 100, 95, 95, 75, 0, 85, 95, 115 }, // Ride 5
            { 140, 110, 105, 115, 95, 85, 0, 80, 100 }, // Ride 6
            { 160, 120, 115, 125, 105, 95, 80, 0, 90 }, // Ride 7
            { 180, 140, 135, 145, 125, 115, 100, 90, 0 } // Ride 8
        };
    }

    public int[][] getMatrix() {
        return matrix;
    }
}