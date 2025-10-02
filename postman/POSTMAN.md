# Colección de Postman - Cashly API

Esta colección incluye todos los endpoints disponibles en la API de Cashly.

## 📦 Archivos

- **`Cashly-API.postman_collection.json`**: Colección completa con todos los endpoints
- **`Cashly-Local.postman_environment.json`**: Entorno de desarrollo local

## 🚀 Importar en Postman

### Opción 1: Importar archivos

1. Abre Postman
2. Click en **Import** (botón superior izquierdo)
3. Arrastra los dos archivos JSON o selecciónalos:
   - `Cashly-API.postman_collection.json`
   - `Cashly-Local.postman_environment.json`
4. Click en **Import**

### Opción 2: Usar desde línea de comandos con Newman

```bash
# Instalar Newman (CLI de Postman)
npm install -g newman

# Ejecutar la colección
newman run Cashly-API.postman_collection.json \
  -e Cashly-Local.postman_environment.json
```

## 🔧 Configuración

### 1. Seleccionar el entorno

En Postman, selecciona **"Cashly Local"** en el dropdown de entornos (esquina superior derecha).

### 2. Variables disponibles

El entorno incluye las siguientes variables que se actualizan automáticamente:

| Variable | Descripción |
|----------|-------------|
| `baseUrl` | URL base de la API (http://localhost:8080) |
| `accessToken` | Token JWT de acceso (se actualiza automáticamente al hacer login) |
| `refreshToken` | Token de refresh (se actualiza automáticamente al hacer login) |
| `userId` | ID del usuario autenticado |
| `accountId` | ID de la última cuenta creada |
| `transactionId` | ID de la última transacción creada |
| `expenseId` | ID del último gasto creado |
| `incomeId` | ID del último ingreso creado |

## 📝 Flujo de uso recomendado

### 1. Verificar que la API está activa

```
GET /actuator/health
```

### 2. Registrar un usuario

```
POST /api/auth/register
```

**Body:**
```json
{
  "email": "tu@email.com",
  "password": "Password123!",
  "firstName": "Tu",
  "lastName": "Nombre"
}
```

> 💡 El `userId` se guarda automáticamente en las variables de entorno.

### 3. Iniciar sesión

```
POST /api/auth/login
```

**Body:**
```json
{
  "email": "tu@email.com",
  "password": "Password123!"
}
```

> 💡 Los tokens (`accessToken` y `refreshToken`) se guardan automáticamente y se usan en las peticiones siguientes.

### 4. Crear una cuenta

```
POST /api/accounts
```

**Body:**
```json
{
  "userId": "{{userId}}",
  "name": "Mi Cuenta",
  "type": "CHECKING",
  "currency": "USD",
  "initialBalance": 1000.00
}
```

> 💡 El `accountId` se guarda automáticamente.

### 5. Crear transacciones, gastos, ingresos

Ahora puedes usar los demás endpoints. Todos usan autenticación Bearer Token automáticamente.

## 🔐 Autenticación

La colección está configurada para usar **Bearer Token** automáticamente en todos los endpoints protegidos.

El token se toma de la variable `{{accessToken}}` que se actualiza automáticamente al hacer login o refresh.

### Renovar el token

Si el token expira, usa:

```
POST /api/auth/refresh
```

El nuevo token se guardará automáticamente.

## 📚 Endpoints disponibles

### Health Check
- ✅ `GET /actuator/health` - Estado de la API

### Authentication
- ✅ `POST /api/auth/register` - Registrar usuario
- ✅ `POST /api/auth/login` - Iniciar sesión
- ✅ `POST /api/auth/refresh` - Renovar token
- ✅ `POST /api/auth/logout` - Cerrar sesión

### Accounts (Cuentas)
- ✅ `POST /api/accounts` - Crear cuenta
- ✅ `GET /api/accounts` - Listar cuentas
- ✅ `GET /api/accounts/{id}` - Obtener cuenta
- ✅ `PUT /api/accounts/{id}` - Actualizar cuenta
- ✅ `PATCH /api/accounts/{id}/deactivate` - Desactivar cuenta
- ✅ `DELETE /api/accounts/{id}` - Eliminar cuenta

### Transactions (Transacciones)
- ✅ `POST /api/transactions` - Crear transacción
- ✅ `GET /api/transactions` - Listar transacciones
- ✅ `GET /api/transactions/{id}` - Obtener transacción
- ✅ `GET /api/transactions/account/{accountId}` - Por cuenta
- ✅ `PUT /api/transactions/{id}` - Actualizar estado
- ✅ `POST /api/transactions/{id}/cancel` - Cancelar transacción

### Expenses (Gastos)
- ✅ `POST /api/expenses` - Crear gasto
- ✅ `GET /api/expenses` - Listar gastos
- ✅ `GET /api/expenses/{id}` - Obtener gasto
- ✅ `PUT /api/expenses/{id}` - Actualizar gasto
- ✅ `DELETE /api/expenses/{id}` - Eliminar gasto

### Expenses Analytics (Analítica de Gastos)
- ✅ `GET /api/expenses/analytics/spending-trend` - Tendencia de gastos
- ✅ `GET /api/expenses/analytics/budget-utilization` - Utilización de presupuesto
- ✅ `GET /api/expenses/analytics/category-analysis` - Análisis por categoría
- ✅ `GET /api/expenses/analytics/monthly-average` - Promedio mensual

### Incomes (Ingresos)
- ✅ `POST /api/incomes` - Crear ingreso
- ✅ `GET /api/incomes` - Listar ingresos
- ✅ `GET /api/incomes/{id}` - Obtener ingreso
- ✅ `PUT /api/incomes/{id}` - Actualizar ingreso
- ✅ `DELETE /api/incomes/{id}` - Eliminar ingreso

### Incomes Analytics (Analítica de Ingresos)
- ✅ `GET /api/incomes/analytics/growth-analysis` - Análisis de crecimiento

## 🎯 Tips

1. **Orden de ejecución**: Los endpoints están organizados en carpetas lógicas. Sigue el orden:
   - Health Check → Authentication → Accounts → Transactions/Expenses/Incomes

2. **Scripts automáticos**: Los scripts de prueba guardan automáticamente los IDs de las respuestas, así puedes usarlos en las siguientes peticiones.

3. **Token automático**: No necesitas copiar/pegar el token manualmente. Se actualiza automáticamente.

4. **Ejemplos de datos**: Todos los endpoints incluyen ejemplos de datos en el body.

## 🐛 Troubleshooting

### Error 401 (Unauthorized)
- Verifica que hayas hecho login
- El token podría haber expirado, usa el endpoint de refresh

### Variables no se actualizan
- Verifica que tienes seleccionado el entorno "Cashly Local"
- Revisa la pestaña "Tests" de cada request para ver los scripts

### Error de conexión
- Verifica que la API esté corriendo: `curl http://localhost:8080/actuator/health`
- Verifica el puerto en `baseUrl`

## 📞 Soporte

Si encuentras algún problema o falta algún endpoint, por favor crea un issue en el repositorio.

---

**Versión**: 1.0.0  
**Última actualización**: 2 de octubre de 2025
