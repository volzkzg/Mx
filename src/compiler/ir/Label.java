package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Label extends Quadruple {
    private static int labelCount = 0;
    public int num;
    public Label() {
        num = labelCount++;
    }
}
