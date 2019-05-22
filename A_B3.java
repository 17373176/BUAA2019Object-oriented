package demo;


import java.math.BigInteger;
import java.util.Scanner;

public class A_B3 {
    public static void main(String[] args) {
        String a, b;
        int A, B, C;
        BigInteger sa, sb, sc;
        Scanner scan = new Scanner(System.in);
        A = scan.nextInt();
        a = scan.next();
        B = scan.nextInt();
        b = scan.next();
        C = scan.nextInt();
        if (scan.nextLine().equals("")) {
            sa = new BigInteger(new BigInteger(a, A).toString(C));
            sb = new BigInteger(new BigInteger(b, B).toString(C));
            sc = sa.add(sb);
            System.out.println(sc);
        }
        scan.close();
    }
}

