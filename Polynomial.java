import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;



public class Polynomial {
    private double[] coeffs;  // [a0, a1, a2, ..., an], a0 is constant
    private int[] degrees; // [0, 1, 2, ..., n], where n is the degree of the polynomial

    // A polynomial [6, -2, 0, 5]
    // 1. [0, 0, 0, 0] (zero polynomial)
    public Polynomial(){  // initializer
        this.coeffs = new double[] {0.0}; // Represents the zero polynomial [0]
        this.degrees = new int[] {0}; // Degree of zero polynomial is 0
    }

    // 2. constructor that takes an array of double as an argument and sets the coefficients accordingly
    public Polynomial(double[] oldcoeffs) {
        int count = 0;

        for (int i = 0; i < oldcoeffs.length; i++) {
            if (oldcoeffs[i] != 0.0) {
                count++;
            }
        }

        this.coeffs = new double[count];
        this.degrees = new int [count];

        int index = 0;

        for (int i = 0; i < oldcoeffs.length; i++) {
            if (oldcoeffs[i] != 0.0) {
                this.coeffs[index] = oldcoeffs[i];
                this.degrees[index] = i;
                index++;
            }
        }
    }
    
    public Polynomial(File file) throws IOException {
        Scanner sc = null;
        String line;

        try {
            sc = new Scanner(file);
            if (sc.hasNextLine()) {
                line = sc.nextLine();
            } else {
                line = "";
            }
            if (line == null) {
                line = "";
            }
            line = line.trim();
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        parseAndSet(line);
    }

    //3. "add" takes one argument of type Polynomial and returns the polynomial from adding polynomials.
    public Polynomial add(Polynomial other) {
        int max = this.coeffs.length + other.coeffs.length;
        double[] tempc = new double[max];
        int[] tempd = new int[max];

        int i = 0, j = 0, k = 0;

        while (i < this.coeffs.length && j < other.coeffs.length) {
            int d1 = this.degrees[i];
            int d2 = other.degrees[j];

            if (d1 == d2) {
                double sum = this.coeffs[i] + other.coeffs[j];
                if (sum != 0.0) {
                    tempc[k] = sum;
                    tempd[k] = d1;
                    k++;
                }
                i++;
                j++;
            } else if (d1 < d2) {
                tempc[k] = this.coeffs[i];
                tempd[k] = d1;
                i++;
                k++;
            } else { // d1 > d2
                tempc[k] = other.coeffs[j];
                tempd[k] = d2;
                j++;
                k++;
            }
        }

        while (i < this.coeffs.length) {
            tempc[k] = this.coeffs[i];
            tempd[k] = this.degrees[i];
            i++;
            k++;
        }

        while (j < other.coeffs.length) {
            tempc[k] = other.coeffs[j];
            tempd[k] = other.degrees[j];
            j++;
            k++;
        }

        double[] newc = new double[k];
        int[] newd = new int[k];
        for (int id = 0; id < k; id++) {
            newc[id] = tempc[id];
            newd[id] = tempd[id];
        }

        if (k == 0) {
            return new Polynomial();
        }

        Polynomial result = new Polynomial();
        result.coeffs = newc;
        result.degrees = newd;

        return result;
    }

    // 4. "evaluate" takes one argument of type double representing a value of x and evaluates the polynomial accordingly.
    public double evaluate(double x) {
        double result = 0.0; // Initialize the result to 0

        for (int i = 0; i < this.coeffs.length; i++) { // Loop through the coefficients
            result += this.coeffs[i] * Math.pow(x, this.degrees[i]); // Calculate the value of the polynomial a0 + a1x + ... ai x^i
        }

        return result; // Return the result
    }

    // 5. "hasRoot" takes one argument of type double and determines whether this value is a root of the polynomial or not.
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0.0; // Check if the polynomial evaluates to 0 at x (double)
    }

    private double[] arrayit() {
        int maxd = this.degrees[this.degrees.length - 1];
        double[] result = new double[maxd + 1];
        for (int i = 0; i < this.coeffs.length; i++) {
            int d = this.degrees[i];
            result[d] = this.coeffs[i];
        }
        return result;
    }

