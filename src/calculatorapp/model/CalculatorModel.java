package calculatorapp.model;

import java.util.Stack;

public class CalculatorModel {
    public double calculate(String expression) {
        try {
            return evaluateExpression(expression);
        } catch (Exception e) {
            return Double.NaN; // Trả về NaN nếu có lỗi
        }
    }

    public double evaluateExpression(String expression) {
        // Chuẩn hóa biểu thức: thay -- thành +
        expression = expression.replaceAll("--", "+");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        char[] tokens = expression.toCharArray();
        int i = 0;

        while (i < tokens.length) {
            // Bỏ qua khoảng trắng nếu có
            if (tokens[i] == ' ') {
                i++;
                continue;
            }

            // Xử lý số (bao gồm số âm và số thập phân)
            if (Character.isDigit(tokens[i]) || tokens[i] == '.' || 
                (tokens[i] == '-' && (i == 0 || "+-*/^(".indexOf(tokens[i - 1]) != -1))) {
                StringBuilder num = new StringBuilder();
                if (tokens[i] == '-') {
                    num.append('-');
                    i++;
                }
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    num.append(tokens[i]);
                    i++;
                }
                try {
                    numbers.push(Double.parseDouble(num.toString()));
                } catch (NumberFormatException e) {
                    return Double.NaN; // Trả về NaN nếu không parse được số
                }
                continue;
            }

            // Xử lý dấu ngoặc mở
            if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } 
            // Xử lý dấu ngoặc đóng
            else if (tokens[i] == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    if (numbers.size() < 2) return Double.NaN; // Không đủ số để tính
                    double b = numbers.pop();
                    double a = numbers.pop();
                    numbers.push(applyOperator(operators.pop(), b, a));
                }
                if (operators.isEmpty()) return Double.NaN; // Thiếu dấu ngoặc mở
                operators.pop(); // Xóa '('
            } 
            // Xử lý toán tử
            else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && operators.peek() != '(' && 
                       precedence(operators.peek()) >= precedence(tokens[i])) {
                    if (numbers.size() < 2) return Double.NaN; // Không đủ số để tính
                    double b = numbers.pop();
                    double a = numbers.pop();
                    numbers.push(applyOperator(operators.pop(), b, a));
                }
                operators.push(tokens[i]);
            }
            i++;
        }

        // Xử lý các toán tử còn lại trong stack
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                return Double.NaN; // Dấu ngoặc không khớp
            }
            if (numbers.size() < 2) return Double.NaN; // Không đủ số để tính
            double b = numbers.pop();
            double a = numbers.pop();
            numbers.push(applyOperator(operators.pop(), b, a));
        }

        if (numbers.isEmpty()) return Double.NaN; // Không có kết quả
        return numbers.pop();
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
            case '/': 
                if (b == 0) return Double.NaN; // Chia cho 0
                return a / b;
            case '^': return Math.pow(a, b);
            default: return Double.NaN;
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