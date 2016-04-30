package compiler.ast.statement;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ir.Function;
import compiler.ir.Goto;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class ContinueStatement extends Statement {
    public void print(int d) {
        indent(d);
        System.out.println("ContinueStatement");
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (forStack.empty()) {
            outputErrorInfomation(this);
            return false;
        }
        return true;
    }

    @Override
    public void generateIR(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack, Function function) {
        ForLoopStatement top = ((ForLoopStatement) forStack.peek());
        function.body.add(new Goto(top.label3));
    }
}
