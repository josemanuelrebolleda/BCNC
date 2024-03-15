# TestBCNC - Proyecto de Prueba para BCNC

Este repositorio contiene un proyecto de prueba para BCNC, una aplicación de microservicios desarrollada en Spring Boot que proporciona tres endpoints para el Manejo de Álbumes

## Descripción

El proceso permite el enriquecimiento de una base de datos de álbumes con sus respectivas fotos, y dos formas de consulta de los álbumes.

## Tecnologías Utilizadas

- **Java 17**: Plataforma de desarrollo de aplicaciones Java.
- **Spring Boot Starter Data JPA (3.2.3)**: Proporciona soporte para la capa de persistencia de datos utilizando JPA (Java Persistence API) en aplicaciones Spring Boot.
- **Spring Boot Starter Web (3.2.3)**: Proporciona soporte para desarrollar aplicaciones web utilizando Spring MVC y incorpora un servidor web embebido.
- **H2 Database (1.4.200)**: Base de datos en memoria utilizada para pruebas y desarrollo.
- **Lombok (1.18.20)**: Biblioteca del proyecto delombok que reduce el código boilerplate (repetitivo) escribiendo getters, setters, constructores, etc.
- **Spring Boot Starter Test (3.2.3)**: Proporciona soporte para pruebas unitarias y de integración en aplicaciones Spring Boot.
- **JUnit Jupiter (5.8.2)**: Framework de pruebas unitarias para Java.

## Requisitos

Para ejecutar el microservicio, necesitarás tener instalado:

- Java Development Kit (JDK) 17 o superior.
- Maven.

## Instalación del Microservicio

Para ejecutar el microservicio, sigue estos pasos:

1. Clona el repositorio:

```bash
git clone https://github.com/josemanuelrebolleda/BCNC.git
```

2. Navega al directorio del proyecto:

```bash
cd BCNC
```

3. Compila y ejecuta el microservicio:

```bash
./mvnw spring-boot:run
```

4. Accede a los endpoints utilizando herramientas como Postman o un navegador web.

## Endpoints

 1. Enriquecer y Guardar Álbumes

- Endpoint: `/albums/enrichAndSave`
- Método HTTP: POST
- Descripción: Ejecuta un algoritmo para enriquecer datos obtenidos a través de un API y los guarda en una base de datos en memoria H2.
- Funcionalidad:
    - Enriquece los álbumes con fotos correspondientes.
    - Guarda los álbumes en la base de datos.
	- Dispone de salidas informadas de posibles errores

 2. Enriquecer y Devolver Álbumes

- Endpoint: `/albums/enrichAlbums`
- Método HTTP: GET
- Descripción: Ejecuta un algoritmo para enriquecer datos obtenidos a través de un API y los devuelve en la respuesta.
- Funcionalidad:
    - Enriquece los álbumes con fotos correspondientes.
    - Devuelve los álbumes enriquecidos en la respuesta.
	- Dispone de salidas informadas de posibles errores

 3. Consulta de Álbumes en la Base de Datos

- Endpoint: `/albums/getAlbumsFromDB`
- Método HTTP: GET
- Descripción: Obtiene todos los álbumes almacenados en la base de datos H2.
- Funcionalidad:
    - Devuelve todos los álbumes almacenados en la base de datos H2.
	- Dispone de salidas informadas de posibles errores

## Consideraciones

- En pro de la eficiencia, he utilizado List (ArrayList) que tienen menor carga de proceso para el tipo de carga de datos que realizamos y un orden inherente que facilita el listado ordenado.
- Se ha utilizado Spring Boot para facilitar el desarrollo del microservicio y se han incluido tests unitarios y de integración para todos los endpoints tratando de garantizar la calidad del código.
- Se han desarrollado los endpoint pensando en un salida de tipo String para poder comunicar incidencias y problemas de ejecución en la salida del REST.

## Pruebas de Integración y Estrategia de Enriquecimiento

En nuestro proyecto, utilizamos pruebas de integración para verificar que diferentes partes de la aplicación trabajen correctamente juntas. 

Un ejemplo de esto es la clase `AlbumServiceImplIntegrationTest`, que prueba cómo el servicio `AlbumServiceImpl` interactúa con otras partes de la aplicación, como el repositorio `AlbumRepository` y la estrategia de enriquecimiento `EnrichingStrategyImpl`.

La estrategia de enriquecimiento `EnrichingStrategyImpl` se utiliza para enriquecer los datos de los álbumes. Esta estrategia se aplica en el método `enriching()` de `AlbumServiceImpl`. En las pruebas, se simula el comportamiento de esta estrategia utilizando un mock, lo que permite verificar que el servicio `AlbumServiceImpl` interactúa correctamente con ella.

