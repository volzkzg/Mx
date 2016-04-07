package compiler.ast.statement.expression.primary;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.expression.Expression;
import compiler.ast.type.StringType;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/31.
 */
public class StringLiteral extends Expression {
    public String value;

    public StringLiteral() {
        value = null;
    }

    public StringLiteral(String value) {
        this.value = value;
    }

    public void print(int d) {
        indent(d);
        System.out.println("StringLiteral " + value);
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        isLvalue = false;
        expressionType = new StringType();
        return true;
    }
}
