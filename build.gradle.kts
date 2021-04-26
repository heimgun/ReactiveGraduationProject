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
java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
  mavenCentral()
}

sourceSets {
  create("integrationTest") {
    java.srcDirs("src/testIntegration/kotlin")
    resources.srcDir("src/testIntegration/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
  }

  create("unitTest") {
    java.srcDirs("src/test/kotlin")
    resources.srcDir("src/test/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
  }
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}

	this["integrationTestImplementation"].extendsFrom(configurations["testImplementation"])
	this["integrationTestRuntime"].extendsFrom(configurations["testRuntime"])
}

dependencies {
  // Spring
  implementation("org.springframework.boot:spring-boot-starter-webflux")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  // Kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.3")


  // Other
  implementation("io.projectreactor:reactor-core:3.4.3")
	implementation("com.algorand:algosdk:1.5.1")

  // Tests
  testImplementation("io.projectreactor:reactor-test:3.4.3")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.5.0-RC")
  testImplementation("org.mockito.kotlin:mockito-kotlin:3.1.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    // Compiler arg jsr305 adds support for annotation-based restrictions on
    // inputs, should we want that. E.g. min/max length of specific args and so on.
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "15"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

// Create test integrationTest and unitTest suite
fun createTestTask(taskName: String, sourceSetName: String) {
  tasks.create<Test>(taskName) {
    testClassesDirs = sourceSets[sourceSetName].output.classesDirs
    classpath = sourceSets[sourceSetName].runtimeClasspath
  }
}
createTestTask("integrationTest", "integrationTest")
createTestTask("unitTest", "test")

// Make the test task run both sets
tasks.test {
  dependsOn(tasks["integrationTest"])
}