package Compiler;
import java.util.*;

public class LexicoBasico
{
    static String linea = "38+2*3/6";
    //Usando delimitadores y regresandolos como tokens
    static StringTokenizer tokens3 = new StringTokenizer(linea, "+-*/=", true);
    static final int NUMERO=0;
    static final int OPERADOR=1;
    static String lexema="";

    public static int lexico()
    {
        if (tokens3.hasMoreTokens())
            lexema=tokens3.nextToken().trim();
        else
            lexema="";
        
        if (lexema.equals("+") || lexema.equals("-") || lexema.equals("*") || lexema.equals("/"))
            return OPERADOR;
        else
            return NUMERO;
    }

    public static void main(String args[])
    {
        // Separación de tokens con StringTokenizer usando el espacio por defecto como separador de tokens
        StringTokenizer tokens = new StringTokenizer(linea);
        while(tokens.hasMoreTokens())
            System.out.println(tokens.nextToken());

        System.out.println();
        
        //Usando delimitadores
        StringTokenizer tokens2 = new StringTokenizer(linea, "+=");
        while(tokens2.hasMoreTokens())
            System.out.println(tokens2.nextToken().trim());

        System.out.println();

        int token = lexico();
        do
        {
            System.out.println(token+" "+lexema);
            token = lexico();
        }
        while (!lexema.equals(""));
    }
}
