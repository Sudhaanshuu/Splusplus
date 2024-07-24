import java.util.ArrayList;
import java.util.List;

enum TokenType {
    SUINT, SUFLOAT, PRINT, INPUT, IF, ELSE, IDENTIFIER, NUMBER, STRING,
    ASSIGN, PLUS, MINUS, STAR, SLASH, LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, EOF
}

class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }
}

public class Lexer {
    private final String input;
    private int pos = 0;
    private final int length;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < length) {
            char currentChar = input.charAt(pos);
            if (Character.isWhitespace(currentChar)) {
                pos++;
                continue;
            }
            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.NUMBER, readNumber()));
                continue;
            }
            if (currentChar == '"') {
                tokens.add(new Token(TokenType.STRING, readString()));
                continue;
            }
            if (Character.isLetter(currentChar)) {
                String identifier = readIdentifier();
                switch (identifier) {
                    case "suINT":
                        tokens.add(new Token(TokenType.SUINT, identifier));
                        break;
                    case "suFLOAT":
                        tokens.add(new Token(TokenType.SUFLOAT, identifier));
                        break;
                    case "please.su.print":
                        tokens.add(new Token(TokenType.PRINT, identifier));
                        break;
                    case "please.su.input":
                        tokens.add(new Token(TokenType.INPUT, identifier));
                        break;
                    case "suIF":
                        tokens.add(new Token(TokenType.IF, identifier));
                        break;
                    case "suELSE":
                        tokens.add(new Token(TokenType.ELSE, identifier));
                        break;
                    default:
                        tokens.add(new Token(TokenType.IDENTIFIER, identifier));
                        break;
                }
                continue;
            }
            switch (currentChar) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+"));
                    pos++;
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-"));
                    pos++;
                    break;
                case '*':
                    tokens.add(new Token(TokenType.STAR, "*"));
                    pos++;
                    break;
                case '/':
                    tokens.add(new Token(TokenType.SLASH, "/"));
                    pos++;
                    break;
                case '=':
                    tokens.add(new Token(TokenType.ASSIGN, "="));
                    pos++;
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "("));
                    pos++;
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")"));
                    pos++;
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, "{"));
                    pos++;
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, "}"));
                    pos++;
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";"));
                    pos++;
                    break;
                default:
                    throw new RuntimeException("Unexpected character: " + currentChar);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private String readNumber() {
        int start = pos;
        while (pos < length && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        return input.substring(start, pos);
    }

    private String readString() {
        pos++; // Skip the opening quote
        int start = pos;
        while (pos < length && input.charAt(pos) != '"') {
            pos++;
        }
        if (pos == length) {
            throw new RuntimeException("Unterminated string literal");
        }
        String str = input.substring(start, pos);
        pos++; // Skip the closing quote
        return str;
    }

    private String readIdentifier() {
        int start = pos;
        while (pos < length && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            pos++;
        }
        return input.substring(start, pos);
    }
}
