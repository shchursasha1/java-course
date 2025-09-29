class Task0301 {
    // Complex Number class
    static class ComplexNumber {
        private double real;
        private double imaginary;
        
        public ComplexNumber(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }
        
        // Addition method
        public ComplexNumber add(ComplexNumber other) {
            return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
        }
        
        // Subtraction method
        public ComplexNumber subtract(ComplexNumber other) {
            return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
        }
        
        // Multiplication method
        public ComplexNumber multiply(ComplexNumber other) {
            double newReal = this.real * other.real - this.imaginary * other.imaginary;
            double newImaginary = this.real * other.imaginary + this.imaginary * other.real;
            return new ComplexNumber(newReal, newImaginary);
        }
        
        // Division method
        public ComplexNumber divide(ComplexNumber other) {
            double denominator = other.real * other.real + other.imaginary * other.imaginary;
            if (denominator == 0) {
                throw new ArithmeticException("Cannot divide by zero complex number");
            }
            double newReal = (this.real * other.real + this.imaginary * other.imaginary) / denominator;
            double newImaginary = (this.imaginary * other.real - this.real * other.imaginary) / denominator;
            return new ComplexNumber(newReal, newImaginary);
        }
        
        // Method to get string representation
        @Override
        public String toString() {
            if (imaginary >= 0) {
                return String.format("%.2f + %.2fi", real, imaginary);
            } else {
                return String.format("%.2f - %.2fi", real, Math.abs(imaginary));
            }
        }
        
        // Getters
        public double getReal() {
            return real;
        }
        
        public double getImaginary() {
            return imaginary;
        }
    }
    
    // Method that returns the product of all elements in the array
    public static ComplexNumber getProduct(ComplexNumber[] numbers) {
        if (numbers.length == 0) {
            return new ComplexNumber(1, 0); // Return 1 for empty array
        }
        
        ComplexNumber product = new ComplexNumber(numbers[0].getReal(), numbers[0].getImaginary());
        for (int i = 1; i < numbers.length; i++) {
            product = product.multiply(numbers[i]);
        }
        return product;
    }
    
    public static void main(String[] args) {
        // Create array of complex numbers
        ComplexNumber[] complexNumbers = {
            new ComplexNumber(1, 2),    // 1 + 2i
            new ComplexNumber(3, -1),   // 3 - 1i
            new ComplexNumber(2, 3),    // 2 + 3i
            new ComplexNumber(1, 1)     // 1 + 1i
        };
        
        System.out.println("Array of complex numbers:");
        for (int i = 0; i < complexNumbers.length; i++) {
            System.out.println("Element " + (i + 1) + ": " + complexNumbers[i]);
        }
        
        // Calculate and display the product
        ComplexNumber product = getProduct(complexNumbers);
        System.out.println("\nProduct of all elements: " + product);
        
        // Demonstrate other operations
        System.out.println("\nDemonstration of operations:");
        ComplexNumber a = new ComplexNumber(3, 4);
        ComplexNumber b = new ComplexNumber(1, 2);
        
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a + b = " + a.add(b));
        System.out.println("a - b = " + a.subtract(b));
        System.out.println("a * b = " + a.multiply(b));
        System.out.println("a / b = " + a.divide(b));
    }
}