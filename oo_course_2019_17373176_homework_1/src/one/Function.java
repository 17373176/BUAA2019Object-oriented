package one;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    private String sting;

    public Function() {
        sting = "0";
    }

    public String lim(String str) { //求导方法
        Function ans = new Function();
        String biao =
                "(([ \t]*[-+]?[ \t]*[-+]?\\d+[ \t]*\\*[ \t]*x[ \t]*\\^"
                + "[ \t]*[-+]?\\d+[ \t]*)"
                + "|([ \t]*[-+]?[ \t]*[-+]?[ \t]*x[ \t]*\\^[ \t]*[-+]?" +
                "\\d+[ \t]*)"
                + "|([ \t]*[-+]?[ \t]*[-+]?\\d+[ \t]*\\*[ \t]*x[ \t]*)"
                + "|([ \t]*[-+]?[ \t]*[-+]?[ \t]*x[ \t]*)"
                + "|(([ \t]*[-+]?[ \t]*[-+]?\\d+)[ \t]*))";
        Pattern multi = Pattern.compile(biao);
        Matcher str1 = multi.matcher(str);
        while (str1.find()) {
            String group = str1.group();
            group = group.replaceAll("(--|\\+)", "");
            String[] sstring = group.split("[*^]");
            int length = getlength(sstring);
            if (length == 1 && sstring[0].contains("x")) { //无系数一次项
                sstring[0] = sstring[0].replaceAll("x", "1");
                dealone(sstring, ans);
            } else if (length == 2) {
                if (sstring[0].contains("x")) { //无系数多次项
                    sstring[0] = sstring[0].replaceAll("x", "1");
                    if (sstring[1].equals("1")) { //1次方
                        dealone(sstring, ans);
                    } else if (!(sstring[1].equals("0")
                            || sstring[1].equals("-0"))) { //非0次方
                        String[] string = new String[3];
                        string[0] = sstring[0];
                        string[2] = sstring[1];
                        dealtwo(string, ans);
                    }
                } else { //含系数一次项
                    dealone(sstring, ans);
                }
            } else if (length == 3) { //多次项
                if (sstring[2].equals("1")) {
                    dealone(sstring, ans);
                } else if (!(sstring[2].equals("0") ||
                        sstring[2].equals("-0"))) {
                    dealtwo(sstring, ans);
                }
            }
        }
        return ans.sting;
    }

    private static void dealone(
                                String[] sstring, Function ans) { //一次项
        BigInteger a = new BigInteger(sstring[0]);// -0的符号没有表示出来
        if (!sstring[0].contains("-") || a.compareTo(BigInteger.ZERO) == 0) {
            ans.sting += "+";
        }
        ans.sting += a.toString();
    }

    private static void dealtwo(
                                String[] sstring, Function ans) { //多次项处理
        BigInteger a = new BigInteger(sstring[0]);
        BigInteger b = new BigInteger(sstring[2]);
        String ab = a.multiply(b).toString();
        if (!(ab.contains("-")) || ab.equals("0")) {
            ans.sting += "+";
        }
        ans.sting += ab;
        if (b.compareTo(new BigInteger("2")) == 0) {
            ans.sting += "*x";
        } else {
            ans.sting += "*x^" + b.subtract(new BigInteger("1")).toString();
        }
    }

    private int getlength(String[] str) {
        return str.length;
    }
}
