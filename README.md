# 📦 Sistema de Gestión de Inventarios

Este proyecto es un sistema integral para la gestión de inventarios desarrollado como iniciativa personal para consolidar mis conocimientos en desarrollo Full-Stack. El objetivo principal fue crear una aplicación robusta, modular y escalable aplicando buenas prácticas de desarrollo como **Arquitectura Hexagonal** y **Domain-Driven Design (DDD)**.

## 🚀 Características Principales (Lo que se puede hacer)

El sistema cuenta con un control de acceso basado en roles (RBAC) y permite realizar las siguientes operaciones:

* **Dashboard Estadístico:** Vista principal con información global del inventario, estadísticas de productos, locaciones, movimientos y actividad de empleados.
* **Gestión de Productos y Categorías:** Creación y administración de categorías y productos (incluyendo detalles, precios e imágenes).
* **Control de Locaciones:** Administración de ubicaciones de almacenamiento físicas o virtuales (almacenes, tiendas, etc.) con sus respectivas direcciones.
* **Movimientos de Inventario:** Registro detallado de entradas y salidas de stock, asociadas a una fecha, producto, locación y usuario responsable.
* **Gestión de Empleados:** Administración del personal y asignación de roles específicos que determinan el nivel de acceso y las vistas disponibles en el sistema.

## 💻 Tecnologías y Arquitectura

### Frontend
* **Angular 21:** Uso intensivo de Signals, Observables, RxJS, Signal Forms, Form Builder y validadores (síncronos y asíncronos).
* **Estilos:** Angular Material y Tailwind CSS para un diseño limpio y responsivo.

### Backend
* **Spring Boot (Java):** API REST completamente stateless.
* **Spring Security & JWT:** Autenticación y autorización robusta basada en tokens y roles de usuario.
* **Spring Data JPA:** Creación de queries dinámicas mediante Specification Filters, paginación, ordenamiento y manejo de transacciones (`@Transactional`).
* **Base de Datos:** PostgreSQL.

### Arquitectura
* **Arquitectura Hexagonal y DDD:** Separación estricta de responsabilidades, aislamiento del dominio, uso de repositorios, servicios, controladores y Value Objects para crear una aplicación altamente desacoplada.

## 🧠 Retos Técnicos y Aprendizajes

Durante el desarrollo de este proyecto, me enfrenté a varios desafíos que me permitieron crecer profesionalmente:

* **Curva de aprendizaje de DDD:** Entender la separación entre una entidad y el dominio, así como el concepto de *Value Objects*, fue complejo al inicio, pero me permitió lograr un backend impecable y modular.
* **Seguridad Stateless:** Lograr implementar JWT junto con filtros de roles por usuario me dio una comprensión profunda de cómo asegurar APIs modernas.
* **Integración de Signals y RxJS:** Transicionar a las nuevas reactividades de Angular (Signals) mientras mantenía la potencia de RxJS me ayudó a optimizar el rendimiento del frontend.

## 🚧 Mejoras Futuras (Roadmap)

* **Optimización de Concurrencia:** Actualmente el dashboard realiza peticiones masivas al backend. Para escalar a miles de productos, planeo implementar paginación asíncrona o WebSockets para actualizar las métricas en tiempo real sin sobrecargar el servidor.
* **Migración a CUIDs:** Evaluar el reemplazo de los actuales identificadores UUID por CUIDs para optimizar la indexación en la base de datos, manejando las dependencias necesarias en Maven.
* **Patrones de Diseño Avanzados:** Implementar el patrón *Factory* para la creación de objetos de dominio complejos.

## ⚙️ Instalación y Configuración Local

### Prerrequisitos

Antes de ejecutar el proyecto en local, asegúrate de tener instalado lo siguiente:

* **Java 25**
* **Node.js** y **npm**
* **PostgreSQL**
* Git

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd gestion-inventarios
```

### 2. Crear la base de datos

El backend espera una base de datos PostgreSQL llamada `gestion_inventarios_db`.

```sql
CREATE DATABASE gestion_inventarios_db;
```

### 3. Configurar variables de entorno

El backend carga variables desde un archivo `.env` ubicado en la raíz del proyecto o dentro de `backend/`.

Crea un archivo `.env` con este contenido:

```env
DB_USER=tu_usuario_postgres
DB_PASSWORD=tu_password_postgres
JWT_SECRET=una_clave_larga_y_segura_para_desarrollo
```

### 4. Datos iniciales

Al iniciar el backend, Spring Boot ejecuta automáticamente `data.sql` y carga datos base como:

* permisos
* roles
* usuarios de prueba
* categorías
* locaciones

Credenciales disponibles por defecto:

* **ADMIN:** `admin@inventarios.com` / `admin123`
* **MANAGER:** `manager@inventarios.com` / `manager123`
* **EMPLOYEE:** `employee@inventarios.com` / `employee123`

### 5. Ejecutar en modo desarrollo

Este modo es el más cómodo si quieres trabajar frontend y backend por separado.

#### Backend

Desde la carpeta `backend`:

```bash
cd backend
./mvnw spring-boot:run
```

El backend quedará disponible en:

```text
http://localhost:8080
```

#### Frontend

En otra terminal, desde la carpeta `frontend`:

```bash
cd frontend
npm install
npm start
```

El frontend quedará disponible en:

```text
http://localhost:4200
```

En desarrollo, Angular usa la configuración de [frontend/src/environments/environment.development.ts](frontend/src/environments/environment.development.ts), que apunta al backend local en `http://localhost:8080`.

### 6. Ejecutar como aplicación integrada

El proyecto también puede empaquetarse como una sola aplicación Spring Boot que compila el frontend, copia `dist` a recursos estáticos y lo sirve desde el backend.

Desde la carpeta `backend`:

```bash
cd backend
./mvnw clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

En este modo, todo queda servido desde:

```text
http://localhost:8080
```

### 7. Build del frontend

Si solo quieres compilar Angular manualmente:

```bash
cd frontend
npm install
npm run build
```

La salida se genera en:

```text
frontend/dist/frontend
```

### 8. Notas importantes

* El backend usa `spring.jpa.hibernate.ddl-auto=update`, así que el esquema se ajusta automáticamente según las entidades.
* El frontend en producción no necesita ejecutarse aparte si arrancas el JAR de Spring Boot.
* El backend expone uploads públicos en `/uploads/**` para imágenes de productos.
* Actualmente el logging de seguridad y SQL está en modo detallado para desarrollo.