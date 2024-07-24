import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "example/hlo.su";
        String code = new String(Files.readAllBytes(Paths.get(fileName)));
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        Node ast = parser.parse();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast);
    }
}
