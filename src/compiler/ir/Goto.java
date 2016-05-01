package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Goto extends Quadruple {
    public Label label;

    public Goto() {
        label = null;
    }

    public Goto(Label label) {
        this.label = label;
    }

    public String print() {
        String ret = "jump " + label.print();
        return ret;
    }
}
