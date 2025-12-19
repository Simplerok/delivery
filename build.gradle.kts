import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.13.0"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    id("com.google.protobuf") version "0.9.5"
}

group = "ru.raif"
version = "0.0.1-SNAPSHOT"
description = "delivery"

val protobufVersion = "4.32.0"
val grpcVersion = "1.75.0"
val grpcKotlinVersion = "1.4.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.kafka:spring-kafka")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.arrow-kt:arrow-core:2.2.0")
    implementation("org.liquibase:liquibase-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")

    implementation("com.google.protobuf:protobuf-java:${protobufVersion}")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    implementation("com.google.protobuf:protobuf-kotlin:${protobufVersion}")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("io.grpc:grpc-netty:${grpcVersion}")
    implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.12")

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.14.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.wiremock:wiremock-standalone:3.5.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    openApiGenerate {
        generatorName.set("kotlin-spring")
        inputSpec.set("${projectDir}/src/main/resources/contract/openApi.yaml")
        outputDir.set("$projectDir/src/main/kotlin/ru/raif/delivery/adapters/in/openapi")
        apiPackage.set("api")
        modelPackage.set("api.model")
        configOptions.set(
            mapOf(
                "interfaceOnly" to "true",
                "useTags" to "true",
                "sortParamsByRequiredFlag" to "false",
                "useResponseEntity" to "false",
                "useSpringBoot3" to "true",
                "exceptionHandler" to "false",
                "skipDefaultInterface" to "true",
            ),
        )
        typeMappings.set(
            mapOf(
                Pair("DateTime", "LocalDateTime"),
            ),
        )
        importMappings.set(
            mapOf(
                Pair("LocalDateTime", "java.time.LocalDateTime"),
                Pair("ErrorResponseDto", "org.raiffeisen.tf.common.core.web.response.ErrorResponseDto"),
                Pair("CommentDto", "org.raiffeisen.tfo.comments.api.dto.comment.CommentDto"),
                Pair("AgreementName", "org.raiffeisen.tfo.agora.enums.AgreementName"),
            ),
        )
    }
}

tasks.compileKotlin.get().dependsOn("openApiGenerate")

//protobuf {
//    sourceSets {
//        main {
//            proto {
//                srcDir("src/main/proto") // Путь к вашим .proto файлам
//            }
//        }
//    }
//
//    protoc {
//        artifact = "com.google.protobuf:protoc:${protobufVersion}"
//    }
//
//    plugins {
//        id("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
//        }
//        id("grpckt") {
//            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
//        }
//    }
//
//    generateProtoTasks {
//        ofSourceSet("main").forEach { task ->
//            task.plugins {
//                id("grpc") {
//                    outputSubDir = "java"
//                }
//                id("grpckt") {
//                    outputSubDir = "kotlin"
//                }
//            }
//        }
//    }
//}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}
