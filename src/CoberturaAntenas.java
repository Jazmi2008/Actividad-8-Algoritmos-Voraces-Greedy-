import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Coloca el mínimo número de antenas para cubrir casas en una carretera recta. */
public class CoberturaAntenas {
    public static class Antena {
        public final long posicion;
        public final List<Long> casasCubiertas = new ArrayList<>();
        public Antena(long posicion) { this.posicion = posicion; }
        public long limiteIzquierdo(long rango) { return Math.subtractExact(posicion, rango); }
        public long limiteDerecho(long rango) { return Math.addExact(posicion, rango); }
    }

    /** Ordena las casas y ubica cada antena en h + R, siendo h la primera casa libre. */
    public static List<Antena> colocarAntenas(long[] casas, long rango) {
        if (casas == null) throw new IllegalArgumentException("La lista de casas no puede ser nula.");
        if (rango < 0) throw new IllegalArgumentException("El rango R debe ser >= 0.");

        long[] orden = Arrays.stream(casas).sorted().distinct().toArray();
        List<Antena> antenas = new ArrayList<>();
        int i = 0;
        while (i < orden.length) {
            long primeraSinCubrir = orden[i];
            long posicion = Math.addExact(primeraSinCubrir, rango);
            long limiteDerecho = Math.addExact(posicion, rango);
            Antena antena = new Antena(posicion);
            while (i < orden.length && orden[i] <= limiteDerecho) {
                antena.casasCubiertas.add(orden[i]);
                i++;
            }
            antenas.add(antena);
        }
        return antenas;
    }

    private static void imprimir(List<Antena> antenas, long rango) {
        System.out.println("Antenas colocadas en:");
        for (Antena antena : antenas) {
            System.out.printf("  %d cubre [%d, %d] -> casas %s%n", antena.posicion,
                    antena.limiteIzquierdo(rango), antena.limiteDerecho(rango), antena.casasCubiertas);
        }
        System.out.println("Cantidad total: " + antenas.size());
    }

    private static void ejecutarEjemplos() {
        System.out.println("=== Ejemplo 1 ===");
        imprimir(colocarAntenas(new long[]{1, 2, 7, 11, 20, 21, 30}, 5), 5);
        System.out.println("\n=== Ejemplo 2 ===");
        imprimir(colocarAntenas(new long[]{2, 4, 8, 15, 18, 22}, 3), 3);
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--ejemplos")) {
            ejecutarEjemplos();
            return;
        }
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Ingrese las posiciones de las casas separadas por espacios:");
            String linea = sc.nextLine().trim();
            String[] partes = linea.replace("[", "").replace("]", "").replace(",", " ").trim().split("\\s+");
            long[] casas;
            if (linea.isEmpty()) casas = new long[0];
            else {
                casas = new long[partes.length];
                for (int i = 0; i < partes.length; i++) casas[i] = Long.parseLong(partes[i]);
            }
            System.out.print("Rango de cobertura R: ");
            long rango = Long.parseLong(sc.nextLine().trim());
            imprimir(colocarAntenas(casas, rango), rango);
        } catch (Exception e) {
            System.err.println("Error en la entrada: " + e.getMessage());
            System.err.println("Formato esperado: [1, 2, 7, 11] y luego el rango R.");
        }
    }
}
