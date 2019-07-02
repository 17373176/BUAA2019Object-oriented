package javaexp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Employee[] a = new Employee[9];
        store(a);
        Scanner scan = new Scanner(System.in);
        String func = scan.next(); // 查询功能
        if (func.equals("01")) {
            String num = scan.next(); //工号
            int time = scan.nextInt(); //周平均工作时间
            int index = reserch(a, num);
            if (index != 0) {
                if (a[index].gettype() == 'S') {
                    if (scan.hasNextDouble()) {
                        double money = scan.nextDouble(); // 签单总额
                    } else {
                        System.out.println("Invalid Query!");
                    }
                } else {
                    Func1 employ = new Func1();
                }
            } else {
                System.out.println("Invalid Query!");
            }
        } else if (func.equals("02")) {
            String num = scan.next();
            int index = reserch(a, num);
            if (index != 0) {
                if (scan.hasNext()) {
                    String juti = scan.next();
                }
                Func2 employ = new Func2();
            } else {
                System.out.println("Invalid Query!");
            }
        } else {
            System.out.println("Invalid Query!");
        }
    }

    protected static void store(Employee[] a) {
        a[1] = new Employee("001", 'R', 5.0);
        a[2] = new Employee("002", 'R', 1.2);
        a[3] = new Employee("003", 'F');
        a[4] = new Employee("004", 'F');
        a[5] = new Employee("005", 'M');
        a[6] = new Employee("006", 'M');
        a[7] = new Employee("007", 'S');
        a[8] = new Employee("008", 'S');
    }

    protected static int reserch(Employee[] a, String num) {
        for (int i = 1; i <= 8; i++) {
            if (num.equals(a[i].getnum())) {
                return i;
            }
        }
        return 0;
    }
}
