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
public class WhileLoopStatement extends Statement {
    public Expression condition;
    public Statement body;

    public WhileLoopStatement() {
        condition = null;
        body = null;
    }

    public WhileLoopStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public void print(int d) {
        indent(d);
        System.out.println("WhileLoopStatement");
        if (condition != null)
            condition.print(d + 1);
        if (body != null)
            body.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (condition == null) { // while condition can't be null
            outputErrorInfomation(this);
            return false;
        }
        if (condition != null && !condition.third(current, functionState, forStack))
            return false;
        if (!(condition.expressionType instanceof BoolType)) {
            outputErrorInfomation(this);
            return false;
        }
        current = current.getNext();
        forStack.push(this);
        if (body != null && !body.third(current, functionState, forStack))
            return false;
        forStack.pop();
        current = current.prev;
        return true;
    }
}
