package compiler.ir;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Return extends Quadruple {
    public Address value;
    public Return() {
        value = null;
    }
    public Return(Address value) {
        this.value = value;
    }

    public String print() {
        String ret = "ret " + value.print();
        return ret;
    }

}
