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

    public List<Token> tokeniza(String texto, int linea) {
        List<Token> tokens = new ArrayList<>();
        Pattern patron = Pattern.compile(
            "\\d+(\\.\\d+)?|" +              // Números
            "[a-zA-Z_][a-zA-Z0-9_]*|" +      // Identificadores válidos
            "[a-zA-Z_][^\\s;()=+\\-*/]*|" +  // Identificadores inválidos (ej. hola$f)
            "[+\\-*/=()]|" +                 // Operadores/paréntesis
            ";|" +                           // Separadores opcionales
            "\\S+"                           // Otros símbolos no válidos agrupados
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
        if (lexema.matches("\\d+(\\.\\d+)?"))
        return TokenType.NUMERO;
        if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
        return TokenType.IDENTIFICADOR;

        // Detectar operadores y paréntesis
        return switch (lexema) {
            case "+" -> TokenType.PLUS;
            case "-" -> TokenType.MINUS;
            case "*" -> TokenType.ASTERISCO;
            case "(" -> TokenType.PAR_IZQ;
            case ")" -> TokenType.PAR_DER;
            case "=" -> TokenType.ASIGNACION;
            default -> TokenType.ERROR;
        };
    }

}
