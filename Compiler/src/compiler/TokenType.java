package compiler;

public enum TokenType {
    TOKEN_NUMERO,       // Números (enteros o decimales): 5, 3.14
    TOKEN_PLUS,         // Operador suma: +
    TOKEN_MINUS,        // Operador resta: -
    TOKEN_ASTERISCO,    // Operador multiplicación: *
    TOKEN_DIVISION,     // Operador división: /
    TOKEN_PAR_IZQ,      // Paréntesis izquierdo: (
    TOKEN_PAR_DER,      // Paréntesis derecho: )
    TOKEN_IDENTIFICADOR, // Variables: a, b, x1
    TOKEN_IGUAL, // Variables: a, b, x1
    TOKEN_ERROR         // Símbolos no reconocidos: @, #
}