package compiler.ast.statement.expression.unary;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.BoolType;
import compiler.ast.type.IntType;
import compiler.ast.type.Type;
import compiler.ir.Address;
import compiler.ir.Function;
import compiler.ir.Temp;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class UnaryExpression extends Expression {
    public Expression expression;
    public UnaryOperator operator;

    public UnaryExpression() {
        expression = null;
        operator = null;
    }

    public UnaryExpression(Expression expression, UnaryOperator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public void print(int d) {
        indent(d);
        System.out.println("UnaryExpression");
        expression.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (!expression.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }

        Type tp = expression.expressionType;
        UnaryOperator op = operator;

        if (op == UnaryOperator.INCREMENT ||
                op == UnaryOperator.DECREMENT) {
            if (!expression.isLvalue) {
                outputErrorInfomation(this);
                return false;
            }
            if (tp instanceof IntType) {
                isLvalue = false;
                expressionType = tp;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (op == UnaryOperator.PLUS ||
                op == UnaryOperator.MINUS ||
                op == UnaryOperator.TILDE) {
            if (tp instanceof IntType) {
                isLvalue = false;
                expressionType = tp;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (op == UnaryOperator.NOT) {
            if (tp instanceof BoolType) {
                isLvalue = false;
                expressionType = tp;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        return true;
    }

    @Override
    public Address getValue(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack, Function function) {
        Temp dest = new Temp();
        if (operator == UnaryOperator.MINUS) {
            
        }
        return dest;
    }
}
