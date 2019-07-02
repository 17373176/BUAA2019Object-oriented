package javaexp;

public class Employee {
    private String num; // 员工工号
    private char type; //职务类型
    private double age; //工龄

    public Employee() {
    }

    public Employee(String num, char type) {
        this.num  = num;
        this.type = type;
    }

    public Employee(String num, char type, double age) {
        this.num  = num;
        this.type = type;
        this.age  = age;
    }

    public String getnum() {
        return this.num;
    }

    public char gettype() {
        return this.type;
    }

    public double getage() {
        return this.age;
    }
}
