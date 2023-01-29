plugins {
    java
    idea
    kotlin("jvm") version "1.8.0"
}

group = "daylightnebula"
version = "0.0.0"

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("reflect"))

    compileOnly(files("libs/KotlinBukkitAPI.jar"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    destinationDirectory.set(file("$rootDir\\testserver\\plugins"))
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}