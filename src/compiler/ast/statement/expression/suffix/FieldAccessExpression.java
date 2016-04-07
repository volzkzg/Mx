package compiler.ast.statement.expression.suffix;

import compiler.ast.Node;
import compiler.ast.Symbol;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.ClassDeclaration;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.VariableDeclarationStatement;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.ClassType;

import java.util.Stack;

/**
 * Created by bluesnap on 16/4/2.
 */
public class FieldAccessExpression extends Expression {
    public Expression object;
    public Symbol field;

    public FieldAccessExpression() {
        object = null;
        field = null;
    }

    public FieldAccessExpression(Symbol object, Symbol field) {
        this.object = object;
        this.field = field;
    }

    public void print(int d) {
        indent(d);
        System.out.println("FieldAccessExpression");
        object.print(d + 1);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        if (!object.third(current, functionState, forStack)) {
            outputErrorInfomation(this);
            return false;
        }

        if (object.expressionType instanceof ClassType) {
            ClassDeclaration className = ((ClassDeclaration) current.find(((ClassType) object.expressionType).className));
            boolean check = false;
            for (VariableDeclarationStatement p : className.classFields) {
                if (p.variableName.name.equals(field.name)) {
                    check = true;
                    expressionType = p.variableType;
                    isLvalue = true;
                    break;
                }
            }
            if (!check) {
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
