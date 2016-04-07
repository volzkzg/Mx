package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.IntType;
import compiler.build.SymbolTable;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class SelfIncrement extends Expression {
    public Expression expression;

    public  SelfIncrement() {
        expression = null;
    }

    public  SelfIncrement(Expression expression) {
        this.expression = expression;
    }

    public void print(int d) {
        indent(d);
        System.out.println("SelfIncrement");
        expression.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (!expression.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }
        if (!expression.isLvalue) {
            outputErrorInfomation(this);
            return false;
        } else {
            if (!(expression.expressionType instanceof IntType)) {
                outputErrorInfomation(this);
                return false;
            } else {
                expressionType = expression.expressionType;
                isLvalue = false;
            }
        }
        return true;
    }
}