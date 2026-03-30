# ============================================================
# STAGE 1 : Compilation Maven
# ============================================================
FROM maven:3.9.11-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copier pom.xml séparément pour profiter du cache des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copier les sources et compiler
COPY src ./src
RUN mvn clean package -DskipTests -q

# ============================================================
# STAGE 2 : Image de production (légère)
# ============================================================
FROM eclipse-temurin:17-jre-alpine AS production

WORKDIR /app

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copier uniquement le WAR depuis le stage builder
COPY --from=builder /build/target/*.war app.war

# Donner les droits à l'utilisateur non-root
RUN chown appuser:appgroup app.war

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.war"]