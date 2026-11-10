import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** Pruebas de casos normales, bordes y comparaciones aleatorias pequeñas. */
public class Pruebas {
    private static int pruebas = 0;

    private static void comprobar(boolean condicion, String mensaje) {
        pruebas++;
        if (!condicion) throw new AssertionError("Prueba " + pruebas + " falló: " + mensaje);
    }

    private static boolean cerca(double a, double b) { return Math.abs(a - b) < 1e-7; }

    private static double valorOptimoMochila(List<MochilaFraccional.Objeto> objetos, double capacidad) {
        List<MochilaFraccional.Objeto> copia = new ArrayList<>(objetos);
        copia.sort((a, b) -> Double.compare(b.razon(), a.razon()));
        double total = 0;
        for (MochilaFraccional.Objeto o : copia) {
            if (o.peso == 0) { if (o.valor > 0) total += o.valor; continue; }
            double tomado = Math.min(capacidad, o.peso);
            total += o.valor * tomado / o.peso;
            capacidad -= tomado;
            if (capacidad <= 1e-9) break;
        }
        return total;
    }

    // Oráculo pequeño: prueba posiciones enteras posibles y busca el mínimo número de antenas.
    private static int minimoAntenasFuerzaBruta(long[] casas, long r, int indice, int usadas) {
        if (indice >= casas.length) return usadas;
        int mejor = Integer.MAX_VALUE;
        long h = casas[indice];
        for (long x = h - r; x <= h + r; x++) {
            long limite = x + r;
            int siguiente = indice;
            while (siguiente < casas.length && casas[siguiente] <= limite) siguiente++;
            mejor = Math.min(mejor, minimoAntenasFuerzaBruta(casas, r, siguiente, usadas + 1));
        }
        return mejor;
    }

    public static void main(String[] args) {
        // 15 comprobaciones deterministas de mochila.
        List<MochilaFraccional.Objeto> a = List.of(new MochilaFraccional.Objeto("A",60,10), new MochilaFraccional.Objeto("B",100,20), new MochilaFraccional.Objeto("C",120,30));
        comprobar(cerca(MochilaFraccional.resolver(a,50).valorTotal,240), "mochila ejemplo 1");
        List<MochilaFraccional.Objeto> b = List.of(new MochilaFraccional.Objeto("A",80,20), new MochilaFraccional.Objeto("B",100,10), new MochilaFraccional.Objeto("C",120,30));
        comprobar(cerca(MochilaFraccional.resolver(b,25).valorTotal,160), "mochila ejemplo 2");
        comprobar(MochilaFraccional.resolver(a,0).seleccionados.isEmpty(), "capacidad cero");
        comprobar(cerca(MochilaFraccional.resolver(List.of(),10).valorTotal,0), "lista vacía");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",10,5),new MochilaFraccional.Objeto("B",20,5)),100).valorTotal,30), "capacidad mayor");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",90,30)),10).valorTotal,30), "fracción única");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",10,5),new MochilaFraccional.Objeto("B",20,10)),10).valorTotal,20), "empate de razón");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("G",7,0)),0).valorTotal,7), "peso cero positivo");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",10,3)),1.5).valorTotal,5), "capacidad decimal");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",0,2)),10).valorTotal,0), "valor cero");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",5,2)),2).pesoUsado,2), "peso usado");
        comprobar(MochilaFraccional.resolver(a,50).seleccionados.size()==3, "tres selecciones");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",8,0),new MochilaFraccional.Objeto("B",4,2)),2).valorTotal,12), "objeto gratis y objeto normal");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",4,2)),10).valorTotal,4), "objeto completo");
        comprobar(cerca(MochilaFraccional.resolver(List.of(new MochilaFraccional.Objeto("A",4,2)),1).valorTotal,2), "media unidad de peso");

        // 14 comprobaciones deterministas de antenas.
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{1,2,7,11,20,21,30},5).size()==2, "antenas ejemplo 1");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{2,4,8,15,18,22},3).size()==3, "antenas ejemplo 2");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{},4).isEmpty(), "sin casas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{10},3).get(0).posicion==13, "una casa");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{3,1,2,2},0).size()==3, "rango cero y repetidas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{30,1,21,2,7,11,20,1},5).size()==2, "desordenadas y repetidas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{-10,-8,-1,0},2).size()==2, "posiciones negativas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{0,6},3).size()==1, "distancia exacta 2R");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{0,7},3).size()==2, "distancia 2R+1");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{1,2,3},1).get(0).posicion==2, "primera antena h+R");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{5,5,5},2).size()==1, "todas las casas repetidas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{-5,5},5).size()==1, "casas a distancia 2R");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{1,100},2).size()==2, "casas alejadas");
        comprobar(CoberturaAntenas.colocarAntenas(new long[]{7,3,6,1},3).size()==1, "casas dentro del mismo alcance");

        Random random = new Random(2026);
        for (int caso = 0; caso < 500; caso++) {
            int n = random.nextInt(7);
            double capacidad = random.nextInt(20) + random.nextDouble();
            List<MochilaFraccional.Objeto> objetos = new ArrayList<>();
            for (int i = 0; i < n; i++) objetos.add(new MochilaFraccional.Objeto("O"+i, random.nextInt(30), random.nextInt(10)+1));
            double obtenido = MochilaFraccional.resolver(objetos, capacidad).valorTotal;
            if (!cerca(obtenido, valorOptimoMochila(objetos, capacidad))) throw new AssertionError("Falló caso aleatorio de mochila " + caso);
        }
        for (int caso = 0; caso < 300; caso++) {
            int n = random.nextInt(7) + 1;
            long r = random.nextInt(4);
            long[] casas = new long[n];
            for (int i = 0; i < n; i++) casas[i] = random.nextInt(13) - 6;
            casas = Arrays.stream(casas).distinct().sorted().toArray();
            int greedy = CoberturaAntenas.colocarAntenas(casas, r).size();
            int optimo = minimoAntenasFuerzaBruta(casas, r, 0, 0);
            if (greedy != optimo) throw new AssertionError("Falló caso aleatorio de antenas " + caso);
        }

        System.out.println("Resultado: " + pruebas + " pruebas deterministas correctas, 0 con fallo.");
        System.out.println("Validación adicional: 500 casos aleatorios de mochila y 300 de antenas correctos.");
    }
}
