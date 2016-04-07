package compiler.ast.statement.expression.primary;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.ArrayType;
import compiler.ast.type.ClassType;
import compiler.ast.type.Type;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class CreationExpression extends Expression {
    public Type name;

    public CreationExpression() {
        name = null;
    }

    public CreationExpression(Type name) {
        this.name = name;
    }

    public void print(int d) {
        indent(d);
        System.out.println("CreationExpression");
        name.print(d + 1);
    }

    // 记得处理数组 括号中 的东西 数字出现的位置

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        Type baseType = getBaseType(name);
        if (name instanceof ArrayType) {
            if (baseType instanceof ClassType) {
                if (current.find(((ClassType) baseType).className) == null) {
                    outputErrorInfomation(this);
                    return false;
                }
            }
            int num = 0;
            Type tmp = name;
            while (tmp instanceof ArrayType) {

                if (((ArrayType) tmp).dimension != null &&
                        !((ArrayType) tmp).dimension.third(current, functionState, forStack)) {
                    outputErrorInfomation(this);
                    return false;
                }

                if (((ArrayType) tmp).dimension != null) {
                   // System.out.println(((ArrayType) tmp).baseType.getClass().getTypeName());
                    if (!(((ArrayType) tmp).baseType instanceof ArrayType)) {
                        num += 10000;
                    } else {
                        num++;
                    }
                }
                tmp = ((ArrayType) tmp).baseType;
            }
            //System.out.println("GO through" + " " + num);
            if (num != 10000) {
                outputErrorInfomation(this);
                return false;
            }
        } else if (name instanceof ClassType) {
            if (current.find(((ClassType) name).className) == null) {
                outputErrorInfomation(this);
                return false;
            }
        }

        expressionType = name;
        isLvalue = false;
        return true;
    }
}
