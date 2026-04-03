# ════════════════════════════════════════════════════════
# ETAPA 1 — BUILD
# Usem la imatge oficial de Maven amb JDK 21 per compilar
# el projecte i generar el fitxer .jar
# ════════════════════════════════════════════════════════
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Directori de treball dins el contenidor
WORKDIR /app

# Copiem primer el pom.xml per aprofitar la cache de Docker:
# si les dependencies no canvien, no les torna a descarregar
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiem el codi font i construim el .jar sense executar tests
COPY src ./src
RUN mvn clean package -DskipTests -B


# ════════════════════════════════════════════════════════
# ETAPA 2 — RUNTIME
# Usem una imatge minima de JRE 21 per executar el .jar.
# No inclou Maven ni el codi font, imatge final mes lleugera
# ════════════════════════════════════════════════════════
FROM eclipse-temurin:21-jre-alpine

# Directori de treball a la imatge final
WORKDIR /app

# Copiem nomes el .jar generat a l'etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exposem el port que usa Spring Boot
EXPOSE 8080

# Comanda d'inici del contenidor
ENTRYPOINT ["java", "-jar", "app.jar"]