package two;

import java.math.BigInteger;

public class Index {
    private String factor;
    private BigInteger symbol;

    public Index(String s, BigInteger a) {
        factor = s;
        symbol = a;
    }

    public void index(Index s) {
        String[] term;
        term = s.factor.split("\\^");
        if (term[0].contains("-")) {
            s.symbol = s.symbol.multiply(new BigInteger("-1"));
        }
        if (term.length == 1) {
            s.factor = "1";
        } else {
            s.factor = term[1];
        }
    }

    public String rfactor(Index s) {
        return s.factor;
    }

    public BigInteger rsymbol(Index s) {
        return s.symbol;
    }
}
