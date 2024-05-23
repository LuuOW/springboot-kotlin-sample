import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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



	//utils
	implementation("com.google.code.gson:gson:2.10.1")


	// mongo
	implementation ("org.mongodb:mongodb-driver-sync:4.11.2")
	implementation ("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation ("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.0")
	implementation("org.mongodb:bson-kotlinx:5.1.0")




	// JPA and Web
	//implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	//runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

/*
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
	jvmArgs = listOf("--add-opens", "java.base/java.io=ALL-UNNAMED")
}
*/

tasks.bootRun {


}

tasks.withType<Test> {
	useJUnitPlatform()
}
