import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java")
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "cc.cerial.cerialplugintemplate"
version = "1.0-SNAPSHOT"
description = "This is the plugin description."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

// IMPORTANT: Set this to the Minecraft version you will develop against.
val mcVer = "1.21.4"

// What to use on dependencies?
//  - If the dependency is a SHADED LIBRARY, use implementation(), and relocate in ShadowJar below
//    (if required).
//  - If the dependency is a RUNTIME LIBRARY, use paperLibrary(). The library will load
//    on the startup of the server, and won't be included in the plugin.
//  - If the dependency is a PLUGIN (or Paper/Purpur API), use compileOnly().l
dependencies {
    compileOnly("io.papermc.paper:paper-api:$mcVer-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// Configuration
// NOTE: The name (in settings.gradle.kts), description and version properties are taken from Gradle.
//       There is no need to change these properties yourself in the "paper" section.
// An example for values is at https://github.com/eldoriarpg/plugin-yml/wiki/Paper#kotlin.
paper {
    // Keep this as true!
    generateLibrariesJson = true

    // The basic plugin information, such as the JavaPlugin extending class, API version, authors and website.
    // As noted above, name, description and version are Gradle properties. These should be changed
    // from Gradle.
    main =       "cc.cerial.cerialplugintemplate.CerialPluginTemplate"
    loader =     "cc.cerial.cerialplugintemplate.CerialLibraryLoader"
    apiVersion = mcVer
    authors =    listOf("oCerial")
    website =    "[Insert GitHub page here]"

    // Below this, register any permissions and plugin dependencies.
}

// Plugin Server
// IMPORTANT: This section is for relocating; the built-in plugin server does not support relocation,
//            so relocation will only be done if the plugin server won't be run.
//            If we relocate without checking if it's a plugin server, the server will crash while hot loading.
tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    // Only relocate when not running test server.
    if (!gradle.startParameter.taskNames.contains("runServer")) {
//        relocate("some.random.class", "some.other.class")
    }
}

// Make shadowJar run at all times.
tasks {
    build {
        dependsOn(shadowJar)
    }
}

// We will run the server version we will compile against.
tasks.withType<RunServer> {
    minecraftVersion(mcVer)
}

// The server properties.
tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        @Suppress("UnstableApiUsage")
        // This means you need to run the debugger server using the Jetbrains Runtime JDK.
        // It is recommended anyway, because it has much more features for the debugger, which
        // allows for seamless debugging.
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-Xms768M", "-Xmx1536M", "-XX:+AllowEnhancedClassRedefinition", "-XX:+UseG1GC",
        "-XX:+ParallelRefProcEnabled", "-XX:MaxGCPauseMillis=200", "-XX:+UnlockExperimentalVMOptions",
        "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch", "-XX:G1HeapWastePercent=5", "-XX:G1MixedGCCountTarget=4",
        "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=90",
        "-XX:G1RSetUpdatingPauseTimePercent=5", "-XX:SurvivorRatio=32", "-XX:+PerfDisableSharedMem",
        "-XX:MaxTenuringThreshold=1", "-Dusing.aikars.flags=https://mcflags.emc.gs", "-Daikars.new.flags=true",
        "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M", "-XX:G1ReservePercent=20")
}