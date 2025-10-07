plugins {
    kotlin("jvm")
}
tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

dependencies {
    implementation(projects.modules.domain)
    implementation(projects.modules.application)
    implementation(libs.spring.boot.starter.jpa)

    implementation("com.mysql:mysql-connector-j:8.3.0")

    implementation("org.flywaydb:flyway-core:10.20.1")
    implementation("org.flywaydb:flyway-mysql:10.20.1")

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}