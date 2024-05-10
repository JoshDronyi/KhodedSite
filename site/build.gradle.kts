import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
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
            description.set("Powered by Kobweb")
        }
    }
}

kotlin {
    jvmToolchain(19)
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
                implementation(libs.kobweb.silk)
                implementation(libs.silk.icons.fa)
                implementation("com.github.stevdza-san:KotlinBootstrap:0.0.8")
            }
        }
        val jvmMain by getting {
            dependencies {
                compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime

                //Kotlin Mailer dependencies (Jakarta)
                implementation(libs.mailer.core)
                implementation(libs.mailer.client)

                //Gmail api dependencies
                implementation("com.google.api-client:google-api-client:2.0.0")
                implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
                implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
                implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
                implementation("com.google.auth:google-auth-library-credentials:1.16.1")
                implementation("com.google.http-client:google-http-client:1.43.1")

                //TODO: LOOK UP flyway GRADLE DEPENDENCIES for database migrations

                // Postgresdb
                implementation("org.postgresql:postgresql:42.7.1")
                // Hikari (for Connection pooling)
                implementation("com.zaxxer:HikariCP:5.1.0")


                //Exposed (Jetbrains library for database connection)
                val exposedVersion = "0.45.0"
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                // use a dao to have it be similar to android logic
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")

            }
        }
    }
}

buildkonfig {
    packageName = "com.probro.khoded"
    objectName = "KhodedConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

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

        //Postgres values
        buildConfigField(FieldSpec.Type.STRING, "devUri", "dev_Uri")
        buildConfigField(FieldSpec.Type.STRING, "devUsername", "test_user")
        buildConfigField(FieldSpec.Type.STRING, "devPassword", "test_password")
        buildConfigField(FieldSpec.Type.STRING, "prodUri", "prod_uri")
        buildConfigField(FieldSpec.Type.STRING, "prodUsername", "prod_user")
        buildConfigField(FieldSpec.Type.STRING, "prodPassword", "prod_password")
    }
}

flyway {
    driver = "org.postgresql.driver"
    url = "jdbc:postgresql://localhost:5432/khodedBackendData"
    user = "admin"
    password = "khodedData"
    schemas = arrayOf("khoded_base_state")
    defaultSchema = "khoded_base_state"
}
