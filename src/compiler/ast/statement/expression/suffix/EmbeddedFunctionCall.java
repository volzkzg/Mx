package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.Symbol;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.ArrayType;
import compiler.ast.type.IntType;
import compiler.ast.type.StringType;
import compiler.build.SymbolTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by bluesnap on 16/4/4.
 */
public class EmbeddedFunctionCall extends Expression {
    public Expression expression;
    public Symbol functionName;
    public List<Expression> args;

    public EmbeddedFunctionCall() {
        functionName = null;
        args = new LinkedList<>();
    }

    public EmbeddedFunctionCall(Symbol functionName, List<Expression> args) {
        this.functionName = functionName;
        this.args = args;
    }

    public void print(int d) {
        indent(d);
        System.out.println("FunctionCall");
        for (Expression p : args) {
            p.print(d + 1);
        }
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (!expression.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }

        String s = functionName.name;
        if (s == "size") {
            if (args.size() != 0) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(expression.expressionType instanceof ArrayType)) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = new IntType();
            isLvalue = false;
        } else if (s == "length") {
            if (args.size() != 0) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(expression.expressionType instanceof StringType)) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = new IntType();
            isLvalue = false;
        } else if (s == "substring") {
            if (args.size() != 2) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(expression.expressionType instanceof StringType)) {
                outputErrorInfomation(this);
                return false;
            }
            Expression exp1, exp2;
            exp1 = args.get(0);
            exp2 = args.get(1);
            if (!exp1.third(current, functionState, forStack)) {
                outputErrorInfomation(this);
                return false;
            }
            if (!exp2.third(current, functionState, forStack)) {
                outputErrorInfomation(this);
                return false;
            }

            if (!(exp1.expressionType instanceof IntType) ||
                    !(exp2.expressionType instanceof  IntType)) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = new StringType();
            isLvalue = false;
        } else if (s == "parseInt") {
            if (args.size() != 0) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(expression.expressionType instanceof StringType)) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = new IntType();
            isLvalue = false;
        } else if (s == "ord") {
            if (args.size() != 1) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(expression.expressionType instanceof StringType)) {
                outputErrorInfomation(this);
                return false;
            }
            Expression exp;
            exp = args.get(0);
            if (!exp.third(current, functionState, forStack)) {
                outputErrorInfomation(this);
                return false;
            }
            if (!(exp.expressionType instanceof IntType)) {
                outputErrorInfomation(this);
                return false;
            }
            expressionType = new IntType();
            isLvalue = false;
        } else {
            outputErrorInfomation(this);
            return false;
        }
        return true;
    }
}
