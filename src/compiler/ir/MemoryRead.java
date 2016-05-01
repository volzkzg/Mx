package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class MemoryRead extends Quadruple {
    public Address dest, src;
    public IntegerConst offset;
    public int size;

    public MemoryRead() {
        dest = null;
        src = null;
        offset = null;
        size = 4;
    }

    public MemoryRead(Address dest, Address src, IntegerConst offset) {
        this.dest = dest;
        this.offset = offset;
        this.src = src;
        this.size = 4;
    }

    public MemoryRead(Address dest, MemoryAddress memoryAddress) {
        this.dest = dest;
        this.offset = memoryAddress.offset;
        this.src = memoryAddress.start;
        this.size = 4;
    }

    public String print() {
        String ret = dest.print() + " = " + "load" + " " + "4" + " " + src.print() + " " + offset.print();
        return ret;
    }
}
