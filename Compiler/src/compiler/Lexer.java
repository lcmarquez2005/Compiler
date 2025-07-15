package compiler;

import java.util.List;

public class Lexer {
    private final Tokenizer tokenizer;

    public Lexer() {
        this.tokenizer = new Tokenizer();
    }

    public List<Token> analizar(String linea) {
        List<Token> tokens = tokenizer.analizar(linea);
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