package compiler;


import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final Tokenizer tokenizer;
    private final Limpiador limpiador;
    private int linea;

    public Lexer() {
        this.tokenizer = new Tokenizer();
        this.limpiador = new Limpiador();
        this.linea = 0;
    }

    // realmente esta tomando todo el texto en lugar de la linea
    public List<Token> analizar(String archivo) {
        List<Token> tokens = new ArrayList<>();


        for (String linea : archivo.split("\\R")) {
            this.linea++;
            String sinComentarios = limpiador.limpiarLinea(linea);

            // si la linea esta vacia brincala
            if (sinComentarios.isBlank()) continue;

            for (String sentencia : limpiador.dividirSentencias(sinComentarios)) {
                String limpia = limpiador.limpiarEspacios(sentencia);
                tokens.addAll(tokenizer.tokeniza(limpia, this.linea));
            }
        }
        this.linea = 0;
        return tokens;
    }

}
