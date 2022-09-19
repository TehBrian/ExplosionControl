plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.kyori.indra.checkstyle") version "2.2.0"
}

group = "xyz.tehbrian"
version = "0.1.1"
description = "Provides fine-grained control over every exploding thing in Minecraft."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://repo.thbn.me/releases/") {
        name = "thbn"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:5.1.0")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("dev.tehbrian:tehlib-paper:0.3.1")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    processResources {
        expand("version" to project.version, "description" to project.description)
    }

    shadowJar {
        archiveBaseName.set("ExplosionControl")
        archiveClassifier.set("")

        val libsPackage = "${project.group}.${project.name}.libs"
        relocate("com.google.inject", "$libsPackage.guice")
        relocate("cloud.commandframework", "$libsPackage.cloud")
        relocate("org.spongepowered.configurate", "$libsPackage.configurate")
        relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
    }

    runServer {
        minecraftVersion("1.18.2")
    }
}
