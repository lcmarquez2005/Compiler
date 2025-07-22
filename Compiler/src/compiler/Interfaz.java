package compiler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Interfaz extends JFrame {

    private JTextArea inputArea;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    private final Color mainBackground = new Color(248, 248, 255);
    private final Color headerColor = new Color(220, 235, 250);
    private final Color borderColor = new Color(200, 200, 200);
    private final Color buttonColor = new Color(100, 149, 237);
    private final Color buttonHoverColor = new Color(70, 130, 180);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
    private final Font regularFont = new Font("Segoe UI", Font.PLAIN, 13);

    private final Lexer lexer = new Lexer(); // Conexión al analizador real

    public Interfaz() {
        createUI();
    }

    private void createUI() {
        setTitle("Analizador Léxico - Lenguajes y Autómatas II");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(mainBackground);
        setLayout(new BorderLayout(10, 10));
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(headerColor);
        infoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel materiaLabel = new JLabel("Instituto Tecnológico de Pachuca - Lenguajes y Autómatas II");
        materiaLabel.setFont(regularFont);
        materiaLabel.setForeground(Color.BLACK);
        materiaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel docenteLabel = new JLabel("Docente: Dr. Arturo González Cerón");
        docenteLabel.setFont(regularFont);
        docenteLabel.setForeground(Color.BLACK);
        docenteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel alumnosLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<b>Alumnos:</b><br>" +
                "<table style='margin: 0 auto;'>" +
                "<tr><td>Luis Carlos Marquez Strociak</td><td style='padding-left: 1px;'>23200286</td>Erik Ivan Quijano Gonzales" +
                "<td style='padding-left: 5px;'>22200759</td>Ingrid Alison Porras Lugo" +
                "<td style='padding-left: 5px;'>22200758</td>" + 

                "</table>" +
                "</div></html>");
        alumnosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        alumnosLabel.setFont(regularFont);
        alumnosLabel.setForeground(Color.BLACK);
        alumnosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(materiaLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(docenteLabel);
        infoPanel.add(alumnosLabel);
        infoPanel.add(Box.createVerticalStrut(3));

        // Área de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Código fuente a analizar"));
        inputPanel.setBackground(mainBackground);

        inputArea = new JTextArea(15, 60);
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        String[] columnNames = { "Lexema", "Tipo", "Posición" };
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(regularFont);
        resultTable.setRowHeight(25);
        resultTable.setGridColor(new Color(180, 180, 180));
        resultTable.setShowHorizontalLines(true);
        resultTable.setShowVerticalLines(true);
        resultTable.setIntercellSpacing(new Dimension(1, 1));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultados del análisis léxico"));
        resultPanel.setBackground(mainBackground);
        resultPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(mainBackground);

        JButton analyzeBtn = createStyledButton("Analizar");
        JButton clearBtn = createStyledButton("Borrar");

        // Acción botón Analizar
        analyzeBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            String code = inputArea.getText();

            if (!code.isEmpty()) {
                List<Token> tokens = lexer.analizar(code);
                for (Token token : tokens) {
                    tableModel.addRow(new Object[] {
                            token.getLexema(),
                            token.getTipo(),
                            "Columna: " + token.getColumna() + "Linea: " + token.getLinea()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, escribe algo para analizar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Acción botón Borrar
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            tableModel.setRowCount(0);
        });

        buttonPanel.add(analyzeBtn);
        buttonPanel.add(clearBtn);

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(mainBackground);
        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(resultPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(buttonPanel);

        add(infoPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(titleFont);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(8, 20, 8, 20)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(buttonHoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(buttonColor);
            }
        });

        return btn;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));
        pack();
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables
     // End of variables declaration//GEN-END:variables
}
