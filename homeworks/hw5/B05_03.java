import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class B05_03 {
    public static void main(String[] args) {
        String inputFile = "figures.txt";
        String outputFile = "result.txt";

        try {
            List<Figure> figures = readFigures(inputFile);
            
            LineSegment longestSegment = findLongestSegment(figures);
            Rectangle largestPerimeterRectangle = findLargestPerimeterRectangle(figures);
            Circle smallestAreaCircle = findSmallestAreaCircle(figures);

            writeResults(outputFile, longestSegment, largestPerimeterRectangle, smallestAreaCircle);
            
            System.out.println("Results written to " + outputFile);
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Data error: " + e.getMessage());
        }
    }

    private static List<Figure> readFigures(String filename) throws IOException {
        List<Figure> figures = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\s+");
                if (parts.length == 0) {
                    continue;
                }
                
                int type = Integer.parseInt(parts[0]);
                
                if (type == 1 && parts.length >= 5) {
                    double x1 = Double.parseDouble(parts[1]);
                    double y1 = Double.parseDouble(parts[2]);
                    double x2 = Double.parseDouble(parts[3]);
                    double y2 = Double.parseDouble(parts[4]);
                    figures.add(new LineSegment(x1, y1, x2, y2));
                } else if (type == 2 && parts.length >= 5) {
                    double x1 = Double.parseDouble(parts[1]);
                    double y1 = Double.parseDouble(parts[2]);
                    double x2 = Double.parseDouble(parts[3]);
                    double y2 = Double.parseDouble(parts[4]);
                    figures.add(new Rectangle(x1, y1, x2, y2));
                } else if (type == 3 && parts.length >= 4) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double r = Double.parseDouble(parts[3]);
                    figures.add(new Circle(x, y, r));
                }
            }
        }
        
        return figures;
    }

    private static LineSegment findLongestSegment(List<Figure> figures) {
        LineSegment longest = null;
        double maxLength = 0;

        for (Figure figure : figures) {
            if (figure instanceof LineSegment) {
                LineSegment segment = (LineSegment) figure;
                double length = segment.getLength();
                if (longest == null || length > maxLength) {
                    longest = segment;
                    maxLength = length;
                }
            }
        }

        return longest;
    }

    private static Rectangle findLargestPerimeterRectangle(List<Figure> figures) {
        Rectangle largest = null;
        double maxPerimeter = 0;

        for (Figure figure : figures) {
            if (figure instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) figure;
                double perimeter = rectangle.getPerimeter();
                if (largest == null || perimeter > maxPerimeter) {
                    largest = rectangle;
                    maxPerimeter = perimeter;
                }
            }
        }

        return largest;
    }

    private static Circle findSmallestAreaCircle(List<Figure> figures) {
        Circle smallest = null;
        double minArea = Double.MAX_VALUE;

        for (Figure figure : figures) {
            if (figure instanceof Circle) {
                Circle circle = (Circle) figure;
                double area = circle.getArea();
                if (smallest == null || area < minArea) {
                    smallest = circle;
                    minArea = area;
                }
            }
        }

        return smallest;
    }

    private static void writeResults(String filename, LineSegment segment, 
                                     Rectangle rectangle, Circle circle) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Longest line segment:");
            if (segment != null) {
                writer.printf("  Coordinates: (%.2f, %.2f) to (%.2f, %.2f)%n", 
                             segment.x1, segment.y1, segment.x2, segment.y2);
                writer.printf("  Length: %.2f%n", segment.getLength());
            } else {
                writer.println("  None found");
            }
            writer.println();

            writer.println("Rectangle with largest perimeter:");
            if (rectangle != null) {
                writer.printf("  Top-left: (%.2f, %.2f), Bottom-right: (%.2f, %.2f)%n",
                             rectangle.x1, rectangle.y1, rectangle.x2, rectangle.y2);
                writer.printf("  Perimeter: %.2f%n", rectangle.getPerimeter());
            } else {
                writer.println("  None found");
            }
            writer.println();

            writer.println("Circle with smallest area:");
            if (circle != null) {
                writer.printf("  Center: (%.2f, %.2f), Radius: %.2f%n",
                             circle.x, circle.y, circle.r);
                writer.printf("  Area: %.2f%n", circle.getArea());
            } else {
                writer.println("  None found");
            }
        }
    }
}

abstract class Figure {}

class LineSegment extends Figure {
    double x1, y1, x2, y2;

    public LineSegment(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}

class Rectangle extends Figure {
    double x1, y1, x2, y2;

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double getPerimeter() {
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        return 2 * (width + height);
    }
}

class Circle extends Figure {
    double x, y, r;

    public Circle(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getArea() {
        return Math.PI * r * r;
    }
}

