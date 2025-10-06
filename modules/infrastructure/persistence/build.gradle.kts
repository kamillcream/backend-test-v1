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
    implementation("org.flywaydb:flyway-mysql")
    implementation("mysql:mysql-connector-java:8.0.33")
    runtimeOnly(libs.database.h2)
    runtimeOnly(libs.database.mariadb)

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.database.h2)

}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}