// Parser.java
package compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase Parser analiza una lista de tokens y verifica su sintaxis.
 * Verifica que:
 * - No falten paréntesis.
 * - No haya divisiones entre cero.
 * - No existan operadores consecutivos inválidos (como ++, *-).
 * - Todas las expresiones terminen con ';'.
 */
public class Parser {
    private final List<Token> tokens; // Lista de tokens a analizar
    private int current; // Índice actual del token que se está analizando en la linea

    private List<SyntaxError> errores;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0; // Inicia en el primer token que es el 0 como de 2+2 serua el 2
        this.errores = new ArrayList<>();
        // TODO tambien deberiamos hacer que haga el parseo apenas se instancie
    }

    /**
     * Método principal para iniciar el análisis sintáctico.
     * Lanza SyntaxError si encuentra algún error.
     */

    // Esta parte no se si del todo dejarla ya que no siempre usamos expresiones
    // como X = 2+2*(4+7)
    public void parse() {
        while (!isAtEnd() && errores.isEmpty()) {
            // Si la expresión inicia con un identificador, puede ser una asignación
            if (match(TokenType.IDENTIFICADOR)) {
                // Espera que después del identificador venga un '=' para asignar
                if (match(TokenType.ASIGNACION)) {
                    expression(); // Analiza la expresión a la derecha del '='
                }
                // else {
                // // Si no hay '=', lanza error específico
                // errores.add(new SyntaxError("Se esperaba '=' despues del identificador",
                // tokens.get(current)));
                // }
            } else {
                // Si no inicia con identificador, analiza directamente una expresión
                expression();
            }

            // TODO Antes de buscar el ';', verificamos si el token actual es un PAR_DER
            if (!isAtEnd() && getCurrent().getTipo() == TokenType.PAR_DER) {
                errores.add(new SyntaxError("Paréntesis izquierdo faltante", null));
                saltoSeguro();
                continue;
            }

            // Luego espera que la sentencia termine con un punto y coma ';'
            if (match(TokenType.SEMICOLON)) {
                // Correcto, continúa
            } else {
                errores.add(new SyntaxError("Se esperaba ';' al final de la sentencia", getPrevious()));
                saltoSeguro();
                continue;
            }

            // El resto sigue igual
            if (!isAtEnd()) {
                if ((current - 1) != 0) {
                    if (this.tokens.get(current).getLinea() == this.getPrevious().getLinea()) {
                        errores.add(new SyntaxError("Token inesperado después de ';': ", getCurrent()));
                    }
                }
            }
        }
    }

    /**
     * Método que analiza una expresión que puede contener sumas y restas.
     */
    private void expression() {
        term(); // Analiza el término inicial

        // Mientras haya un operador + o -
        while (current < tokens.size() &&
                (tokens.get(current).getTipo() == TokenType.PLUS ||
                        tokens.get(current).getTipo() == TokenType.MINUS)) {

            Token operador = tokens.get(current); // Guarda operador actual
            current++; // Avanza al siguiente token

            // Verifica que no haya operadores consecutivos inválidos
            if (current < tokens.size() &&
                    (tokens.get(current).getTipo() == TokenType.PLUS ||
                            tokens.get(current).getTipo() == TokenType.MINUS ||
                            tokens.get(current).getTipo() == TokenType.ASTERISCO ||
                            tokens.get(current).getTipo() == TokenType.DIVISION ||
                            tokens.get(current).getTipo() == TokenType.ASIGNACION)) {
                errores.add(new SyntaxError("Operadores consecutivos no permitidos: '", operador, tokens.get(current)));
            }

            term(); // Analiza el siguiente término después del operador
        }
    }

    /**
     * Método que analiza un término que puede contener multiplicaciones y
     * divisiones.
     */
    private void term() {
        factor(); // Analiza el factor inicial

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
                errores.add(new SyntaxError("Operadores consecutivos no permitidos: ", operador, tokens.get(current)));
            }

            factor(); // Analiza el siguiente factor
        }
    }

    /**
     * Método que analiza un factor.
     * Un factor puede ser ya sea como:
     * - Un número
     * - Un identificador
     * - Una expresión entre paréntesis
     */
    private void factor() {
        if (current >= tokens.size()) {
            errores.add(new SyntaxError("Se esperaba un factor", getPrevious()));
        }

        Token token = getCurrent();
        TokenType tipo = token.getTipo();

        if (tipo == TokenType.PAR_DER) {
            errores.add(new SyntaxError("Paréntesis izquierdo faltante", token));
        } else if (tipo == TokenType.PAR_IZQ) {
            current++;
            expression();

            if (current >= tokens.size() || tokens.get(current).getTipo() != TokenType.PAR_DER) {
                errores.add(new SyntaxError("Paréntesis derecho faltante", getPrevious()));
            }
            current++;
        } else if (tipo == TokenType.IDENTIFICADOR || tipo == TokenType.NUMERO) {
            current++;
        } else {
            errores.add(new SyntaxError("Factor inválido: ", token));
        }
    }

    /**
     * Verifica si el siguiente token tras una división es el número 0.
     * Si es así, lanza un error de división por cero.
     */
    private void checkDivisionByZero() {
        if (current + 1 < tokens.size() &&
                tokens.get(current + 1).getTipo() == TokenType.NUMERO &&
                tokens.get(current + 1).getLexema().equals("0")) {
            errores.add(new SyntaxError("División por cero", tokens.get(current + 1)));
        }
    }

    /**
     * Si el token actual coincide con el tipo esperado, consume el token y retorna
     * que es verdadero.
     */
    private boolean match(TokenType tipo) {
        if (check(tipo)) {
            advance();
            return true;
        }
        return false;
    }

    /*
    *
    *
    *
    */
    public List<SyntaxError> getErrores() {
        return this.errores;
    }

    /*
     * Metodo que me sirve para saltar hasta el proximo ; cuando hay un error
     * de manera que no saltaran miles de errores solo por un unico error
     */
    private void saltoSeguro() {
    // Mientras no se haya llegado al final y el token actual no sea un punto y coma, por lo que sigue avanzando
        while (!isAtEnd() && getCurrent().getTipo() != TokenType.SEMICOLON) {
            advance();
        }
    // Si termino el while y aún hay tokens (estamos en el punto y coma). Se avanza una vez más para pasarlo y no quedarse estancado en el
        if (!isAtEnd()) {
            advance();
        }
    }

    /**
     * Verifica si el token actual es del tipo dado sin consumirlo.
     */
    private boolean check(TokenType tipo) {
        return !isAtEnd() && getCurrent().getTipo() == tipo;
    }

    /**
     * Avanza el índice para consumir el token actual y retorna el token consumido.
     */
    private Token advance() {
        if (!isAtEnd())
            current++;
        return getPrevious();
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
    private Token getCurrent() {
        return tokens.get(current);
    }

    /**
     * Retorna el token anterior (último consumido).
     */
    private Token getPrevious() {
        return tokens.get(current - 1);
    }

}
