
plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
}

group = "com.cropyback"
version = "0.0.1-SNAPSHOT"



java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//security
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-jose:6.2.4")
	implementation("org.springframework.security:spring-security-crypto:5.6.4")
	implementation("org.springframework.security:spring-security-jwt:1.1.1.RELEASE")

	//thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	implementation("org.springframework.kafka:spring-kafka")

	// websockets
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// twilio (auth)
	implementation("com.twilio.sdk:twilio:10.2.0")

	//cache
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")


	// mail
	implementation("com.sun.mail:jakarta.mail:2.0.1")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")

	//utils
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

	implementation("com.jcraft:jsch:0.1.55")

	//rabbitmq
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.amqp:spring-rabbit-stream")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")






	// JPA and Web
	//implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	//runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
