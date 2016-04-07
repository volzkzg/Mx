package compiler;

import compiler.ast.SymbolTable;
import compiler.build.AstBuilderListener;
import compiler.parser.GrammarLexer;
import compiler.parser.GrammarParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class SemanticCheckTest {
	private String filename;
	private boolean shouldPass;

	public SemanticCheckTest(String filename, boolean shouldPass) {
		this.filename = filename;
		this.shouldPass = shouldPass;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> parameters = new ArrayList<>();
		for (File file : new File("testfile/passed/").listFiles()) {
			if (file.isFile() && file.getName().endsWith(".mx")) {
				parameters.add(new Object[]{
						"testfile/passed/" + file.getName(),
						true
				});
			}
		}
		for (File file : new File("testfile/compile_error/").listFiles()) {
			if (file.isFile() && file.getName().endsWith(".mx")) {
				parameters.add(new Object[]{
						"testfile/compile_error/" + file.getName(),
						false
				});
			}
		}
		return parameters;
	}

	@Test
	public void testPass() throws IOException {
		System.out.println(filename);

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

		if (shouldPass != ret) {
			fail("Failed!");
		}
	}
}