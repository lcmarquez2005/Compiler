package compiler;

import java.util.*;
import java.util.regex.*;

public class Tokenizer {
    public List<Token> analizar(String linea) {
        List<Token> tokens = new ArrayList<>();
        // Patrón: números (incluyendo negativos), operadores, paréntesis, o cualquier otro símbolo no reconocido
        Pattern patron = Pattern.compile("-?\\d+|[+\\-*/()]|[^\\s\\d+\\-*/()]");
        Matcher matcher = patron.matcher(linea);

        while (matcher.find()) {
            String lexema = matcher.group();
            int posicion = matcher.start();
            TokenType tipo = clasificar(lexema);
            tokens.add(new Token(lexema, tipo, posicion));
        }

        return tokens;
    }

    private TokenType clasificar(String lexema) {
        if (lexema.matches("-?\\d+")) return TokenType.NUMERO;
        else if (lexema.matches("[+\\-*/]")) return TokenType.OPERADOR;
        else if (lexema.matches("[()]")) return TokenType.PARENTESIS;
        else return TokenType.ERROR;
    }
}