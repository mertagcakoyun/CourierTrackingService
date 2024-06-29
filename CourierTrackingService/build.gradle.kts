
plugins {
            id("java")
            id("org.springframework.boot") version "3.2.5"
            id("io.spring.dependency-management") version "1.1.0"
        }

group = "com.tracker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.24")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.mockito:mockito-core:4.0.0")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.test {
    useJUnitPlatform()
}