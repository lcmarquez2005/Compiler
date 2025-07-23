// Parser.java
package compiler;

import java.util.List;

/**
 * Esta clase Parser analiza una lista de tokens y verifica su sintaxis.
 * Verifica que:
 *  - No falten paréntesis.
 *  - No haya divisiones entre cero.
 *  - No existan operadores consecutivos inválidos (como ++, *-).
 *  - Todas las expresiones terminen con ';'.
 */
public class Parser {
    private final List<Token> tokens; // Lista de tokens a analizar
    private int current;               // Índice actual del token que se está analizando en la linea

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;              // Inicia en el primer token que es el 0 como de 2+2 serua el 2
    }

    /**
     * Método principal para iniciar el análisis sintáctico.
     * Lanza SyntaxError si encuentra algún error.
     */

     // Esta parte no se si del todo dejarla ya que no siempre usamos expresiones como X = 2+2*(4+7)
public void parse() throws SyntaxError {
    if (match(TokenType.IDENTIFICADOR)) {
        if (match(TokenType.ASIGNACION)) {
            E();
        } else {
            throw new SyntaxError("Se esperaba '=' después del identificador", peek());
        }
    } else {
        E();
    }

    // Antes de buscar el ';', verificamos si el token actual es un PAR_DER inesperado
    if (!isAtEnd() && peek().getTipo() == TokenType.PAR_DER) {
        throw new SyntaxError("Paréntesis izquierdo faltante", peek());
    }

    if (match(TokenType.SEMICOLON)) {
        // OK
    } else {
        throw new SyntaxError("Se esperaba ';' al final de la sentencia", peek());
    }

    // El resto sigue igual
    if (!isAtEnd()) {
        if ((current - 1) != 0) {
            if (this.tokens.get(current).getLinea() == this.tokens.get(current - 1).getLinea()) {
                throw new SyntaxError("Token inesperado después de ';': '" +
                        peek().getLexema() + "'", peek());
            }
        }
    }
}


    /**
     * Método que analiza una expresión que puede contener sumas y restas.
     */
    private void E() throws SyntaxError {
        T(); // Analiza el término inicial

        // Mientras haya un operador + o -
        while (current < tokens.size() &&
                (tokens.get(current).getTipo() == TokenType.PLUS ||
                        tokens.get(current).getTipo() == TokenType.MINUS)) {

            Token operador = tokens.get(current); // Guarda operador actual
            current++;                            // Avanza al siguiente token

            // Verifica que no haya operadores consecutivos inválidos
            if (current < tokens.size() &&
                    (tokens.get(current).getTipo() == TokenType.PLUS ||
                            tokens.get(current).getTipo() == TokenType.MINUS ||
                            tokens.get(current).getTipo() == TokenType.ASTERISCO ||
                            tokens.get(current).getTipo() == TokenType.DIVISION ||
                            tokens.get(current).getTipo() == TokenType.ASIGNACION)) {
                throw new SyntaxError("Operadores consecutivos no permitidos: '" +
                        operador.getLexema() + tokens.get(current).getLexema() + "'", tokens.get(current));
            }

            T(); // Analiza el siguiente término después del operador
        }
    }

    /**
     * Método que analiza un término que puede contener multiplicaciones y divisiones.
     */
    private void T() throws SyntaxError {
        F(); // Analiza el factor inicial

        // Mientras haya un operador * o / (Multiplicacion o División)
        while (current < tokens.size() &&
                (tokens.get(current).getTipo() == TokenType.ASTERISCO ||
                        tokens.get(current).getTipo() == TokenType.DIVISION)) {

            Token operador = tokens.get(current);

            // Si es división, verifica división por cero
            if (tokens.get(current).getTipo() == TokenType.DIVISION) {
                checkDivisionByZero();
            }

            current++; // Avanza al siguiente token (factor derecho)

            // Verifica que no haya operadores consecutivos inválidos
            if (current < tokens.size() &&
                    (tokens.get(current).getTipo() == TokenType.PLUS ||
                            tokens.get(current).getTipo() == TokenType.MINUS ||
                            tokens.get(current).getTipo() == TokenType.ASTERISCO ||
                            tokens.get(current).getTipo() == TokenType.DIVISION ||
                            tokens.get(current).getTipo() == TokenType.ASIGNACION)) {
                throw new SyntaxError("Operadores consecutivos no permitidos: '" +
                        operador.getLexema() + tokens.get(current).getLexema() + "'", tokens.get(current));
            }

            F(); // Analiza el siguiente factor
        }
    }

    /**
     * Método que analiza un factor.
     * Un factor puede ser ya sea como:
     *  - Un número
     *  - Un identificador
     *  - Una expresión entre paréntesis
     */
    private void F() throws SyntaxError {
        if (current >= tokens.size()) {
            throw new SyntaxError("Se esperaba un factor", tokens.get(current - 1));
        }

        Token token = tokens.get(current);
        TokenType tipo = token.getTipo();

        if (tipo == TokenType.PAR_DER) {
            throw new SyntaxError("Paréntesis izquierdo faltante", token);
        } else if (tipo == TokenType.PAR_IZQ) {
            current++;
            E();

            if (current >= tokens.size() || tokens.get(current).getTipo() != TokenType.PAR_DER) {
                throw new SyntaxError("Paréntesis derecho faltante", tokens.get(current - 1));
            }
            current++;
        } else if (tipo == TokenType.IDENTIFICADOR || tipo == TokenType.NUMERO) {
            current++;
        } else {
            throw new SyntaxError("Factor inválido: '" + token.getLexema() + "'", token);
        }
    }


    /**
     * Verifica si el siguiente token tras una división es el número 0.
     * Si es así, lanza un error de división por cero.
     */
    private void checkDivisionByZero() throws SyntaxError {
        if (current + 1 < tokens.size() &&
                tokens.get(current + 1).getTipo() == TokenType.NUMERO &&
                tokens.get(current + 1).getLexema().equals("0")) {
            throw new SyntaxError("División por cero", tokens.get(current + 1));
        }
    }

    /**
     * Si el token actual coincide con el tipo esperado, consume el token y retorna que es verdadero.
     */
    private boolean match(TokenType tipo) {
        if (check(tipo)) {
            advance();
            return true;
        }
        return false;
    }

    /**
     * Verifica si el token actual es del tipo dado sin consumirlo.
     */
    private boolean check(TokenType tipo) {
        return !isAtEnd() && peek().getTipo() == tipo;
    }

    /**
     * Avanza el índice para consumir el token actual y retorna el token consumido.
     */
    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    /**
     * Indica si ya se analizaron todos los tokens.
     */
    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    /**
     * Retorna el token actual (el siguiente a analizar).
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Retorna el token anterior (último consumido).
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

}
