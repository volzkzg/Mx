package compiler.ir;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bluesnap on 16/4/28.
 */
public class IR {
    public List<Function> parts;

    public IR() {
        parts = new LinkedList<>();
    }

    public IR(List<Function> parts) {
        this.parts = parts;
    }

    public String print() {
        String ret = "";
        for (Function p : parts) {
            ret += p.print() + "\n\n";
        }
        return ret;
    }
}
