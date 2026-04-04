# 🏦 Sistema de Gestión Bancaria

Proyecto desarrollado para la materia de **Construccion de Software II**, enfocado en la implementación de una arquitectura robusta, escalable y siguiendo los principios de **Clean Architecture**.

## 🚀 Tecnologías Utilizadas
* **Java 17+**: Lenguaje de programación principal.
* **Spring Boot 3.x**: Framework para la gestión de dependencias y servicios.
* **Maven**: Gestor de proyectos y dependencias.
* **Git**: Control de versiones.

## 🏗️ Arquitectura del Proyecto
El sistema utiliza **Arquitectura Hexagonal (Ports & Adapters)** para desacoplar la lógica de negocio de las implementaciones tecnológicas.

* **Domain (Core):** Contiene las reglas de negocio (Services), los modelos (Entities) y las definiciones de interfaces (Ports).
* **Infrastructure:** Contiene los adaptadores (JPA Repositories, REST Controllers) que conectan el core con el mundo exterior.

## 📋 Funcionalidades Principales
- **Gestión de Clientes:** Registro y validación de Personas Naturales y Empresas (NIT).
- **Cuentas Bancarias:** Apertura, depósitos, retiros y transferencias.
- **Módulo de Préstamos:** Flujo de solicitud, aprobación por analista y desembolso automático.
- **Seguridad:** Autenticación basada en roles (Cajero, Analista, Cliente, etc.).
- **Auditoría:** Registro de operaciones en bitácora inmutable (NoSQL approach).

## 📚 Contexto de la Materia
- **Materia:** Construcción de Software II
- **Objetivo:** Aplicar principios de arquitectura limpia, patrones de diseño y buenas prácticas en el desarrollo de una solución empresarial.
- **Enfoque:** Clean Architecture, Hexagonal Architecture, Domain-Driven Design (DDD) y patrones SOLID.

## 📦 Estructura del Proyecto
```
bank/
├── src/main/java/app/domain/
│   ├── enums/                 # Catálogos de valores (AccountStatus, LoanStatus, etc.)
│   ├── exceptions/            # Excepciones personalizadas
│   ├── models/                # Entidades de dominio
│   ├── ports/                 # Interfaces (adaptadores)
│   └── services/              # Lógica de negocio
├── src/main/resources/
│   └── application.properties # Configuración de la aplicación
├── src/test/java/
│   └── app/                   # Pruebas unitarias
├── pom.xml                    # Configuración de Maven
└── README.md
```

### Modelos Principales
- **Person** (abstracta): Base para usuarios y clientes
- **User**: Usuarios del sistema (Cajero, Analista, Administrador)
- **Client** (abstracta): Base para clientes
  - **NaturalPerson**: Personas naturales
  - **Company**: Empresas/Personas jurídicas
- **BankAccount**: Cuentas corrientes y de ahorros
- **Loan**: Préstamos bancarios
- **Transfer**: Transferencias entre cuentas
- **BankProduct**: Productos financieros
- **OperationLog**: Bitácora de todas las operaciones

## ⚙️ Requisitos Previos
- **Java 17** o superior
- **Maven 3.8+**
- **Git**
- Editor de código (VS Code, IntelliJ IDEA, etc.)

## 🚀 Guía de Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/Juan0u0/Bank.git
cd Bank/bank
```

### 2. Compilar el proyecto
Usando Maven:
```bash
mvn clean install
```

O en Windows:
```bash
mvnw.cmd clean install
```

### 3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

O en Windows:
```bash
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 4. Configuración (opcional)
Editar `src/main/resources/application.properties` para personalizar:
```properties
server.port=8080
spring.application.name=Bank
# Agregar configuración de base de datos según sea necesario
```

## 🎯 Funcionalidades Detalladas

### Gestión de Clientes
- Registro de Personas Naturales con validación de edad (mínimo 18 años)
- Registro de Empresas con NIT y datos corporativos
- Validaciones de email, teléfono y datos de identificación

### Gestión de Cuentas
- Creación de cuentas corrientes y de ahorros
- Consulta de saldo y estado
- Depósitos y retiros
- Transferencias entre cuentas con validación de fondos

### Gestión de Préstamos
- Solicitud de préstamos por monto y plazo
- Aprobación/Rechazo por analistas
- Desembolso automático a la cuenta asociada
- Cálculo de tasas de interés

### Seguridad y Auditoría
- Autenticación basada en roles (ADMIN, ANALYST, CASHIER, CLIENT)
- Registro completo de operaciones en bitácora
- Trazabilidad de usuario, fecha y hora de cada transacción

## 🧪 Pruebas
Ejecutar las pruebas unitarias:
```bash
mvn test
```

## 📝 Notas Importantes
- El proyecto utiliza **Lombok** para reducir código boilerplate
- Sigue la nombración en **inglés** con notación **camelCase**
- Implementa **Clean Architecture** con separación de responsabilidades
- Las validaciones de negocio se encuentran en los servicios del dominio

## 📄 Licencia
Este proyecto está bajo licencia MIT.

## 🤝 Contribuciones
Desarrollado por:
- **Ana Belen Macas Caicer**
- **Juan Jose Berrio Velasquez**

Para preguntas o sugerencias, contactar a través del repositorio.