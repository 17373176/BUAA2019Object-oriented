package one;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Hwork1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine(); // input a line
        String biao =
                "([ \t]*[-+]?[ \t]*[-+]?((\\d+([ \t]*\\*[ \t]*x([ \t]*\\^" +
                "[ \t]*[-+]?\\d+)?)?)|([ \t]*x([ \t]*\\^[ \t]*[-+]?\\d+)?))" +
                "[ \t]*)([ \t]*[-+][ \t]*[-+]?((\\d+([ \t]*\\*[ \t]*x([ \t]*" +
                "\\^[ \t]*[-+]?\\d+)?)?)|([ \t]*x([ \t]*\\^[ \t]*[-+]?\\d+)?" +
                "))[ \t]*)*+";
        Pattern multi = Pattern.compile(biao);
        if (str.equals("")) { // empty string
            System.out.println("WRONG FORMAT!");
        } else {
            Matcher str1 = multi.matcher(str);
            if (str1.matches()) {
                Function ans = new Function();
                str = str.replaceAll("[ \t]", "");
                System.out.print(ans.lim(str));
            } else { // 不成功
                System.out.print("WRONG FORMAT!");
            }
        }
        scan.close();
    }
}