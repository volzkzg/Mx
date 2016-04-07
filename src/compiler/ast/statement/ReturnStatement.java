package compiler.ast.statement;


import compiler.ast.Node;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.VoidType;
import compiler.build.SymbolTable;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class ReturnStatement extends Statement {
    public Expression returnExpression;

    public ReturnStatement() {
        returnExpression = null;
    }

    public ReturnStatement(Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    public void print(int d) {
        indent(d);
        System.out.println("ReturnStatement");
        if (returnExpression != null)
            returnExpression.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (functionState == null) {
            outputErrorInfomation(this);
            return false;
        }
        if (returnExpression == null) {
            if (functionState.returnType instanceof VoidType) {
                return true;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        } else {
            if (!returnExpression.third(current, functionState, forStack))
                return false;
            if (!typeCompare(returnExpression.expressionType, functionState.returnType)) {
                outputErrorInfomation(this);
                return false;
            }
        }
        return true;
    }
}
