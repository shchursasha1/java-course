public class B03_01 {
    /**
     * Immutable complex number with basic arithmetic operations.
     */
    public static final class Complex {
        private final double real;
        private final double imaginary;

        public Complex(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }

        public double getReal() {
            return real;
        }

        public double getImaginary() {
            return imaginary;
        }

        public Complex add(Complex other) {
            if (other == null) {
                throw new IllegalArgumentException("Other complex number cannot be null");
            }
            return new Complex(this.real + other.real, this.imaginary + other.imaginary);
        }

        public Complex subtract(Complex other) {
            if (other == null) {
                throw new IllegalArgumentException("Other complex number cannot be null");
            }
            return new Complex(this.real - other.real, this.imaginary - other.imaginary);
        }

        public Complex multiply(Complex other) {
            if (other == null) {
                throw new IllegalArgumentException("Other complex number cannot be null");
            }
            double a = this.real;
            double b = this.imaginary;
            double c = other.real;
            double d = other.imaginary;
            return new Complex(a * c - b * d, a * d + b * c);
        }

        public Complex divide(Complex other) {
            if (other == null) {
                throw new IllegalArgumentException("Other complex number cannot be null");
            }
            double c = other.real;
            double d = other.imaginary;
            double denominator = c * c + d * d;
            if (denominator == 0.0) {
                throw new ArithmeticException("Division by zero complex number");
            }
            double a = this.real;
            double b = this.imaginary;
            return new Complex((a * c + b * d) / denominator, (b * c - a * d) / denominator);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(real);
            if (imaginary >= 0) {
                sb.append(" + ");
            } else {
                sb.append(" - ");
            }
            sb.append(Math.abs(imaginary)).append("i");
            return sb.toString();
        }
    }

    /**
     * Returns the product of all elements of the provided array.
     * If the array is null or empty, returns multiplicative identity (1 + 0i).
     */
    public static Complex productOfAll(Complex[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return new Complex(1.0, 0.0);
        }
        Complex product = new Complex(1.0, 0.0);
        for (Complex number : numbers) {
            if (number == null) {
                throw new IllegalArgumentException("Array contains null complex number");
            }
            product = product.multiply(number);
        }
        return product;
    }

    public static void main(String[] args) {
        // example
        Complex[] values = new Complex[] {
                new Complex(1, 2),
                new Complex(3, -1),
                new Complex(0.5, 4)
        };

        Complex product = productOfAll(values);
        System.out.println("Product of array elements: " + product);

        // basic operations
        Complex x = new Complex(4, 5);
        Complex y = new Complex(2, -3);
        System.out.println("x + y = " + x.add(y));
        System.out.println("x - y = " + x.subtract(y));
        System.out.println("x * y = " + x.multiply(y));
        System.out.println("x / y = " + x.divide(y));
    }
}


