package compiler.ast.statement;


import compiler.ast.Node;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.BoolType;
import compiler.build.SymbolTable;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class IfStatement extends Statement {
    public Expression condition;
    public Statement body, elseBody;

    public IfStatement() {
        condition = null;
        body = null;
        elseBody = null;
    }

    public IfStatement(Expression condition, Statement body, Statement elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public void print(int d) {
        indent(d);
        System.out.println("IfStatement");
        if (condition != null)
            condition.print(d + 1);
        if (body != null)
            body.print(d + 1);
        if (elseBody != null)
            elseBody.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (condition != null && !condition.third(current, functionState, forStack))
            return false;
        if (!(condition.expressionType instanceof BoolType)) {
            outputErrorInfomation(this);
            return false;
        }
        current = current.getNext();
        if (body != null && !body.third(current, functionState, forStack))
            return false;
        current = current.prev;
        current = current.getNext();
        if (elseBody != null && !elseBody.third(current, functionState, forStack))
            return false;
        current = current.prev;
        return true;
    }
}
