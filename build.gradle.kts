import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("kapt") version "1.4.32"
}

group = "ru.mfp"
version = "0.0.1-SNAPSHOT"
val mapStructVersion = "1.5.3.Final"
val jjwtVersion = "0.9.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")
	implementation("org.mapstruct:mapstruct:$mapStructVersion")
	implementation("io.jsonwebtoken:jjwt:$jjwtVersion")
	implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
	runtimeOnly("org.postgresql:postgresql")
	kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

kapt {
	arguments {
		arg("mapstruct.defaultComponentModel", "spring")
		arg("mapstruct.defaultInjectionStrategy", "constructor")
		arg("mapstruct.unmappedTargetPolicy", "ignore")
		arg("mapstruct.disableBuilders", "true")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
