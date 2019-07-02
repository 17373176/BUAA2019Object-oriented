package two;

import java.math.BigInteger;

public class Polyterm {

    public String Polyterm(String group) {
        String term = "";
        String[] sstring = group.split("\\^");
        int length = sstring.length;
        String[] string = new String[3];
        if (length == 2) {
            string[0] = "1";
            string[1] = sstring[0];
            string[2] = sstring[1];
            if (sstring[0].contains("sin(x)")) {
                term = dealsin(string, term);
            } else if (sstring[0].contains("cos(x)")) {
                term = dealcos(string, term);
            } else if (sstring[0].contains("x")) {
                term = dealx(string, term);
            }
        }
        return term;
    }

    public String dealx(String[] sstring, String term) { //多次项处理
        String t = term;
        BigInteger a = new BigInteger(sstring[0]);
        BigInteger b = new BigInteger(sstring[2]);
        String ab = a.multiply(b).toString();
        /*if (!(ab.contains("-")) ) {
            t += "+";
        }*/
        t += ab;
        t += "*x^" + b.subtract(new BigInteger("1")).toString();
        return t;
    }

    public String dealsin(String[] sstring, String term) {
        String t = term;
        BigInteger a = new BigInteger(sstring[0]);
        BigInteger b = new BigInteger(sstring[2]);
        String ab = a.multiply(b).toString();
        /*if (!(ab.contains("-"))) {
            t += "+";
        }*/
        t += ab;
        t += "*sin(x)^" + b.subtract(new BigInteger("1")).toString();
        t += "*1*cos(x)^1";
        return t;
    }

    public String dealcos(String[] sstring, String term) {
        String t = term;
        BigInteger a = new BigInteger(sstring[0]);
        BigInteger b = new BigInteger(sstring[2]);
        String ab = a.multiply(b).toString();
        /*if (!(ab.contains("-"))) {
            t += "+";
        }*/
        t += ab;
        t += "*cos(x)^" + b.subtract(new BigInteger("1")).toString();
        t += "*-1*sin(x)^1";
        return t;
    }
}
