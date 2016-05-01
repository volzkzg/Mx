package compiler.ir;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bluesnap on 16/4/28.
 */
public class Function {
    public String name;
    public int size;
    public List<Quadruple> body;
    public List<Address> args;

    public Function() {
        name = null;
        size = 0;
        body = new LinkedList<Quadruple>();
        args = new LinkedList<Address>();
    }

    public Function(String name, int size, List<Quadruple> body, List<Address> args) {
        this.name = name;
        this.size = size;
        this.body = body;
        this.args = args;
    }

    public String print() {
        String ret = "func" + " " + name;
        for (int i = 0; i < args.size(); ++i) {
            ret += " " + args.get(i).print();
        }
        int w = 0;
        ret += " " + "{\n" + "%" + name + ":\n";
        for (Quadruple p : body) {
            if (p instanceof Label) {
                if (w == 1) ret += "\n";
                w = 1;
                ret += p.print() + ":\n";
            } else {
                ret += "\t" + p.print() + "\n";
            }
        }
        ret += "}";
        return ret;
    }
}
