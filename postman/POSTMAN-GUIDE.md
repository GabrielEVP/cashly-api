# 🧪 Guía Rápida de Pruebas - Cashly API

## 📋 Pre-requisitos

1. La API debe estar corriendo en `http://localhost:8080`
2. Importar la colección en Postman (ver `POSTMAN.md`)

## 🎯 Flujo Completo de Pruebas

### Paso 1: Verificar que la API funciona ✅

**Request:** `GET /actuator/health`

**Respuesta esperada:**
```json
{
  "status": "UP"
}
```

---

### Paso 2: Registrar un nuevo usuario 👤

**Request:** `POST /api/auth/register`

**Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "active": true,
  "emailVerified": false,
  "createdAt": "2025-10-02T10:00:00"
}
```

✨ **Auto-guardado:** El `id` se guarda automáticamente en `{{userId}}`

---

### Paso 3: Iniciar sesión 🔐

**Request:** `POST /api/auth/login`

**Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "dd4b8c7f-f9b6-49c0-95be-85736630e2e7",
  "tokenType": "Bearer",
  "expiresIn": 900000,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "active": true,
    "emailVerified": false,
    "createdAt": "2025-10-02T10:00:00"
  }
}
```

✨ **Auto-guardado:** 
- `accessToken` → `{{accessToken}}`
- `refreshToken` → `{{refreshToken}}`
- Todos los endpoints siguientes usarán el token automáticamente

---

### Paso 4: Crear una cuenta bancaria 🏦

**Request:** `POST /api/accounts`

**Headers:**
- `Authorization: Bearer {{accessToken}}` (automático)

**Body:**
```json
{
  "userId": "{{userId}}",
  "name": "Cuenta de Ahorro",
  "type": "SAVINGS",
  "currency": "USD",
  "initialBalance": 5000.00,
  "description": "Mi cuenta principal de ahorros"
}
```

**Tipos de cuenta disponibles:**
- `CHECKING` - Cuenta corriente
- `SAVINGS` - Cuenta de ahorro
- `CREDIT` - Tarjeta de crédito
- `INVESTMENT` - Cuenta de inversión

