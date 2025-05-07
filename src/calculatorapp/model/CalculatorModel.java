package calculatorapp.model;

import java.util.Stack;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {
    private boolean isDegreeMode = true; // true: độ, false: radian

    public void setDegreeMode(boolean degreeMode) {
        this.isDegreeMode = degreeMode;
    }

    public boolean isDegreeMode() {
        return isDegreeMode;
    }

    public double calculate(String expression) {
        try {
            return evaluateExpression(expression);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public double evaluateExpression(String expression) {
        expression = expression.replaceAll("--", "+");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        char[] tokens = expression.toCharArray();
        int i = 0;

        while (i < tokens.length) {
            if (tokens[i] == ' ') {
                i++;
                continue;
            }

            if (Character.isDigit(tokens[i]) || tokens[i] == '.' ||
                (tokens[i] == '-' && (i == 0 || "+-*/^(".indexOf(tokens[i - 1]) != -1))) {
                StringBuilder num = new StringBuilder();
                if (tokens[i] == '-') {
                    num.append('-');
                    i++;
                }
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    num.append(tokens[i++]);
                }
                try {
                    numbers.push(Double.parseDouble(num.toString()));
                } catch (NumberFormatException e) {
                    return Double.NaN;
                }
                continue;
            }

            if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    if (numbers.size() < 2) return Double.NaN;
                    double b = numbers.pop();
                    double a = numbers.pop();
                    numbers.push(applyOperator(operators.pop(), b, a));
                }
                if (operators.isEmpty()) return Double.NaN;
                operators.pop();
            } else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && operators.peek() != '(' &&
                       precedence(operators.peek()) >= precedence(tokens[i])) {
                    if (numbers.size() < 2) return Double.NaN;
                    double b = numbers.pop();
                    double a = numbers.pop();
                    numbers.push(applyOperator(operators.pop(), b, a));
                }
                operators.push(tokens[i]);
            }
            i++;
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '(') return Double.NaN;
            if (numbers.size() < 2) return Double.NaN;
            double b = numbers.pop();
            double a = numbers.pop();
            numbers.push(applyOperator(operators.pop(), b, a));
        }

        return numbers.isEmpty() ? Double.NaN : numbers.pop();
    }

    private boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '^';
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

    private double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b == 0 ? Double.NaN : a / b;
            case '^': return Math.pow(a, b);
            default: return Double.NaN;
        }
    }

    // Hàm toán học nâng cao
    public double evaluateAdvancedFunction(String func, double value) {
        double rad = isDegreeMode ? Math.toRadians(value) : value;

        switch (func) {
            case "sin": return Math.sin(rad);
            case "cos": return Math.cos(rad);
            case "tan": return Math.tan(rad);
            case "cot":
                double tan = Math.tan(rad);
                if (tan == 0) throw new ArithmeticException("cot undefined");
                return 1.0 / tan;
            case "log":
                if (value <= 0) throw new ArithmeticException("log undefined");
                return Math.log10(value);
            case "ln":
                if (value <= 0) throw new ArithmeticException("ln undefined");
                return Math.log(value);
            case "x!":
                if (value < 0 || value != (int) value) throw new ArithmeticException("factorial undefined");
                return factorial((int) value);
            default:
                throw new IllegalArgumentException("Unknown function: " + func);
        }
    }

    private double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Save/load history
    public void saveHistoryToFile(File file, List<String> historyList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String item : historyList) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu lịch sử: " + e.getMessage());
        }
    }

    public List<String> loadHistoryFromFile(File file) {
        List<String> history = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    history.add(line);
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc lịch sử: " + e.getMessage());
            }
        }
        return history;
    }
}
