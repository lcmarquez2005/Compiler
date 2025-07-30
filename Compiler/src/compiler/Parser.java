package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase Parser analiza una lista de tokens y verifica su sintaxis.
 * Verifica que:
 * - No falten paréntesis.
 * - No haya divisiones entre cero.
 * - No existan operadores consecutivos inválidos (como ++, *-).
 * - Todas las expresiones terminen con ';'.
 * Además, evalúa el resultado de las expresiones y soporta asignaciones a
 * variables.
 */
public class Parser {
    private final List<Token> tokens; // Lista de tokens a analizar
    private int current; // Índice actual del token que se está analizando en la linea

    private List<SyntaxError> errores;
    private List<String> resultados;

    // Mapa para almacenar variables y sus valores numéricos
    private Map<String, Double> variables = new HashMap<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0; // Inicia en el primer token que es el 0 como de 2+2 serua el 2
        this.errores = new ArrayList<>();
        this.resultados = new ArrayList<>();
        // TODO tambien deberiamos hacer que haga el parseo apenas se instancie
    }

    /**
     * Método principal para iniciar el análisis sintáctico.
     * Lanza SyntaxError si encuentra algún error.
     */
    public void parse() {
        while (!isAtEnd() && errores.isEmpty()) {
            Token currentToken = getCurrent();
            String lexema = currentToken.getLexema();

            // Verificar si el token es ERROR
            if (currentToken.getTipo() == TokenType.ERROR) {
                // Si el lexema está en palabras reservadas válidas, no es error, solo procesar
                // normal
                if (Tokenizer.PALABRAS_RESERVADAS.contains(lexema)) {
                    // No hacer nada aquí, dejar que continúe el parseo normal
                } else if (Tokenizer.PALABRAS_MAL_USADAS.contains(lexema)) {
                    errores.add(new SyntaxError("Posible palabra reservada mal usada o error de sintaxis: " + lexema,
                            currentToken));
                    saltoSeguro();
                    continue;
                } else {
                    errores.add(new SyntaxError("Palabra o símbolo no permitido: " + lexema, currentToken));
                    saltoSeguro();
                    continue;
                }
            }

            double resultadoExpresion = 0;

            // Si la expresión inicia con un identificador o palabra reservada, puede ser
            // asignación
            if (match(TokenType.IDENTIFICADOR)) {
                String varName = getPrevious().getLexema();
                if (match(TokenType.ASIGNACION)) {
                    resultadoExpresion = expression(); // Evalúa expresión derecha del '='
                    variables.put(varName, resultadoExpresion); // Guarda valor en variable
                    System.out.println(varName + " = " + resultadoExpresion);
                } else {
                    // No es asignación, quizás solo un identificador solo o error
                    errores.add(new SyntaxError("Si se quiere asignar valor agregar '=' después del identificador",
                            currentToken));
                    saltoSeguro();
                    continue;
                }
            } else {
                resultadoExpresion = expression();
                if (errores.isEmpty()) {
                    this.resultados.add("Resultado: " + resultadoExpresion);
                }
            }

            // Verifica paréntesis izquierdo faltante
            if (!isAtEnd() && getCurrent().getTipo() == TokenType.PAR_DER) {
                errores.add(new SyntaxError("Paréntesis izquierdo faltante", null));
                saltoSeguro();
                continue;
            }

            // Espera que la sentencia termine con ';'
            if (match(TokenType.SEMICOLON)) {
                // Correcto, continúa
            } else {
                errores.add(new SyntaxError("Se esperaba ';' al final de la sentencia", getPrevious()));
                saltoSeguro();
                continue;
            }

            // Detectar tokens inesperados tras ';' en la misma línea
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
     * Retorna el resultado evaluado como double.
     */
    private double expression() {
        double resultado = term(); // Analiza el término inicial

        // Mientras haya un operador + o -
        while (current < tokens.size() &&
                (tokens.get(current).getTipo() == TokenType.PLUS ||
                        tokens.get(current).getTipo() == TokenType.MINUS)) {

            Token operador = tokens.get(current); // Guarda operador actual
            current++; // Avanza al siguiente token

            double siguiente = term(); // Evalúa el siguiente término después del operador

            if (operador.getTipo() == TokenType.PLUS) {
                resultado += siguiente;
            } else if (operador.getTipo() == TokenType.MINUS) {
                resultado -= siguiente;
            }
        }
        return resultado;
    }

    /**
     * Método que analiza un término que puede contener multiplicaciones y
     * divisiones.
     * Retorna el resultado evaluado como double.
     */
    private double term() {
        double resultado = factor(); // Analiza el factor inicial

        // Mientras haya un operador * o / (Multiplicacion o División)
        while (!isAtEnd() &&
                (getCurrent().getTipo() == TokenType.ASTERISCO ||
                        getCurrent().getTipo() == TokenType.DIVISION)) {

            Token operador = getCurrent();

            current++; // Avanza al siguiente token (factor derecho)
            // Si es división, verifica división por cero
            if (operador.getTipo() == TokenType.DIVISION) {
                checkDivisionByZero();
            }

            double siguiente = factor();

            if (operador.getTipo() == TokenType.ASTERISCO) {
                resultado *= siguiente;
            } else if (operador.getTipo() == TokenType.DIVISION) {
                if (siguiente == 0) {
                    errores.add(new SyntaxError("División por cero", operador));
                    // Evitar excepción, devolver algún valor
                    return 0;
                }
                resultado /= siguiente;
            }
        }
        return resultado;
    }

    /**
     * Método que analiza un factor.
     * Un factor puede ser ya sea como:
     * - Un número
     * - Un identificador (variable)
     * - Una expresión entre paréntesis
     * Retorna el resultado evaluado como double.
     */
    private double factor() {
        if (isAtEnd()) {
            errores.add(new SyntaxError("Se esperaba un factor", getPrevious()));
            return 0;
        }

        Token token = getCurrent();
        TokenType tipo = token.getTipo();

        if (tipo == TokenType.NUMERO) {
            // Convierte el lexema a número double y devuelve
            try {
                double valor = Double.parseDouble(token.getLexema());
                advance();
                return valor;
            } catch (NumberFormatException e) {
                errores.add(new SyntaxError("Número inválido: " + token.getLexema(), token));
                advance();
                return 0;
            }
        } else if (tipo == TokenType.IDENTIFICADOR) {
            String nombreVar = token.getLexema();
            advance();
            if (!variables.containsKey(nombreVar)) {
                errores.add(new SyntaxError("Variable no definida: " + nombreVar, token));
                return 0;
            }
            return variables.get(nombreVar);
        } else if (tipo == TokenType.PAR_DER) {
            errores.add(new SyntaxError("Paréntesis izquierdo faltante", token));
            return 0;
        } else if (tipo == TokenType.PAR_IZQ) {
            current++;
            double resultado = expression();

            if (current >= tokens.size() || tokens.get(current).getTipo() != TokenType.PAR_DER) {
                errores.add(new SyntaxError("Paréntesis derecho faltante", getPrevious()));
            } else {
                current++;
            }
            return resultado;
        } else {
            errores.add(new SyntaxError("Factor inválido: ", token));
            advance();
            return 0;
        }
    }

    /**
     * Verifica si el siguiente token tras una división es el número 0.
     * Si es así, lanza un error de división por cero.
     */
    private void checkDivisionByZero() {
        if (current < tokens.size()) {
            Token siguiente = tokens.get(current);
            if (siguiente.getTipo() == TokenType.NUMERO) {
                try {
                    double valor = Double.parseDouble(siguiente.getLexema());
                    if (valor == 0.0) {
                        errores.add(new SyntaxError("División por cero", siguiente));
                    }
                } catch (NumberFormatException e) {
                    // No es un número válido, no hacemos nada
                }
            }
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

    public List<String> getResultados() {
        return this.resultados;
    }

    /*
     * Metodo que me sirve para saltar hasta el proximo ; cuando hay un error
     * de manera que no saltaran miles de errores solo por un unico error
     */
    private void saltoSeguro() {
        // Mientras no se haya llegado al final y el token actual no sea un punto y
        // coma, por lo que sigue avanzando
        while (!isAtEnd() && getCurrent().getTipo() != TokenType.SEMICOLON) {
            advance();
        }
        // Si termino el while y aún hay tokens (estamos en el punto y coma). Se avanza
        // una vez más para pasarlo y no quedarse estancado en el
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
        if (current == 0) {
            return tokens.get(0);
        }
        return tokens.get(current - 1);
    }

}
