package compiler.ast.statement.expression;

import compiler.ast.statement.Statement;
import compiler.ast.type.Type;

/**
 * Created by bluesnap on 16/3/30.
 */
public class Expression extends Statement {
    public Type expressionType;
    public boolean isLvalue;
}
