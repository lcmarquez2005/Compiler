package compiler;

public enum TokenType {
    NUMERO, // Números (enteros o decimales): 5, 3.14
    PLUS, // Operador suma: +
    MINUS, // Operador resta: -
    ASTERISCO, // Operador multiplicación: *
    DIVISION, // Operador división: /
    PAR_IZQ, // Paréntesis izquierdo: (
    PAR_DER, // Paréntesis derecho: )
    IDENTIFICADOR, // Variables: a, b, x1
    ASIGNACION, // Variables: a, b, x1
    ERROR, // Símbolos no reconocidos: @, #
    SEMICOLON // Signo de terminación ;
}
