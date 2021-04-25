plugins {
  val kotlinVersion = "1.5.0-RC"

  id("org.springframework.boot") version "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("java")

  id("org.jetbrains.kotlin.jvm") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
  annotation("javax.persistence.MappedSuperclass")
}

group = "se.newton"
version = "0.0.1-SNAPSHOT"
description = "Some kind of stock price client thing"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
}

dependencies {
  // Spring
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  // Kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // Other
  implementation("io.projectreactor:reactor-core:3.4.3")

  // Tests
  testImplementation("io.projectreactor:reactor-test:3.4.3")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.5.0-RC")
  testImplementation("org.mockito.kotlin:mockito-kotlin:3.1.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}