**Respuesta esperada (201 Created):**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Cuenta de Ahorro",
  "type": "SAVINGS",
  "currency": "USD",
  "balance": 5000.00,
  "description": "Mi cuenta principal de ahorros",
  "isActive": true,
  "createdAt": "2025-10-02T10:05:00"
}
```

✨ **Auto-guardado:** El `id` se guarda en `{{accountId}}`

---

### Paso 5: Crear una transacción 💸

**Request:** `POST /api/transactions`

**Body:**
```json
{
  "userId": "{{userId}}",
  "accountId": "{{accountId}}",
  "type": "DEPOSIT",
  "amount": 1500.00,
  "description": "Transferencia recibida",
  "category": "INCOME",
  "date": "2025-10-02T10:00:00Z"
}
```

**Tipos de transacción:**
- `DEPOSIT` - Depósito
- `WITHDRAWAL` - Retiro
- `TRANSFER` - Transferencia

**Respuesta esperada (201 Created):**
```json
{
  "id": "t1t2t3t4-t5t6-t789-t0ab-cdef12345678",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "accountId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "type": "DEPOSIT",
  "amount": 1500.00,
  "description": "Transferencia recibida",
  "status": "PENDING",
  "category": "INCOME",
  "date": "2025-10-02T10:00:00",
  "createdAt": "2025-10-02T10:10:00"
}
```

✨ **Auto-guardado:** El `id` se guarda en `{{transactionId}}`

---

### Paso 6: Registrar un gasto 🛒

**Request:** `POST /api/expenses`

**Body:**
```json
{
  "userId": "{{userId}}",
  "accountId": "{{accountId}}",
  "amount": 250.00,
  "category": "FOOD",
  "description": "Compras del supermercado",
  "date": "2025-10-02T10:00:00Z",
  "isRecurring": false
}
```

**Categorías de gastos:**
- `FOOD` - Alimentación
- `TRANSPORT` - Transporte
- `ENTERTAINMENT` - Entretenimiento
- `UTILITIES` - Servicios
- `HEALTHCARE` - Salud
- `EDUCATION` - Educación
- `SHOPPING` - Compras
- `OTHER` - Otros

**Respuesta esperada (201 Created):**
```json
{
  "id": "e1e2e3e4-e5e6-e789-e0ab-cdef12345678",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "accountId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 250.00,
  "category": "FOOD",
  "description": "Compras del supermercado",
  "date": "2025-10-02T10:00:00",
  "isRecurring": false,
  "createdAt": "2025-10-02T10:15:00"
}
```

✨ **Auto-guardado:** El `id` se guarda en `{{expenseId}}`

---

### Paso 7: Registrar un ingreso 💰

**Request:** `POST /api/incomes`

**Body:**
```json
{
  "userId": "{{userId}}",
  "accountId": "{{accountId}}",
  "amount": 4000.00,
  "source": "SALARY",
  "description": "Salario mensual Octubre",
  "date": "2025-10-01T00:00:00Z",
  "isRecurring": true
}
```

**Fuentes de ingreso:**
- `SALARY` - Salario
- `FREELANCE` - Trabajo independiente
- `INVESTMENT` - Inversiones
- `RENTAL` - Alquiler
- `BUSINESS` - Negocio
- `GIFT` - Regalo
- `OTHER` - Otros

**Respuesta esperada (201 Created):**
```json
{
  "id": "i1i2i3i4-i5i6-i789-i0ab-cdef12345678",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "accountId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 4000.00,
  "source": "SALARY",
  "description": "Salario mensual Octubre",
  "date": "2025-10-01T00:00:00",
  "isRecurring": true,
  "createdAt": "2025-10-02T10:20:00"
}
```

✨ **Auto-guardado:** El `id` se guarda en `{{incomeId}}`

---

### Paso 8: Consultar analítica de gastos 📊

#### Tendencia de gastos

**Request:** `GET /api/expenses/analytics/spending-trend`

**Query Params:**
- `userId={{userId}}`
- `startDate=2025-01-01`
- `endDate=2025-12-31`

**Respuesta esperada:**
```json
{
  "period": {
    "start": "2025-01-01",
    "end": "2025-12-31"
  },
  "totalSpent": 3500.00,
  "averageMonthly": 350.00,
  "trend": "INCREASING",
  "monthlyData": [
    {
      "month": "2025-10",
      "amount": 250.00
    }
  ]
}
```

#### Análisis por categoría

**Request:** `GET /api/expenses/analytics/category-analysis`

**Query Params:**
- `userId={{userId}}`
- `startDate=2025-01-01`
- `endDate=2025-12-31`

**Respuesta esperada:**
```json
{
  "totalExpenses": 3500.00,
  "categories": [
    {
      "category": "FOOD",
      "amount": 1200.00,
      "percentage": 34.3,
      "count": 15
    },
    {
      "category": "TRANSPORT",
      "amount": 800.00,
      "percentage": 22.9,
      "count": 8
    }
  ]
}
```

---

### Paso 9: Consultar analítica de ingresos 📈

**Request:** `GET /api/incomes/analytics/growth-analysis`

**Query Params:**
- `userId={{userId}}`
- `months=12`

**Respuesta esperada:**
```json
{
  "period": "Last 12 months",
  "totalIncome": 48000.00,
  "averageMonthly": 4000.00,
  "growthRate": 5.5,
  "trend": "GROWING",
  "monthlyData": [
    {
      "month": "2025-10",
      "amount": 4000.00,
      "growth": 5.5
    }
  ]
}
```

---

## 🔄 Renovar el token

Si el token expira (normalmente después de 15 minutos), puedes renovarlo:

**Request:** `POST /api/auth/refresh`

**Body:**
```json
{
  "refreshToken": "{{refreshToken}}"
}
```

El nuevo `accessToken` se guardará automáticamente.

---

## 🗑️ Cerrar sesión

**Request:** `POST /api/auth/logout`

**Body:**
```json
{
  "refreshToken": "{{refreshToken}}"
}
```

**Respuesta esperada:** 204 No Content

---

## ✅ Checklist de Pruebas Completas

- [ ] Health check funciona
- [ ] Registro de usuario exitoso
- [ ] Login exitoso y token guardado
- [ ] Crear cuenta bancaria
- [ ] Listar cuentas del usuario
- [ ] Actualizar cuenta
- [ ] Crear transacción
- [ ] Listar transacciones por usuario
- [ ] Listar transacciones por cuenta
- [ ] Actualizar estado de transacción
- [ ] Cancelar transacción
- [ ] Crear gasto
- [ ] Listar gastos
- [ ] Actualizar gasto
- [ ] Eliminar gasto
- [ ] Ver analítica de gastos (tendencia)
- [ ] Ver analítica de gastos (por categoría)
- [ ] Ver analítica de gastos (promedio mensual)
- [ ] Crear ingreso
- [ ] Listar ingresos
- [ ] Actualizar ingreso
- [ ] Eliminar ingreso
- [ ] Ver analítica de ingresos
- [ ] Refresh token funciona
- [ ] Logout funciona

---

## 🎨 Tips de Postman

1. **Ver variables**: Click en el ícono del ojo 👁️ (esquina superior derecha) para ver todas las variables

2. **Consola**: `View > Show Postman Console` para ver todos los requests/responses

3. **Runner**: `Runner` para ejecutar toda la colección automáticamente

4. **Documentación**: Click derecho en la colección > `View Documentation`

5. **Compartir**: Click en los tres puntos de la colección > `Share` para compartir con tu equipo

---

**¡Listo para probar! 🚀**
