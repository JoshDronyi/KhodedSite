import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.title
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
    alias(libs.plugins.kotlin.serialization)
    id("com.codingfeline.buildkonfig") version "0.15.1"
    id("org.flywaydb.flyway") version "11.1.0"
}

group = "com.probro.khoded"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            head.add {
                title("Khoded | Custom Web Development & Digital Solutions for Entrepreneurs")
                link(rel = "canonical", href = "https://khoded.onrender.com")
                meta(name = "robots", content = "index, follow")
            }
            description.set(
                "Transform your vision with custom web development, secure hosting, and comprehensive" +
                        " branding. Specializing in tailored digital solutions for entrepreneurs. Book your free" +
                        " consultation."
            )
        }
    }
}

kotlin {
    // Use Java 21.0.8 to match Docker Eclipse Temurin version
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM) // Eclipse Temurin
    }
    configAsKobwebApplication("khoded", includeServer = true)

    // Fixed JS compilation configuration to properly include dependencies
    js(IR) {
        browser {
            commonWebpackConfig {
                mode = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode.DEVELOPMENT
                devtool = "source-map"
            }
        }
        // Disable aggressive optimizations that break module resolution
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            // Removed -Xir-minimized-member-names which breaks dependency resolution
        }
        // Ensure dependencies are compiled and included
        useCommonJs()
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                api(libs.kotlinx.serialization.json)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.compose)
                implementation(libs.kobweb.silk)
                implementation(libs.silk.foundation)

                // Icon libraries
                implementation(libs.silk.icons.fa)
                implementation(libs.silk.icons.mdi)
            }
        }
        
        val jsTest by getting {
            dependencies {
                implementation(libs.kotlin.test.js)
            }
        }
        val jvmMain by getting {
            dependencies {
                compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime

                // Ktor Client for lightweight email HTTP requests (replaces Kotlin Mailer)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Additional Ktor dependencies for email service  
                implementation(libs.ktor.client.plugins)

                // JDBI for lightweight PostgreSQL database access
                implementation(libs.jdbi.core)
                implementation(libs.jdbi.postgres)
                implementation(libs.jdbi.kotlin)
                implementation(libs.jdbi.kotlin.sqlobject)

                // HikariCP for connection pooling
                implementation(libs.hikaricp)
                implementation(libs.postgresql)

                // HTTP Status constants
                implementation(libs.httpcore)

                // Gmail api dependencies - temporarily disabled for performance testing
                // The new KtorEmailService replaces these heavy dependencies
                // implementation("com.google.api-client:google-api-client:2.0.0")
                // implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
                // implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
                // implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
                // implementation("com.google.auth:google-auth-library-credentials:1.16.1")
                // implementation("com.google.http-client:google-http-client:1.43.1")

                // Database migrations with Flyway - properly configured
                implementation(libs.flyway.core)
                implementation(libs.flyway.postgres)

                // Temporarily disable database dependencies to test the application
                // Since you're not saving data yet, we can run without database for now
                // This eliminates ~2.2MB of dependencies during testing

                // Previous heavy database stack (disabled for performance testing):
                // implementation("org.postgresql:postgresql:42.7.1")          // ~1.1MB
                // implementation("com.zaxxer:HikariCP:5.1.0")                 // ~150KB + deps
                // implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")     // ~500KB
                // implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")     // ~100KB
                // implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")      // ~200KB
                // implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")    // ~50KB
                // implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion") // ~100KB
                // implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")    // ~50KB
                // implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")     // ~50KB

                // Note: sqlx4k is Native-only (no JVM support), so we'll implement 
                // a different lightweight solution later or use in-memory storage for now

            }
        }
        
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit5)
                runtimeOnly(libs.junit.jupiter.engine)
                runtimeOnly(libs.junit.platform.launcher)
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

// SECURITY: Load configuration from environment variables only
// No longer loading from local.properties for security
fun getEnvVar(name: String, default: String = ""): String = System.getenv(name) ?: default

