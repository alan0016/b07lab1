import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        try {
            // 1. 6 - 2x + 5x^3  =>  [6, -2, 0, 5]
            double[] oldcoeffs = new double[] {6.0, -2.0, 0.0, 5.0};
            Polynomial p1 = new Polynomial(oldcoeffs);
            System.out.print("p1 (from array): ");
            p1.display();

            // 2. from file
            File inFile;
            if (args.length >= 1) {
                inFile = new File(args[0]);
            } else {
                inFile = new File("poly_input.txt");
                if (!inFile.exists()) {
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(inFile);
                        fw.write("5-3x2+7x8"); // 5 - 3x^2 + 7x^8
                        fw.write(System.lineSeparator());
                    } finally {
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException ignore) {}
                        }
                    }
                }
            }
            Polynomial p2 = new Polynomial(inFile);
            System.out.print("p2 (from file):  ");
            p2.display();

            // 3. add
            Polynomial sum = p1.add(p2);
            System.out.print("p1 + p2:         ");
            sum.display();

            Polynomial prod = p1.multiply(p2);
            System.out.print("p1 * p2:         ");
            prod.display();

            // 4. evaluate and hasRoot
            double x = 2.0;
            double val = p1.evaluate(x);
            System.out.println("p1(" + x + ") = " + val);
            System.out.println("p1 has root at " + x + "? " + p1.hasRoot(x));

            // 5. save to file
            sum.saveToFile("sum.txt");
            prod.saveToFile("product.txt");
            System.out.println("Saved sum -> sum.txt");
            System.out.println("Saved product -> product.txt");

        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}