package compiler.ast;

/**
 * Created by bluesnap on 16/3/30.
 */

import compiler.ast.declaration.Declaration;
import compiler.ast.declaration.FunctionDeclaration;
import compiler.build.SymbolTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class AbstractSyntaxTree extends Node {
    public List<Declaration> declarations;

    public AbstractSyntaxTree() {
        declarations = new LinkedList<>();
    }

    public AbstractSyntaxTree(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public void print(int d) {
        System.out.println("AbstractSyntaxTree");
        for (Declaration p : declarations) {
            p.print(d);
        }
    }

    public boolean first(SymbolTable current) {
        for (Node p : declarations) {
            if (!p.first(current)) return false;
        }
        return true;
    }

    @Override
    public boolean second(SymbolTable current) {
        for (Node p : declarations) {
            if (!p.second(current)) return false;
        }
        return true;
    }

    @Override
    public boolean third(SymbolTable current, FunctionDeclaration functionState, Stack<Node> forStack) {
        for (Node p : declarations) {
            if (!p.third(current, functionState, forStack)) return false;
        }
        return true;
    }
}
