# Dockerfile optimizado para proyecto Kotlin Multiplatform - Módulo Server
# ==================================================================================

# Stage 1: Cache Gradle dependencies
FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home

# Copiar archivos de configuración principales del proyecto
COPY server/build.gradle.kts gradle.properties settings.gradle.kts /home/gradle/app/
COPY gradle /home/gradle/app/gradle

# Copiar archivos de configuración de módulos específicos
#COPY shared/build.gradle.kts /home/gradle/app/shared/build.gradle.kts
COPY server/build.gradle.kts /home/gradle/app/server/build.gradle.kts

# Crear estructura básica de directorios para evitar errores
RUN mkdir -p /home/gradle/app/server/src/main/kotlin

WORKDIR /home/gradle/app

# Descargar dependencias del proyecto completo
RUN gradle dependencies --no-daemon -q || true

# Stage 2: Build Application
FROM gradle:latest AS build

# Copiar cache de dependencias
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle

# Copiar todo el código fuente del proyecto
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Construir el fat JAR específicamente para el módulo server
# Usamos buildFatJar que está configurado en el build.gradle.kts del server
RUN gradle :server:buildFatJar --no-daemon --info

# Verificar que el JAR se generó correctamente
RUN ls -la /home/gradle/src/server/build/libs/

# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime

# Exponer puerto 8080
EXPOSE 8080

# Crear directorio de la aplicación
RUN mkdir /app

# Copiar el JAR generado desde el módulo server
COPY --from=build  /home/gradle/src/server/build/libs/lifetime-journal.jar /app/app.jar

# Punto de entrada con configuraciones de JVM optimizadas
ENTRYPOINT ["java", \
    "-server", \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=100", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "/app/app.jar"]
