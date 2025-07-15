package compiler;
public class Limpiador {

    // comentario multilínea
    public static String limpiarComentarios(String linea, boolean[] dentroComentario) {
        if (dentroComentario[0]) {
            // Si estamos dentro de un comentario multilínea
            if (linea.contains("*/")) {
                linea = linea.substring(linea.indexOf("*/") + 1);
                dentroComentario[0] = false;
                // Puede quedar texto después, continuar limpiando en esta misma línea
                linea = limpiarComentarios(linea, dentroComentario);
            } else {
                // Toda la línea está dentro del comentario, se elimina
                return "";
            }
        } else {
            // Si no estamos dentro de comentario multilínea

            // Quitar comentarios de línea simples "//"
            if (linea.contains("//")) {
                linea = linea.substring(0, linea.indexOf("//"));
            }

            // Quitar comentarios en bloque que están en la misma línea /* ... */
            while (linea.contains("/*") && linea.contains("*/") && linea.indexOf("/*") < linea.indexOf("*/")) {
                int inicio = linea.indexOf("/*");
                int fin = linea.indexOf("*/") + 2;
                linea = linea.substring(0, inicio) + linea.substring(fin);
            }

            // Si empieza comentario multilínea sin cierre en esta línea
            if (linea.contains("/*") && !linea.contains("*/")) {
                linea = linea.substring(0, linea.indexOf("/*"));
                dentroComentario[0] = true;
            }
        }
        return linea;
    }

    public static String limpiarEspaciosTabsSaltos(String linea) {
        return linea.replaceAll("[ \t\r\n]", "");
    }
}
