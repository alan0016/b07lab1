public class Polynomial {
    private double[] coefficients;  // [a0, a1, a2, ..., an], a0 is constant

    // A polynomial [6, -2, 0, 5]
    // 1. [0, 0, 0, 0] (zero polynomial)
    public Polynomial(){  // initializer
        this.coefficients = new double[] {0}; // Represents the zero polynomial [0]
    }
    
    // 2. constructor that takes an array of double as an argument and sets the coefficients accordingly
    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients; // Sets the coefficients to the provided array
    }

    //3. "add" takes one argument of type Polynomial and returns the polynomial from adding polynomials.
    public Polynomial add(Polynomial other) {
        int maxLength = Math.max(this.coefficients.length, other.coefficients.length); // The max length of the two polynomials
        double[] result = new double[maxLength]; // Create a result array of the max length
        
        for (int i = 0; i < maxLength; i++) { // Loop through the max length
            double a;
            if (i < this.coefficients.length) { 
                a = this.coefficients[i];
            } else {
                a = 0;
            }

            double b;
            if (i < other.coefficients.length) {
                b = other.coefficients[i];
            } else {
                b = 0;
            }

            result[i] = a + b; // Add the coefficients
        }
        return new Polynomial(result); // Return a new polynomial
    }

    // 4. "evaluate" takes one argument of type double representing a value of x and evaluates the polynomial accordingly.
    public double evaluate(double x) {
        double result = 0; // Initialize the result to 0
        
        for (int i = 0; i < this.coefficients.length; i++) { // Loop through the coefficients
            result += this.coefficients[i] * Math.pow(x, i); // Calculate the value of the polynomial a0 + a1x + ... ai x^i
        }
        return result; // Return the result
    }

    // 5. "hasRoot" takes one argument of type double and determines whether this value is a root of the polynomial or not.
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0.0; // Check if the polynomial evaluates to 0 at x (double)
    }
}