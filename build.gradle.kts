plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "net.redespring"
version = "1.0.0"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }

    repositories {
        mavenCentral()

        maven("https://repo.hpfxd.com/releases/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://jitpack.io/")
        maven("https://mvnrepository.com/artifact/com.github.azbh111/craftbukkit-1.8.8")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("com.hpfxd.pandaspigot:pandaspigot-api:1.8.8-R0.1-SNAPSHOT")
        compileOnly("com.github.azbh111:craftbukkit-1.8.8:R")
        compileOnly("org.jetbrains:annotations:24.0.1")

        val lombok = "org.projectlombok:lombok:1.18.26"
        compileOnly(lombok)
        annotationProcessor(lombok)
    }
}