package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class ArithmeticExpr extends Quadruple {
    public ArithmeticOp op;
    public Address dest, src1, src2;

    public ArithmeticExpr() {
        op = null;
        dest = null;
        src1 = null;
        src2 = null;
    }

    public ArithmeticExpr(Address dest, Address src1, ArithmeticOp op, Address src2) {
        this.dest = dest;
        this.src1 = src1;
        this.op = op;
        this.src2 = src2;
    }

    public ArithmeticExpr(Address dest, ArithmeticOp op, Address src1) {
        this.dest = dest;
        this.op = op;
        this.src1 = src1;
        this.src2 = null;
    }

}
