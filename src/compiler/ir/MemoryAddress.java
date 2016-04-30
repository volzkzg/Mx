package compiler.ir;

/**
 * Created by bluesnap on 16/4/30.
 */
public class MemoryAddress extends Address {
    public Address start;
    public IntegerConst offset;

    MemoryAddress() {
        start = null;
        offset = null;
    }

    MemoryAddress(Address start, IntegerConst offset) {
        this.start = start;
        this.offset = offset;
    }
}
