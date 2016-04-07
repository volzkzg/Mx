package compiler.ast.statement;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.BoolType;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class ForLoopStatement extends Statement {
    public Expression initExpression;
    public Expression step, condition;
    public Statement body;

    public ForLoopStatement() {
        initExpression = null;
        step = null;
        condition = null;
        body = null;
    }

    public ForLoopStatement(Expression initExpression, Expression step,
                            Expression condition, Statement body) {
        this.initExpression = initExpression;
        this.step = step;
        this.condition = condition;
        this.body = body;
    }

    public void print(int d) {
        indent(d);
        System.out.println("ForLoopStatement");
        if (initExpression != null)
            initExpression.print(d + 1);
        if (condition != null)
            condition.print(d + 1);
        if (step != null)
            step.print(d + 1);
        if (body != null)
            body.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        forStack.push(this);
        if (initExpression != null && !initExpression.third(current, functionState, forStack))
            return false;
        if (condition != null && !condition.third(current, functionState, forStack))
            return false;
        if (condition != null && !(condition.expressionType instanceof BoolType)) {
            outputErrorInfomation(this);
            return false;
        }
        if (step != null && !step.third(current, functionState, forStack))
            return false;
        current = current.getNext();
        if (body != null && !body.third(current, functionState, forStack))
            return false;
        current = current.prev;
        forStack.pop();
        return true;
    }
}
