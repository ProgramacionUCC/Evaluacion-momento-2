# Sistema de Gestión de Usuarios y Solicitudes

API REST hecha con Spring Boot para gestionar usuarios (administradores, empleados y clientes) y las solicitudes de atención que los clientes crean y los empleados atienden. La información se guarda en MySQL con Spring Data JPA.

## Tecnologías

- Java 25 y Spring Boot 4.1.1
- Spring Web, Spring Data JPA y Bean Validation
- MySQL
- Lombok
- Maven

## Cómo ejecutarlo

1. Tener MySQL corriendo en `localhost:3306`.
2. Ejecutar el script [docs/evaluacion2.sql](docs/evaluacion2.sql). Crea la base `sistema_gestion_usuarios` y las tablas.
3. Revisar usuario y clave de MySQL en `src/main/resources/application.properties` (por defecto `root` / `root`).
4. Ejecutar `Evaluacion2Application` desde el IDE, o con:

```bash
./mvnw spring-boot:run
```

La app queda en `http://localhost:8080`.

`ddl-auto=validate`: Hibernate no crea tablas, solo comprueba que las entidades coincidan con las del script.

## Modelo de clases

```
Usuario (abstracta: id, nombre, correo, obtenerDescripcion())
 ├── Administrador (nivelAcceso)
 ├── Empleado (cargo, salario)
 └── Cliente (puntosFidelidad)  ──< Solicitud (descripcion, estado, fechaCreacion, empleado)
```

- **Herencia:** las tres subclases usan `SINGLE_TABLE`: una sola tabla `usuarios` con la columna `tipo_usuario` que indica el rol.
- **Polimorfismo:** cada rol implementa `obtenerDescripcion()` a su manera.
- **Relación:** un cliente tiene muchas solicitudes (`@OneToMany` / `@ManyToOne`). El empleado de una solicitud es opcional.

## Validaciones

| Campo | Regla |
|---|---|
| nombre | No vacío |
| correo | Formato válido y no repetido |
| nivelAcceso | Entre 1 y 5 |
| salario | Mayor que cero |
| puntosFidelidad | No negativo |
| descripción (solicitud) | No vacía |

Los errores responden 400 con el detalle. Un id que no existe responde 404.

## Endpoints

### Usuarios

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/usuarios` | Crea un usuario según `tipoUsuario` |
| GET | `/usuarios` | Lista paginada y ordenada |
| GET | `/usuarios/{id}` | Busca por id |

Parámetros de la lista: `pagina` (desde 0), `tamano`, `ordenarPor` (`id`, `nombre`, `correo`) y `direccion` (`asc` o `desc`).

Ejemplo (`ADMINISTRADOR`, `EMPLEADO` o `CLIENTE`):

```json
{
  "tipoUsuario": "EMPLEADO",
  "nombre": "Luis Pérez",
  "correo": "luis@mail.com",
  "cargo": "Soporte técnico",
  "salario": 2500000
}
```

### Solicitudes

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/solicitudes` | Crea una solicitud para un cliente |
| GET | `/solicitudes` | Lista paginada, con filtro opcional por `estado` |
| GET | `/solicitudes/{id}` | Busca por id |

Parámetros de la lista: `pagina`, `tamano`, `ordenarPor` (`id`, `fechaCreacion`, `estado`), `direccion` y `estado` (`PENDIENTE`, `EN_PROCESO`, `RESUELTA`, `CANCELADA`).

```json
{
  "descripcion": "No puedo ingresar a mi cuenta",
  "clienteId": 3,
  "empleadoId": 2
}
```

### Gestor de solicitudes (estructuras de datos)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/solicitudes/cola` | Pendientes en orden de llegada |
| POST | `/solicitudes/procesar` | Atiende la primera de la cola |
| POST | `/solicitudes/procesar-todas` | Atiende toda la cola |
| GET | `/solicitudes/historial` | Procesadas, la más reciente primero |
| POST | `/solicitudes/deshacer` | Devuelve la última procesada a la cola |
| GET | `/solicitudes/buscar?texto=` | Búsqueda lineal por descripción |
| GET | `/solicitudes/ordenadas?criterio=` | Ordenamiento por `id`, `fecha` o `estado` |
| GET | `/solicitudes/memoria/{id}` | Búsqueda binaria por id |

## Estructuras de datos

`GestorSolicitudes` mantiene en memoria:

- **Lista** (`ArrayList<Solicitud>`): todas las solicitudes.
- **Cola** (`Queue<Solicitud>`): las pendientes, se atienden en orden de llegada (FIFO).
- **Pila** (`Stack<Solicitud>`): las procesadas; la última en procesarse es la primera en deshacerse (LIFO).

Algoritmos propios: búsqueda lineal, búsqueda binaria y ordenamiento por inserción.

El gestor vive en memoria: al reiniciar la app la lista, la cola y el historial empiezan vacíos y se llenan con las solicitudes que se creen después.

## Pruebas

Colección de Postman lista para importar: [docs/evaluacion2.postman_collection.json](docs/evaluacion2.postman_collection.json).

## Estructura del proyecto

```
src/main/java/evaluacionMomento2/evaluacion2/
  controller/   endpoints REST
  dto/          request y response
  entity/       entidades JPA
  exception/    errores y manejo global
  repository/   interfaces JpaRepository
  service/      interfaces, gestor de solicitudes y service/impl
docs/           script SQL y colección de Postman
```
