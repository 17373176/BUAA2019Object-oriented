package firstexp;



import java.util.Scanner;


class matrix {
    private int[][] mat;

    public matrix() {
        mat = null;
    }

    public matrix(int order) {
        mat = new int[order][order];
    }

    public matrix(String str) {
        int order;
        /*int p;
        for (p = 0; p < str.length()/2; p++){
            if (str.charAt(p) == '{' && str.charAt(str.length() - p - 1) == '}') {
                continue;
            } else {
                System.out.println("Illegal Input!");
            }
        }*/
        String[] strs = str.split("[{},]");
        int i;
        for (i = 2; i < strs.length; i++) {
            if (!strs[i].equals(""))
                continue;
            else
                break;
        }
        order = i - 2;
        if (order == 0) {
            System.out.println("Empty Matrix!");
            System.exit(0);
        }
        mat = new int[order][order];
        int j;
        for (i = 0; i < strs.length; i += 2 + order) {
            for (j = 0; j < order; j++) {
                mat[i / (2 + order)][j] = Integer.parseInt(strs[i + 2 + j]);
            }
        }
    }

    protected int getOrder() {
        return mat.length;
    }

    protected matrix add(matrix addThis) {
        int i, j, order;
        order = getOrder();
        matrix temp = new matrix(order);
        for (i = 0; i < order; i++) {
            for (j = 0; j < order; j++) {
                temp.mat[i][j] = mat[i][j] + addThis.mat[i][j];
            }
        }
        return temp;
    }

    protected matrix sub(matrix subThis) {
        int i, j, order;
        order = getOrder();
        matrix temp = new matrix(order);
        for (i = 0; i < order; i++) {
            for (j = 0; j < order; j++) {
                temp.mat[i][j] = mat[i][j] - subThis.mat[i][j];
            }
        }
        return temp;
    }

    protected matrix transpose() {
        int order;
        order = getOrder();
        matrix temp = new matrix(order);
        int i, j;
        for (i = 0; i < order; i++) {
            for (j = 0; j < order; j++) {
                temp.mat[i][j] = mat[j][i];
            }
        }
        return temp;
    }

    protected matrix multiply(matrix multiplyThis) {
        int i, j, k, order, element;
        order = getOrder();
        matrix temp = new matrix(order);
        for (i = 0; i < order; i++) {
            for (j = 0; j < order; j++) {
                element = 0;
                for (k = 0; k < order; k++) {
                    element += mat[i][k] * multiplyThis.mat[k][j];
                }
                temp.mat[i][j] = element;
            }
        }
        return temp;
    }

    public String toString() {
        String s = new String();
        int i, j, order;
        order = getOrder();
        for (i = 0; i < order; i++) {
            for (j = 0; j < order; j++) {
                s += String.valueOf(mat[i][j]);
                s += '\t';
            }
            s = s + '\n';
        }
        return (s);
    }
}

public class matrixCal {
    int[][] matrix1, matrix2, answer;
    int dim;
    char operator;

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        matrix m1 = new matrix(keyboard.nextLine());
        String op;
        char operator = '\0';
        op = keyboard.nextLine();
        operator = op.charAt(0);
        if (operator == 't') {
            System.out.print(m1.transpose());
        } else if (operator == '+') {
            matrix m2 = new matrix(keyboard.nextLine());
            if (m1.getOrder() != m2.getOrder()) { // if n!=m, output error
                System.out.println("Illegal Input!");
            } else {
                System.out.print(m1.add(m2));
            }
        } else if (operator == '-') {
            matrix m2 = new matrix(keyboard.nextLine());
            if (m1.getOrder() != m2.getOrder()) { // if n!=m, output error
                System.out.println("Illegal Input!");
            } else {
                System.out.print(m1.sub(m2));
            }
        } else if (operator == '*') {
            matrix m2 = new matrix(keyboard.nextLine());
            if (m1.getOrder() != m2.getOrder()) { // if n!=m, output error
                System.out.println("Illegal Input!");
            } else {
                System.out.print(m1.multiply(m2));
            }
        } else {
            System.out.println("Illegal Input!");
        }
        keyboard.close();
    }
}