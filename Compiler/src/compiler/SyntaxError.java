package compiler;

/**
 * Clase que representa un error de sintaxis durante el análisis.
 * 
 * Extiende Exception para poder lanzar y capturar estos errores específicos
 * en el parser cuando se detecta una violación de reglas sintácticas.
 */
public class SyntaxError extends Exception {
    // Token que causó el error, contiene lexema, tipo, línea y columna
    private final Token token;
    
    /**
     * Constructor que inicializa el mensaje del error y el token asociado.
     * 
     * @param message Mensaje descriptivo del error sintáctico
     * @param token Token donde se detectó el error (Aun que la ubuicacion falta ponerla correctamente)
     */
    public SyntaxError(String message, Token token) {
        super(message);
        this.token = token;
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
}
