# EVALUACION - Bank

## Informacion general
- Estudiante(s): Ana Belen Macas Caicer, Juan Jose Berrio Velasquez
- Rama evaluada: develop
- Commit evaluado: 773db23
- Fecha: 2026-04-13
- Novedades de ramas: empate de recencia entre `develop` y `main` (`%ct` igual), se aplica criterio de desempate del proceso y se evalua `develop`.
- Alcance aplicado: solo capa de dominio (`bank/src/main/java/app/domain/**`).

## Tabla de calificacion

| Criterio | Peso | Puntaje (1-5) | Justificacion breve |
|---|---:|---:|---|
| 1. Modelado de dominio | 20% | 5.0 | Se evidencian entidades centrales del contexto bancario (`Client`, `NaturalPerson`, `Company`, `User`, `BankAccount`, `Loan`, `Transfer`, `BankProduct`, `OperationLog`). |
| 2. Modelado de puertos | 20% | 4.5 | Existen puertos por agregado (`ClientPort`, `UserPort`, `BankAccountPort`, `LoanPort`, `TransferPort`, `OperationLogPort`, `BankProductPort`) con contratos funcionales; hay algunos metodos CRUD genericos, pero el diseno es mayormente semantico. |
| 3. Servicios de dominio | 20% | 4.5 | Hay buena cobertura de casos de uso (creacion, aprobacion, rechazo, desembolso, ejecucion y busquedas). Faltan piezas explicitas equivalentes a algunos servicios recomendados (por ejemplo expiracion dedicada de pendientes como servicio separado). |
| 4. Enums y estados | 10% | 5.0 | Estados y catalogos principales modelados con enums (`LoanStatus`, `TransferStatus`, `AccountStatus`, `AccountType`, `Currency`, etc.). |
| 5. Reglas de negocio criticas | 10% | 4.5 | Se validan reglas clave de prestamos y transferencias (monto > 0, saldo suficiente, rol analista para aprobar/desembolsar, umbral de aprobacion empresarial). |
| 6. Bitacora y trazabilidad | 5% | 5.0 | Se evidencia servicio y puerto de bitacora con registro detallado por operacion (`RegisterOperation`, `OperationLogPort`). |
| 7. Estructura interna de dominio | 10% | 5.0 | Dominio organizado y coherente por paquetes (`models`, `enums`, `ports`, `services`, `exceptions`). |
| 8. Calidad tecnica base en domain | 5% | 4.5 | Nomenclatura mayormente clara y consistente; existen detalles menores de consistencia nominal (`SistemRole`). |

## Calculo
- Nota base ponderada: **4.70 / 5.0**
- Penalizaciones aplicadas: **ninguna**
- Bonus aplicados: **+0.3** (buen diseno de puertos reutilizables + buena trazabilidad de bitacora)
- Nota final (tope 5.0): **5.00 / 5.0**

## Hallazgos
- Fortalezas:
  - Dominio amplio y bien cubierto respecto al enunciado.
  - Servicios de dominio con validaciones de negocio relevantes.
  - Trazabilidad fuerte por bitacora con detalles operativos.
- Oportunidades de mejora:
  - Expresar de forma mas explicita algunos servicios del contexto esperado (por ejemplo expiracion de transferencias pendientes como caso de uso dedicado).
  - Homogeneizar nomenclatura de enums/roles para evitar ambiguedades.

## Recomendaciones
1. Incorporar un servicio de expiracion de transferencias pendientes por ventana de 60 minutos como caso de uso explicito.
2. Estandarizar nombres de tipos/roles en dominio para mejorar mantenibilidad.
3. Mantener el mismo nivel de detalle de auditoria en todos los flujos transaccionales.

## Nota final
**5.00 / 5.0**