buildkonfig {
    packageName = "com.probro.khoded"
    objectName = "KhodedConfig"

    defaultConfigs {
        // SECURITY: All sensitive values now use environment variables only
        // Gmail Config - loaded from environment variables
        buildConfigField(FieldSpec.Type.STRING, "Type", "\"${getEnvVar("GOOGLE_TYPE", "service_account")}\"")
        buildConfigField(FieldSpec.Type.STRING, "ProjectID", "\"${getEnvVar("GOOGLE_PROJECT_ID")}\"")
        buildConfigField(FieldSpec.Type.STRING, "PrivateKeyID", "\"${getEnvVar("GOOGLE_PRIVATE_KEY_ID")}\"")
        buildConfigField(FieldSpec.Type.STRING, "PrivateKey", "\"${getEnvVar("GOOGLE_PRIVATE_KEY")}\"")
        buildConfigField(FieldSpec.Type.STRING, "ClientEmail", "\"${getEnvVar("GOOGLE_CLIENT_EMAIL")}\"")
        buildConfigField(FieldSpec.Type.STRING, "ClientID", "\"${getEnvVar("GOOGLE_CLIENT_ID")}\"")
        buildConfigField(FieldSpec.Type.STRING, "AuthUri", "\"${getEnvVar("GOOGLE_AUTH_URI", "https://accounts.google.com/o/oauth2/auth")}\"")
        buildConfigField(FieldSpec.Type.STRING, "TokenUri", "\"${getEnvVar("GOOGLE_TOKEN_URI", "https://oauth2.googleapis.com/token")}\"")
        buildConfigField(FieldSpec.Type.STRING, "AuthProviderUrl", "\"${getEnvVar("GOOGLE_AUTH_PROVIDER_CERT_URL", "https://www.googleapis.com/oauth2/v1/certs")}\"")
        buildConfigField(FieldSpec.Type.STRING, "ClientCertUrl", "\"${getEnvVar("GOOGLE_CLIENT_CERT_URL")}\"")
        buildConfigField(FieldSpec.Type.STRING, "UniversDomain", "\"${getEnvVar("GOOGLE_UNIVERSE_DOMAIN", "googleapis.com")}\"")

        // Database values - Using environment variables for security
        buildConfigField(FieldSpec.Type.STRING, "devUri", "\"${getEnvVar("DATABASE_DEV_URI", "jdbc:postgresql://localhost:5432/khoded_dev")}\"")
        buildConfigField(FieldSpec.Type.STRING, "devUsername", "\"${getEnvVar("DATABASE_DEV_USERNAME", "khoded_dev_user")}\"")
        buildConfigField(FieldSpec.Type.STRING, "devPassword", "\"${getEnvVar("DATABASE_DEV_PASSWORD")}\"")
        buildConfigField(FieldSpec.Type.STRING, "prodUri", "\"${getEnvVar("DATABASE_PROD_URI")}\"")
        buildConfigField(FieldSpec.Type.STRING, "prodUsername", "\"${getEnvVar("DATABASE_PROD_USERNAME")}\"")
        buildConfigField(FieldSpec.Type.STRING, "prodPassword", "\"${getEnvVar("DATABASE_PROD_PASSWORD")}\"")
    }
}

flyway {
    driver = "org.postgresql.driver"
    url = getEnvVar("FLYWAY_URL", "jdbc:postgresql://localhost:5432/khoded_dev")
    user = getEnvVar("FLYWAY_USER", "khoded_dev_user") 
    password = getEnvVar("FLYWAY_PASSWORD")
    schemas = arrayOf("khoded_base_state")
    defaultSchema = "khoded_base_state"
}

// Gradle build optimizations for smaller Docker images (modern 2025 approach)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        // Enable aggressive optimizations
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

// Configure JUnit5 for JVM tests
tasks.withType<Test> {
    useJUnitPlatform()
}

// Clean build artifacts to reduce Docker context size
tasks.register("cleanForDocker") {
    doFirst {
        delete("${layout.buildDirectory.get().asFile}/js")
        delete("${layout.buildDirectory.get().asFile}/classes")
        delete("${layout.buildDirectory.get().asFile}/generated")
        delete("${layout.buildDirectory.get().asFile}/tmp")
        // Build artifacts cleaned for Docker optimization
    }
}