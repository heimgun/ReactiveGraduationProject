plugins {
  id("org.springframework.boot") version "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("java")
}

group = "se.newton"
version = "0.0.1-SNAPSHOT"
description = "JavaStockPriceClient"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
}

dependencies {
  // Spring
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  // Other
  implementation("io.projectreactor:reactor-core:3.4.3")
  implementation("org.projectlombok:lombok:1.18.18")

  // Tests
  testImplementation("io.projectreactor:reactor-test:3.4.3")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}