# Truco Java - Aplicación Cliente/Servidor

Este es un proyecto de una aplicación de Truco en Java que utiliza RMI (Remote Method Invocation) para la comunicación entre cliente y servidor.

## Requisitos

- **Java 8 o superior** debe estar instalado en tu máquina.

## Instrucciones para ejecutar

### 1. Ejecutar el servidor

Primero, debes iniciar el servidor. Esto se hace ejecutando el siguiente comando:

```bash
java -jar TrucoServer.jar
Esto pondrá en marcha el servidor de Truco, el cual estará a la espera de conexiones de los clientes.

### 2. Ejecutar los clientes
Una vez que el servidor esté corriendo, puedes iniciar los clientes. Cada cliente debe conectarse a un puerto diferente para poder jugar.

Cliente 1:

```bash
java -jar TrucoClient.jar 6401
Cliente 2:

```bash
java -jar TrucoClient.jar 6402
Los clientes se conectarán al servidor usando los puertos especificados (en este caso, 6401 y 6402).

Descripción de los comandos
java -jar TrucoServer.jar: Inicia el servidor de Truco.
java -jar TrucoClient.jar <puerto>: Inicia un cliente de Truco y lo conecta al servidor usando el puerto especificado.
### 3. Recomendaciones
Asegúrate de que los puertos que elijas estén disponibles y no estén siendo usados por otros programas.
## Problemas comunes
No se encuentra el archivo JAR: Asegúrate de estar en el directorio correcto donde se encuentra el archivo .jar antes de ejecutarlo.
Problemas de puerto: Verifica que los puertos seleccionados estén libres y no estén siendo utilizados por otros procesos.
Contribuciones
Si deseas contribuir, por favor crea un fork del proyecto y envía un pull request.

¡Disfruta jugando al Truco!