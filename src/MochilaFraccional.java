import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Resuelve el problema de la mochila fraccional mediante Greedy. */
public class MochilaFraccional {
    private static final double EPS = 1e-9;

    public static class Objeto {
        public final String nombre;
        public final double valor;
        public final double peso;

        public Objeto(String nombre, double valor, double peso) {
            if (nombre == null || nombre.isBlank()) {
                throw new IllegalArgumentException("El nombre del objeto no puede estar vacío.");
            }
            if (!Double.isFinite(valor) || valor < 0) {
                throw new IllegalArgumentException("El valor debe ser un número finito >= 0.");
            }
            if (!Double.isFinite(peso) || peso < 0) {
                throw new IllegalArgumentException("El peso debe ser un número finito >= 0.");
            }
            this.nombre = nombre;
            this.valor = valor;
            this.peso = peso;
        }

        public double razon() {
            if (peso == 0) return valor > 0 ? Double.POSITIVE_INFINITY : 0;
            return valor / peso;
        }
    }

    public static class Seleccion {
        public final Objeto objeto;
        public final double pesoTomado;

        public Seleccion(Objeto objeto, double pesoTomado) {
            this.objeto = objeto;
            this.pesoTomado = pesoTomado;
        }

        public double valorAportado() {
            if (objeto.peso == 0) return objeto.valor;
            return objeto.valor * (pesoTomado / objeto.peso);
        }

        public boolean esCompleto() {
            return Math.abs(pesoTomado - objeto.peso) <= EPS;
        }
    }

    public static class Resultado {
        public final List<Seleccion> seleccionados;
        public final double valorTotal;
        public final double pesoUsado;

        public Resultado(List<Seleccion> seleccionados, double valorTotal, double pesoUsado) {
            this.seleccionados = seleccionados;
            this.valorTotal = valorTotal;
            this.pesoUsado = pesoUsado;
        }
    }

    /** Ordena por valor/peso y toma primero los objetos con mejor rendimiento. */
    public static Resultado resolver(List<Objeto> objetos, double capacidad) {
        if (objetos == null) throw new IllegalArgumentException("La lista de objetos no puede ser nula.");
        if (!Double.isFinite(capacidad) || capacidad < 0) {
            throw new IllegalArgumentException("La capacidad debe ser un número finito >= 0.");
        }

        List<Objeto> orden = new ArrayList<>(objetos); // No modifica la lista recibida.
        orden.sort(Comparator.comparingDouble(Objeto::razon).reversed());

        List<Seleccion> seleccionados = new ArrayList<>();
        double restante = capacidad;
        double valorTotal = 0;
        double pesoUsado = 0;

        for (Objeto objeto : orden) {
            if (objeto.razon() <= 0) break; // Los objetos restantes no aportan valor.

            if (objeto.peso == 0) {
                seleccionados.add(new Seleccion(objeto, 0));
                valorTotal += objeto.valor;
            } else if (objeto.peso <= restante + EPS) {
                seleccionados.add(new Seleccion(objeto, objeto.peso));
                valorTotal += objeto.valor;
                pesoUsado += objeto.peso;
                restante = Math.max(0, restante - objeto.peso);
            } else if (restante > EPS) {
                seleccionados.add(new Seleccion(objeto, restante));
                valorTotal += objeto.valor * (restante / objeto.peso);
                pesoUsado += restante;
                restante = 0;
                break;
            } else {
                break;
            }
        }
        return new Resultado(seleccionados, valorTotal, pesoUsado);
    }

    private static void imprimir(Resultado resultado, double capacidad) {
        System.out.println("Objetos seleccionados:");
        if (resultado.seleccionados.isEmpty()) System.out.println("(ninguno)");
        for (Seleccion s : resultado.seleccionados) {
            if (s.objeto.peso == 0) {
                System.out.printf(Locale.US, "  %s completo (peso 0, valor %.2f)%n", s.objeto.nombre, s.objeto.valor);
            } else if (s.esCompleto()) {
                System.out.printf(Locale.US, "  %s completo (peso %.2f, valor %.2f)%n",
                        s.objeto.nombre, s.pesoTomado, s.valorAportado());
            } else {
                System.out.printf(Locale.US, "  %.2f/%.2f parte del objeto %s (valor aportado %.2f)%n",
                        s.pesoTomado, s.objeto.peso, s.objeto.nombre, s.valorAportado());
            }
        }
        System.out.printf(Locale.US, "Peso usado: %.2f de %.2f%n", resultado.pesoUsado, capacidad);
        System.out.printf(Locale.US, "Valor total obtenido: %.2f%n", resultado.valorTotal);
    }

    private static void ejecutarEjemplos() {
        List<Objeto> primero = List.of(new Objeto("A", 60, 10), new Objeto("B", 100, 20), new Objeto("C", 120, 30));
        System.out.println("=== Ejemplo 1 ==="); imprimir(resolver(primero, 50), 50);
        List<Objeto> segundo = List.of(new Objeto("A", 80, 20), new Objeto("B", 100, 10), new Objeto("C", 120, 30));
        System.out.println("\n=== Ejemplo 2 ==="); imprimir(resolver(segundo, 25), 25);
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--ejemplos")) {
            ejecutarEjemplos();
            return;
        }
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Capacidad de la mochila: ");
            double capacidad = sc.nextDouble();
            System.out.print("Cantidad de objetos: ");
            int n = sc.nextInt();
            if (n < 0) throw new IllegalArgumentException("La cantidad de objetos no puede ser negativa.");
            List<Objeto> objetos = new ArrayList<>();
            System.out.println("Ingrese cada objeto como: nombre valor peso");
            for (int i = 0; i < n; i++) {
                objetos.add(new Objeto(sc.next(), sc.nextDouble(), sc.nextDouble()));
            }
            imprimir(resolver(objetos, capacidad), capacidad);
        } catch (Exception e) {
            System.err.println("Error en la entrada: " + e.getMessage());
            System.err.println("Formato esperado: capacidad, cantidad y luego nombre valor peso por objeto.");
        }
    }
}
