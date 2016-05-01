package compiler;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import compiler.ast.AbstractSyntaxTree;
import compiler.ast.SymbolTable;
import compiler.build.AstBuilderListener;
import compiler.ir.IR;
import compiler.parser.GrammarLexer;
import compiler.parser.GrammarParser;
import interpreter.LLIRInterpreter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

/**
 * Created by abcdabcd987 on 2016-04-13.
 */
@RunWith(Parameterized.class)
public class IntermediateRepresentationTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        for (File f : new File("testfile/ir/").listFiles()) {
            if (f.isFile() && f.getName().endsWith(".mx")) {
                params.add(new Object[] { "testfile/ir/" + f.getName() });
            }
        }
        return params;
    }
    private String filename;
    public IntermediateRepresentationTest(String filename) {
        this.filename = filename;
    }
    @Test
    public void testPass() throws IOException, InterruptedException {
        System.out.println(filename);

        ByteArrayOutputStream irTextOut = new ByteArrayOutputStream();
        OutputStream tee = irTextOut;
        PrintStream out = new PrintStream(tee);

        InputStream is = new FileInputStream(filename);
        ANTLRInputStream input = new ANTLRInputStream(is);
        GrammarLexer lexer = new GrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);
        ParseTree tree = parser.program(); // parse; start at prog <label id="code.tour.main.6"/>

        ParseTreeWalker walker = new ParseTreeWalker();
        AstBuilderListener astBuilder = new AstBuilderListener(parser);
        walker.walk(astBuilder, tree);

        boolean ret = true;
        SymbolTable table = new SymbolTable();
        ret = ret & AstBuilderListener.property.get(tree).first(table);
        ret = ret & AstBuilderListener.property.get(tree).second(table);
        ret = ret & AstBuilderListener.property.get(tree).third(table, null, new Stack<>());

        IR ir = ((AbstractSyntaxTree) AstBuilderListener.property.get(tree)).generateIR(table, null, new Stack<>());
        out.println(ir.print());
        System.out.println(ir.print());

        out.flush();
        irTextOut.close();

        // Run the virtual machine
        byte[] irText = irTextOut.toByteArray();
        ByteInputStream vmIn = new ByteInputStream(irText, irText.length);
        LLIRInterpreter vm = new LLIRInterpreter(vmIn, false);
        vm.run();

        // Assert result
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        do {
            line = br.readLine();
        } while (!line.startsWith("/*! assert:"));
        String assertion = line.replace("/*! assert:", "").trim();
        if (assertion.equals("exitcode")) {
            int expected = Integer.valueOf(br.readLine().trim());
            if (vm.getExitcode() != expected)
                throw new RuntimeException("exitcode = " + vm.getExitcode() + ", expected: " + expected);
        } else if (assertion.equals("exception")) {
            if (!vm.exitException())
                throw new RuntimeException("exit successfully, expected an exception.");
        } else {
            throw new RuntimeException("unknown assertion.");
        }
        br.close();
    }
}
