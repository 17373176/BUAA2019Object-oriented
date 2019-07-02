package two;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {

    private String poly =
            "([ \t]*[-+]?[ \t]*[-+]?((([ \t]*[-+]?\\d+([ \t]*\\*[ \t]*(x|" +
                    "((sin|cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*" +
                    "[-+]?\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                    "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))([ \t]*\\*" +
                    "[ \t]*(([-+]?\\d+([ \t]*\\*[ \t]*(x|((sin|cos)[ \t]*" +
                    "\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)?)|" +
                    "([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*" +
                    "\\^[ \t]*[-+]?\\d+)?)))*[ \t]*)"
                    +
                    "([ \t]*[-+][ \t]*[-+]?((([ \t]*[-+]?\\d+([ \t]*\\*" +
                    "[ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*" +
                    "\\^[ \t]*[-+]?\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\" +
                    "([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))"
                    + // 修改了正则的括号
                    "([ \t]*\\*[ \t]*(([-+]?\\d+([ \t]*\\*[ \t]*(x|((sin|" +
                    "cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?" +
                    "\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x[ \t]*" +
                    "\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))*[ \t]*)*+";

    public void Poly(String str) {
        Polynomial polynomial = new Polynomial();
        Pattern multi = Pattern.compile(polynomial.poly);
        Pattern space = Pattern.compile("[ \t]*");
        Matcher matchstr = multi.matcher(str);
        Matcher matchspace = space.matcher(str);
        if (matchspace.matches() || str.equals("")) {
            System.out.print("WRONG FORMAT!");
        } else if (matchstr.matches()) {
            Function ans = new Function();
            String s = str.replaceAll("[ \t]", "");
            System.out.print(ans.lim(s));
        } else { // 不成功
            System.out.print("WRONG FORMAT!");
        }
    }
}