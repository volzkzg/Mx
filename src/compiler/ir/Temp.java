package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Temp extends Address {
    private static int tempCount = 0;
    public int num;
    public Temp() {
        num = tempCount++;
    }
}
