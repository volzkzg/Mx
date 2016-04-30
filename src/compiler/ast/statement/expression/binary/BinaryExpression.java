package compiler.ast.statement.expression.binary;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.statement.expression.suffix.ArrayAccessExpression;
import compiler.ast.statement.expression.suffix.FieldAccessExpression;
import compiler.ast.type.*;
import compiler.ir.*;

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

    @Override
    public Address getValue(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack, Function function) {
        Address lhs, rhs;
        lhs = leftHandSide.getValue(current, functionState, forStack, function);
        rhs = rightHandSide.getValue(current, functionState, forStack, function);
        Temp dest = new Temp();

        if (operator == BinaryOperator.ASSIGN) {
            if (leftHandSide instanceof ArrayAccessExpression ||
                    leftHandSide instanceof FieldAccessExpression) {
                MemoryAddress memoryAddress = leftHandSide.getAddress(current, functionState, forStack, function);
                MemoryRead memoryRead = new MemoryRead(dest, memoryAddress);
                Assign assign = new Assign(dest, rhs);
                MemoryWrite memoryWrite = new MemoryWrite(dest, memoryAddress);
                function.body.add(memoryRead);
                function.body.add(assign);
                function.body.add(memoryWrite);
            } else {
                Assign assign = new Assign();
                assign.dest = lhs;
                assign.src = rhs;
                function.body.add(assign);
                return lhs;
            }

            /*
            Assign assign = new Assign();
            assign.dest = lhs;
            assign.src = rhs;
            function.body.add(assign);
            return lhs;
            */
        } else if (operator == BinaryOperator.ADD) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.ADD, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.SUB) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.SUB, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.MUL) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.MUL, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.DIV) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.DIV, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.MOD) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.MOD, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.BITWISE_AND) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.AND, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.BITWISE_EXCLUSIVE_OR) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.XOR, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.BITWISE_INCLUSIVE_OR) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.OR, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.LOGICAL_AND) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.AND, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.LOGICAL_OR) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.OR, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.EQUAL) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.EQ, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.NOT_EQUAL) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.NE, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.LESS) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.LT, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.LEQ) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.LE, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.GREAT) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.GT, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.GEQ) {
            RelationExpr relationExpr = new RelationExpr(dest, lhs, RelationOp.GE, rhs);
            function.body.add(relationExpr);
        } else if (operator == BinaryOperator.LEFT_SHIFT) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.SHL, rhs);
            function.body.add(arithmeticExpr);
        } else if (operator == BinaryOperator.RIGHT_SHIFT) {
            ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, lhs, ArithmeticOp.SHR, rhs);
            function.body.add(arithmeticExpr);
        }

        return dest;
    }
}
