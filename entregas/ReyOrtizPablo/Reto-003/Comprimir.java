import java.util.Arrays;

public class Comprimir {

    static class Par {
        int numero;
        char letra;

        Par(int p, char c) {
            this.numero = p;
            this.letra = c;
        }

        @Override
        public String toString() {
            return "(" + numero + "," + letra + ")";
        }
    }

    public static Par[] comprimir(String cadena) {
        String[] diccionario = new String[1];
        int[] valores = new int[1];
        Par[] salida = new Par[1];
        
        int valor = 1;
        int contadorSalida = 0;
        int contadorDiccionario = 0;
        String w = "";

        System.out.println("Diccionario inicial: ");

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            String wc = w + c;

            int indice = buscarEnDiccionario(diccionario, contadorDiccionario, wc);

            if (indice == -1) {
                int p = w.isEmpty() ? 0 : buscarEnDiccionario(diccionario, contadorDiccionario, w);
                
                if (contadorSalida >= salida.length) {
                    salida = Arrays.copyOf(salida, salida.length * 2);
                }
                salida[contadorSalida++] = new Par(p, c);

                if (contadorDiccionario >= diccionario.length) {
                    diccionario = Arrays.copyOf(diccionario, diccionario.length * 2);
                    valores = Arrays.copyOf(valores, valores.length * 2);
                }
                
                diccionario[contadorDiccionario] = wc;
                valores[contadorDiccionario] = valor;
                
                System.out.println("Agregado al diccionario: " + wc + " -> " + valor);
                valor++;
                contadorDiccionario++;
                w = "";
            } else {
                w = wc;
            }
        }
        return Arrays.copyOf(salida, contadorSalida);
    }

    public static int buscarEnDiccionario(String[] diccionario, int contador, String secuencia) {
        for (int i = 0; i < contador; i++) {
            if (diccionario[i].equals(secuencia)) {
                return i + 1;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        String cadena = "abababcbdc"; 
        Par[] resultado = comprimir(cadena);

        System.out.println("\nCadena original: " + cadena);
        System.out.println("Compresión resultante: ");
        for (Par p : resultado) {
            System.out.print(p + " ");
        }
        System.out.println();
    }
}