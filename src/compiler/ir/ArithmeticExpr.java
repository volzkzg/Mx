package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class ArithmeticExpr extends Quadruple {
    public ArithmeticOp op;
    public Address dest, src1, src2; // src1 or src2 may be IntegerConst

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

    public String print() {
        String ret = "";
        ret += dest.print() + " = ";
        switch (op) {
            case ADD:ret += "add"; break;
            case SUB:ret += "sub"; break;
            case MUL:ret += "mul"; break;
            case DIV:ret += "div"; break;
            case MOD:ret += "rem"; break;
            case SHL:ret += "shl"; break;
            case SHR:ret += "shr"; break;
            case XOR:ret += "xor"; break;
            case OR:ret += "or"; break;
            case AND:ret += "and"; break;
            case MINUS:ret += "neg"; break;
            case TILDE:ret += "not"; break;
        }
        ret += " " + src1.print();
        if (src2 != null) {
            ret += " " + src2.print();
        }
        return ret;
    }
}
