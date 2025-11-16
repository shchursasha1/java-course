import java.util.HashMap;
import java.util.Scanner;

public class C08_04 {
    
    static class Polynomial {
        private HashMap<Integer, Double> coefficients;
        
        public Polynomial() {
            coefficients = new HashMap<>();
        }
        
        public void setCoefficient(int degree, double coefficient) {
            if (coefficient != 0) {
                coefficients.put(degree, coefficient);
            } else {
                coefficients.remove(degree);
            }
        }
        
        public double getCoefficient(int degree) {
            return coefficients.getOrDefault(degree, 0.0);
        }
        
        public Polynomial add(Polynomial other) {
            Polynomial result = new Polynomial();
            
            for (int degree : this.coefficients.keySet()) {
                result.setCoefficient(degree, this.getCoefficient(degree));
            }
            
            for (int degree : other.coefficients.keySet()) {
                double sum = result.getCoefficient(degree) + other.getCoefficient(degree);
                result.setCoefficient(degree, sum);
            }
            
            return result;
        }
        
        @Override
        public String toString() {
            if (coefficients.isEmpty()) {
                return "0";
            }
            
            StringBuilder sb = new StringBuilder();
            int maxDegree = coefficients.keySet().stream().max(Integer::compare).orElse(0);
            
            boolean first = true;
            for (int i = maxDegree; i >= 0; i--) {
                if (coefficients.containsKey(i)) {
                    double coef = coefficients.get(i);
                    
                    if (!first && coef > 0) {
                        sb.append(" + ");
                    } else if (coef < 0) {
                        sb.append(first ? "-" : " - ");
                        coef = Math.abs(coef);
                    } else if (!first) {
                        sb.append(" + ");
                    }
                    
                    if (i == 0) {
                        sb.append(coef);
                    } else if (i == 1) {
                        if (coef == 1.0) {
                            sb.append("x");
                        } else {
                            sb.append(coef).append("x");
                        }
                    } else {
                        if (coef == 1.0) {
                            sb.append("x^").append(i);
                        } else {
                            sb.append(coef).append("x^").append(i);
                        }
                    }
                    
                    first = false;
                }
            }
            
            return sb.toString();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter degree of first polynomial: ");
        int degree1 = scanner.nextInt();
        Polynomial poly1 = new Polynomial();
        
        System.out.println("Enter coefficients for first polynomial (from x^" + degree1 + " to x^0):");
        for (int i = degree1; i >= 0; i--) {
            System.out.print("Coefficient for x^" + i + ": ");
            double coef = scanner.nextDouble();
            poly1.setCoefficient(i, coef);
        }
        
        System.out.print("\nEnter degree of second polynomial: ");
        int degree2 = scanner.nextInt();
        Polynomial poly2 = new Polynomial();
        
        System.out.println("Enter coefficients for second polynomial (from x^" + degree2 + " to x^0):");
        for (int i = degree2; i >= 0; i--) {
            System.out.print("Coefficient for x^" + i + ": ");
            double coef = scanner.nextDouble();
            poly2.setCoefficient(i, coef);
        }
        
        Polynomial sum = poly1.add(poly2);
        
        System.out.println("\nFirst polynomial:  " + poly1);
        System.out.println("Second polynomial: " + poly2);
        System.out.println("Sum:               " + sum);
        
        scanner.close();
    }
}

