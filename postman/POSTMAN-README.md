# 📦 Archivos de Postman - Resumen

## ✅ Archivos Creados

He creado una colección completa de Postman para probar todos los endpoints de tu API Cashly:

### 1. **Cashly-API.postman_collection.json**
   - Colección completa con **todos los endpoints** organizados por categorías
   - Incluye **scripts automáticos** que guardan tokens y IDs
   - **Autenticación Bearer Token** configurada automáticamente
   - Ejemplos de datos en todos los requests

### 2. **Cashly-Local.postman_environment.json**
   - Variables de entorno pre-configuradas
   - Se actualizan automáticamente con los scripts
   - Listo para usar en desarrollo local

### 3. **POSTMAN.md**
   - Guía completa de instalación e importación
   - Descripción de todos los endpoints disponibles
   - Instrucciones de configuración

### 4. **POSTMAN-GUIDE.md**
   - Ejemplos detallados de cada endpoint
   - Flujo completo de pruebas paso a paso
   - Datos de ejemplo para cada request
   - Respuestas esperadas

## 🚀 Cómo Empezar (3 pasos)

### Paso 1: Importar en Postman

```bash
# Abre Postman
# Click en "Import"
# Arrastra estos archivos:
#   - Cashly-API.postman_collection.json
#   - Cashly-Local.postman_environment.json
```

### Paso 2: Seleccionar el entorno

```
En Postman, selecciona "Cashly Local" en el dropdown de entornos (esquina superior derecha)
```

### Paso 3: ¡Probar!

```
1. Ejecuta: Health Check
2. Ejecuta: Register User
3. Ejecuta: Login
4. ¡Los demás endpoints ya funcionarán automáticamente con el token!
```

## 📋 Endpoints Incluidos

### 🏥 Health Check
- ✅ GET /actuator/health

### 🔐 Authentication (4 endpoints)
- ✅ POST /api/auth/register
- ✅ POST /api/auth/login
- ✅ POST /api/auth/refresh
- ✅ POST /api/auth/logout

### 🏦 Accounts (6 endpoints)
- ✅ POST /api/accounts - Crear
- ✅ GET /api/accounts - Listar todas
- ✅ GET /api/accounts/{id} - Obtener una
- ✅ PUT /api/accounts/{id} - Actualizar
- ✅ PATCH /api/accounts/{id}/deactivate - Desactivar
- ✅ DELETE /api/accounts/{id} - Eliminar

### 💸 Transactions (6 endpoints)
- ✅ POST /api/transactions - Crear
- ✅ GET /api/transactions - Listar por usuario
- ✅ GET /api/transactions/{id} - Obtener una
- ✅ GET /api/transactions/account/{accountId} - Por cuenta
- ✅ PUT /api/transactions/{id} - Actualizar estado
- ✅ POST /api/transactions/{id}/cancel - Cancelar

### 🛒 Expenses (9 endpoints)
- ✅ POST /api/expenses - Crear
- ✅ GET /api/expenses - Listar todos
- ✅ GET /api/expenses/{id} - Obtener uno
- ✅ PUT /api/expenses/{id} - Actualizar
- ✅ DELETE /api/expenses/{id} - Eliminar
- ✅ GET /api/expenses/analytics/spending-trend
- ✅ GET /api/expenses/analytics/budget-utilization
- ✅ GET /api/expenses/analytics/category-analysis
- ✅ GET /api/expenses/analytics/monthly-average

### 💰 Incomes (6 endpoints)
- ✅ POST /api/incomes - Crear
- ✅ GET /api/incomes - Listar todos
- ✅ GET /api/incomes/{id} - Obtener uno
- ✅ PUT /api/incomes/{id} - Actualizar
- ✅ DELETE /api/incomes/{id} - Eliminar
- ✅ GET /api/incomes/analytics/growth-analysis

**Total: 38 endpoints listos para probar** 🎉

## ⚡ Características Especiales

### 🤖 Scripts Automáticos

Cada endpoint importante tiene scripts que:
- Guardan automáticamente el `accessToken` al hacer login
- Guardan automáticamente los IDs (userId, accountId, etc.)
- Los usan en las siguientes peticiones

**Ejemplo:**
```javascript
// Después de hacer login, el script guarda:
pm.collectionVariables.set('accessToken', response.accessToken);
pm.collectionVariables.set('userId', response.user.id);

// Y automáticamente se usan en las siguientes requests:
"userId": "{{userId}}"
"Authorization": "Bearer {{accessToken}}"
```

### 🔑 Autenticación Automática

No necesitas copiar/pegar tokens manualmente. Todos los endpoints protegidos usan:

```
Authorization: Bearer {{accessToken}}
```

Y el token se actualiza automáticamente al hacer login o refresh.

### 📝 Ejemplos de Datos

Todos los requests incluyen ejemplos realistas:

```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

## 🎯 Flujo de Prueba Recomendado

1. ✅ **Health Check** - Verificar que la API está viva
2. ✅ **Register User** - Crear un usuario
3. ✅ **Login** - Obtener tokens (se guardan automáticamente)
4. ✅ **Create Account** - Crear una cuenta bancaria
5. ✅ **Create Transaction** - Crear una transacción
6. ✅ **Create Expense** - Registrar un gasto
7. ✅ **Create Income** - Registrar un ingreso
8. ✅ **Get Analytics** - Ver estadísticas

## 📊 Variables Automáticas

Estas variables se actualizan automáticamente:

| Variable | Se actualiza en | Se usa en |
|----------|-----------------|-----------|
| `accessToken` | Login, Refresh | Todos los endpoints protegidos |
| `refreshToken` | Login, Refresh | Refresh, Logout |
| `userId` | Register, Login | Accounts, Transactions, Expenses, Incomes |
| `accountId` | Create Account | Transactions, Expenses, Incomes |
| `transactionId` | Create Transaction | Get, Update, Cancel Transaction |
| `expenseId` | Create Expense | Get, Update, Delete Expense |
| `incomeId` | Create Income | Get, Update, Delete Income |

## 💡 Tips

1. **Ver variables actuales**: Click en el ícono del ojo 👁️ en Postman
2. **Ejecutar toda la colección**: Usa el Runner de Postman
3. **Usar desde CLI**: Instala newman (`npm install -g newman`)
4. **Compartir con el equipo**: Exporta y comparte los archivos JSON

## 🐛 Solución de Problemas

### Error 401
- Verifica que hiciste login
- El token podría haber expirado, usa refresh

### Variables vacías
- Verifica que seleccionaste el entorno "Cashly Local"
- Ejecuta Register → Login primero

### API no responde
- Verifica que la API está corriendo: `curl http://localhost:8080/actuator/health`

## 📚 Documentación Adicional

- **POSTMAN.md** - Guía completa de uso
- **POSTMAN-GUIDE.md** - Ejemplos detallados con responses
- **README.md** - Documentación general del proyecto

---

## 🎉 ¡Listo para usar!

Ahora puedes probar TODOS los endpoints de tu API fácilmente desde Postman.

**¿Dudas?** Revisa `POSTMAN.md` y `POSTMAN-GUIDE.md` para más detalles.
