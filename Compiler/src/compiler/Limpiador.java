package compiler;
public class Limpiador {

    private boolean dentroComentario = false;

    public String limpiarLinea(String linea) {
        if (dentroComentario) {
            if (linea.contains("*/")) {
                linea = linea.substring(linea.indexOf("*/") + 2);
                dentroComentario = false;
                return limpiarLinea(linea);
            }
            return "";
        }

        // Eliminar comentarios de línea
        if (linea.contains("//")) {
            linea = linea.substring(0, linea.indexOf("//"));
        }

        // Comentarios /* ... */
        while (linea.contains("/*") && linea.contains("*/") && linea.indexOf("/*") < linea.indexOf("*/")) {
            int start = linea.indexOf("/*");
            int end = linea.indexOf("*/") + 2;
            linea = linea.substring(0, start) + linea.substring(end);
        }

        // Comentario abierto sin cierre
        if (linea.contains("/*")) {
            linea = linea.substring(0, linea.indexOf("/*"));
            dentroComentario = true;
        }

        return linea;
    }

    public String[] dividirSentencias(String linea) {
        return linea.trim().split("\\s*;\\s*"); // Puedes cambiar el separador aquí
    }

    public String limpiarEspacios(String cadena) {
        return cadena.replaceAll("[ \t\r\n]", "");
    }
}
