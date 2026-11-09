# Estágio 1: build com JDK 21 + Maven (compila, roda os testes e empacota)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Dependências em camada própria para aproveitar cache entre builds
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B clean package

# Estágio 2: runtime só com JRE 21, sem Maven, JDK ou código-fonte
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=build /workspace/target/app.jar app.jar

USER app

# Porta declarada por padrão do projeto; a aplicação atual é de console
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
