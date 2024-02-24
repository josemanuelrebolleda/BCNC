 Microservicio de Manejo de Álbumes

Este es un microservicio desarrollado en Spring Boot que proporciona tres endpoints para el manejo de álbumes y fotos.

 Endpoints

 1. Enriquecer y Guardar Álbumes

- Endpoint: `/albums/enrichAndSave`
- Método HTTP: POST
- Descripción: Ejecuta un algoritmo para enriquecer datos obtenidos a través de un API y los guarda en una base de datos en memoria H2.
- Funcionalidad:
    - Enriquece los álbumes con fotos correspondientes.
    - Guarda los álbumes en la base de datos.
- Test unitario: Se incluye al menos un test unitario para esta funcionalidad.
- Test de integración: Se incluye al menos un test de integración para esta funcionalidad.

 2. Enriquecer y Devolver Álbumes

- Endpoint: `/albums/enrich`
- Método HTTP: GET
- Descripción: Ejecuta un algoritmo para enriquecer datos obtenidos a través de un API y los devuelve en la respuesta.
- Funcionalidad:
    - Enriquece los álbumes con fotos correspondientes.
    - Devuelve los álbumes enriquecidos en la respuesta.
- Test unitario: Se incluye al menos un test unitario para esta funcionalidad.
- Test de integración: Se incluye al menos un test de integración para esta funcionalidad.

 3. Consulta de Álbumes en la Base de Datos

- Endpoint: `/albums`
- Método HTTP: GET
- Descripción: Obtiene todos los álbumes almacenados en la base de datos H2.
- Funcionalidad:
    - Devuelve todos los álbumes almacenados en la base de datos H2.
- Test unitario: Se incluye al menos un test unitario para esta funcionalidad.
- Test de integración: Se incluye al menos un test de integración para esta funcionalidad.

 Consideraciones

- La eficiencia es una prioridad, por lo que se han elegido las estructuras de datos adecuadas para optimizar el rendimiento.
- Se ha utilizado Spring Boot para facilitar el desarrollo del microservicio y se han incluido tests unitarios y de integración para garantizar la calidad del código.