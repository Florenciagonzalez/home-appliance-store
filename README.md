# Home Appliance Store

Backend de una tienda de electrodomésticos desarrollado con Java y Spring Boot. Permite administrar productos, armar carritos y registrar ventas como historial de compras.

El proyecto es un MVP: al registrar una venta se guarda una copia de los productos, cantidades y precios del carrito en ese momento. Las consultas posteriores de la venta leen esa copia, independientemente de los cambios en el catálogo o el carrito.

## Tecnologías

- Java 17 y Maven Wrapper.
- Spring Boot, Spring Web y Spring Data JPA.
- Spring Cloud OpenFeign para comunicación HTTP entre servicios.
- Eureka para registro y descubrimiento de servicios.
- MySQL y Lombok.
- Docker y Docker Compose.

## Arquitectura

| Componente | Puerto | Responsabilidad | Base de datos |
| --- | --- | --- | --- |
| `eureka-sv` | 8761 | Registro de servicios | — |
| `products-service` | 8081 | Catálogo de productos | `products_service` |
| `shopcart-service` | 8083 | Carritos, cantidades y consulta de productos | `shopcart_service` |
| `sales-service` | 8082 | Registro y consulta de ventas históricas | `sales_service` |

El flujo de consultas entre servicios es `sales → shopcart → products`. Los clientes Feign resuelven los servicios por su nombre mediante Eureka. Cada microservicio administra sus propias entidades y base de datos.

## Requisitos

- JDK 17 con `JAVA_HOME` apuntando a la carpeta del JDK.
- Docker Desktop con Docker Compose.
- MySQL disponible en el equipo anfitrión, puerto 3306. Se recomienda MySQL 8 o superior.
- Acceso a los repositorios Maven para descargar las dependencias iniciales.

Compose inicia las aplicaciones y Eureka; MySQL debe estar iniciado por separado. La configuración utiliza `host.docker.internal` para acceder desde los contenedores al MySQL del anfitrión, según la resolución de nombres de Docker Desktop.

## Preparación de MySQL

Crear las tres bases antes de iniciar las aplicaciones:

```sql
CREATE DATABASE IF NOT EXISTS products_service;
CREATE DATABASE IF NOT EXISTS shopcart_service;
CREATE DATABASE IF NOT EXISTS sales_service;
```

La configuración de desarrollo actual utiliza el usuario `root` y contraseña vacía. Si tu instalación tiene otras credenciales, agregá `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` al bloque `environment` de cada microservicio en `docker-compose.yml`. El usuario debe tener permisos sobre su base y aceptar conexiones desde Docker.

Las aplicaciones usan `spring.jpa.hibernate.ddl-auto=update` para generar o actualizar las tablas. Esta propiedad no crea las bases de datos.

## Compilación y ejecución

Ejecutar desde la raíz del repositorio. Los siguientes ejemplos usan PowerShell en Windows.

### 1. Generar los JAR

Los Dockerfiles copian el JAR de `target`; no ejecutan Maven. Es necesario compilar antes de construir las imágenes:

```powershell
.\eureka-sv\mvnw.cmd -f .\eureka-sv\pom.xml clean package -DskipTests
.\products-service\mvnw.cmd -f .\products-service\pom.xml clean package -DskipTests
.\shopcart-service\mvnw.cmd -f .\shopcart-service\pom.xml clean package -DskipTests
.\sales-service\mvnw.cmd -f .\sales-service\pom.xml clean package -DskipTests
```

`-DskipTests` omite la ejecución de tests para generar los artefactos. Para ejecutarlos, quitá esa opción. También podés ejecutar `package` desde Maven → Lifecycle en IntelliJ.

### 2. Iniciar los contenedores

```powershell
docker compose up -d --build
docker compose ps
```

