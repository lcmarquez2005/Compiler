package compiler;

import java.util.*;
import java.util.regex.*;

public class Tokenizer {

     // Conjunto de palabras reservadas
    public static final Set<String> PALABRAS_RESERVADAS = Set.of(
        "if", "else", "while", "for", "do", 
        "break", "continue", "return", "function", "var"
    );

        public static final Set<String> PALABRAS_MAL_USADAS = Set.of(
        "far", "farr", "ef", "whole", "wail", "ent", "estrin", "strin"
        );


    /**
     * Analiza una línea de texto y la convierte en una lista de tokens.
     * 
     * @param linea La cadena de texto a analizar
     * @return Lista de tokens identificados en el texto
     */

    public List<Token> tokeniza(String texto, int linea) {
        List<Token> tokens = new ArrayList<>();
        Pattern patron = Pattern.compile(
        "\\d+(\\.\\d+)?|" +                        // Números
        "[a-zA-Z_][a-zA-Z0-9_]*|" +                // Identificadores válidos
        ";|" +                                     // Punto y coma
        "[+\\-*/=()]" +                            // Operadores/paréntesis individuales
        "|\\S+"                                    // Cualquier otro símbolo no válido
);


        Matcher matcher = patron.matcher(texto);

        while (matcher.find()) {
            String lexema = matcher.group();
            int columna = matcher.start() + 1; // +1 si quieres empezar en columna 1

            tokens.add(new Token(lexema, clasificar(lexema), columna, 0, linea));
        }

        return tokens;
    }

    // Determinamos el tipo del token en base al lexema
private TokenType clasificar(String lexema) {
    // Números
    if (lexema.matches("\\d+(\\.\\d+)?"))
        return TokenType.NUMERO;

    // Palabras
    if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
        if (PALABRAS_RESERVADAS.contains(lexema)) return TokenType.PALABRAS_RESERVADAS;
        if (PALABRAS_MAL_USADAS.contains(lexema)) return TokenType.ERROR;
        if (lexema.equalsIgnoreCase("and") || lexema.equalsIgnoreCase("or") || lexema.equalsIgnoreCase("not"))
            return TokenType.ERROR;
        return TokenType.IDENTIFICADOR;
    }

    // Punto y coma
    if (lexema.equals(";")) return TokenType.SEMICOLON;

    // Operadores y símbolos permitidos
    return switch (lexema) {
        case "+" -> TokenType.PLUS;
        case "-" -> TokenType.MINUS;
        case "*" -> TokenType.ASTERISCO;
        case "/" -> TokenType.DIVISION;
        case "(" -> TokenType.PAR_IZQ;
        case ")" -> TokenType.PAR_DER;
        case "=" -> TokenType.ASIGNACION;
        default -> TokenType.ERROR;
    };
}


}
