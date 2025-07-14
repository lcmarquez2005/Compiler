package compiler;

import java.util.*;

/*Mesale errores jajaja -Att Alison */
// Debe tener expresiones aritmeticas y algebraicaso
// 5+3
// a+b
//  Ejemplo recomendado:
// TOKEN	LEXEMA
// TOKEN_NUMERO	38
// TOKEN_PLUS	+
// TOKEN_ASTERISCO	*
// TOKEN_PAR_IZQ	(
// TOKEN_PAR_DER	)
public class LexicoBasico {
    static String linea = "38+2*3/6 2+2 (28+7)*2";
    // Usando delimitadores y regresandolos como tokens
    static StringTokenizer tokens3 = new StringTokenizer(linea, "\\d+|[+\\-*/=]|[(){}\\[\\],;]", true);
    static final String NUMERO = "NUMERO";
    static final String OPERADOR = "OPERADOR";
    static final String PUNTUADOR = "PUNTUADOR";// serian (){}[] , ;
    static String lexema = "";

    public static String lexico() {
        if (tokens3.hasMoreTokens())
            lexema = tokens3.nextToken().trim();
        else
            lexema = "";

        if (lexema.matches("\\+|\\-|\\*|/"))
            return OPERADOR;
        else  
        if (lexema.matches("\\(|\\)|\\[|\\]|\\{|\\}|\\|")) 
            return PUNTUADOR;
        else
            return NUMERO;
    }

    public static void main(String args[]) {
        // Separación de tokens con StringTokenizer usando el espacio por defecto como
        // separador de tokens
        System.out.println("Tokens");
        StringTokenizer tokens = new StringTokenizer(linea);
        while (tokens.hasMoreTokens())
            System.out.println(tokens.nextToken());

        System.out.println();

        // Usando delimitadores
        StringTokenizer tokens2 = new StringTokenizer(linea, "+=");
        while (tokens2.hasMoreTokens())
            System.out.println(tokens2.nextToken().trim());

        System.out.println();

        String token = lexico();
        System.out.printf("%-12s| %-12s\n", "Token", "Lexema");
        System.out.println("----------------------------");

        while (!lexema.equals("")) {
            System.out.printf("%-12s| %-12s\n", token, lexema);
            token = lexico();
        }

        // System.out.println("Numeros:" + NUMERO);
        // System.out.println("Operadores" + OPERADOR);
        // System.out.println("Puntuadores" + PUNTUADOR);
    }
}
