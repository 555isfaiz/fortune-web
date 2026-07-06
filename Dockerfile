FROM eclipse-temurin:25-jre

WORKDIR /deployments

COPY --chmod=0755 target/quarkus-app/lib/ /deployments/lib/
COPY --chmod=0755 target/quarkus-app/*.jar /deployments/
COPY --chmod=0755 target/quarkus-app/app/ /deployments/app/
COPY --chmod=0755 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
