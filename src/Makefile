all: clean
	@mkdir -p ./bin
	@cd ./compiler && javac -cp \
		../antlr-4.5.2-complete.jar \
		./ast/*.java \
		./ast/*/*.java \
		./ast/*/*/*.java \
		./ast/*/*/*/*.java \
		./build/*.java \
		./parser/*.java \
		-d ../bin
	@cp ./antlr-4.5.2-complete.jar ./bin
	@cd ./bin && jar xf ./antlr-4.5.2-complete.jar \
			  && rm -rf ./META-INF \
			  && jar cef build/Build Compiler.jar ./ \
			  && rm -rf ./antlr-4.5.2-complete.jar ./Compiler ./org

clean:
	rm -rf ./bin
