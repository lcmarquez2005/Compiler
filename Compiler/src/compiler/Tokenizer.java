package compiler;

import java.util.*;
import java.util.regex.*;

public class Tokenizer {

    /**
     * Analiza una línea de texto y la convierte en una lista de tokens.
     * 
     * @param linea La cadena de texto a analizar
     * @return Lista de tokens identificados en el texto
     */

    public List<Token> tokeniza(String texto) {
        List<Token> tokens = new ArrayList<>();
        Pattern patron = Pattern.compile(
                "\\d+(\\.\\d+)?|" + // números
                        "[a-zA-Z_][a-zA-Z0-9_]*|" + // identificadores
                        "[+\\-*/=()]" + // operadores y paréntesis
                        "|\\S" // cualquier otro símbolo no espacio
        );

        Matcher matcher = patron.matcher(texto);

        while (matcher.find()) {
            String lexema = matcher.group();
            int columna = matcher.start() + 1; // +1 si quieres empezar en columna 1

            TokenType tipo = clasificar(lexema);
            tokens.add(new Token(lexema, tipo, columna, 0, 0)); // ajusta 0s si usas fila/columna real
        }

        return tokens;
    }

    // Determinamos el tipo del token en base al lexema
    private TokenType clasificar(String lexema) {
        if (lexema.matches("\\d+(\\.\\d+)?"))
            return TokenType.TOKEN_NUMERO;
        if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
            return TokenType.TOKEN_IDENTIFICADOR;

        // Detectar operadores y paréntesis
        return switch (lexema) {
            case "+" -> TokenType.TOKEN_PLUS;
            case "-" -> TokenType.TOKEN_MINUS;
            case "*" -> TokenType.TOKEN_ASTERISCO;
            case "/" -> TokenType.TOKEN_DIVISION;
            case "(" -> TokenType.TOKEN_PAR_IZQ;
            case ")" -> TokenType.TOKEN_PAR_DER;
            case "=" -> TokenType.TOKEN_ASIGNACION;
            default -> TokenType.TOKEN_ERROR;
        };
    }

}