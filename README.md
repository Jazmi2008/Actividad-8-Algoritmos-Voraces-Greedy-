# Actividad 8: Algoritmos Voraces (Greedy)

Implementación en **Java** de los dos problemas de la actividad:

1. **Mochila fraccional:** maximiza el valor total usando el criterio valor/peso.
2. **Cobertura de antenas:** minimiza la cantidad de antenas colocando cada una en `h + R`, donde `h` es la primera casa todavía no cubierta.

## Requisitos

- Java JDK 11 o superior.
- Terminal o consola de comandos.

## Estructura

```text
Actividad8_Greedy/
├── src/
│   ├── MochilaFraccional.java
│   ├── CoberturaAntenas.java
│   └── Pruebas.java
├── ejemplos/
│   ├── mochila_ejemplo1.txt
│   ├── mochila_ejemplo2.txt
│   ├── antenas_ejemplo1.txt
│   └── antenas_ejemplo2.txt
├── docs/
└── README.md
```

## Compilar

Desde la carpeta principal del proyecto:

```bash
mkdir out
javac -d out src/*.java
```

En Windows PowerShell también se pueden ejecutar esos comandos. Si la carpeta `out` ya existe, no es necesario volver a crearla.

## Ejecutar los ejemplos incluidos

```bash
java -cp out MochilaFraccional --ejemplos
java -cp out CoberturaAntenas --ejemplos
```

## Ingresar datos propios

### Mochila fraccional

```bash
java -cp out MochilaFraccional
```

El programa solicita:

1. Capacidad máxima de la mochila.
2. Cantidad de objetos.
3. Una línea por objeto con el formato `nombre valor peso`.

Ejemplo de entrada:

```text
50
3
A 60 10
B 100 20
C 120 30
```

### Cobertura de antenas

```bash
java -cp out CoberturaAntenas
```

El programa solicita las posiciones de las casas y luego el rango `R`. Se aceptan posiciones separadas por espacios o comas, con o sin corchetes.

Ejemplo:

```text
[1, 2, 7, 11, 20, 21, 30]
5
```

## Ejecutar las pruebas

```bash
java -cp out Pruebas
```

El programa realiza comprobaciones de casos normales y casos límite, además de comparar 500 casos aleatorios de mochila y 300 casos aleatorios de antenas contra métodos de referencia para entradas pequeñas.

## Estrategias y complejidad

- **Mochila fraccional:** se ordenan los objetos por razón valor/peso, de mayor a menor. Después se recorren una sola vez. Tiempo: `O(n log n)` por el ordenamiento; espacio: `O(n)` por las copias y selecciones.
- **Cobertura de antenas:** se ordenan las casas y se eliminan duplicados. Para la primera casa no cubierta `h`, se coloca una antena en `h + R` y se avanza hasta la última casa que alcanza. Tiempo: `O(n log n)` por el ordenamiento; espacio: `O(n)`.

## Notas

- En la mochila se permiten fracciones de objetos. Los valores y pesos deben ser finitos y no negativos; la capacidad también debe ser finita y no negativa.
- En cobertura, las posiciones y el rango son números enteros (`long`) y `R` no puede ser negativo.
- Los programas no modifican los arreglos o listas de entrada recibidos por sus métodos principales.
