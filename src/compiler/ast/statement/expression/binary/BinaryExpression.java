package compiler.ast.statement.expression.binary;

import compiler.ast.Node;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.*;
import compiler.build.SymbolTable;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class BinaryExpression extends Expression {
    public Expression leftHandSide, rightHandSide;
    public BinaryOperator operator;

    public BinaryExpression() {
        leftHandSide = null;
        rightHandSide = null;
        operator = null;
    }

    public BinaryExpression(Expression leftHandSide, Expression rightHandSide, BinaryOperator operator) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.operator = operator;
    }

    public void print(int d) {
        indent(d);
        System.out.println("BinaryExpression " + operator.name());

        leftHandSide.print(d + 1);
        rightHandSide.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {

        if (!leftHandSide.third(current, functionState, forStack))
            return false;

        if (!rightHandSide.third(current, functionState, forStack))
            return false;

        Type tp1 = leftHandSide.expressionType;
        Type tp2 = rightHandSide.expressionType;
        BinaryOperator op = operator;

        if (!typeCompare(tp1, tp2)) {
            if (operator == BinaryOperator.ASSIGN &&
                    tp2 instanceof NullType &&
                    (tp1 instanceof ArrayType ||
                    tp1 instanceof ClassType)) {
                expressionType = new NullType();
                isLvalue = false;
                return true;
            } else if (operator == BinaryOperator.EQUAL ||
                    operator == BinaryOperator.NOT_EQUAL) {
                if (tp1 instanceof NullType &&
                        (tp2 instanceof ArrayType ||
                        tp2 instanceof ClassType)) {
                    expressionType = new BoolType();
                    isLvalue = false;
                    return true;
                } else if (tp2 instanceof NullType &&
                            (tp1 instanceof ArrayType ||
                                tp1 instanceof ClassType)) {
                    expressionType = new BoolType();
                    isLvalue = false;
                    return true;
                } else {
                    outputErrorInfomation(this);
                    return false;
                }
            } else{
                outputErrorInfomation(this);
                return false;
            }
        }

        if ((op == BinaryOperator.LOGICAL_AND) ||
                (op == BinaryOperator.LOGICAL_OR)) {
            if (tp1 instanceof BoolType) {
                expressionType = new BoolType();
                isLvalue = false;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if ((op == BinaryOperator.BITWISE_AND) ||
                (op == BinaryOperator.BITWISE_EXCLUSIVE_OR) ||
                (op == BinaryOperator.BITWISE_INCLUSIVE_OR)) {
            if (tp1 instanceof IntType) {
                expressionType = tp1;
                isLvalue = false;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (op == BinaryOperator.EQUAL) {
            if ((tp1 instanceof VoidType) ||
                    (tp1 instanceof NullType)) {
                outputErrorInfomation(this);
                return false;
            } else {
                expressionType = new BoolType();
                isLvalue = false;
            }
        }

        if (op == BinaryOperator.NOT_EQUAL) {
            if ((tp1 instanceof VoidType) ||
                    (tp1 instanceof StringType) ||
                    (tp1 instanceof NullType)) {
                outputErrorInfomation(this);
                return false;
            } else {
                expressionType = new BoolType();
                isLvalue = false;
            }
        }

        if ((op == BinaryOperator.LESS) ||
                (op == BinaryOperator.LEQ) ||
                (op == BinaryOperator.GREAT) ||
                (op == BinaryOperator.GEQ)) {
            if ((tp1 instanceof VoidType) ||
                    (tp1 instanceof ArrayType) ||
                    (tp1 instanceof BoolType) ||
                    (tp1 instanceof ClassType)) {
                outputErrorInfomation(this);
                return false;
            } else {
                expressionType = new BoolType();
                isLvalue = false;
            }
        }

        if (op == BinaryOperator.ADD) {
            if (tp1 instanceof StringType) {
                expressionType = tp1;
                isLvalue = false;
            } else if (tp1 instanceof IntType) {
                expressionType = tp1;
                isLvalue = false;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (op == BinaryOperator.SUB ||
                op == BinaryOperator.MUL ||
                op == BinaryOperator.DIV ||
                op == BinaryOperator.MOD ||
                op == BinaryOperator.LEFT_SHIFT ||
                op == BinaryOperator.RIGHT_SHIFT) {
            if (tp1 instanceof IntType) {
                expressionType = tp1;
                isLvalue = false;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (op == BinaryOperator.ASSIGN) {
            if (!leftHandSide.isLvalue) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = tp1;
            isLvalue = false;
        }

        return true;
    }
}
