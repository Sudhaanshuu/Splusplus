import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node parse() {
        return parseProgram();
    }

    private Node parseProgram() {
        ProgramNode programNode = new ProgramNode();
        while (!isAtEnd()) {
            programNode.addStatement(parseStatement());
        }
        return programNode;
    }

    private Node parseStatement() {
        Token token = peek();
        switch (token.type) {
            case SUINT:
            case SUFLOAT:
                return parseVariableDeclaration();
            case IDENTIFIER:
                return parseAssignmentOrFunctionCall();
            case PRINT:
                return parsePrintStatement();
            case IF:
                return parseIfStatement();
            default:
                throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private Node parseVariableDeclaration() {
        Token typeToken = advance();
        Token identifierToken = consume(TokenType.IDENTIFIER, "Expect variable name.");
        consume(TokenType.ASSIGN, "Expect '=' after variable name.");
        Node initializer = parseExpression();
        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VariableDeclarationNode(typeToken, identifierToken, initializer);
    }

    private Node parseAssignmentOrFunctionCall() {
        Token identifierToken = advance();
        if (match(TokenType.ASSIGN)) {
            Node value = parseExpression();
            consume(TokenType.SEMICOLON, "Expect ';' after assignment.");
            return new AssignmentNode(identifierToken, value);
        } else {
            throw new RuntimeException("Unexpected token: " + peek());
        }
    }

    private Node parsePrintStatement() {
        advance(); // consume 'please.su.print'
        Node expression = parseExpression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new PrintNode(expression);
    }

    private Node parseIfStatement() {
        advance(); // consume 'suIF'
        consume(TokenType.LPAREN, "Expect '(' after 'suIF'.");
        Node condition = parseExpression();
        consume(TokenType.RPAREN, "Expect ')' after if condition.");
        Node thenBranch = parseStatement();
        Node elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = parseStatement();
        }
        return new IfNode(condition, thenBranch, elseBranch);
    }

    private Node parseExpression() {
        return parseAddition();
    }

    private Node parseAddition() {
        Node left = parseMultiplication();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Node right = parseMultiplication();
            left = new BinaryOperatorNode(left, operator, right);
        }
        return left;
    }

    private Node parseMultiplication() {
        Node left = parsePrimary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Node right = parsePrimary();
            left = new BinaryOperatorNode(left, operator, right);
        }
        return left;
    }

    private Node parsePrimary() {
        if (match(TokenType.NUMBER)) {
            return new NumberNode(previous().value);
        }
        if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(previous().value);
        }
        if (match(TokenType.LPAREN)) {
            Node expr = parseExpression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return new GroupingNode(expr);
        }
        throw new RuntimeException("Unexpected token: " + peek());
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) pos++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message);
    }
}
