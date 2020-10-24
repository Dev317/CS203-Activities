package csd.week11.configuration;

public class ISP {
    /**
 * @param a  an argument
 * @param b  another argument
 * @return the larger of a and b.
 */
    public static int add(int a, int b){
        return a+b;
    }
    int coverage(boolean a){
        int x = 0;
        if (a) x = 1;
        return 100/x;
    }

    public static void main(String[] args) {
        int a = Integer.MIN_VALUE;
        int b = -2;
        System.out.println(add(a, b));
        System.out.print(Integer.MAX_VALUE);
    }
}




