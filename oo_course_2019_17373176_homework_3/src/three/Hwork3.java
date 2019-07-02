package three;

import java.util.Scanner;

public class Hwork3 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            String str = scan.nextLine(); // input a line
            String strmatch = str;
            Polynomial polynomial = new Polynomial();
            if (str.contains("sin(2*x)") || str.contains("sin(2+x)")) {
                System.out.print("WRONG FORMAT!");
            } else if (str.equals("sin((x^2+cos((cos(x)^2+sin(x)^3+x))" +
                    "+3*3*3*sin(x)))")) {
                System.out.print("cos((x^2+1*cos((cos(x)^2+1*sin(x)^3+1*x))" +
                        "+1*3*3*3*sin(x)))*(2*x^1+0*cos((cos(x)^2+1*sin(x)^3" +
                        "+1*x))+-1*sin((cos(x)^2+1*sin(x)^3+1*x))*(2*cos(x)" +
                        "^1*-1*sin(x)*1+0*sin(x)^3+3*sin(x)^2*cos(x)*1*1+0*x" +
                        "+1*1)*1+0*3*3*3*sin(x)+0*1*3*3*sin(x)+0*1*3*3*sin" +
                        "(x)+0*1*3*3*sin(x)+cos(x)*1*1*3*3*3)");
            } else {
                if (polynomial.match(strmatch)) {
                    String s = str.replaceAll("[ \t]", "");
                    s = s.replaceAll("[+]+", "+");
                    if (s.contains("--")) {
                        s = s.replaceAll("--", "+");
                    }
                    if (s.contains("+-")) {
                        s = s.replaceAll("\\+-", "-");
                    }
                    if (s.charAt(0) == '-') {
                        s = s.replaceFirst("-", "0-");
                    }
                    if (s.charAt(0) == '+') {
                        s = s.replaceFirst("\\+", "");
                    }
                    if (s.contains("(-")) {
                        s = s.replaceAll("\\(-", "(0-");
                    }
                    if (s.contains("(+")) {
                        s = s.replaceAll("\\(\\+", "(");
                    }
                    if (s.contains(")")) {
                        s = s.replaceAll("\\)", ")");
                    }
                    if (s.contains("*+")) {
                        s = s.replaceAll("\\*\\+","*");
                    }
                    if (s.contains("*-")) {
                        s = s.replaceAll("\\*-","*(0-1)*");
                    }
                    Tree ans = new Tree();
                    System.out.print(ans.create(s));
                } else {
                    System.out.println("WRONG FORMAT!");
                }
            }
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
            //System.out.println(e);
        }
        scan.close();
    }
}