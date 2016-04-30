package compiler.ast.statement.expression;

import compiler.ast.Node;
import compiler.ast.SymbolTable;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.ast.statement.Statement;
import compiler.ast.type.Type;
import compiler.ir.Address;
import compiler.ir.Function;
import compiler.ir.MemoryAddress;

import java.util.Stack;

/**
 * Created by bluesnap on 16/3/30.
 */
public class Expression extends Statement {
    public Type expressionType;
    public boolean isLvalue;

    public MemoryAddress getAddress(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack,
                                            Function function) {
        return null;
    }

    public Address getValue(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack,
                            Function function) {
        return null;
    }
}
