package compiler.ir;

/**
 * Created by bluesnap on 16/4/29.
 */
public class Branch extends Quadruple {
    public Address src;
    public Label label1, label2;

    public Branch() {
        src = null;
        label1 = null;
        label2 = null;
    }

    public Branch(Address src, Label label1, Label label2) {
        this.src = src;
        this.label1 = label1;
        this.label2 = label2;
    }
}
