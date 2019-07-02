package three;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {

    private String poly = //首项
            "[ \t]*[-+*]?[ \t]*[-+]?(([ \t]*[-+]?\\d+([ \t]*\\*[ \t]*(x|((" +
                    "sin|cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]" +
                    "*[-+]?\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                    "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?))([ \t]*" +
                    "\\*[ \t]*(([-+]?\\d+([ \t]*\\*[ \t]*(x|((sin|cos)" +
                    "[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?" +
                    "\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                    "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))*[ \t]*";
    private String poly2 =
            "([ \t]*[-+*][ \t]*[-+]?(([ \t]*[-+]?\\d+([ \t]*\\*[ \t]*(x|((" +
                    "sin|cos)[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]" +
                    "*[-+]?\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                    "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?))([ \t]*" +
                    "\\*[ \t]*(([-+]?\\d+([ \t]*\\*[ \t]*(x|((sin|cos)" +
                    "[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?" +
                    "\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                    "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))*[ \t]*)";
    private String digui = "[ \t]*[-+*]?[ \t]*(sin|cos)?[ \t]*[-+*]?\\("; //嵌套开始

    private static int i = 0;
    private static int flag = 0;
    private Pattern kuohao = Pattern.compile(digui);
    private Pattern space = Pattern.compile("[ \t]*");
    private Pattern end = Pattern.compile("[ \t]*\\)[ \t]*(\\*|(\\^[ \t]*" +
            "[+]?\\d+))?[ \t]*"); //结束括号

    public boolean match(String str) {
        Matcher matchspace = space.matcher(str);
        if (matchspace.matches() || str.equals("")) {
            return false;
        } else if (recur(str)) {
            return true;
        }
        return false;
    }

    public boolean recur(String str) {
        Pattern multi;
        if (flag == 0) {
            multi = Pattern.compile(poly);
            flag = 1;
        } else {
            multi = Pattern.compile(poly2);
        }
        Matcher matchstr = multi.matcher(str);
        Matcher matchspace = space.matcher(str);
        Matcher mach = kuohao.matcher(str);
        Matcher endkuo = end.matcher(str);
        if ((matchspace.matches() || str.equals("")) && i == 0) {
            return true;
        } else if (mach.lookingAt()) { // 嵌套形式
            i += 1;
            flag = 0;
            return recur(str.replaceFirst(digui, ""));
        } else if (matchstr.lookingAt()) {
            String group = matchstr.group();
            if (group.contains("^")) {
                Pattern fun = Pattern.compile("[ \t]*\\^[ \t]*[+]?\\d+");
                Matcher func = fun.matcher(group);
                while (func.lookingAt()) {
                    BigInteger a = new BigInteger(func.group()
                            .replaceAll("[ \t]*\\^[ \t]*", ""));
                    if (a.compareTo(new BigInteger("10000")) > 0) {
                        return false;
                    }
                    group = group.replaceFirst("[ \t]*\\^[ \t]*[+]?\\d+", "");
                    func = fun.matcher(group);
                }
            }
            return recur(str.replaceFirst(poly, ""));
        } else if (endkuo.lookingAt()) {
            i -= 1;
            flag = 0;
            String group = endkuo.group();
            if (group.contains("^")) {
                Pattern fun = Pattern.compile("[ \t]*\\)[ \t]*\\^[ \t]*[+]?" +
                        "\\d+");
                Matcher func = fun.matcher(group);
                while (func.lookingAt()) {
                    String ss = func.group()
                            .replaceAll("[ \t]*\\)[ \t]*\\^[ \t]*", "");
                    BigInteger a = new BigInteger(ss);
                    if (a.compareTo(new BigInteger("10000")) > 0) {
                        return false;
                    }
                    group = group.replaceFirst("[ \t]*\\)[ \t]*\\^[ \t]*", "");
                    func = fun.matcher(group);
                }
            }
            return recur(str.replaceFirst("[ \t]*\\)[ \t]*(\\*|(\\^[ \t]*" +
                    "[+]?\\d+))?[ \t]*", ""));
        }
        return false;
    }
}
