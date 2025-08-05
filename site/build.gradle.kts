import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.title
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
//    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
    alias(libs.plugins.kotlin.serialization)
    id("com.codingfeline.buildkonfig") version "0.15.1"
    id("org.flywaydb.flyway") version "10.0.0"
}

group = "com.probro.khoded"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            head.add {
                title("Khoded | Custom Web Development & Digital Solutions for Entrepreneurs")
                link(rel = "cannonical", href = "https://khoded.onrender.com")
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
    jvmToolchain(21)
    configAsKobwebApplication("khoded", includeServer = true)


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
        val jvmMain by getting {
            dependencies {
                compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime

                // Ktor Client for lightweight email HTTP requests (replaces Kotlin Mailer)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Additional Ktor dependencies for email service  
                implementation("io.ktor:ktor-client-plugins:3.0.3")

                // JDBI for lightweight PostgreSQL database access
                implementation("org.jdbi:jdbi3-core:3.45.1")
                implementation("org.jdbi:jdbi3-postgres:3.45.1")
                implementation("org.jdbi:jdbi3-kotlin:3.45.1")
                implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.45.1")

                // HikariCP for connection pooling
                implementation("com.zaxxer:HikariCP:5.1.0")
                implementation("org.postgresql:postgresql:42.7.3")

                // HTTP Status constants
                implementation("org.apache.httpcomponents:httpcore:4.4.16")

                // Gmail api dependencies - temporarily disabled for performance testing
                // The new KtorEmailService replaces these heavy dependencies
                // implementation("com.google.api-client:google-api-client:2.0.0")
                // implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
                // implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
                // implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
                // implementation("com.google.auth:google-auth-library-credentials:1.16.1")
                // implementation("com.google.http-client:google-http-client:1.43.1")

                //TODO: LOOK UP flyway GRADLE DEPENDENCIES for database migrations

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
    }
}

// Load properties for build configuration
val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

buildkonfig {
    packageName = "com.probro.khoded"
    objectName = "KhodedConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"

    defaultConfigs {
        //Gmail Config
        buildConfigField(FieldSpec.Type.STRING, "Type", properties.getProperty("type"))
        buildConfigField(FieldSpec.Type.STRING, "ProjectID", properties.getProperty("project_id"))
        buildConfigField(FieldSpec.Type.STRING, "PrivateKeyID", properties.getProperty("private_key_id"))
        buildConfigField(FieldSpec.Type.STRING, "PrivateKey", properties.getProperty("private_key"))
        buildConfigField(FieldSpec.Type.STRING, "ClientEmail", properties.getProperty("client_email"))
        buildConfigField(FieldSpec.Type.STRING, "ClientID", properties.getProperty("client_id"))
        buildConfigField(FieldSpec.Type.STRING, "AuthUri", properties.getProperty("auth_uri"))
        buildConfigField(FieldSpec.Type.STRING, "TokenUri", properties.getProperty("token_uri"))
        buildConfigField(
            FieldSpec.Type.STRING, "AuthProviderUrl", properties.getProperty("auth_provider_x509_cert_url")
        )
        buildConfigField(FieldSpec.Type.STRING, "ClientCertUrl", properties.getProperty("client_x509_cert_url"))
        buildConfigField(FieldSpec.Type.STRING, "UniversDomain", properties.getProperty("universe_domain"))

        //Postgres values - Using environment variables for security
        buildConfigField(
            FieldSpec.Type.STRING,
            "devUri",
            "\"${properties.getProperty("dev_database_uri", "jdbc:postgresql://localhost:5432/khoded_dev")}\""
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "devUsername",
            "\"${properties.getProperty("dev_database_username", "khoded_dev_user")}\""
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "devPassword",
            "\"${properties.getProperty("dev_database_password", "dev_password_change_me")}\""
        )
        buildConfigField(FieldSpec.Type.STRING, "prodUri", "\"${properties.getProperty("prod_database_uri", "")}\")")
        buildConfigField(FieldSpec.Type.STRING, "prodUsername", "\"${properties.getProperty("prod_database_username", "")}\")")
        buildConfigField(FieldSpec.Type.STRING, "prodPassword", "\"${properties.getProperty("prod_database_password", "")}\"")
    }
}

flyway {
    driver = "org.postgresql.driver"
    url = System.getenv("FLYWAY_URL") ?: properties.get("flyway_url").toString()
    user = System.getenv("FLYWAY_USER") ?: properties.get("flyway_user").toString()
    password = System.getenv("FLYWAY_PASSWORD") ?: properties.get("flyway_password").toString()
    schemas = arrayOf("khoded_base_state")
    defaultSchema = "khoded_base_state"
}
