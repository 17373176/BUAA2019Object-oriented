package demo;


import java.math.BigInteger;
import java.util.Scanner;

public class A_B2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        String b = scan.next();
        if (scan.nextLine().equals("")) {
            int FLAG = 1;//标记符号

            //有限状态机
            if (a.charAt(0) == '+' || a.charAt(0) == '-' || (a.charAt(0) >= '0' && a.charAt(0) <= '9')) {
                for (int i = 1; i < a.length(); i++) {
                    if (a.charAt(i) < '0' || a.charAt(i) > '9') {
                        FLAG = 0; //非法
                        break;
                    }
                }
                if (b.charAt(0) == '+' || b.charAt(0) == '-' || (b.charAt(0) >= '0' && b.charAt(0) <= '9')) {
                    for (int i = 1; i < b.length(); i++) {
                        if (b.charAt(i) < '0' || b.charAt(i) > '9') {
                            FLAG = 0; //非法
                            break;
                        }
                    }
                } else FLAG = 0;
            } else FLAG = 0;

            //数组计算结果
            if (FLAG == 1) {
                BigInteger A, B, C;
                A = new BigInteger(a);
                B = new BigInteger(b);
                C = A.add(B);
                System.out.println(C);
            } else {
                System.out.println("WRONG FORMAT!");
            }
        } else {
            System.out.println("WRONG FORMAT!");
        }
        scan.close();
    }
}
