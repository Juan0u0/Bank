Colección de curl para probar endpoints (host: http://localhost:8083)

1) Auth - Login (obtener token)
curl -X POST "http://localhost:8083/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"ADMINISTRATOR\",\"password\":\"123\"}"

2) Administrator - Create bank product
curl -X POST "http://localhost:8083/administrator/bank-products" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"productName\":\"Cuenta Ahorro Plus\",\"category\":\"SAVINGS\",\"status\":\"APPROVED\"}"

3) Window Employee - Create account
curl -X POST "http://localhost:8083/window-employee/accounts" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"accountType\":\"SAVINGS\",\"balance\":1000,\"currency\":\"USD\",\"clientDocument\":\"CLIENT_DOC\"}"

4) Commercial Employee - Create user
curl -X POST "http://localhost:8083/commercial-employee/users" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"document\":\"EMP_DOC_1\",\"name\":\"Empleado Comercial\",\"username\":\"COMMERCIAL_EMPLOYEE\",\"password\":\"123\",\"role\":\"COMMERCIAL_EMPLOYEE\",\"email\":\"comm@example.com\",\"cellPhone\":\"3001234567\",\"adress\":\"Calle 1\"}"

5) Company Employee - Create transfer
curl -X POST "http://localhost:8083/company-employee/transfers" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"originAccountNumber\":\"ACC123\",\"destinationAccountNumber\":\"ACC456\",\"amount\":50}"

6) Company Supervisor - Approve transfer
curl -X POST "http://localhost:8083/company-supervisor/transfers/1/approve" -H "Authorization: Bearer <TOKEN>"

7) Internal Analyst - Approve loan
curl -X POST "http://localhost:8083/internal-analyst/loans/1/approve" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"amountApproved\":500}"

8) Client - Request loan
curl -X POST "http://localhost:8083/client/loans/request" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"loanType\":\"PERSONAL\",\"amountRequested\":1000,\"interestRate\":5,\"term\":12}"

9) Client - Create transfer
curl -X POST "http://localhost:8083/client/transfers" -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" -d "{\"originAccountNumber\":\"ACC123\",\"destinationAccountNumber\":\"ACC456\",\"amount\":25}"

Notas:
- Reemplace <TOKEN> por el token obtenido en el login (Bearer <TOKEN>)
- Ajuste datos (documentos, cuentas, ids) según la base de datos y resultados al ejecutar la aplicación
