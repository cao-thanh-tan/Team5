package calculatorapp.controller;

import calculatorapp.model.CalculatorModel;
import calculatorapp.view.CalculatorUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorController implements ActionListener {
    private final CalculatorModel model;
    private final CalculatorUI view;
    private final StringBuilder currentExpression;
    private boolean isNewInput;

    public CalculatorController(CalculatorModel model, CalculatorUI view) {
        this.model = model;
        this.view = view;
        this.currentExpression = new StringBuilder();
        this.isNewInput = true;
        addActionListeners();
    }

    private void addActionListeners() {
        view.getSo0().addActionListener(this);
        view.getSo1().addActionListener(this);
        view.getSo2().addActionListener(this);
        view.getSo3().addActionListener(this);
        view.getSo4().addActionListener(this);
        view.getSo5().addActionListener(this);
        view.getSo6().addActionListener(this);
        view.getSo7().addActionListener(this);
        view.getSo8().addActionListener(this);
        view.getSo9().addActionListener(this);
        view.getSoThapPhan().addActionListener(this);
        view.getCong().addActionListener(this);
        view.getTru().addActionListener(this);
        view.getNhan().addActionListener(this);
        view.getChia().addActionListener(this);
        view.getBang().addActionListener(this);
        view.getXoaHet().addActionListener(this);
        view.getXoaKiTu().addActionListener(this);
        view.getSoMu().addActionListener(this);
        view.getCan().addActionListener(this);
        view.getPhanTram().addActionListener(this);
        view.getSoNguyen().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "C": // Xóa toàn bộ biểu thức
                currentExpression.setLength(0);
                view.updateDisplay("0");
                isNewInput = true;
                break;

            case "←": // Xóa một ký tự
                if (currentExpression.length() > 0) {
                    currentExpression.deleteCharAt(currentExpression.length() - 1);
                }
                if (currentExpression.length() == 0) {
                    view.updateDisplay("0");
                    isNewInput = true;
                } else {
                    view.updateDisplay(currentExpression.toString());
                }
                break;

            case "=": // Tính toán kết quả
                if (currentExpression.length() > 0) {
                    if (currentExpression.toString().matches(".*[+\\-*/^]$")) {
                        currentExpression.deleteCharAt(currentExpression.length() - 1);
                    }
                    try {
                        double result = model.evaluateExpression(currentExpression.toString());
                        view.updateDisplay(String.valueOf(result));
                        view.updateHistory(currentExpression.toString(), String.valueOf(result));
                        currentExpression.setLength(0);
                        currentExpression.append(result);
                        isNewInput = true;
                    } catch (Exception ex) {
                        view.updateDisplay("Error");
                    }
                }
                break;

            case "+": case "-": case "*": case "/": case "^": // Các phép toán cơ bản
                if (currentExpression.length() == 0) {
                    if (command.equals("-")) {
                        currentExpression.append(command); // Cho phép bắt đầu bằng dấu trừ
                        isNewInput = false;
                    }
                } else {
                    char lastChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (isNewInput) {
                        currentExpression.append(command); // Nối toán tử vào kết quả trước đó
                        isNewInput = false;
                    } else if ("+-*/^".indexOf(lastChar) != -1) {
                        if (command.equals("-")) {
                            currentExpression.append(command); // Nối - sau bất kỳ toán tử nào
                        } else {
                            currentExpression.setCharAt(currentExpression.length() - 1, command.charAt(0)); // Thay thế toán tử cuối
                        }
                    } else {
                        currentExpression.append(command);
                        isNewInput = false;
                    }
                }
                view.updateDisplay(currentExpression.toString());
                break;

            case "+/-": // Đổi dấu số
                if (currentExpression.length() == 0 || currentExpression.toString().equals("0")) {
                    currentExpression.setLength(0);
                    currentExpression.append("-");
                    view.updateDisplay("-");
                    isNewInput = false;
                } else {
                    int lastOperatorIndex = -1;
                    for (int j = currentExpression.length() - 1; j >= 0; j--) {
                        if ("+-*/^".indexOf(currentExpression.charAt(j)) != -1) {
                            lastOperatorIndex = j;
                            break;
                        }
                    }
                    if (lastOperatorIndex == -1) { // Không có toán tử
                        if (currentExpression.charAt(0) == '-') {
                            currentExpression.deleteCharAt(0);
                        } else {
                            currentExpression.insert(0, "-");
                        }
                    } else { // Có toán tử, đổi dấu số cuối
                        int numStart = lastOperatorIndex + 1;
                        if (numStart < currentExpression.length() && currentExpression.charAt(numStart) == '-') {
                            currentExpression.deleteCharAt(numStart);
                        } else if (numStart == currentExpression.length()) {
                            currentExpression.append("-");
                        } else {
                            currentExpression.insert(numStart, "-");
                        }
                    }
                    view.updateDisplay(currentExpression.toString());
                }
                break;

            case ".": // Xử lý dấu thập phân
                int lastOperatorIndex = -1;
                for (int i = currentExpression.length() - 1; i >= 0; i--) {
                    if ("+-*/^".indexOf(currentExpression.charAt(i)) != -1) {
                        lastOperatorIndex = i;
                        break;
                    }
                }
                String currentNumber = (lastOperatorIndex == -1) ?
                        currentExpression.toString() :
                        currentExpression.substring(lastOperatorIndex + 1);
                if (!currentNumber.contains(".")) {
                    currentExpression.append(".");
                    view.updateDisplay(currentExpression.toString());
                    isNewInput = false;
                }
                break;

            case "√": // Căn bậc hai
                if (currentExpression.length() > 0) {
                    try {
                        double number = Double.parseDouble(currentExpression.toString());
                        if (number >= 0) {
                            double result = Math.sqrt(number);
                            view.updateDisplay(String.valueOf(result));
                            view.updateHistory("√" + currentExpression.toString(), String.valueOf(result));
                            currentExpression.setLength(0);
                            currentExpression.append(result);
                            isNewInput = true;
                        } else {
                            view.updateDisplay("Error");
                        }
                    } catch (NumberFormatException ex) {
                        view.updateDisplay("Error");
                    }
                }
                break;

            case "%": // Phần trăm
                if (currentExpression.length() > 0) {
                    try {
                        double number = Double.parseDouble(currentExpression.toString());
                        double result = number / 100;
                        view.updateDisplay(String.valueOf(result));
                        view.updateHistory(currentExpression.toString() + "%", String.valueOf(result));
                        currentExpression.setLength(0);
                        currentExpression.append(result);
                        isNewInput = true;
                    } catch (NumberFormatException ex) {
                        view.updateDisplay("Error");
                    }
                }
                break;

            default: // Xử lý khi bấm số
                if (isNewInput && !currentExpression.toString().equals("-")) {
                    currentExpression.setLength(0);
                    isNewInput = false;
                }
                currentExpression.append(command);
                view.updateDisplay(currentExpression.toString());
                break;
        }
    }
}