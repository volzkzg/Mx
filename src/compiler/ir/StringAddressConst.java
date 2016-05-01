package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class StringAddressConst extends Const {
    public String value;
    public StringAddressConst() {
        value = null;
    }
    public StringAddressConst(String value) {
        this.value = value;
    }

    public String print() {
        return value;
    }
}
