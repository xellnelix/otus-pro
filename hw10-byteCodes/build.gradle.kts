plugins {
    id("java")
}

group = "ru.otus.xellnelix"
version = "unspecified"

repositories {
    mavenCentral()
}


dependencies {
    implementation("ch.qos.logback:logback-classic")
}


tasks.test {
    useJUnitPlatform()
}