Abrí [Eureka](http://localhost:8761) y esperá a que estén registrados `PRODUCTS-SERVICE`, `SHOPCART-SERVICE` y `SALES-SERVICE` antes de probar las llamadas entre ellos.

Para ver logs o detener las aplicaciones:

```powershell
docker compose logs -f sales-service
docker compose down
```

### Actualizar un solo servicio

Después de modificar código, regenerá primero su JAR. Por ejemplo:

```powershell
.\sales-service\mvnw.cmd -f .\sales-service\pom.xml package -DskipTests
docker compose up -d --build --no-deps sales-service
```

### Ejecutar desde IntelliJ

Para ejecutar las aplicaciones fuera de Docker, cambiá la URL de Eureka a `http://localhost:8761/eureka`, por ejemplo mediante la variable de entorno `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` en las configuraciones de ejecución de los tres microservicios. Las URLs de datasource en los archivos `application.properties` ya apuntan a MySQL en `localhost`.

## API

Las solicitudes con cuerpo JSON requieren `Content-Type: application/json`.

### Productos — `http://localhost:8081`

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/products` | Listar productos |
| GET | `/api/products/id/{id}` | Consultar un producto |
| POST | `/api/products` | Crear un producto |
| PUT | `/api/products/id/{id}` | Actualizar un producto |
| DELETE | `/api/products/id/{id}` | Eliminar un producto; devuelve 204 |
| POST | `/api/products/listOfProducts` | Consultar por lista de IDs, por ejemplo `[1, 2]` |

### Carritos — `http://localhost:8083`

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/shopcarts` | Listar carritos |
| POST | `/api/shopcarts` | Crear un carrito vacío, sin body |
| GET | `/api/shopcarts/{id}` | Consultar IDs de productos y cantidades |
| POST | `/api/shopcarts/{id}/addProduct` | Agregar un producto con su cantidad |
| GET | `/api/shopcarts/{id}/products` | Consultar detalles y total con precios actuales del catálogo |
| DELETE | `/api/shopcarts/{id}` | Eliminar un carrito |
| DELETE | `/api/shopcarts/{id}/products/{id_product}` | Eliminar un producto del carrito |

### Ventas — `http://localhost:8082`

| Método | Ruta | Descripción |
| --- | --- | --- |
| POST | `/api/sales` | Registrar una venta a partir de `shopcartId` |
| GET | `/api/sales/{id}` | Consultar una venta y sus detalles históricos |

## Ejemplo del flujo completo

Podés ejecutar estas solicitudes con Postman o un cliente HTTP. Los IDs son ilustrativos: utilizá los devueltos por las operaciones de creación.

### 1. Crear un producto

```http
POST http://localhost:8081/api/products
Content-Type: application/json

{
  "name": "Heladera",
  "brand": "Marca de ejemplo",
  "unit_price": 500000
}
```

Devuelve 201 con el producto creado y su `id_product`.

### 2. Crear un carrito

```http
POST http://localhost:8083/api/shopcarts
```

Devuelve 201 con el carrito creado y su `id`.

### 3. Agregar el producto

```http
POST http://localhost:8083/api/shopcarts/1/addProduct
Content-Type: application/json

{
  "id_product": 1,
  "quantity": 2
}
```

### 4. Consultar el carrito con detalles

```http
GET http://localhost:8083/api/shopcarts/1/products
```

### 5. Registrar la venta

```http
POST http://localhost:8082/api/sales
Content-Type: application/json

{
  "shopcartId": 1
}
```

Devuelve 201 con la venta, fecha, total e ítems. Sales guarda sus precios con `BigDecimal` y dos decimales, y persiste la venta y sus detalles en una transacción local.

### 6. Consultar el historial de esa venta

```http
GET http://localhost:8082/api/sales/1
```

Para comprobar que la venta conserva la información histórica, actualizá el precio del producto mediante el PUT de products y consultá nuevamente la venta: el precio registrado debe permanecer igual.

## Manejo de errores

Cada microservicio tiene un manejador central con `@RestControllerAdvice`. Las excepciones propias se traducen al siguiente formato:

```json
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "El producto con ID 999 no fue encontrado",
  "path": "/api/products/id/999"
}
```

| HTTP | Código | Situación |
| --- | --- | --- |
| 400 | `BAD_REQUEST` | Validación u operación inválida |
| 404 | `RESOURCE_NOT_FOUND` | Recurso no encontrado |
| 503 | `SERVICE_UNAVAILABLE` | Fallo al consultar un servicio requerido |

La traducción de errores remotos depende de los casos capturados en cada cliente consumidor. Este formato corresponde a las excepciones manejadas explícitamente; no implica que todo error de Spring o de infraestructura tenga esa misma respuesta.

## Tests

Para ejecutar los tests de un módulo:

```powershell
.\sales-service\mvnw.cmd -f .\sales-service\pom.xml test
```

Los tests actuales de carga de contexto utilizan la configuración de la aplicación: requieren MySQL accesible y la base correspondiente creada. Para ejecutar desde el anfitrión, configurá la URL de Eureka como se indica en la sección de ejecución desde IntelliJ.

## Alcance actual y mejoras pendientes

- No incluye pagos, autenticación ni control de stock.
- Registrar una venta no elimina ni cierra el carrito.
- Cada carrito puede generar una sola venta. Los intentos repetidos devuelven 409 (`CART_ALREADY_PURCHASED`); una restricción única en sales protege también ante solicitudes simultáneas.
- El carrito consulta los precios actuales; la venta conserva los precios aplicados al registrarla.
- Las transacciones son locales a cada microservicio.
- Quedan como mejoras la documentación OpenAPI, tests de negocio e integración, y Bean Validation.

## Problemas frecuentes

| Problema | Qué revisar |
| --- | --- |
| Docker no encuentra `target/*.jar` | Ejecutar Maven `package` antes de reconstruir la imagen |
| `JAVA_HOME` inválido | Debe apuntar a la carpeta del JDK, sin incluir `bin` |
| `Unknown database` | Crear la base indicada en MySQL |
| Error de autenticación o conexión a MySQL | Revisar credenciales, puerto y acceso desde Docker |
| Error al consultar otro servicio | Revisar sus logs y su registro en Eureka |
| Los cambios no aparecen en Docker | Regenerar el JAR y reconstruir/recrear el contenedor |

### Cambios de persistencia

Los importes de products, shopcart y sales usan `BigDecimal`. Los precios se guardan con dos decimales y redondeo `HALF_UP`; revis? los datos existentes al actualizar una base que antes almacenaba precios con coma flotante.

Al agregar nuevamente un producto al carrito se suma su cantidad. Los carritos antiguos con filas repetidas necesitan una consolidaci?n de datos por separado.

Para verificar en MySQL la restricci?n que impide ventas duplicadas, despu?s de iniciar sales:

```sql
SHOW INDEX FROM sales_service.sale WHERE Key_name = 'uk_sale_shopcart';
```

Debe aparecer el ?ndice con `Non_unique = 0`. Si existen ventas duplicadas, resolvelas antes de aplicar la restricci?n; revis? tambi?n los logs de actualizaci?n del esquema.
