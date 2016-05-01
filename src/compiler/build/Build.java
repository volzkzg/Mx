package compiler.build;

/**
 * Created by bluesnap on 16/3/31.
 */

import compiler.ast.AbstractSyntaxTree;
import compiler.ast.SymbolTable;
import compiler.ir.IR;
import compiler.parser.GrammarLexer;
import compiler.parser.GrammarParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Stack;

public class Build {
    public static void main(String[] args) throws Exception {
//        String inputFile = "/Users/bluesnap/Documents/Project/IntelliJ Idea Project/Mx/src/compiler/testfile/compile_error/incop-1-5120309049-liaochao.mx";
//        String inputFile = "/Users/bluesnap/Documents/Project/IntelliJ Idea Project/Mx/src/compiler/testfile/test.mx";
        String inputFile = "/Users/bluesnap/Documents/Project/IntelliJ Idea Project/Mx/testfile/ir/array1.mx";
        InputStream is = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(is);
        GrammarLexer lexer = new GrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);
        ParseTree tree = parser.program(); // parse;

        ParseTreeWalker walker = new ParseTreeWalker();
        AstBuilderListener astBuilder = new AstBuilderListener(parser);
        walker.walk(astBuilder, tree);

        AstBuilderListener.property.get(tree).print(0);

        boolean ret = true;
        SymbolTable table = new SymbolTable();
        ret = ret & (AstBuilderListener.property.get(tree).first(table));
        System.out.println("Not fuck in first round");
        ret = ret & (AstBuilderListener.property.get(tree).second(table));
        System.out.println("Not fuck in second round");
        ret = ret & (AstBuilderListener.property.get(tree).third(table, null, new Stack<>()));
        System.out.println("Not fuck in third round");

        if (ret)
            System.out.println("Sematic Check YES");
        else
            System.out.println("Sematic Check NO");

        IR ir = ((AbstractSyntaxTree) AstBuilderListener.property.get(tree)).generateIR(table, null, new Stack<>());
        System.out.println(ir.print());
    }
}