package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class StringAddressConst extends Const {
    public String value;
    StringAddressConst() {
        value = null;
    }
    StringAddressConst(String value) {
        this.value = value;
    }
}
