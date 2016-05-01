package compiler.ir;

/**
 * Created by bluesnap on 16/4/30.
 */
public class RelationExpr extends Quadruple {
    public RelationOp op;
    public Address dest, src1, src2;

    public RelationExpr() {
        op = null;
        dest = null;
        src1 = null;
        src2 = null;
    }

    public RelationExpr(Address dest, Address src1, RelationOp op, Address src2) {
        this.dest = dest;
        this.src1 = src1;
        this.op = op;
        this.src2 = src2;
    }

    public RelationExpr(Address dest, RelationOp op, Address src1) {
        this.dest = dest;
        this.op = op;
        this.src1 = src1;
        this.src2 = null;
    }

    public String print() {
        String ret = dest.print() + " " + "=" + " ";
        switch (op) {
            case EQ:ret += "seq "; break;
            case NE:ret += "sne "; break;
            case LE:ret += "sle "; break;
            case LT:ret += "slt "; break;
            case GE:ret += "sge "; break;
            case GT:ret += "sgt "; break;
        }
        ret += src1.print() + " " + src2.print();
        return ret;
    }
}
