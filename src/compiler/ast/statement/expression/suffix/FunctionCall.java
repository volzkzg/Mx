package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.Symbol;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.declaration.VariableDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.IntType;
import compiler.ast.type.StringType;
import compiler.ast.type.Type;
import compiler.ast.type.VoidType;
import compiler.build.SymbolTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class FunctionCall extends Expression {
    public Symbol functionName;
    public List<Expression> args;

    public FunctionCall() {
        functionName = null;
        args = new LinkedList<>();
    }

    public FunctionCall(Symbol functionName, List<Expression> args) {
        this.functionName = functionName;
        this.args = args;
    }

    public void print(int d) {
        indent(d);
//        System.out.println("FunctionCall : " + functionName.name);
        for (Expression p : args) {
            p.print(d + 1);
        }
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (functionName == null) {
            outputErrorInfomation(this);
            return false;
        }

        for (Expression p : args) {
            if (!p.third(current, functionState, forStack)) {
                outputErrorInfomation(this);
                return false;
            }
        }

        // 特判 内置函数

        if (current.find(functionName) == null) {
            String s = functionName.name;
            if (s == "print") {
                if (args.size() != 1) {
                    outputErrorInfomation(this);
                    return false;
                }
                Expression exp = args.get(0);
                if (exp.expressionType instanceof StringType) {
                    expressionType = new VoidType();
                    isLvalue = false;
                } else {
                    outputErrorInfomation(this);
                    return false;
                }
                return true;
            } else if (s == "println") {
                if (args.size() != 1) {
                    outputErrorInfomation(this);
                    return false;
                }
                Expression exp = args.get(0);
                if (exp.expressionType instanceof StringType) {
                    expressionType = new VoidType();
                    isLvalue = false;
                } else {
                    outputErrorInfomation(this);
                    return false;
                }
                return true;
            } else if (s == "getString") {
                if (args.size() != 0) {
                    outputErrorInfomation(this);
                    return false;
                }
                expressionType = new StringType();
                isLvalue = true;
                return true;
            } else if (s == "getInt") {
                if (args.size() != 0) {
                    outputErrorInfomation(this);
                    return false;
                }
                expressionType = new IntType();
                isLvalue = false;
                return true;
            } else if (s == "toString") {
                if (args.size() != 1) {
                    outputErrorInfomation(this);
                    return false;
                }
                Expression exp = args.get(0);
                if (exp.expressionType instanceof IntType) {
                    expressionType = new StringType();
                    isLvalue = true;
                } else {
                    outputErrorInfomation(this);
                    return false;
                }
                return true;
            } else {
                outputErrorInfomation(this);
                return false;
            }
        }

        if (!(current.find(functionName) instanceof FunctionDeclaration)) {
            outputErrorInfomation(this);
            return false;
        }

        FunctionDeclaration func = (FunctionDeclaration) current.find(functionName);

        List<VariableDeclaration> lst1 = func.parameterList;
        List<Expression> lst2 = args;

        // 参数个数是否对等
        if (lst1.size() != lst2.size()) {
            outputErrorInfomation(this);
            return false;
        }

        // 每个参数对应的类型是否相等
        for (int i = 0; i < lst1.size(); ++i) {
            Type tp1 = lst1.get(i).variableType;
            Type tp2 = lst2.get(i).expressionType;
            if (!typeCompare(tp1, tp2)) {
                outputErrorInfomation(this);
                return false;
            }
        }

        expressionType = func.returnType;
        isLvalue = false;

        return true;
    }
}
