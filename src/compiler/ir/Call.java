package compiler.ir;

import java.util.LinkedList;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Call extends Quadruple {
    public Address returnValue;
    public String callee;
    public LinkedList<Address> args;

    public Call() {
        returnValue = null;
        callee = null;
        args = new LinkedList<>();
    }

    public Call(Address returnValue, String callee, LinkedList<Address> args) {
        this.returnValue = returnValue;
        this.callee = callee;
        this.args = args;
    }
}
