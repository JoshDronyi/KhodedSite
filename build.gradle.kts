plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kobweb.application) apply false
    alias(libs.plugins.kobweb.library) apply false
    alias(libs.plugins.kobwebx.markdown) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.build.konfig) apply false
    alias(libs.plugins.flyway) apply false
}

subprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://jitpack.io")
    }
}