package compiler;

/**
 * Clase que representa un error de sintaxis durante el análisis.
 * 
 * Extiende Exception para poder lanzar y capturar estos errores específicos
 * en el parser cuando se detecta una violación de reglas sintácticas.
 */
public class SyntaxError {
    // Token que causó el error, contiene lexema, tipo, línea y columna
    private final Token token;
    private String message;
    private Token token2;

    // private int lineaError;

    /**
     * Constructor que inicializa el mensaje del error y el token asociado.
     * 
     * @param message Mensaje descriptivo del error sintáctico
     * @param token   Token donde se detectó el error (Aun que la ubuicacion falta
     *                ponerla correctamente)
     */
    public SyntaxError(String message, Token token) {
        this.message = message;
        this.token = token;
    }

    public SyntaxError(String message, Token token, Token token2) {
        this.message = message;
        this.token = token;
        this.token2 = token2;
    }

    /**
     * Devuelve el token asociado al error.
     * Esto permite obtener detalles como la línea y columna del error
     * para mostrar mensajes precisos al usuario.
     * 
     * @return Token que causó el error
     */
    public Token getToken() {
        return token;
    }

    public String getError() {
        if (this.token != null) {
            if (this.token2 == null) {
                return "Error (Linea " + token.getLinea() + ") -> " + message + " Token: " + token.getLexema();
            }
            return "Error en Linea " + token.getLinea() + message + " Token: " + token.getLexema() + ", Token2: "
                    + token.getLexema();
        }
        return "Error:" + message;

    }
}
