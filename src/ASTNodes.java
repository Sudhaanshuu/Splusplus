import java.util.ArrayList;
import java.util.List;

abstract class Node {}

class ProgramNode extends Node {
    List<Node> statements = new ArrayList<>();

    void addStatement(Node statement) {
        statements.add(statement);
    }
}

class VariableDeclarationNode extends Node {
    Token type;
    Token name;
    Node initializer;

    VariableDeclarationNode(Token type, Token name, Node initializer) {
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }
}

class AssignmentNode extends Node {
    Token name;
    Node value;

    AssignmentNode(Token name, Node value) {
        this.name = name;
        this.value = value;
    }
}

class PrintNode extends Node {
    Node expression;

    PrintNode(Node expression) {
        this.expression = expression;
    }
}

class IfNode extends Node {
    Node condition;
    Node thenBranch;
    Node elseBranch;

    IfNode(Node condition, Node thenBranch, Node elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}

class BinaryOperatorNode extends Node {
    Node left;
    Token operator;
    Node right;

    BinaryOperatorNode(Node left, Token operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

class NumberNode extends Node {
    String value;

    NumberNode(String value) {
        this.value = value;
    }
}

class VariableNode extends Node {
    String name;

    VariableNode(String name) {
        this.name = name;
    }
}

class GroupingNode extends Node {
    Node expression;

    GroupingNode(Node expression) {
        this.expression = expression;
    }
}
