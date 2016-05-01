package compiler.ast.statement.expression.unary;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.BoolType;
import compiler.ast.type.IntType;
import compiler.ast.type.Type;
import compiler.ir.*;

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
            Address hs = expression.getValue(current, functionState, forStack, function);
            if (hs instanceof IntegerConst) {
                return new IntegerConst(-((IntegerConst) hs).value);
            } else {
                ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, ArithmeticOp.MINUS, hs);
                function.body.add(arithmeticExpr);
                return dest;
            }
        } else if (operator == UnaryOperator.NOT) {
            Address hs = expression.getValue(current, functionState, forStack, function);
            if (hs instanceof IntegerConst) {
                if (((IntegerConst) hs).value != 0) {
                    return new IntegerConst(0);
                } else {
                    return new IntegerConst(1);
                }
            } else {
                ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, hs, ArithmeticOp.XOR, new IntegerConst(1));
                function.body.add(arithmeticExpr);
                return dest;
            }
        } else if (operator == UnaryOperator.TILDE) {
            Address hs = expression.getValue(current, functionState, forStack, function);
            if (hs instanceof IntegerConst) {
                return new IntegerConst(~((IntegerConst) hs).value);
            } else {
                ArithmeticExpr arithmeticExpr = new ArithmeticExpr(dest, ArithmeticOp.TILDE, hs);
                function.body.add(arithmeticExpr);
                return dest;
            }
        } else if (operator == UnaryOperator.PLUS) {
            Address hs = expression.getValue(current, functionState, forStack, function);
            return hs;
        } else if (operator == UnaryOperator.INCREMENT) {
            Address address = expression.getAddress(current, functionState, forStack, function);
            if (address instanceof Temp) {
                function.body.add(new ArithmeticExpr(address, address, ArithmeticOp.ADD, new IntegerConst(1)));
                return address;
            } else if (address instanceof MemoryAddress) {
                Temp temp = new Temp();
                MemoryRead memoryRead = new MemoryRead(temp, (MemoryAddress) address);
                ArithmeticExpr arithmeticExpr = new ArithmeticExpr(temp, temp, ArithmeticOp.ADD, new IntegerConst(1));
                MemoryWrite memoryWrite = new MemoryWrite(temp, (MemoryAddress) address);
                function.body.add(memoryRead);
                function.body.add(arithmeticExpr);
                function.body.add(memoryWrite);
                return temp;
            }
        } else if (operator == UnaryOperator.DECREMENT) {
            Address address = expression.getAddress(current, functionState, forStack, function);
            if (address instanceof Temp) {
                function.body.add(new ArithmeticExpr(address, address, ArithmeticOp.SUB, new IntegerConst(1)));
                return address;
            } else if (address instanceof MemoryAddress) {
                Temp temp = new Temp();
                MemoryRead memoryRead = new MemoryRead(temp, (MemoryAddress) address);
                ArithmeticExpr arithmeticExpr = new ArithmeticExpr(temp, temp, ArithmeticOp.SUB, new IntegerConst(1));
                MemoryWrite memoryWrite = new MemoryWrite(temp, (MemoryAddress) address);
                function.body.add(memoryRead);
                function.body.add(arithmeticExpr);
                function.body.add(memoryWrite);
                return temp;
            }
        }
        return dest;
    }
}
