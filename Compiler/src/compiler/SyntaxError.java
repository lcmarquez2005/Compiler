package compiler;

  /**
   * Almacena el mensaje de error, el número de línea y columna donde ocurrió.
      */

public class SyntaxError extends Exception {
    private final Token token;
    
    public SyntaxError(String message, Token token) {
        super(message);
        this.token = token;
    }
    
    public Token getToken() {
        return token;
    }
}