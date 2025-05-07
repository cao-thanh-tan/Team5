package calculatorapp.view;

import calculatorapp.controller.CalculatorController;
import calculatorapp.model.CalculatorModel;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class CalculatorUI extends javax.swing.JFrame {
    private CalculatorController controller;
    private CalculatorModel model;
    private JList<String> lichSu;
    private DefaultListModel<String> historyModel;

    private JRadioButton radianMode;
    private JRadioButton degreeMode;
    private JButton btnSin, btnCos, btnTan, btnCot, btnLog, btnLn, btnFact;

    public CalculatorUI() {
        initComponents();
        setupHistoryList();
        setIcon();
        model = new CalculatorModel();
        addAdvancedButtons(); // ⬅️ KHỞI TẠO NÚT TRƯỚC
        controller = new CalculatorController(model, this); // ⬅️ SAU ĐÓ GÁN CONTROLLER
        setButtonCommands();
        setMenuActions();


        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("E:\\CalculatorApp\\src\\calculatorapp\\icon\\digital-7.ttf")).deriveFont(36f);
            display.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calculatorapp/icon/Calculator (System Icon).png")));
    }

    private void setMenuActions() {
        JMenuItem exitItem = new JMenuItem("Thoát");
        JMenuItem saveHistoryItem = new JMenuItem("Lưu lịch sử");
        JMenuItem loadHistoryItem = new JMenuItem("Tải lịch sử");

        exitItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        saveHistoryItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                model.saveHistoryToFile(file, Collections.list(historyModel.elements()));
            }
        });

        loadHistoryItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                List<String> history = model.loadHistoryFromFile(file);
                historyModel.clear();
                for (String item : history) {
                    historyModel.addElement(item);
                }
            }
        });

        file.add(saveHistoryItem);
        file.add(loadHistoryItem);
        file.add(exitItem);
    }

    private void setupHistoryList() {
        historyModel = new DefaultListModel<>();
        lichSu = new JList<>(historyModel);
        lichSu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lichSu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historyClicked(evt);
            }
        });

        JScrollPane scrollPane = new JScrollPane(lichSu);
        scrollPane.setBounds(300, 100, 250, 340);
        jPanel3.add(scrollPane);
    }

    private void setButtonCommands() {
        cong.setActionCommand("+");
        tru.setActionCommand("-");
        nhan.setActionCommand("*");
        chia.setActionCommand("/");
        xoaHet.setActionCommand("C");
        xoaKiTu.setActionCommand("←");
        soNguyen.setActionCommand("+/-");
        soThapPhan.setActionCommand(".");
        soMu.setActionCommand("^");
        can.setActionCommand("√");
        phanTram.setActionCommand("%");
    }

    private void addAdvancedButtons() {
        JPanel customPanel = jPanelChucNangThem;
        
        customPanel.setBounds(580, 30, 240, 180); // mở rộng panel

        btnSin = new JButton("sin");
        btnCos = new JButton("cos");
        btnTan = new JButton("tan");
        btnCot = new JButton("cot");
        btnLog = new JButton("log");
        btnLn = new JButton("ln");
        btnFact = new JButton("x!");

        JButton[] buttons = { btnSin, btnCos, btnTan, btnCot, btnLog, btnLn, btnFact };
        String[] commands = { "sin", "cos", "tan", "cot", "log", "ln", "x!" };

        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setActionCommand(commands[i]);
            btn.addActionListener(controller);

            btn.setBackground(new java.awt.Color(255, 153, 0)); // màu cam
            btn.setForeground(java.awt.Color.WHITE);
            btn.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));

            int row = i / 3;
            int col = i % 3;
            btn.setBounds(col * 75, row * 45, 70, 40);
            customPanel.add(btn);
        }

        degreeMode = new JRadioButton("Deg", true);
        radianMode = new JRadioButton("Rad");

        degreeMode.setBounds(0, 140, 70, 30);
        radianMode.setBounds(80, 140, 70, 30);
        degreeMode.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));
        radianMode.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));

        degreeMode.setBackground(new java.awt.Color(51, 51, 51));
        radianMode.setBackground(new java.awt.Color(51, 51, 51));
        degreeMode.setForeground(java.awt.Color.WHITE);
        radianMode.setForeground(java.awt.Color.WHITE);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(degreeMode);
        modeGroup.add(radianMode);

        degreeMode.addActionListener(e -> model.setDegreeMode(true));
        radianMode.addActionListener(e -> model.setDegreeMode(false));

        customPanel.add(degreeMode);
        customPanel.add(radianMode);
    }

    public void updateDisplay(String text) {
        if (text.length() > 25) {
            text = text.substring(0, 25);
        }
        display.setText(text);
    }

    public String getDisplayText() {
        return display.getText().trim();
    }

    public void updateHistory(String expression, String result) {
        historyModel.addElement(expression + " = " + result);
    }

    private void xoaHetActionPerformed(java.awt.event.ActionEvent evt) {
        updateDisplay("0");
    }

    private void historyClicked(java.awt.event.MouseEvent evt) {
        if (!lichSu.isSelectionEmpty()) {
            String selectedHistory = lichSu.getSelectedValue();

            if (selectedHistory != null && selectedHistory.contains("=")) {
                String[] parts = selectedHistory.split("=");

                if (parts.length == 2) {
                    updateDisplay(parts[1].trim());
                }
            }
        }
    }

    public void addActionListeners() {
        ActionListener listener = controller;
        so0.addActionListener(listener);
        so1.addActionListener(listener);
        so2.addActionListener(listener);
        so3.addActionListener(listener);
        so4.addActionListener(listener);
        so5.addActionListener(listener);
        so6.addActionListener(listener);
        so7.addActionListener(listener);
        so8.addActionListener(listener);
        so9.addActionListener(listener);
        soThapPhan.addActionListener(listener);
        cong.addActionListener(listener);
        tru.addActionListener(listener);
        nhan.addActionListener(listener);
        chia.addActionListener(listener);
        bang.addActionListener(listener);
        xoaHet.addActionListener(listener);
        xoaKiTu.addActionListener(listener);
        soMu.addActionListener(listener);
        can.addActionListener(listener);
        phanTram.addActionListener(listener);
        soNguyen.addActionListener(listener);
    }

    public JButton getSo0() { return so0; }
    public JButton getSo1() { return so1; }
    public JButton getSo2() { return so2; }
    public JButton getSo3() { return so3; }
    public JButton getSo4() { return so4; }
    public JButton getSo5() { return so5; }
    public JButton getSo6() { return so6; }
    public JButton getSo7() { return so7; }
    public JButton getSo8() { return so8; }
    public JButton getSo9() { return so9; }
    public JButton getSoThapPhan() { return soThapPhan; }
    public JButton getCong() { return cong; }
    public JButton getTru() { return tru; }
    public JButton getNhan() { return nhan; }
    public JButton getChia() { return chia; }
    public JButton getBang() { return bang; }
    public JButton getXoaHet() { return xoaHet; }
    public JButton getXoaKiTu() { return xoaKiTu; }
    public JButton getSoMu() { return soMu; }
    public JButton getCan() { return can; }
    public JButton getPhanTram() { return phanTram; }
    public JButton getSoNguyen() { return soNguyen; }
    
    public JButton getBtnSin() {
        return btnSin;
    }

    public JButton getBtnCos() {
        return btnCos;
    }

    public JButton getBtnTan() {
        return btnTan;
    }

    public JButton getBtnCot() {
        return btnCot;
    }

    public JButton getBtnLog() {
        return btnLog;
    }

    public JButton getBtnLn() {
        return btnLn;
    }

    public JButton getBtnFact() {
        return btnFact;
    }
    
    public JRadioButton getDegreeModeButton() {
        return degreeMode;
    }

    public JRadioButton getRadianModeButton() {
        return radianMode;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        display = new javax.swing.JLabel();
        so7 = new javax.swing.JButton();
        so4 = new javax.swing.JButton();
        so1 = new javax.swing.JButton();
        soNguyen = new javax.swing.JButton();
        so0 = new javax.swing.JButton();
        so8 = new javax.swing.JButton();
        so5 = new javax.swing.JButton();
        so2 = new javax.swing.JButton();
        soThapPhan = new javax.swing.JButton();
        so6 = new javax.swing.JButton();
        so9 = new javax.swing.JButton();
        so3 = new javax.swing.JButton();
        xoaHet = new javax.swing.JButton();
        xoaKiTu = new javax.swing.JButton();
        cong = new javax.swing.JButton();
        tru = new javax.swing.JButton();
        bang = new javax.swing.JButton();
        soMu = new javax.swing.JButton();
        chia = new javax.swing.JButton();
        can = new javax.swing.JButton();
        nhan = new javax.swing.JButton();
        phanTram = new javax.swing.JButton();
        jPanelChucNangThem = new javax.swing.JPanel();
        Menu = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        display.setBackground(new java.awt.Color(255, 255, 255));
        display.setFont(new java.awt.Font("sansserif", 1, 36)); // NOI18N
        display.setForeground(new java.awt.Color(51, 51, 51));
        display.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        display.setText("0");
        display.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        display.setOpaque(true);

        so7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so7.setText("7");
        so7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                so7ActionPerformed(evt);
            }
        });

        so4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so4.setText("4");

        so1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so1.setText("1");

        soNguyen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        soNguyen.setText("+/-");

        so0.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so0.setText("0");

        so8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so8.setText("8");

        so5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so5.setText("5");

        so2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so2.setText("2");

        soThapPhan.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        soThapPhan.setText(".");

        so6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so6.setText("6");

        so9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so9.setText("9");
        so9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                so9ActionPerformed(evt);
            }
        });

        so3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        so3.setText("3");

        xoaHet.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        xoaHet.setText("CE");

        xoaKiTu.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        xoaKiTu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calculatorapp/icon/Backspace_Icon.png"))); // NOI18N
        xoaKiTu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoaKiTuActionPerformed(evt);
            }
        });

        cong.setBackground(new java.awt.Color(255, 153, 0));
        cong.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        cong.setForeground(new java.awt.Color(255, 255, 255));
        cong.setText("+");
        cong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                congActionPerformed(evt);
            }
        });

        tru.setBackground(new java.awt.Color(255, 153, 0));
        tru.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tru.setForeground(new java.awt.Color(255, 255, 255));
        tru.setText("-");

        bang.setBackground(new java.awt.Color(255, 153, 0));
        bang.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        bang.setForeground(new java.awt.Color(255, 255, 255));
        bang.setText("=");

        soMu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        soMu.setText("^");

        chia.setBackground(new java.awt.Color(255, 153, 0));
        chia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        chia.setForeground(new java.awt.Color(255, 255, 255));
        chia.setText("/");

        can.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        can.setText("√");
        can.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canActionPerformed(evt);
            }
        });

        nhan.setBackground(new java.awt.Color(255, 153, 0));
        nhan.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        nhan.setForeground(new java.awt.Color(255, 255, 255));
        nhan.setText("*");

        phanTram.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        phanTram.setText("%");

        jPanelChucNangThem.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanelChucNangThemLayout = new javax.swing.GroupLayout(jPanelChucNangThem);
        jPanelChucNangThem.setLayout(jPanelChucNangThemLayout);
        jPanelChucNangThemLayout.setHorizontalGroup(
            jPanelChucNangThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
        jPanelChucNangThemLayout.setVerticalGroup(
            jPanelChucNangThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 179, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jPanelChucNangThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(so0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(soThapPhan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bang, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(so7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(so4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(so1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(so8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(so5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(so2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(so6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tru, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(so3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cong, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(so9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(soNguyen, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(can, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(soMu, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(xoaHet, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(xoaKiTu, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(phanTram, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(chia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(290, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelChucNangThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(xoaKiTu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(224, 224, 224))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(xoaHet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(soMu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(soNguyen, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(phanTram, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(can, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(so7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(so4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(so1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(so9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(so6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tru, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(so3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cong, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(so8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(so5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(so2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(so0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soThapPhan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bang, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        file.setText("File");
        Menu.add(file);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void xoaKiTuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaKiTuActionPerformed
        // TODO add your handling code here:
        String text = getDisplayText();
        if (!text.isEmpty() && !text.equals("0")) {
            updateDisplay(text.substring(0, text.length() - 1));
        }
    }//GEN-LAST:event_xoaKiTuActionPerformed

    private void congActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_congActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_congActionPerformed

    private void so7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_so7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_so7ActionPerformed

    private void so9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_so9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_so9ActionPerformed

    private void canActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_canActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CalculatorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new CalculatorUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar Menu;
    private javax.swing.JButton bang;
    private javax.swing.JButton can;
    private javax.swing.JButton chia;
    private javax.swing.JButton cong;
    private javax.swing.JLabel display;
    private javax.swing.JMenu file;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelChucNangThem;
    private javax.swing.JButton nhan;
    private javax.swing.JButton phanTram;
    private javax.swing.JButton so0;
    private javax.swing.JButton so1;
    private javax.swing.JButton so2;
    private javax.swing.JButton so3;
    private javax.swing.JButton so4;
    private javax.swing.JButton so5;
    private javax.swing.JButton so6;
    private javax.swing.JButton so7;
    private javax.swing.JButton so8;
    private javax.swing.JButton so9;
    private javax.swing.JButton soMu;
    private javax.swing.JButton soNguyen;
    private javax.swing.JButton soThapPhan;
    private javax.swing.JButton tru;
    private javax.swing.JButton xoaHet;
    private javax.swing.JButton xoaKiTu;
    // End of variables declaration//GEN-END:variables

    
}
