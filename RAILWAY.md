# 🚂 Despliegue en Railway

## Configuración de Variables de Entorno

Para desplegar la aplicación Cashly en Railway, necesitas configurar las siguientes variables de entorno:

### 1. Base de Datos MySQL

Railway puede proporcionar MySQL de dos formas:

#### Opción A: Usar MySQL de Railway (Recomendado)
1. En tu proyecto de Railway, agrega un servicio MySQL:
   - Click en **"+ New"** → **"Database"** → **"Add MySQL"**
2. Railway creará automáticamente estas variables en el servicio MySQL:
   - `MYSQL_URL`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`
   - `MYSQL_DATABASE`
   - `MYSQL_HOST`
   - `MYSQL_PORT`

3. En tu servicio de la **aplicación Spring Boot**, agrega estas variables referenciando el servicio MySQL:

```bash
SPRING_DATASOURCE_URL=${{MySQL.MYSQL_URL}}
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
DB_NAME=${{MySQL.MYSQL_DATABASE}}
```

> **Nota**: Reemplaza `MySQL` con el nombre real de tu servicio MySQL en Railway.

#### Opción B: Base de Datos Externa
Si usas una base de datos MySQL externa (como PlanetScale, AWS RDS, etc.):

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/<database>?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
DB_NAME=nombre_de_tu_base_de_datos
```

### 2. Configuración JWT

```bash
JWT_SECRET=tu_secreto_super_seguro_de_al_menos_256_bits
JWT_ACCESS_TOKEN_EXPIRATION=900000
JWT_REFRESH_TOKEN_EXPIRATION=604800000
```

> ⚠️ **IMPORTANTE**: Genera un secret seguro para producción. Puedes usar:
> ```bash
> openssl rand -base64 32
> ```

### 3. Configuración de Spring

```bash
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

### 4. Configuración de Logging

```bash
LOG_LEVEL=INFO
```

### 5. Configuración CORS (Opcional)

Si tu frontend está en otro dominio:

```bash
CORS_ALLOWED_ORIGINS=https://tu-frontend.com,https://www.tu-frontend.com
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
```

---

## Pasos de Despliegue

### 1. Preparar el Proyecto en Railway

1. Ve a [railway.app](https://railway.app) e inicia sesión
2. Click en **"New Project"**
3. Selecciona **"Deploy from GitHub repo"**
4. Selecciona tu repositorio `cashly-api`

### 2. Configurar el Servicio de Base de Datos

1. En tu proyecto, click en **"+ New"**
2. Selecciona **"Database"** → **"Add MySQL"**
3. Railway creará el servicio MySQL automáticamente

### 3. Configurar Variables de Entorno en la App

1. Click en tu servicio de la aplicación Spring Boot
2. Ve a la pestaña **"Variables"**
3. Click en **"RAW Editor"** (más fácil para copiar/pegar)
4. Agrega las variables siguiendo el formato de la **Opción A** o **Opción B** de arriba

Ejemplo completo:

```bash
# Base de Datos (referenciando el servicio MySQL de Railway)
SPRING_DATASOURCE_URL=${{MySQL.MYSQL_URL}}
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
DB_NAME=${{MySQL.MYSQL_DATABASE}}

# JWT
JWT_SECRET=MiSecretSuperSeguroParaProduccion123456789
JWT_ACCESS_TOKEN_EXPIRATION=900000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# Spring
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# Logging
LOG_LEVEL=INFO
```

### 4. Configurar el Build

Railway detectará automáticamente que es un proyecto Maven y usará el `Dockerfile` si está presente.

Si necesitas configurar manualmente:
- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -jar target/cashly-api-0.0.1-SNAPSHOT.jar`

### 5. Deploy

Railway desplegará automáticamente cuando:
- Hagas push a la rama configurada (por defecto `main`)
- O manualmente haciendo click en **"Deploy"**

---

## Verificar el Despliegue

1. Una vez desplegado, Railway te dará una URL pública
2. Puedes ver los logs en tiempo real en la pestaña **"Deployments"**
3. Verifica que la aplicación se conecte correctamente:
   ```bash
   curl https://tu-app.up.railway.app/actuator/health
   ```

---

## Troubleshooting

### Error: "Communications link failure"
- ✅ Verifica que las variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, y `SPRING_DATASOURCE_PASSWORD` estén correctamente configuradas
- ✅ Asegúrate de estar referenciando el servicio MySQL correctamente: `${{MySQL.VARIABLE}}`
- ✅ Verifica que el servicio MySQL esté corriendo

### Error: "Failed to configure a DataSource"
- ✅ Asegúrate de tener todas las variables de datasource configuradas
- ✅ Verifica que la URL tenga el formato correcto: `jdbc:mysql://host:port/database`

### La aplicación no inicia
- 📋 Revisa los logs en Railway
- 🔍 Busca errores de configuración o variables faltantes
- ⚙️ Verifica que el `SERVER_PORT` sea `8080` (Railway lo mapea automáticamente)

---

## Comandos Útiles

### Ver logs en tiempo real
En el dashboard de Railway → Tu servicio → **"Deployments"** → Click en el deployment activo

### Redeploy manual
En el dashboard → **"Deployments"** → Click en los tres puntos → **"Redeploy"**

### Variables de entorno
En el dashboard → Tu servicio → **"Variables"**

---

## Recursos

- [Railway Docs](https://docs.railway.app/)
- [Railway MySQL](https://docs.railway.app/databases/mysql)
- [Spring Boot on Railway](https://docs.railway.app/guides/spring-boot)
