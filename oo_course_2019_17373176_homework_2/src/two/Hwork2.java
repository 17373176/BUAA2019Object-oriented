package two;

import java.util.Scanner;

public class Hwork2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            String str = scan.nextLine(); // input a line
            Polynomial polynomial = new Polynomial();
            polynomial.Poly(str);
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
        }
        scan.close();
    }
}