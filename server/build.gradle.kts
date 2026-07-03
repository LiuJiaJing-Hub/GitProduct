plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20240303")
}

tasks.register<JavaExec>("run") {
    mainClass.set("VideoServerKt")
    classpath = sourceSets["main"].runtimeClasspath
}