Las pruebas de integración son fundamentales para asegurar que los diferentes componentes de nuestra aplicación funcionen bien juntos, y la estrategia de enriquecimiento nos permite manipular y mejorar los datos de los álbumes de manera eficiente.

### integrationTestEnrichAndSaveAlbumsOK

El método `integrationTestEnrichAndSaveAlbumsOK()` es una prueba de integración que verifica el correcto funcionamiento del método `enrichAndSaveAlbums()` de la clase `AlbumServiceImpl`. Aquí está el desglose de lo que hace esta prueba:

1. **Configuración de los mocks**: Al inicio de la prueba, se configura el mock `enrichingStrategyMock` para que, cuando se llame al método `enrich()`, devuelva una lista de álbumes enriquecidos (`enrichedAlbums`). Esto simula el comportamiento esperado de la estrategia de enriquecimiento en un escenario real.

2. **Llamada al método bajo prueba**: A continuación, se llama al método `enrichAndSaveAlbums()` de `albumServiceImpl`. Este método debería llamar a `enrich()` de `enrichingStrategyMock`, obtener la lista de álbumes enriquecidos y guardarlos en la base de datos.

3. **Verificación del resultado**: Se verifica que el método `enrichAndSaveAlbums()` devuelva una respuesta con el código de estado HTTP 200 (OK) y el cuerpo "Almacenado en BDD". Esto indica que los álbumes enriquecidos se han guardado correctamente en la base de datos.

4. **Verificación de las interacciones con los mocks**: Por último, se verifica que el método `saveAll()` del mock `albumRepositoryMock` se haya llamado una vez con la lista de álbumes enriquecidos como argumento. Esto asegura que el método `enrichAndSaveAlbums()` está interactuando correctamente con el repositorio de álbumes.

En resumen, esta prueba verifica que, cuando se llama a `enrichAndSaveAlbums()`, se enriquecen los álbumes, se guardan en la base de datos y se devuelve una respuesta exitosa.

### testEnrichAndSaveAlbumsOK

El método `testEnrichAndSaveAlbumsOK()` es una prueba unitaria que verifica el correcto funcionamiento del método `enrichAndSaveAlbums()` de la clase `AlbumServiceImpl`. Aquí está el desglose de lo que hace esta prueba:

1. **Configuración de los mocks**: Al inicio de la prueba, se configura el mock `enrichingStrategyMock` para que, cuando se llame al método `enrich()`, devuelva una lista de álbumes enriquecidos (`enrichedAlbums`). Esto simula el comportamiento esperado de la estrategia de enriquecimiento en un escenario real.

2. **Llamada al método bajo prueba**: A continuación, se llama al método `enrichAndSaveAlbums()` de `albumServiceImpl`. Este método debería llamar a `enrich()` de `enrichingStrategyMock`, obtener la lista de álbumes enriquecidos y guardarlos en la base de datos.

3. **Verificación del resultado**: Se verifica que el método `enrichAndSaveAlbums()` devuelva una respuesta con el código de estado HTTP 200 (OK) y el cuerpo "Almacenado en BDD". Esto indica que los álbumes enriquecidos se han guardado correctamente en la base de datos.

4. **Verificación de las interacciones con los mocks**: Por último, se verifica que el método `saveAll()` del mock `albumRepositoryMock` se haya llamado una vez con la lista de álbumes enriquecidos como argumento. Esto asegura que el método `enrichAndSaveAlbums()` está interactuando correctamente con el repositorio de álbumes.

En resumen, esta prueba verifica que, cuando se llama a `enrichAndSaveAlbums()`, se enriquecen los álbumes, se guardan en la base de datos y se devuelve una respuesta exitosa.

### Uso de la clase Tools en las pruebas

La clase `Tools` se utiliza en las pruebas unitarias e integración de `AlbumServiceImpl` para preparar los datos necesarios antes de cada prueba. Esto se realiza en los métodos anotados con `@BeforeEach` en las clases `AlbumServiceImplIntegrationTest` y `AlbumServiceImplTest`.

#### Tools para AlbumServiceImplIntegrationTest y AlbumServiceImplTest

En `AlbumServiceImplIntegrationTest`y en `AlbumServiceImplTest`, el método `@BeforeEach` utiliza la clase `Tools` para generar una lista de álbumes de prueba enriquecidos que se utilizarán en las pruebas de integración. Este método llama a `tools.setup()`, que devuelve una lista de álbumes enriquecidos, y los añade a la lista `enrichedAlbums` que se utilizará en las pruebas.

## Análisis estático de código

Este proyecto está configurado para usar SonarQube, pero no he conseguido conectarlo adecuadamente con mi perfil. Nunca lo había hecho y tras dos días de pelea tuve que renunciar.

## Versión

TestBCNC 1.3

## Autor

Jose Manuel Rebolleda Díaz
josemanuelrebolleda@gmail.com