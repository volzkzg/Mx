package compiler.ir;

/**
 * Created by bluesnap on 16/5/1.
 */
public class Allocate extends Quadruple {
    public Address dest, size;
    public Allocate() {
        dest = null;
        size = null;
    }
    public Allocate(Address dest, Address size) {
        this.dest = dest;
        this.size = size;
    }

    public String print() {
        return dest.print() + " = alloc " + size.print();
    }


}
