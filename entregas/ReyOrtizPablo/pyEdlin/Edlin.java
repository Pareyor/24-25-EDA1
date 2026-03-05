package entregas.ReyOrtizPablo.pyEdlin;

import java.util.*;

class Edlin {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int[] activeLine = { 0 };
        // CORRECCIÓN 2: Documento como Array para ser consistente con la clase History
        String[] document = {
                "Bienvenidos al editor EDLIN",
                "Utilice el menu inferior para editar el texto",
                "------",
                "[L] permite definir la linea activa",
                "[E] permite editar la linea activa",
                "[I] permite intercambiar dos lineas",
                "[B] borra el contenido de la linea activa",
                "[D] deshacer la última acción",
                "[R] rehacer la última acción",
                "[C] copiar la línea activa",
                "[P] pegar en otra línea",
                "[S] sale del programa"};

        // Uso consistente de tu clase History
        History history = new History(10, document.length);
        History redoStack = new History(10, document.length);
        String copiedLine = null;

        while (true) {
            print(document, activeLine);
            System.out.println("Comandos: [L]inea | [E]ditar | [I]ntercambiar | [B]orrar | [C]opiar | [P]egar | [D]eshacer | [R]ehacer | [S]alir");
            char op = Character.toUpperCase(askChar());

            if (op == 'S') break;

            // Guardar estado antes de cambios (Consistencia en flujo)
            if ("LEIBP".indexOf(op) != -1) {
                history.push(document);
                // Al hacer una acción nueva, el redo se limpia por estándar
            }

            switch (op) {
                case 'L': setActiveLine(document, activeLine); break;
                case 'E': edit(document, activeLine); break;
                case 'I': exchangeLines(document); break;
                case 'B': delete(document, activeLine); break;
                case 'C': copiedLine = document[activeLine[0]]; break;
                case 'P': paste(document, copiedLine); break;
                case 'D': 
                    String[] prev = history.pop();
                    if (prev != null) {
                        redoStack.push(document);
                        document = prev;
                    }
                    break;
                case 'R':
                    String[] next = redoStack.pop();
                    if (next != null) {
                        history.push(document);
                        document = next;
                    }
                    break;
            }
        }
    }

    static void print(String[] doc, int[] active) {
        System.out.println("-".repeat(50));
        for (int i = 0; i < doc.length; i++) {
            System.out.println(i + (i == active[0] ? ":*| " : ": | ") + doc[i]);
        }
        System.out.println("-".repeat(50));
    }

    static char askChar() { return input.next().charAt(0); }
    static int askInt() { return input.nextInt(); }
    static String askString() { input.nextLine(); return input.nextLine(); }

    static void edit(String[] doc, int[] active) {
        System.out.println("EDITANDO> " + doc[active[0]]);
        doc[active[0]] = askString();
    }

    static void setActiveLine(String[] doc, int[] active) {
        System.out.print("Línea: ");
        int n = askInt();
        if (n >= 0 && n < doc.length) active[0] = n;
    }

    static void exchangeLines(String[] doc) {
        System.out.print("Línea 1: "); int l1 = askInt();
        System.out.print("Línea 2: "); int l2 = askInt();
        if (l1 >= 0 && l1 < doc.length && l2 >= 0 && l2 < doc.length) {
            String t = doc[l1]; doc[l1] = doc[l2]; doc[l2] = t;
        }
    }

    static void delete(String[] doc, int[] active) {
        System.out.print("Confirme nº línea [" + active[0] + "]: ");
        if (askInt() == active[0]) doc[active[0]] = "";
    }

    static void paste(String[] doc, String copied) {
        if (copied == null) return;
        System.out.print("Pegar en: ");
        int n = askInt();
        if (n >= 0 && n < doc.length) doc[n] = copied;
    }
}