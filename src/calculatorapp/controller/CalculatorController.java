package calculatorapp.controller;

import calculatorapp.model.CalculatorModel;
import calculatorapp.view.CalculatorUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CalculatorController implements ActionListener{
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

            case "√": // Căn bậc hai
                double sqrtResult = model.squareRoot(Double.parseDouble(view.getDisplayText()));
                view.updateDisplay(String.format("%.7g", sqrtResult)); // Hiển thị tối đa 7 số
                currentExpression.setLength(0);
                currentExpression.append(sqrtResult);
                isNewInput = true;
                break;

            case "%": // Phần trăm
                double percentResult = model.percentage(Double.parseDouble(view.getDisplayText()));
                view.updateDisplay(String.valueOf(percentResult));
                currentExpression.setLength(0);
                currentExpression.append(percentResult);
                isNewInput = true;
                break;
            
            case "+/-": // Đổi dấu số
                if (currentExpression.length() > 0) {
                    if (currentExpression.toString().equals("0")) {
                        break; 
                    }

                    int lastOperatorIndex = -1;
                    for (int j = currentExpression.length() - 1; j >= 0; j--) {
                        if ("+-*/^".indexOf(currentExpression.charAt(j)) != -1) {
                            lastOperatorIndex = j;
                            break;
                        }
                    }

                    if (lastOperatorIndex == -1) { 
                        if (currentExpression.charAt(0) == '-') {
                            currentExpression.deleteCharAt(0);
                        } else {
                            currentExpression.insert(0, "-");
                        }
                    } else { // Nếu có toán tử, đảo dấu số cuối cùng
                        int numStart = lastOperatorIndex + 1;
                        if (currentExpression.charAt(numStart) == '-') {
                            currentExpression.deleteCharAt(numStart);
                        } else {
                            currentExpression.insert(numStart, "-");
                        }
                    }

                    view.updateDisplay(currentExpression.toString());
                }
                break;
            
            case ".": // Số thập phân
                if (!currentExpression.toString().contains(".")) {
                    currentExpression.append(".");
                    view.updateDisplay(currentExpression.toString());
                }
                break;
            
            case "^": // Lũy thừa
                currentExpression.append("^");
                view.updateDisplay(currentExpression.toString());
                break;

            case "=": // Tính toán kết quả
                if (currentExpression.length() == 0) return;
                if (currentExpression.toString().matches(".*[+\\-*/^]$")) {
                    currentExpression.deleteCharAt(currentExpression.length() - 1);
                }
                double result = model.calculate(currentExpression.toString());
                view.updateDisplay(String.valueOf(result));
                currentExpression.setLength(0);
                currentExpression.append(result);
                isNewInput = true;
                break;
            
            case "+": case "-": case "*": case "/": // Các phép toán cơ bản
                if (currentExpression.length() > 0) {
                    char lastChar = currentExpression.charAt(currentExpression.length() - 1);
                    if ("+-*/".indexOf(lastChar) != -1) {
                        currentExpression.setCharAt(currentExpression.length() - 1, command.charAt(0));
                    } else {
                        currentExpression.append(command);
                    }
                }
                view.updateDisplay(currentExpression.toString());
                isNewInput = false;
                break;
            
            default: // Xử lý khi bấm số
                if (isNewInput) {
                    currentExpression.setLength(0);
                    isNewInput = false;
                }
                currentExpression.append(command);
                view.updateDisplay(currentExpression.toString());
                break;
        }
    }

}
