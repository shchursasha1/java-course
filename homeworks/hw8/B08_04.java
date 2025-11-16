import java.util.PriorityQueue;
import java.util.Comparator;

public class B08_04 {
    
    static class Point {
        double x;
        double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double distanceToOrigin() {
            return Math.sqrt(x * x + y * y);
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ") - distance: " + 
                   String.format("%.2f", distanceToOrigin());
        }
    }
    
    public static void main(String[] args) {
        Point[] points = {
            new Point(3, 4),
            new Point(1, 1),
            new Point(5, 12),
            new Point(0, 2),
            new Point(6, 8),
            new Point(2, 0),
            new Point(7, 1),
            new Point(4, 3)
        };
        
        PriorityQueue<Point> pq = new PriorityQueue<>(
            Comparator.comparingDouble(Point::distanceToOrigin)
        );
        
        for (Point point : points) {
            pq.offer(point);
        }
        
        System.out.println("Points sorted by distance to origin:");
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }
}

