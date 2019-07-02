package two;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Merge {

    public String merge(String ans) {
        String mergeans = "";
        Pattern termans = Pattern.compile(
                "[-+]?\\d+\\*[-+]?\\d+\\*x\\^[-+]?\\d+" +
                        "(\\*[-+]?\\d+\\*sin\\(x\\)\\^[-+]?\\d+\\*[-+]?\\d+" +
                        "\\*cos\\(x\\)\\^[-+]?\\d+((\\*[-+]?\\d+\\*sin\\(x" +
                        "\\)\\^[-+]?\\d+)|(\\*[-+]?\\d+\\*cos\\(x\\)\\^[-+]?" +
                        "\\d+))?)");
        Matcher polyans = termans.matcher(ans);
        while (polyans.find()) {
            String group = polyans.group();
            group = group.replaceAll("\\+", "");
            String[] string = group.split("\\*-");
            boolean flag = false;
            if (string.length == 1) {
                if (string[0].contains("*0")) {
                    // mergeans += "+0"; 可以去掉“+0”
                } else if (string[0].contains("-")) { // 含负号的系数
                    mergeans += string[0];
                } else {
                    mergeans += "+" + string[0];
                }
            } else {
                for (int i = 1; i < string.length; i++) {
                    if (string[0].contains("*0")) {
                        // mergeans += "+0";
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    if (string.length % 2 == 0 && string[0].contains("-")) {
                        mergeans += "+" + string[0].replaceFirst("-","");
                    } else if (string.length % 2 == 1 &&
                            string[0].contains("-")) {
                        mergeans += string[0];
                    } else if (string.length % 2 == 0 &&
                            !string[0].contains("-")) {
                        mergeans += "-" + string[0];
                    } else {
                        mergeans += "+" + string[0];
                    }
                    for (int i = 1; i < string.length; i++) {
                        mergeans += "*" + string[i];
                    }
                }
            }
        }
        return mergeans;
    }
}
