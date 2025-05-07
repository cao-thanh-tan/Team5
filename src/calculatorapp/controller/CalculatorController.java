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
    private boolean isDegreeMode = true;

    public void setDegreeMode(boolean degreeMode) {
        this.isDegreeMode = degreeMode;
    }


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

        // Các nút nâng cao
        view.getBtnSin().addActionListener(this);
        view.getBtnCos().addActionListener(this);
        view.getBtnTan().addActionListener(this);
        view.getBtnCot().addActionListener(this);
        view.getBtnLog().addActionListener(this);
        view.getBtnLn().addActionListener(this);
        view.getBtnFact().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "sin": case "cos": case "tan": case "cot":
            case "log": case "ln": case "x!":
                try {
                    double inputValue = Double.parseDouble(view.getDisplayText());
                    double result = model.evaluateAdvancedFunction(command, inputValue);
                    String mode = model.isDegreeMode() ? " (Deg)" : " (Rad)";
                    view.updateDisplay(result + mode);
                    view.updateHistory(command + "(" + inputValue + ")", String.valueOf(result));
                    currentExpression.setLength(0);
                    currentExpression.append(result);
                    isNewInput = true;
                } catch (NumberFormatException ex) {
                    view.updateDisplay("Error");
                }
                break;

            case "C":
                currentExpression.setLength(0);
                view.updateDisplay("0");
                isNewInput = true;
                break;

            case "←":
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

            case "=":
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

            case "+": case "-": case "*": case "/": case "^":
                if (currentExpression.length() == 0) {
                    if (command.equals("-")) {
                        currentExpression.append(command);
                        isNewInput = false;
                    }
                } else {
                    char lastChar = currentExpression.charAt(currentExpression.length() - 1);
                    if (isNewInput) {
                        currentExpression.append(command);
                        isNewInput = false;
                    } else if ("+-*/^".indexOf(lastChar) != -1) {
                        if (command.equals("-")) {
                            currentExpression.append(command);
                        } else {
                            currentExpression.setCharAt(currentExpression.length() - 1, command.charAt(0));
                        }
                    } else {
                        currentExpression.append(command);
                        isNewInput = false;
                    }
                }
                view.updateDisplay(currentExpression.toString());
                break;

            case "+/-":
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
                    if (lastOperatorIndex == -1) {
                        if (currentExpression.charAt(0) == '-') {
                            currentExpression.deleteCharAt(0);
                        } else {
                            currentExpression.insert(0, "-");
                        }
                    } else {
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

            case ".":
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

            case "√":
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

            case "%":
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

            default:
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
