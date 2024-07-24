import java.util.HashMap;
import java.util.Map;

class Interpreter {
    private final Map<String, Object> environment = new HashMap<>();

    void interpret(Node node) {
        if (node instanceof ProgramNode) {
            for (Node statement : ((ProgramNode) node).statements) {
                interpret(statement);
            }
        } else if (node instanceof VariableDeclarationNode) {
            VariableDeclarationNode varDecl = (VariableDeclarationNode) node;
            Object value = evaluate(varDecl.initializer);
            environment.put(varDecl.name.value, value);
        } else if (node instanceof AssignmentNode) {
            AssignmentNode assignment = (AssignmentNode) node;
            Object value = evaluate(assignment.value);
            environment.put(assignment.name.value, value);
        } else if (node instanceof PrintNode) {
            Object value = evaluate(((PrintNode) node).expression);
            System.out.println(value);
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            Object condition = evaluate(ifNode.condition);
            if ((boolean) condition) {
                interpret(ifNode.thenBranch);
            } else if (ifNode.elseBranch != null) {
                interpret(ifNode.elseBranch);
            }
        }
    }

    private Object evaluate(Node node) {
        if (node instanceof NumberNode) {
            return Integer.parseInt(((NumberNode) node).value);
        } else if (node instanceof VariableNode) {
            return environment.get(((VariableNode) node).name);
        } else if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode binary = (BinaryOperatorNode) node;
            Object left = evaluate(binary.left);
            Object right = evaluate(binary.right);
            switch (binary.operator.type) {
                case PLUS:
                    return (int) left + (int) right;
                case MINUS:
                    return (int) left - (int) right;
                case STAR:
                    return (int) left * (int) right;
                case SLASH:
                    return (int) left / (int) right;
            }
        } else if (node instanceof GroupingNode) {
            return evaluate(((GroupingNode) node).expression);
        }
        return null;
    }
}
