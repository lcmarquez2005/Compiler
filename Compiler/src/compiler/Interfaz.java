package compiler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
                "<tr><td>Luis Carlos Marquez Strociak</td><td style='padding-left: 1px;'>23200286</td>Erik Ivan Quijano Gonzales"
                +
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

        // Panel contenedor de resultados
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultados del análisis léxico"));
        resultPanel.setBackground(mainBackground);

        JPanel tokenAndErrorPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tokenAndErrorPanel.setBackground(mainBackground);

        JScrollPane tableScroll = new JScrollPane(resultTable);
        tokenAndErrorPanel.add(tableScroll);

        // Panel nuevo: errores léxicos
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBorder(BorderFactory.createTitledBorder("Errores:"));
        errorPanel.setBackground(mainBackground);

        JTextArea errorTextArea = new JTextArea();
        errorTextArea.setEditable(false);
        errorTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setBackground(Color.WHITE);
        errorTextArea.setText("");
        errorPanel.add(new JScrollPane(errorTextArea), BorderLayout.CENTER);

        tokenAndErrorPanel.add(errorPanel);
        resultPanel.add(tokenAndErrorPanel, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(mainBackground);

        JButton analyzeBtn = createStyledButton("Analizar");
        JButton clearBtn = createStyledButton("Borrar");

        // Acción botón Analizar
        analyzeBtn.addActionListener(e -> {
            // Limpiar la tabla de tokens y el área de errores antes de analizar de nuevo
            tableModel.setRowCount(0);
            errorTextArea.setText("");

            // Obtener el texto ingresado por el usuario en el área de entrada
            String code = inputArea.getText();

            // Verificar que el usuario haya escrito algo
            if (!code.isEmpty()) {
                // Llamar al lexer para obtener la lista de tokens del código ingresado
                List<Token> tokens = lexer.analizar(code);

                // Mostrar los tokens en la tabla de la interfaz
                for (Token token : tokens) {
                    tableModel.addRow(new Object[] {
                            token.getLexema(), // El texto original del token
                            token.getTipo(), // El tipo de token (Número, Operador, etc.)
                            "Línea: " + token.getLinea() // Posición en el código
                    });
                }

                // Intentar analizar la sintaxis usando el parser
                Parser parser = new Parser(tokens);
                parser.parse(); // Aquí se revisa la sintaxis, incluyendo la validación del ';'
                List<SyntaxError> errores = parser.getErrores();
                List<String> resultados = parser.getResultados();

                if (errores.isEmpty()) {

                    String mensaje = "";
                    int i = 0;
                    for (String resultado : resultados) {
                        i++;
                        mensaje += "Linea " + i + " " + resultado + "\n";
                    }

                    JOptionPane.showMessageDialog(null, mensaje);
                } else {

                    for (SyntaxError error : errores) {
                        errorTextArea.append(error.getError() + "\n");
                    }
                }

            } else {
                // Si el campo está vacío, mostrar advertencia al usuario
                JOptionPane.showMessageDialog(this, "Escribe algo antes de analizar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Acción botón Borrar
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            tableModel.setRowCount(0);
            errorTextArea.setText("");
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

    /**
     * Método para dividir una lista de tokens en múltiples sublistas,
     * separando cada una por el token de punto y coma ';'. No se si seria de
     * utilizada mucho ya que es para usar varias sentencias
     *
     * 
     * @param tokens lista completa de tokens generada por el lexer
     * @return lista de listas de tokens, donde cada sublista representa una
     *         sentencia completa
     */
    private List<List<Token>> splitTokensBySemicolon(List<Token> tokens) {
        List<List<Token>> statements = new ArrayList<>(); // Lista final de sentencias separadas
        List<Token> current = new ArrayList<>(); // Lista temporal para almacenar tokens de la sentencia actual

        // Recorrer todos los tokens
        for (Token token : tokens) {
            current.add(token); // Agregar token a la sentencia actual

            // Si el token es un punto y coma, significa que la sentencia actual terminó
            if (token.getTipo() == TokenType.SEMICOLON) {
                statements.add(current); // Añadir la sentencia completa a la lista de sentencias
                current = new ArrayList<>(); // Empezar a guardar tokens para la siguiente sentencia
            }
        }

        // Si quedan tokens que no terminan con punto y coma, agregarlos también como
        // una sentencia
        if (!current.isEmpty()) {
            statements.add(current);
        }

        return statements; // Devolver lista de sentencias separadas por ';'
    }

}
