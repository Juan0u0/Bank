# EVALUACIÓN - Bank

## Información General
- Estudiante(s): Ana Belen Macas Caicer, Juan Jose Berrio Velasquez
- Rama evaluada: main
- Fecha de evaluación: 2026-03-23

## Tabla de Calificación

| # | Criterio | Peso | Puntaje (1-5) | Nota ponderada |
|---|---|---|---|---|
| 1 | Modelado de dominio | 25% | 4 | 1.00 |
| 2 | Relaciones entre entidades | 15% | 5 | 0.75 |
| 3 | Uso de Enums | 15% | 4 | 0.60 |
| 4 | Manejo de estados | 5% | 4 | 0.20 |
| 5 | Tipos de datos | 5% | 2 | 0.10 |
| 6 | Separación Usuario vs Cliente | 10% | 5 | 0.50 |
| 7 | Bitácora | 5% | 3 | 0.15 |
| 8 | Reglas básicas de negocio | 5% | 2 | 0.10 |
| 9 | Estructura del proyecto | 10% | 4 | 0.40 |
| 10 | Repositorio | 10% | 2 | 0.20 |
| **TOTAL** | | **100%** | | **4.00** |

## Penalizaciones
- Ninguna

## Bonus
- Herencia correcta (`Client` → `NaturalPerson` / `Company`): +0.2
- Código limpio (Lombok, nombres claros, estructura consistente): +0.2
- Nombres claros y consistentes (inglés, notación camelCase): +0.1

## Nota Final: 4.5 / 5.0

> Cálculo: 4.00 + 0.2 + 0.2 + 0.1 = 4.5

---

## Análisis por Criterio

### 1. Modelado de dominio (Puntaje: 4)
Jerarquía de herencia excelente: `Person` (abstracta) → `Client` (abstracta) → `NaturalPerson` y `Company`. `User` extiende `Person` de forma independiente. Todas las entidades clave presentes: `BankAccount`, `Loan`, `Transfer`, `OperationLog`, `BankProduct`. `Company` sin embargo es anémica — solo tiene un campo `SistemRole role` pero le faltan atributos propios de empresa como `companyName` o `taxId` (NIT).

### 2. Relaciones entre entidades (Puntaje: 5)
Las relaciones se modelan con referencias de objetos reales:
- `BankAccount` → `Client` (directo)
- `Loan` → `Client` + `BankAccount` (directo)
- `Transfer` → `BankAccount` (origen/destino) + `User` (creador/aprobador)
- `OperationLog` → `User` + `BankAccount`
- Herencia clara: `NaturalPerson extends Client`, `Company extends Client`, `User extends Person`

### 3. Uso de Enums (Puntaje: 4)
Enums implementados: `SistemRole` ✓, `UserStatus` ✓, `AccountType` ✓, `AccountStatus` ✓, `LoanStatus` ✓, `LoanType` ✓, `TransferStatus` ✓, `ProductCategory` ✓, `ProductApproval` ✓. Falta: enum de `Moneda`/`Currency` — el campo `coin` en `BankAccount` es `String`. Pequeña omisión en un proyecto de alto nivel.

### 4. Manejo de estados (Puntaje: 4)
Estados modelados con enums en todas las entidades relevantes: `AccountStatus`, `LoanStatus`, `TransferStatus`, `UserStatus`. No hay métodos explícitos de transición pero esto es permitido según la rúbrica.

### 5. Tipos de datos (Puntaje: 2)
Se usa `Long` para montos (`balance`, `amount`, `amountRequested`, `amountApproved`, `interestRate`) en lugar de `BigDecimal`, lo cual es inadecuado para precisión financiera. `LocalDate` correctamente usado para fechas. `coin` es `String` en lugar de un enum de moneda.

### 6. Separación Usuario vs Cliente (Puntaje: 5)
Separación arquitectónica impecable: `User extends Person` (con credenciales, rol, estado); `Client extends Person` (abstracta base) → `NaturalPerson`, `Company`. Las dos jerarquías son completamente independientes aunque comparten la base `Person`.

### 7. Bitácora (Puntaje: 3)
`OperationLog` tiene campos definidos (logId, operationType, operationDate, user, userRole, bankAccount). Sin embargo el campo `details` es `String` en lugar de `Map<String, Object>` — no es flexible para datos variables. La estructura es rígida para distintos tipos de operación.

### 8. Reglas básicas de negocio (Puntaje: 2)
No se implementan validaciones ni reglas en los constructores. Lombok genera constructores vacíos (@NoArgsConstructor) sin lógica de dominio. No hay métodos que hagan cumplir reglas como validar saldo, verificar estado de cuenta, etc.

### 9. Estructura del proyecto (Puntaje: 4)
Proyecto Spring Boot con Maven, estructura `src/main/java/app/domain/`. Separación en paquetes `models` y `enums`. Podría mejorarse con subpaquetes por agregado (`client/`, `account/`, `loan/`, `transfer/`) como hace DDD, pero la estructura actual es buena.

### 10. Repositorio (Puntaje: 2)
- **Nombre:** `Bank` — en inglés, simple.
- **README:** Solo lista los colaboradores. Falta materia, tecnología, descripción del proyecto, instrucciones de ejecución.
- **Commits:** 4 commits, algunos con buenas descripciones ("Refactor LoanStatus enum...") pero uno con "por fin D:" — no profesional.
- **Ramas:** Solo `main`, sin `develop`.
- **Tag:** Ninguno.

---

## Fortalezas
- Excelente jerarquía de herencia: `Person → Client → NaturalPerson/Company` y `Person → User`.
- Relaciones entre entidades con referencias de objeto real — modelo navegable.
- Uso extenso y correcto de enums para casi todos los catálogos.
- Código limpio con Lombok, nomenclatura consistente en inglés.
- Separación clara entre `User` y `Client`.
- Spring Boot + Maven con estructura de paquetes coherente.

## Oportunidades de mejora
- Agregar `BigDecimal` para todos los campos monetarios (`balance`, `amount`, tasas).
- Crear enum `Currency`/`Moneda` para el campo `coin` en `BankAccount`.
- Enriquecer la clase `Company` con atributos propios (companyName, taxId, legalRepresentative).
- Reemplazar `String details` en `OperationLog` por `Map<String, Object>` para flexibilidad.
- Agregar validaciones en constructores o métodos de negocio.
- Mejorar el README con información completa sobre la materia, tecnología y ejecución.
- Crear rama `develop` y usar mensajes de commit profesionales (ADD/CHG/FIX).
- Crear tag de entrega.
