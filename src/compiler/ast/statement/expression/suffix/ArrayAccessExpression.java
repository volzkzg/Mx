package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.ArrayType;
import compiler.ast.type.IntType;
import compiler.ast.type.Type;
import compiler.build.SymbolTable;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class ArrayAccessExpression extends Expression {
    public Expression expression;
    public Expression dimension;

    public ArrayAccessExpression() {
        expression = null;
        dimension = null;
    }

    public ArrayAccessExpression(Expression identifier, Expression dimension) {
        this.expression = identifier;
        this.dimension = dimension;
    }

    public void print(int d) {
        indent(d);
        System.out.println("ArrayAccessExpression");
        expression.print(d + 1);
        dimension.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (!expression.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }
        if (!dimension.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }

        Type tp1 = expression.expressionType;
        Type tp2 = dimension.expressionType;

        if (tp1 instanceof ArrayType) {
            if (tp2 instanceof IntType) {
                expressionType = ((ArrayType) tp1).baseType;
                isLvalue = true;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        } else {
            outputErrorInfomation(this);
            return false;
        }

        return true;
    }
}
