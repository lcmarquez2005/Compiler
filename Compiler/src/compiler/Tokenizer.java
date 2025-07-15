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

    public List<Token> analizar(String linea) {
        List<Token> tokens = new ArrayList<>();
    
    // Patrón regex para identificar:
    // 1. Números (enteros o decimales): \\d+(\\.\\d+)?
    // 2. Variables (solo letras): [a-zA-Z]+
    // 3. Operadores y paréntesis: [+\\-*/()]
    // 4. Cualquier otro símbolo no espacio: \\S

        Pattern patron = Pattern.compile("\\d+(\\.\\d+)?|[a-zA-Z]+|[+\\-*/()]|\\S");
        Matcher matcher = patron.matcher(linea);

        // Aqui recorremos todas las coincidencias del patrón en el texto
        while (matcher.find()) {
            String lexema = matcher.group(); // Obtenemos el texto del token encontrado
            int posicion = matcher.start(); // Obtenemos la posición INICIAL del token en el texto

            // Aqui se crea y se añade el nuevo token a la lista
            TokenType tipo = clasificar(lexema);

            // Determinamos el tipo del token creado
            tokens.add(new Token(lexema, tipo, posicion, 0 , 0));
        }

        return tokens;
    }


    // Determinamos el tipo del token en base al lexema
    private TokenType clasificar(String lexema) {

        // Comprobamos si nuetro lecema es un numero (entero o de tipo decimal)
        if (lexema.matches("\\d+(\\.\\d+)?")) return TokenType.TOKEN_NUMERO; 

        // Comprobamos si nuestro lexema es un identificador (variable)
        else if (lexema.matches("[a-zA-Z]+")) return TokenType.TOKEN_IDENTIFICADOR;

        // Comprobamos si nuestro lexema es un operador o paréntesis
        else if (lexema.equals("+")) return TokenType.TOKEN_PLUS;
        else if (lexema.equals("-")) return TokenType.TOKEN_MINUS;
        else if (lexema.equals("*")) return TokenType.TOKEN_ASTERISCO;
        else if (lexema.equals("/")) return TokenType.TOKEN_DIVISION;
        else if (lexema.equals("(")) return TokenType.TOKEN_PAR_IZQ;
        else if (lexema.equals(")")) return TokenType.TOKEN_PAR_DER;
        else if (lexema.equals("=")) return TokenType.TOKEN_IGUAL;
        else return TokenType.TOKEN_ERROR;
    }
}