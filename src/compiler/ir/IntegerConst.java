package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class IntegerConst extends Const {
    public int value;
    IntegerConst() {}
    IntegerConst(int value) {
        this.value = value;
    }
}
