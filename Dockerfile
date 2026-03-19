ARG IMAGE_TAG_JRE=bellsoft/liberica-runtime-container:jre-17-slim-musl
ARG IMAGE_TAG_MAVEN=library/maven:3.9.1-eclipse-temurin-17

FROM $IMAGE_TAG_MAVEN AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip=true


#Run Stage
FROM $IMAGE_TAG_JRE AS layers
WORKDIR /layers
COPY --from=build /build/target/*.jar kalita-service.jar
RUN java -Djarmode=layertools -jar kalita-service.jar extract


FROM ${IMAGE_TAG_JRE}
WORKDIR /tmp
COPY --from=layers /layers/dependencies/ ./
COPY --from=layers /layers/snapshot-dependencies/ ./
COPY --from=layers /layers/spring-boot-loader/ ./
COPY --from=layers /layers/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]