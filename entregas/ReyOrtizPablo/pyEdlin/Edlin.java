package entregas.ReyOrtizPablo.pyEdlin;

import java.util.*;

class Edlin {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int[] activeLine = { 1 };
        List<String> document = new ArrayList<>(Arrays.asList(
                "Bienvenidos al editor EDLIN",
                "Utilice el menu inferior para editar el texto",
                "------",
                "[L] permite definir la linea activa",
                "[E] permite editar la linea activa",
                "[I] permite intercambiar dos lineas",
                "[B] borra el contenido de la linea activa",
                "[U] deshacer la última acción",
                "[R] rehacer la última acción",
                "[C] copiar la línea activa",
                "[P] pegar en otra línea",
                "[S] sale del programa"));

        Stack<List<String>> history = new Stack<>();
        Stack<List<String>> redoStack = new Stack<>();
        saveState(history, document);
        String copiedLine = null;

        do {
            print(document, activeLine);
        } while (processActions(document, activeLine, history, redoStack, copiedLine));
    }

    // --- MÉTODOS DE ENTRADA CORREGIDOS ---
    static char askChar() {
        return input.next().charAt(0);
    }

    static int askInt() {
        return input.nextInt();
    }

    static String askString() {
        input.nextLine(); // Limpiar el buffer de saltos de línea anteriores
        return input.nextLine();
    }

    // ... (El resto del código permanece igual que tu original)
    static void print(List<String> document, int[] activeLine) {
        clearScreen();
        printHorizontalLine();
        for (int line = 0; line < document.size(); line++) {
            System.out.println(line + separator(line, activeLine[0]) + document.get(line));
        }
        printHorizontalLine();
    }

    static String separator(int line, int activeLine) {
        return line == activeLine ? ":*| " : ": | ";
    }

    static void printHorizontalLine() {
        System.out.println("-".repeat(50));
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static boolean processActions(List<String> document, int[] activeLine, Stack<List<String>> history,
            Stack<List<String>> redoStack, String copiedLine) {
        System.out.println("Comandos: [L]inea | [E]ditar | [I]ntercambiar | [B]orrar | [C]opiar | [P]egar | [D]eshacer | [R]ehacer | [S]alir");

        switch (askChar()) {
            case 'S': case 's': return false;
            case 'L': case 'l': saveState(history, document); redoStack.clear(); setActiveLine(document, activeLine); break;
            case 'E': case 'e': saveState(history, document); redoStack.clear(); edit(document, activeLine); break;
            case 'I': case 'i': saveState(history, document); redoStack.clear(); exchangeLines(document); break;
            case 'B': case 'b': saveState(history, document); redoStack.clear(); delete(document, activeLine); break;
            case 'C': case 'c': copiedLine = copy(document, activeLine); break;
            case 'P': case 'p': saveState(history, document); redoStack.clear(); paste(document, copiedLine); break;
            case 'D': case 'd': undo(history, redoStack, document); break;
            case 'R': case 'r': redo(redoStack, history, document); break;
        }
        return true;
    }

    static void delete(List<String> document, int[] activeLine) {
        System.out.println("Confirme línea activa para borrar [" + activeLine[0] + "]:");
        if (askInt() == activeLine[0]) {
            document.set(activeLine[0], "");
        }
    }

    static void exchangeLines(List<String> document) {
        System.out.print("Línea origen: ");
        int origin = askInt();
        System.out.print("Línea destino: ");
        int dest = askInt();
        if (origin >= 0 && origin < document.size() && dest >= 0 && dest < document.size()) {
            String temp = document.get(dest);
            document.set(dest, document.get(origin));
            document.set(origin, temp);
        }
    }

    static void edit(List<String> document, int[] activeLine) {
        System.out.println("EDITANDO> " + document.get(activeLine[0]));
        document.set(activeLine[0], askString());
    }

    static void setActiveLine(List<String> document, int[] activeLine) {
        System.out.print("Nueva línea activa: ");
        int nova = askInt();
        if (nova >= 0 && nova < document.size()) activeLine[0] = nova;
    }

    static void saveState(Stack<List<String>> history, List<String> document) {
        history.push(new ArrayList<>(document));
    }

    static String copy(List<String> document, int[] activeLine) {
        return document.get(activeLine[0]);
    }

    static void paste(List<String> document, String copiedLine) {
        if (copiedLine == null) return;
        System.out.print("Línea para pegar: ");
        int dest = askInt();
        if (dest >= 0 && dest < document.size()) document.set(dest, copiedLine);
    }

    static void undo(Stack<List<String>> history, Stack<List<String>> redoStack, List<String> document) {
        if (!history.isEmpty()) {
            redoStack.push(new ArrayList<>(document));
            List<String> prev = history.pop();
            document.clear();
            document.addAll(prev);
        }
    }

    static void redo(Stack<List<String>> redoStack, Stack<List<String>> history, List<String> document) {
        if (!redoStack.isEmpty()) {
            history.push(new ArrayList<>(document));
            List<String> next = redoStack.pop();
            document.clear();
            document.addAll(next);
        }
    }
}