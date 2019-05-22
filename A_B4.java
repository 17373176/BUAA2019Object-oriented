package demo;


import java.math.BigInteger;
import java.util.Scanner;

public class A_B4 {
    private static final int s0 = 0, s1 = 1, s2 = 2;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine(), a;
        s = s.replace(" ", "");
        BigInteger sum = new BigInteger("0"), b;
        int state = s0, i;
        char op = ' ';
        for (i = 1; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') continue;
            break;
        }
        a = s.substring(0, i);
        b = new BigInteger(a);
        sum = sum.add(b);
        a = "";
        if (i < s.length()) op = s.charAt(i);

        for (i += 1; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (state) {
                case s0: {
                    if (c == '-' || c == '+') {
                        state = s1;
                        a = a + c;
                    } else if (c >= '0' && c <= '9') {
                        state = s2;
                        a = a + c;
                    }
                    break;
                }
                case s1: {
                    if (c >= '0' && c <= '9') {
                        state = s2;
                        a = a + c;
                    }
                    break;
                }
                case s2: {
                    if (c == '-' || c == '+') {
                        state = s0;
                        b = new BigInteger(a);
                        a = "";
                        if (op == '-') sum = sum.subtract(b);
                        else sum = sum.add(b);
                        op = c;
                    } else if (c >= '0' && c <= '9') {
                        state = s2;
                        a = a + c;
                    }
                    break;
                }
                default:
                    break;
            }

        }
        b = new BigInteger(a);
        if (op == '-') System.out.println(sum.subtract(b));
        else System.out.println(sum.add(b));
        scan.close();
    }
}
