package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.IntType;
import compiler.ir.*;

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

    @Override
    public Address getValue(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack, Function function) {
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
        return null;
    }
}
