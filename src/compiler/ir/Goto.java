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
}
