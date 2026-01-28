plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.openapi.generator") version "7.8.0"
    id("java")
}

group = "org.katyshevtseva"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
    outputDir.set("$buildDir/generated/openapi")
    modelPackage.set("com.katyshevtseva.dto")
    apiPackage.set("com.katyshevtseva.api")
    typeMappings.put("OffsetDateTime", "LocalDateTime")
    importMappings.put("java.time.OffsetDateTime", "java.time.LocalDateTime")
    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",
            "interfaceOnly" to "true",
            "useBeanValidation" to "true",
            "dateLibrary" to "java8"
        )
    )
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/openapi/src/main/java")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}