plugins {
    id("java")
}

group = "ru.otus.xellnelix"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}