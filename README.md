# Proyecto-SpringBoot-KinalApp

El sistema permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) conectándose a una base de datos MySQL.

### Tecnologías Utilizadas

* **Java JDK 21**
* **IntelliJ IDEA**
* **MySQL 8.0**
* **Postman**
* **Spring Boot**

## Arquitectura del proyecto

La arquitectura del proyecto que se utilizara es MVC, para llevar a cabo el trabajo,
esto nos ayudara a facilitar la organizacion del proyecto.

- **controllers** Maneja las peticiones HTTP
- **services** Contiene la lógica de negocio
- **repositories** Acceso a datos (JPA)
- **entities** Representación de la base de datos

## Variables de entorno

configurar en `application.properties`:

```properties
spring.application.name=KinalApp

# Conexion MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/dbClientes_in5av?createDatabaseIfNotExist=true
spring.datasource.username=IN5AV
spring.datasource.password=!+admin5av

# Puerto del servidor 
# server.port=8001

# JPA / hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Manejo de errores
La API maneja errores mediante respuestas HTTP:

- **200** OK
- **201** Creado
- **400** Error en datos
- **404** No encontrado
- **500** Error del servidor


