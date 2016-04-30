package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Assign extends Quadruple {
    public Address dest, src;

    public Assign() {
        dest = null;
        src = null;
    }

    public Assign(Address dest, Address src) {
        this.dest = dest;
        this.src = src;
    }
}
