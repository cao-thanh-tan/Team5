package calculatorapp.model;

import java.util.Stack;

public class CalculatorModel {
    public double calculate(String expression) {
        try {
            return evaluateExpression(expression);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private double evaluateExpression(String expression) {
        expression = expression.replaceAll("--", "+"); // Đổi -- thành +
        if (expression.startsWith("-")) { // Xử lý số âm đầu biểu thức
            expression = "0" + expression;
        }

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        char[] tokens = expression.toCharArray();
        int i = 0;

        while (i < tokens.length) {
            if (Character.isDigit(tokens[i]) || tokens[i] == '.') {
                StringBuilder num = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    num.append(tokens[i++]);
                }
                numbers.push(Double.parseDouble(num.toString()));
                continue;
            }

            if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(tokens[i])) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(tokens[i]);
            }
            i++;
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '^';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        if (op == '^') return 3;
        return -1;
    }

    private double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return (b == 0) ? Double.NaN : a / b;
            case '^': return Math.pow(a, b);
            default: return 0;
        }
    }

    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public double squareRoot(double number) {
        return (number < 0) ? Double.NaN : Math.sqrt(number);
    }

    public double percentage(double number) {
        return number / 100.0;
    }
}
