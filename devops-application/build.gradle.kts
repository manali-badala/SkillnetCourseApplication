plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ie.pt"
version = "0.0.1-SNAPSHOT"
description = "devops-application"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
            implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
            implementation ("org.hibernate.orm:hibernate-community-dialects")
            implementation ("org.springframework.boot:spring-boot-starter-webmvc")
            implementation ("org.springframework.boot:spring-boot-starter-webservices")
            compileOnly ("org.projectlombok:lombok")
            developmentOnly ("org.springframework.boot:spring-boot-devtools")
            runtimeOnly ("org.xerial:sqlite-jdbc")
            annotationProcessor ("org.projectlombok:lombok")

            testImplementation ("org.springframework.boot:spring-boot-starter-test")

            testImplementation ("org.springframework.boot:spring-boot-starter-webmvc-test")
            testImplementation ("org.springframework.boot:spring-boot-starter-webservices-test")
            testCompileOnly ("org.projectlombok:lombok")
            testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
            testAnnotationProcessor ("org.projectlombok:lombok")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
