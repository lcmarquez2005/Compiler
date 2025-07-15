package Compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LexicoCore {
    public static final int NUMERO = 0;
    public static final int OPERADOR = 1;
    public static final int PARENTESIS = 2;
    public static final int DESCONOCIDO = -1;

    public static class Token {
        public final int tipo;
        public final String lexema;

        public Token(int tipo, String lexema) {
            this.tipo = tipo;
            this.lexema = lexema;
        }
    }

    public static List<Token> analizarExpresion(String expresion) {
        List<Token> tokens = new ArrayList<>();
        if (expresion == null || expresion.trim().isEmpty()) {
            return tokens;
        }

        StringTokenizer st = new StringTokenizer(expresion, "+-*/=()", true);
        
        while (st.hasMoreTokens()) {
            String lexema = st.nextToken().trim();
            if (lexema.isEmpty()) continue;

            int tipo;
            if (lexema.matches("\\d+")) {
                tipo = NUMERO;
            } else if (lexema.matches("[+\\-*/=]")) {
                tipo = OPERADOR;
            } else if (lexema.matches("[()]")) {
                tipo = PARENTESIS;
            } else {
                tipo = DESCONOCIDO;
            }
            
            tokens.add(new Token(tipo, lexema));
        }
        
        return tokens;
    }
}