    public Polynomial multiply(Polynomial other) {
        double [] A = this.arrayit();
        double [] B = other.arrayit();
        int lenA = A.length;
        int lenB = B.length;
        int lenC = lenA + lenB - 1;
        double[] C = new double[lenC];

        for (int i = 0; i < lenA; i++) {
            if (A[i] == 0.0) {
                continue;
            }

            for (int j = 0; j < lenB; j++) {
                if (B[j] == 0.0) {
                    continue;
                }

                C[i + j] += A[i] * B[j];
            }
        }

        int count = 0;
        for (int k = 0; k < C.length; k++) {
            if (C[k] != 0.0) {
                count++;
            }
        }

        if (count == 0) {
            return new Polynomial();
        }

        double [] newcoeffs = new double[count];
        int [] newdegrees = new int[count];

        int index = 0;
        for (int p = 0; p < C.length; p++) {
            if (C[p] != 0.0) {
                newcoeffs[index] = C[p];
                newdegrees[index] = p;
                index++;
            }
        }

        Polynomial result = new Polynomial();
        result.coeffs = newcoeffs;
        result.degrees = newdegrees;

        return result;
    }

    public void saveToFile(String filename) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);
            String text = toCompactString();
            fw.write(text);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            throw e;
        } finally {
            if (fw != null) {
                try { fw.close(); } catch (IOException ignore) {}
            }
        }
    }

    private String toCompactString() {
        if (this.coeffs.length == 0) return "0";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.coeffs.length; i++) {
            double c = this.coeffs[i];
            int d = this.degrees[i];

            if (i == 0) {
                if (c < 0) {
                    sb.append('-');
                }
            } else {
                sb.append(c >= 0 ? '+' : '-');
            }

            double abs = Math.abs(c);

            if (d == 0) {
                sb.append(trimDouble(abs));
            } else {
                if (Math.abs(abs - 1.0) < 1e-12) {
                    sb.append('x');
                } else {
                    sb.append(trimDouble(abs)).append('x');
                }
                if (d != 1) {
                    sb.append(d);
                }
            }
        }
        return sb.toString();
    }

    private String trimDouble(double v) {
        String s = Double.toString(v);
        int dot = s.indexOf('.');
        if (dot == -1) {
            return s;
        }
        int end = s.length() - 1;
        while (end > dot && s.charAt(end) == '0') {
            end--;
        }
        if (end == dot) {
            end--;
        }
        return s.substring(0, end + 1);
    }

    private void parseAndSet(String s) {
        if (s == null || s.length() == 0) {
            this.coeffs = new double[] {0.0};
            this.degrees = new int[] {0};
            return;
        }

        String t = s.replace("-", "+-");
        if (t.startsWith("+")) {
            t = t.substring(1);
        }
        String[] parts = t.split("\\+");

        int maxd = 0;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part == null || part.length() == 0) {
                continue;
            }
            int x = part.indexOf('x');
            if (x < 0) {
                x = part.indexOf('X');
            }

            int d;
            if (x >= 0) {
                if (x == part.length() - 1) {
                    d = 1;
                } else {
                    String degstr = part.substring(x + 1);
                    d = Integer.parseInt(degstr);
                }
            } else {
                d = 0;
            }

            if (d > maxd) {
                maxd = d;
            }
        }

        double[] dense = new double[maxd + 1];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part == null || part.length() == 0) {
                continue;
            }

            int x = part.indexOf('x');
            if (x < 0) {
                x = part.indexOf('X');
            }

            double c;
            int d;

            if (x >= 0) {
                String coeffstr = part.substring(0, x);
                if (coeffstr.length() == 0 || coeffstr.equals("+")) {
                    c = 1.0;
                } else if (coeffstr.equals("-")) {
                    c = -1.0;
                } else {
                    c = Double.parseDouble(coeffstr);
                }

                if (x == part.length() - 1) {
                    d = 1;
                } else {
                    String degstr = part.substring(x + 1);
                    d = Integer.parseInt(degstr);
                }
            } else {
                c = Double.parseDouble(part);
                d = 0;
            }

            if (c != 0.0) {
                dense[d] += c;
            }
        }


        int count = 0;
        for (int d = 0; d < dense.length; d++) {
            if (dense[d] != 0.0) {
                count++;
            }
        }

        if (count == 0) {
            this.coeffs = new double[] {0.0};
            this.degrees = new int[] {0};
            return;
        }

        double[] newc = new double[count];
        int[] newd = new int[count];

        int index = 0;
        for (int d = 0; d < dense.length; d++) {
            if (dense[d] != 0.0) {
                newc[index] = dense[d];
                newd[index] = d;
                index++;
            }
        }

        this.coeffs = newc;
        this.degrees = newd;
    }
    
    public void display() {
        System.out.println("POLYNOMIAL:");
        for (int i = 0; i < this.coeffs.length; i++) {
            double c = this.coeffs[i];
            int d = this.degrees[i];

            if (d == 0) {
                System.out.print("(" + c + ")");
            } else if (d == 1) {
                System.out.print("(" + c + ")x");
            } else {
                System.out.print("(" + c + ")x^" + d);
            }

            if (i != this.coeffs.length - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println();
    }
}