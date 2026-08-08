import java.util.*;

/**
 * Expression Evaluator - Console based Java application
 * Evaluates mathematical expressions with +, -, *, /, ^, and parentheses,
 * respecting operator precedence, using the Shunting Yard algorithm
 * to convert infix notation to postfix (RPN), then evaluates the RPN.
 *
 * Supports decimal numbers and negative numbers (e.g. -3 + 4).
 */
public class ExpressionEvaluator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Expression Evaluator =====");
        System.out.println("Supports + - * / ^ and parentheses. Type 'exit' to quit.\n");

        while (true) {
            System.out.print("Enter expression: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            if (input.isEmpty()) {
                continue;
            }

            try {
                List<String> tokens = tokenize(input);
                List<String> postfix = infixToPostfix(tokens);
                double result = evaluatePostfix(postfix);

                if (result == Math.floor(result) && !Double.isInfinite(result)) {
                    System.out.println("Result: " + (long) result);
                } else {
                    System.out.println("Result: " + result);
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid expression (" + e.getMessage() + ")");
            }
        }

        sc.close();
    }

    // Breaks the input string into tokens: numbers, operators, parentheses
    static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        int n = expr.length();

        while (i < n) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < n && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i));
                    i++;
                }
                tokens.add(num.toString());
                continue;
            }

            // Handle unary minus: negative number if '-' is at start or after an operator/'('
            if (c == '-' && (tokens.isEmpty() || isOperator(tokens.get(tokens.size() - 1)) || tokens.get(tokens.size() - 1).equals("("))) {
                StringBuilder num = new StringBuilder("-");
                i++;
                while (i < n && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i));
                    i++;
                }
                if (num.length() == 1) {
                    throw new IllegalArgumentException("invalid use of '-'");
                }
                tokens.add(num.toString());
                continue;
            }

            if ("+-*/^()".indexOf(c) != -1) {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            throw new IllegalArgumentException("unexpected character '" + c + "'");
        }

        return tokens;
    }

    static boolean isOperator(String token) {
        return token.length() == 1 && "+-*/^".contains(token);
    }

    static int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    static boolean isRightAssociative(String op) {
        return op.equals("^");
    }

    // Shunting Yard algorithm: infix -> postfix
    static List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!opStack.isEmpty() && isOperator(opStack.peek()) &&
                        (precedence(opStack.peek()) > precedence(token) ||
                         (precedence(opStack.peek()) == precedence(token) && !isRightAssociative(token)))) {
                    output.add(opStack.pop());
                }
                opStack.push(token);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (opStack.isEmpty()) {
                    throw new IllegalArgumentException("mismatched parentheses");
                }
                opStack.pop(); // remove the '('
            }
        }

        while (!opStack.isEmpty()) {
            String op = opStack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("mismatched parentheses");
            }
            output.add(op);
        }

        return output;
    }

    static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Evaluates a postfix (RPN) expression
    static double evaluatePostfix(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("not enough operands");
                }
                double b = stack.pop();
                double a = stack.pop();
                double result;

                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        if (b == 0) throw new ArithmeticException("division by zero");
                        result = a / b;
                        break;
                    case "^":
                        result = Math.pow(a, b);
                        break;
                    default:
                        throw new IllegalArgumentException("unknown operator " + token);
                }
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("malformed expression");
        }

        return stack.pop();
    }
  }
  
