# README - Balatro Project

## Descripción General

El proyecto **Balatro** es un juego basado en Java que involucra mecánicas de cartas, selección de mazos y varios estados de juego como victoria, derrota y compras en la tienda. Este documento describe la estructura y funcionalidad de los componentes del proyecto, así como el funcionamiento general del juego.

## Cómo Funciona el Juego

El juego Balatro es un juego de cartas donde el jugador debe seleccionar un mazo y enfrentarse a diversas rondas en las que puede ganar o perder dependiendo de sus decisiones y estrategias. A lo largo del juego, el jugador podrá:

- Elegir y modificar su mazo.
- Comprar mejoras en la tienda.
- Jugar rondas enfrentándose a desafíos.
- Alcanzar condiciones de victoria o derrota.

Cada ronda tiene mecánicas específicas de apuestas, efectos de jefe y selección de cartas, lo que permite una experiencia dinámica y estratégica.

## Estructura del Proyecto

El proyecto consta de varios archivos principales en Java y un directorio `components` que contiene clases modulares. A continuación, se presenta un resumen detallado de las clases principales y su funcionalidad.

### **Clases Principales del Juego** (Ubicadas en `balatro/`)

- **`BalatroBlind.java`**: Gestiona las mecánicas de ciegas en el juego, posiblemente determinando restricciones o desafíos adicionales en cada ronda.
- **`BalatroDeckSelection.java`**: Maneja el proceso de selección de mazos para el jugador, permitiéndole elegir su estrategia inicial.
- **`BalatroField.java`**: Representa el campo de juego donde ocurren las acciones, gestionando el estado actual de las cartas en juego.
- **`BalatroGameOver.java`**: Controla la lógica de finalización del juego y define lo que ocurre cuando un jugador pierde, mostrando mensajes y opciones de reinicio.
- **`BalatroShop.java`**: Implementa la funcionalidad de la tienda donde los jugadores pueden comprar cartas o mejoras para su mazo.
- **`BalatroStart.java`**: Punto de entrada principal del juego, inicializando todos los componentes y gestionando el flujo de inicio.
- **`BalatroWin.java`**: Maneja el estado de victoria, determinando cuándo y cómo un jugador gana el juego.

### **Clases de Componentes** (Ubicadas en `balatro/components/`)

- **`Ante.java`**: Define las mecánicas de apuesta en el juego, posiblemente regulando cuánto debe arriesgar el jugador en cada ronda.
- **`BossEffect.java`**: Implementa efectos de jefes, que pueden añadir desafíos únicos a las rondas.
- **`CardSelectionListener.java`**: Proporciona funcionalidad para manejar eventos de selección de cartas en el juego, detectando las elecciones del jugador.

## Dependencias

- El proyecto está escrito en **Java**.
- Puede requerir bibliotecas adicionales para gráficos o mecánicas del juego, las cuales deben verificarse en el archivo `build.gradle`.

## Cómo Ejecutar el Juego

1. Asegúrese de tener Java instalado (se recomienda JDK 8 o posterior).
2. Compile el proyecto con el siguiente comando:
   ```sh
   javac -d bin src/balatro/*.java src/balatro/components/*.java
   ```
3. Ejecute el archivo principal del juego:
   ```sh
   java -cp bin balatro.BalatroStart
   ```

## Mejoras Futuras

- Agregar más documentación dentro del código.
- Mejorar la interfaz gráfica y las animaciones.
- Implementar una funcionalidad para guardar y cargar partidas.
- Añadir más mecánicas de juego para mayor profundidad estratégica.

## Conclusión

Este proyecto proporciona un enfoque estructurado para la implementación de un juego basado en cartas con selección de mazo, estados de juego y mecánicas de tienda. Comprender el papel de cada clase ayudará en el desarrollo y mantenimiento futuro, permitiendo mejoras y expansiones en la jugabilidad.