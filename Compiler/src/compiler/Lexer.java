package compiler;

import java.util.List;

public class Lexer {
    private final Tokenizer tokenizer;
    private final Limpiador limpiador;
    private static final char SEPARADOR = ';';

    boolean[] dentroComentario = new boolean[] { false };

    public Lexer() {
        this.tokenizer = new Tokenizer();
        this.limpiador = new Limpiador();
    }

    private String[] dividirPorSeparador(String linea) {
        return linea.trim().split("\\s*" + SEPARADOR + "\\s*");
    }


    public List<Token> analizar(String linea) {
        String lineaSinComentarios = limpiador.limpiarComentarios(linea, dentroComentario); 
        String[] cadenas = dividirPorSeparador(lineaSinComentarios);
        int columnaActual = 1;
        List<Token> tokens = tokenizer.analizar(lineaSinComentarios);
            for (String cadena : cadenas) {
                String lexema = limpiador.limpiarEspaciosTabsSaltos(cadena);
                // analizar cadena por cadena  de esta forma tambien puede ser toda la linea, pero habria que cambiar el dividir por serparador
            }
        imprimirTokens(tokens);
        return tokens;
    }

    private void imprimirTokens(List<Token> tokens) {
        System.out.println("Lexema     | Tipo       | Posición");
        System.out.println("-----------|------------|---------");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}