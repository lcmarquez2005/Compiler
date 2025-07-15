package compiler;

public class Token {
private final String lexema;
private final TokenType tipo;
private final int posicion;

public Token(String lexema, TokenType tipo, int posicion) {
    this.lexema = lexema;
    this.tipo = tipo;
    this.posicion = posicion;
}

public String getLexema() {
    return lexema;
}

public TokenType getTipo() {
    return tipo;
}

public int getPosicion() {
    return posicion;
}

@Override
public String toString() {
    return String.format("%-10s | %-10s | Posición: %d", lexema, tipo, posicion);
}

}