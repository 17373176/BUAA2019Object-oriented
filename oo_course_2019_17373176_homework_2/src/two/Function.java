package two;

import java.math.BigInteger;
//import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    public String lim(String str) { //term
        String ans = "0";
        String match =
                "([ \t]*[-+]?[ \t]*[-+]?(((\\d+([ \t]*\\*[ \t]*(x|((sin|cos)" +
                        "[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?" +
                        "\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                        "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))([ \t]*" +
                        "\\*[ \t]*(([-+]?\\d+([ \t]*\\*[ \t]*(x|((sin|cos)" +
                        "[ \t]*\\([ \t]*x[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?" +
                        "\\d+)?)?)|([ \t]*(x|((sin|cos)[ \t]*\\([ \t]*x" +
                        "[ \t]*\\)))([ \t]*\\^[ \t]*[-+]?\\d+)?)))*[ \t]*)";
        Pattern multi = Pattern.compile(match);
        Matcher str1 = multi.matcher(str);
        while (str1.find()) {
            String list = "cos(x)^";
            String group = str1.group();
            group = group.replaceAll("(--|\\+)", "");
            //分解为单个式子求解
            list = resovle(list, group);
            String[] line = list.split("\\*");
            Polyterm polyterm = new Polyterm();
            ans += "+" + line[3] + "*" + polyterm.Polyterm(line[2]) + "*";
            ans += "1*" + line[1] + "*1*" + line[0] + "+" + line[3];
            ans += "*1*" + line[2] + "*" + polyterm.Polyterm(line[1]) + "*1*";
            ans += line[0] + "+" + line[3] + "*1*" + line[2] + "*1*" + line[1];
            ans += "*" + polyterm.Polyterm(line[0]);
        }
        String ansmerge;
        Merge merge = new Merge();
        ansmerge = merge.merge(ans);
        return ansmerge;
    }

    public String resovle(String list, String string) {
        String s = list;
        String group = string;
        Pattern term4 = Pattern.compile("[-+]?cos\\(x\\)(\\^[-+]?\\d+)?");
        Matcher poly4 = term4.matcher(group);
        BigInteger a = BigInteger.ZERO;
        BigInteger symbol = BigInteger.ONE;
        while (poly4.find()) {
            String factor = poly4.group();
            group = group.replaceFirst("cos\\(x\\)(\\^[-+]?\\d+)?","");
            Index indexa = new Index(factor, symbol);
            indexa.index(indexa);
            symbol = symbol.multiply(indexa.rsymbol(indexa));
            a = a.add(new BigInteger(indexa.rfactor(indexa)));
            poly4 = term4.matcher(group);
        }
        s += a.toString() + "*sin(x)^";
        a = BigInteger.ZERO;
        Pattern term3 = Pattern.compile("[-+]?sin\\(x\\)(\\^[-+]?\\d+)?");
        Matcher poly3 = term3.matcher(group);
        while (poly3.find()) {
            String factor = poly3.group();
            group = group.replaceFirst("sin\\(x\\)(\\^[-+]?\\d+)?","");
            Index indexa = new Index(factor, symbol);
            indexa.index(indexa);
            symbol = symbol.multiply(indexa.rsymbol(indexa));
            a = a.add(new BigInteger(indexa.rfactor(indexa)));
            poly3 = term3.matcher(group);
        }
        s += a.toString() + "*x^";
        a = BigInteger.ZERO;
        Pattern term2 = Pattern.compile("[-+]?x(\\^[-+]?\\d+)?");
        Matcher poly2 = term2.matcher(group);
        while (poly2.find()) {
            String factor = poly2.group();
            group = group.replaceFirst("x(\\^[-+]?\\d+)?","");
            Index indexa = new Index(factor, symbol);
            indexa.index(indexa);
            symbol = symbol.multiply(indexa.rsymbol(indexa));
            a = a.add(new BigInteger(indexa.rfactor(indexa)));
            poly2 = term2.matcher(group);
        }
        s += a.toString() + "*";
        a = BigInteger.ONE;
        Pattern term1 = Pattern.compile("[-+]?\\d+");
        Matcher poly1 = term1.matcher(group);
        while (poly1.find()) {
            String factor = poly1.group();
            group = group.replaceFirst("[-+]?\\d+","");
            a = a.multiply(new BigInteger(factor));
            poly1 = term1.matcher(group);
        }
        s += symbol.multiply(a).toString();
        return s;
    }
}