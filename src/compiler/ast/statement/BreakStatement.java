package compiler.ast.statement;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class BreakStatement extends Statement {
    public void print(int d) {
        indent(d);
        System.out.println("BreakStatement");
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (forStack.empty()) {
            outputErrorInfomation(this);
            return false;
        }
        return true;
    }
}
