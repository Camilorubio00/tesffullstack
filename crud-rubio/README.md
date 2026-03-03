## Descripción

Este proyecto consiste en un backend desarrollado con Spring Boot que implementa un CRUD completo para la entidad **Person**.

El sistema permite:

- Insertar personas
- Obtener listado de personas
- Obtener persona por Id
- Actualizar persona por Id
- Eliminar una persona

Toda la API responde con un JSON estándar con la forma:

```json
{
  "status": true,
  "msg": "Mensaje devuelto por la consulta o en caso de error o falla",
  "data": [{}, {}, ...]
}
```

## Tecnologías Utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Maven

## Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:

- **Model**: Representa la tabla `person` en la base de datos (`model/Person.java`).
- **Repository**: Permite realizar operaciones CRUD sin escribir SQL manual (`repository/PersonRepository.java`).
- **Service**: Contiene la lógica de negocio del CRUD (`service/PersonService.java`).
- **Controller**: Expone los endpoints REST (`controller/PersonController.java`).
- **DTO**: Modelo de respuesta estándar de la API (`dto/ApiResponse.java`).

## Requisitos Previos

Antes de ejecutar el proyecto, es necesario tener instalado:

- Java 17 o superior
- Maven
- MySQL Server
- Postman o `curl` (opcional para pruebas)

## Configuración de Base de Datos

1. Asegúrate de tener MySQL corriendo en `localhost:3306`.
2. Ejecuta el script de creación de la base de datos que se encuentra en la carpeta `bd` del repositorio:

   ```bash
   mysql -u root -p < bd/script_creacion_bd.sql
   ```

   Esto creará la base de datos `bd_rubio` (o la que corresponda según el script) y la tabla `person`.

3. Verifica la configuración de conexión en `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bd_rubio?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=root1234
   ```

   Ajusta `username` y `password` según tu instalación local de MySQL.

## Ejecución del Proyecto

Desde la raíz del proyecto backend:

```bash
cd crud-rubio
./mvnw spring-boot:run
```

La aplicación se levantará por defecto en `http://localhost:8080`.

## Endpoints REST

Base URL: `http://localhost:8080/api/personas`

### Obtener todas las personas

- **Método**: `GET`
- **URL**: `/api/personas`

Ejemplo con `curl`:

```bash
curl -X GET "http://localhost:8080/api/personas"
```

### Obtener persona por ID

- **Método**: `GET`
- **URL**: `/api/personas/{id}`

Ejemplo:

```bash
curl -X GET "http://localhost:8080/api/personas/1"
```

### Crear persona

- **Método**: `POST`
- **URL**: `/api/personas`
- **Body (JSON)**:

```json
{
  "nombre": "Juan",
  "apellido": "Perez",
  "fechaNacimiento": "1990-05-10",
  "puesto": "Desarrollador",
  "sueldo": 4500.0
}
```

Ejemplo con `curl`:

```bash
curl -X POST "http://localhost:8080/api/personas" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Perez",
    "fechaNacimiento": "1990-05-10",
    "puesto": "Desarrollador",
    "sueldo": 4500.0
  }'
```

### Actualizar persona

- **Método**: `PUT`
- **URL**: `/api/personas/{id}`

Ejemplo con `curl`:

```bash
curl -X PUT "http://localhost:8080/api/personas/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Actualizado",
    "apellido": "Perez",
    "fechaNacimiento": "1990-05-10",
    "puesto": "Senior Developer",
    "sueldo": 6000.0
  }'
```

### Eliminar persona

- **Método**: `DELETE`
- **URL**: `/api/personas/{id}`

Ejemplo con `curl`:

```bash
curl -X DELETE "http://localhost:8080/api/personas/1"
```

En todos los casos, la respuesta seguirá el formato:

```json
{
  "status": true,
  "msg": "Mensaje devuelto por la consulta o en caso de error o falla",
  "data": []
}
```

