package compiler;

public class Token {
private final String lexema; // Texto del token (ej: "42", "+")
private final TokenType tipo; // Tipo del token (ej: 4 = TOKEN_NUMERO)
private final int posicion; // Posición en el texto original

public Token(String lexema, TokenType tipo, int posicion) {
    this.lexema = lexema;
    this.tipo = tipo;
    this.posicion = posicion;
}

// Obtenemos el texto del token
public String getLexema() {
    return lexema;
}

// Obtenemos el tipo del token
public TokenType getTipo() {
    return tipo;
}

// Obtenemos la posición del token en el texto original
public int getPosicion() {
    return posicion;
}

@Override
public String toString() {
    return String.format("%-15s | %-15s | Posición: %d", 
        "TOKEN_" + tipo.name(), 
        lexema, 
        posicion);
}

}