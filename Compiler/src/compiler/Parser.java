// Parser.java
package compiler;

import java.util.List;

/**
 * Esta clase Parser analiza una lista de tokens y verifica su sintaxis.
 * Que no falten paréntesis.
 * Que no haya divisiones entre cero.
 * Que los operadores no estén mal colocados (como ++ o *-).
 * Que todas las expresiones terminen con ;.
 */

public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public void parse() throws SyntaxError {
    if (match(TokenType.IDENTIFICADOR)) {
        // Puede ser una asignación si sigue un '='
        if (match(TokenType.ASIGNACION)) {
            E(); // Analiza expresión a la derecha de la asignación
        } else {
            throw new SyntaxError("Se esperaba '=' después del identificador", peek());
        }
    } else {
        // No empieza con identificador, puede ser solo una expresión
        E();
    }

    // Validar punto y coma al final
    if (match(TokenType.SEMICOLON)) {
        // OK
    } else {
        throw new SyntaxError("Se esperaba ';' al final de la sentencia", peek());
    }

    // No deben sobrar tokens
    if (!isAtEnd()) {
        throw new SyntaxError("Token inesperado después de ';': '" +
                peek().getLexema() + "'", peek());
    }
}



    private void E() throws SyntaxError {
    T();
    while (current < tokens.size() &&
           (tokens.get(current).getTipo() == TokenType.PLUS ||
            tokens.get(current).getTipo() == TokenType.MINUS)) {

        Token operador = tokens.get(current);
        current++;

        if (current < tokens.size() &&
            (tokens.get(current).getTipo() == TokenType.PLUS ||
             tokens.get(current).getTipo() == TokenType.MINUS ||
             tokens.get(current).getTipo() == TokenType.ASTERISCO ||
             tokens.get(current).getTipo() == TokenType.DIVISION ||
             tokens.get(current).getTipo() == TokenType.ASIGNACION)) {
            throw new SyntaxError("Operadores consecutivos no permitidos: '" +
                operador.getLexema() + tokens.get(current).getLexema() + "'", tokens.get(current));
        }

        T();
    }
}


    private void T() throws SyntaxError {
    F();
    while (current < tokens.size() &&
           (tokens.get(current).getTipo() == TokenType.ASTERISCO ||
            tokens.get(current).getTipo() == TokenType.DIVISION)) {

        Token operador = tokens.get(current);

        if (tokens.get(current).getTipo() == TokenType.DIVISION) {
            checkDivisionByZero();
        }

        current++;

        if (current < tokens.size() &&
            (tokens.get(current).getTipo() == TokenType.PLUS ||
             tokens.get(current).getTipo() == TokenType.MINUS ||
             tokens.get(current).getTipo() == TokenType.ASTERISCO ||
             tokens.get(current).getTipo() == TokenType.DIVISION ||
             tokens.get(current).getTipo() == TokenType.ASIGNACION)) {
            throw new SyntaxError("Operadores consecutivos no permitidos: '" +
                operador.getLexema() + tokens.get(current).getLexema() + "'", tokens.get(current));
        }

        F();
    }
}


    private void F() throws SyntaxError {
        if (current >= tokens.size()) {
            throw new SyntaxError("Se esperaba un factor", tokens.get(current - 1));
        }

        TokenType tipo = tokens.get(current).getTipo();
        if (tipo == TokenType.PAR_IZQ) {
            current++;
            E();
            if (current >= tokens.size() || tokens.get(current).getTipo() != TokenType.PAR_DER) {
                throw new SyntaxError("Paréntesis derecho faltante", tokens.get(current - 1));
            }
            current++;
        } else if (tipo == TokenType.IDENTIFICADOR || tipo == TokenType.NUMERO) {
            current++;
        } else {
            throw new SyntaxError("Factor inválido: '" + tokens.get(current).getLexema() + "'", tokens.get(current));
        }
    }

    private void checkDivisionByZero() throws SyntaxError {
        if (current + 1 < tokens.size() &&
                tokens.get(current + 1).getTipo() == TokenType.NUMERO &&
                tokens.get(current + 1).getLexema().equals("0")) {
            throw new SyntaxError("División por cero", tokens.get(current + 1));
        }
    }
    private boolean match(TokenType tipo) {
    if (check(tipo)) {
        advance();
        return true;
    }
    return false;
}

private boolean check(TokenType tipo) {
    return !isAtEnd() && peek().getTipo() == tipo;
}

private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
}

private boolean isAtEnd() {
    return current >= tokens.size();
}

private Token peek() {
    return tokens.get(current);
}

private Token previous() {
    return tokens.get(current - 1);
}

}