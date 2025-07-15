package compiler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();

        System.out.println("Analizador léxico básico");
        System.out.println("Escribe una expresión y presiona Enter. Escribe 'salir' para terminar.");

        while (true) {
            System.out.print("> ");
            String expresion = scanner.nextLine();

            if (expresion.equalsIgnoreCase("salir")) {
                System.out.println("Programa finalizado.");
                break;
            }

            lexer.analizar(expresion);
            System.out.println(); // Espacio entre expresiones
        }

        scanner.close();
    }
}
