package compiler;

/*
 * Este enum contiene los tipos de expresiones permitidas
 * en este momento solo habran expresiones aritmeticas y algebraicas
 * tambien se define las mixtas para cuando se juntan estas y se marque el error
 */
public enum ExprType {
    ARITHMETIC, // sólo literales numéricas
    ALGEBRAIC, // sólo identificadores
    MIXED // mezcla inválida
}
