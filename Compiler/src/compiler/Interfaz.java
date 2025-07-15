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

        // Panel superior: info alumno y docente
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(headerColor);
        infoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel alumnoLabel = new JLabel("Alumno: PORRAS LUGO INGRID ALISON      No. Control: 22200758");
        JLabel materiaLabel = new JLabel("Instituto Tecnológico de Pachuca - Lenguajes y Autómatas II");
        JLabel docenteLabel = new JLabel("Docente: Dr. Arturo González Cerón");

        alumnoLabel.setFont(titleFont);
        materiaLabel.setFont(regularFont);
        docenteLabel.setFont(regularFont);

        alumnoLabel.setForeground(Color.BLACK);
        materiaLabel.setForeground(Color.BLACK);
        docenteLabel.setForeground(Color.BLACK);

        infoPanel.add(alumnoLabel);
        infoPanel.add(materiaLabel);
        infoPanel.add(docenteLabel);

        // Área de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Código fuente a analizar"));
        inputPanel.setBackground(mainBackground);

        inputArea = new JTextArea(5, 40);
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Tabla de resultados
        String[] columnNames = {"Lexema", "Tipo", "Posición"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(regularFont);
        resultTable.setRowHeight(25);
        resultTable.setGridColor(borderColor);
        resultTable.setShowHorizontalLines(true);
        resultTable.setShowVerticalLines(false);
        resultTable.setIntercellSpacing(new Dimension(0, 5));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(230, 230, 250));
        headerRenderer.setForeground(Color.BLACK);
        headerRenderer.setFont(titleFont);
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
            String code = inputArea.getText().trim();

            if (!code.isEmpty()) {
                List<Token> tokens = lexer.analizar(code);
                for (Token token : tokens) {
                   /* tableModel.addRow(new Object[]{
                        token.lexema,
                        token.tipo,
                        token.posicion
                    });*/
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, escribe algo para analizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
                new EmptyBorder(8, 20, 8, 20)
        ));

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
