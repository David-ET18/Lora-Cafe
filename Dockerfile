# =================================================================
# ETAPA 1: Construcción (Build Stage)
# Usamos una imagen de Maven que ya tiene Java y Maven instalados.
# Esto mantiene nuestra imagen final pequeña, ya que no necesitará Maven.
# =================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker.
# Si las dependencias no cambian, Docker no las volverá a descargar.
COPY pom.xml .

# Descargamos todas las dependencias
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# Construimos la aplicación, empaquetándola en un .jar.
# -DskipTests acelera el proceso al no ejecutar las pruebas.
RUN mvn package -DskipTests


# =================================================================
# ETAPA 2: Ejecución (Runtime Stage)
# Usamos una imagen de Java muy ligera, solo con lo necesario para ejecutar.
# =================================================================
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos ÚNICAMENTE el .jar compilado desde la etapa de construcción.
# El resto (código fuente, Maven) se descarta, haciendo la imagen final muy pequeña.
COPY --from=build /app/target/loracafe-0.0.1-SNAPSHOT.jar ./app.jar

# Exponemos el puerto en el que correrá la aplicación dentro del contenedor.
# Render usará este puerto.
EXPOSE 8090

# El comando que se ejecutará cuando el contenedor se inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]