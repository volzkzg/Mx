package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class MemoryWrite extends Quadruple {
    public Address dest, src;
    public IntegerConst offset;
    public int size;

    public MemoryWrite() {
        dest = null;
        src = null;
        offset = null;
        size = 4;
    }

    public MemoryWrite(Address dest, IntegerConst offset, Address src) {
        this.dest = dest;
        this.offset = offset;
        this.src = src;
        this.size = 4;
    }

    public MemoryWrite(Address src, MemoryAddress memoryAddress) {
        this.src = src;
        this.offset = memoryAddress.offset;
        this.dest = memoryAddress.start;
        this.size = 4;
    }

    public String print() {
        String ret = "store" + " " + "4" + " " + dest.print() + " " + src.print() + " " + offset.print();
        return ret;
    }
}
