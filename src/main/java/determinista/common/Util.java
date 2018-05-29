package determinista.common;

public class Util {
    public static double sigmoid(double x) {
        return 1/(1+Math.exp(-x));
    }
    public static double dsigmoid(double x) {
        return x*(1-x);
    }

    static double round(double x)
    {
        x = (Math.round(x * 10000));
        x /= 10000;
        return x;
    }
}
