package com.probro.khoded.email

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Draft
import com.google.api.services.gmail.model.Message
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.probro.khoded.KhodedConfig
import com.probro.khoded.api.json
import com.varabyte.kobweb.api.log.Logger
import jakarta.mail.MessagingException
import jakarta.mail.Session
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.apache.commons.codec.binary.Base64
import java.io.*
import java.net.URI
import java.util.*


/*  Made from Google's GmailQuickstart.
    class to demonstrate use of Gmail list labels API
 */
object GmailQuickstart {
    /**
     * Application name.
     */
    private const val APPLICATION_NAME = "KhodedSite"

    /**
     * Global instance of the JSON factory.
     */
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Directory to store authorization tokens for this application.
     */
    private const val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES = listOf(
        GmailScopes.GMAIL_COMPOSE,
        GmailScopes.GMAIL_MODIFY,
        GmailScopes.GMAIL_LABELS
    )
    private const val CREDENTIALS_FILE_PATH =
        "credentials.json" //path to the processed resources file.

    @OptIn(ExperimentalSerializationApi::class)
    class KhodedMailer(
        private val logger: Logger
    ) {

        // Build a new authorized API client service.
        private val service by lazy {
            logger.info("Setting up http transport")
            val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
            logger.info("GOt http transport: $HTTP_TRANSPORT")
            logger.info("Heading out for credentials.")

            val credentials = getServiceCredentials(httpTransport = HTTP_TRANSPORT)
            logger.info("Got credentials of $credentials")
            Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build()
        }

        @Throws(IOException::class)
        private fun getServiceCredentials(httpTransport: NetHttpTransport): GoogleCredentials {
            logger.info("Getting credentials.")
            val resource: JsonObject = getGmailJson()
            logger.info("Got the json object fine $resource")
            val file = File.createTempFile("credentials", ".json").apply {
                bufferedWriter().apply {
                    write(json.encodeToString(resource))
                    close()
                }
            }
            logger.info("Got the file created. $file")
            logger.info("getting service account credentials from file.")
            val credential = ServiceAccountCredentials.newBuilder().setServiceAccountUser("admin@khoded.com")
                .setScopes(SCOPES)
                .setClientId(KhodedConfig.ClientID)
                .setClientEmail(KhodedConfig.ClientEmail)
                .setProjectId(KhodedConfig.ProjectID)
                .setPrivateKeyId(KhodedConfig.PrivateKeyID)
                .setPrivateKeyString(KhodedConfig.PrivateKey)
                .setTokenServerUri(URI.create(KhodedConfig.TokenUri))
                .setHttpTransportFactory { httpTransport }
            return credential.build()
        }


        /**
         * Creates an authorized Credential object.
         *
         * @param HTTP_TRANSPORT The network HTTP Transport.
         * @return An authorized Credential object.
         * @throws IOException If the credentials.json file cannot be found.
         */
        @Throws(IOException::class)
        private fun getCredentials(httpTransport: NetHttpTransport): Credential {
            // Load client secrets.
            logger.info("Getting credentials.")
            val resource: JsonObject = getGmailJson()
            logger.info("Got the json object fine $resource")
            val file = File.createTempFile("credentials", ".json").apply {
                bufferedWriter().apply {
                    write(json.encodeToString(resource))
                    close()
                }
            }
            logger.info("Got the file created. $file")
            val clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(file.inputStream()))

            logger.info("Got the client secrets. $clientSecrets")
            logger.info(
                "Building flow with variables httpTransport:$httpTransport,\n " +
                        "jsonFactor:$JSON_FACTORY,\n.clientSecrets:$clientSecrets,\n and scopes of $SCOPES"
            )
            // Build flow and trigger user authorization request.
            val flow = GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES
            )
                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
            //TODO: HANDLE THIS PART

            val receiver = LocalServerReceiver.Builder().setPort(8888).build()
            logger.info("Built the receiver object $receiver")
            val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("me")
            //returns an authorized Credential object.
            logger.info("Built the flow object $flow")
            return credential
        }

        private fun getGmailJson(): JsonObject {
            logger.info("building out json object")
            val temp = buildJsonObject {
                put(ServiceAccountKey.TYPE.value, KhodedConfig.Type)
                put(ServiceAccountKey.PROJECTID.value, KhodedConfig.ProjectID)
                put(ServiceAccountKey.PRIVATE_KEY_ID.value, KhodedConfig.PrivateKeyID)
                put(ServiceAccountKey.PRIVATE_KEY.value, KhodedConfig.PrivateKey)
                put(ServiceAccountKey.CLIENT_EMAIL.value, KhodedConfig.ClientEmail)
                put(ServiceAccountKey.CLIENT_ID.value, KhodedConfig.ClientID)
                put(ServiceAccountKey.AUTH_URI.value, KhodedConfig.AuthUri)
                put(ServiceAccountKey.TOKEN_URI.value, KhodedConfig.TokenUri)
                put(ServiceAccountKey.AUTH_PROVIDER_URL.value, KhodedConfig.AuthProviderUrl)
                put(ServiceAccountKey.CLIENT_URL.value, KhodedConfig.ClientCertUrl)
                put(ServiceAccountKey.UNIVERSE_DOMAIN.value, KhodedConfig.UniversDomain)
            }
            logger.info("json object was ${temp.jsonObject}")
            return temp.jsonObject
        }

        /**
         * Create a draft email.
         *
         * @param fromEmailAddress - Email address to appear in the from: header
         * @param toEmailAddress   - Email address of the recipient
         * @return the created draft, `null` otherwise.
         * @throws MessagingException - if a wrongly formatted address is encountered.
         * @throws IOException        - if service account credentials file not found.
         */
        @Throws(MessagingException::class, IOException::class)
        fun createDraftMessage(
            fromEmailAddress: String?,
            toEmailAddress: String?,
            messageSubject: String,
            bodyText: String
        ): Draft? {
            // Encode as MIME message
            val props = Properties()
            val session = Session.getDefaultInstance(props, null)
            val buffer = ByteArrayOutputStream()
            logger.info("Creating message.")
            MimeMessage(session).apply {
                setFrom(fromEmailAddress)
                setRecipients(MimeMessage.RecipientType.TO, toEmailAddress)
                subject = messageSubject
                setText(bodyText)
                // Encode and wrap the MIME message into a gmail message
                writeTo(buffer)
                logger.info("Created message $this")
            }
            val message = Message().apply {
                setRaw(
                    Base64.encodeBase64URLSafeString(
                        buffer.toByteArray()
                    )
                )
            }
            try {
                logger.info("Tryna send the draft.")
                return Draft().apply {
                    setMessage(message)
                    service.users().drafts().create("me", this).execute()
                }.also {
                    println("Draft id: " + it.id)
                    println(it.toPrettyString())
                }
            } catch (e: GoogleJsonResponseException) {
                // TODO(developer) - handle error appropriately
                val error = e.details
                if (error.code == 403) {
                    System.err.println("Unable to create draft: " + e.message)
                } else {
                    throw e
                }
            }
            return null
        }

        /**
         * Send an email from the user's mailbox to its recipient.
         *
         * @param fromEmailAddress - Email address to appear in the from: header
         * @param toEmailAddress   - Email address of the recipient
         * @return the sent message, `null` otherwise.
         * @throws MessagingException - if a wrongly formatted address is encountered.
         * @throws IOException        - if service account credentials file not found.
         */
        @Throws(MessagingException::class, IOException::class)
        fun sendEmail(
            fromEmailAddress: String? = KhodedConfig.ClientEmail,
            toEmailAddress: String?,
            messageSubject: String,
            bodyText: String,
        ): Message? {
            // Encode as MIME message
            val props = Properties()
            val session = Session.getDefaultInstance(props, null)
            val messageByteStream = ByteArrayOutputStream()
            logger.info("Creating mime message.")
            MimeMessage(session).apply {
                setFrom(InternetAddress(fromEmailAddress))
                addRecipient(
                    MimeMessage.RecipientType.TO,
                    InternetAddress(toEmailAddress)
                )
                subject = messageSubject
                setText(bodyText)
                // Encode and wrap the MIME message into a gmail message
                writeTo(messageByteStream)
            }
            logger.info("Adding message bytes")
            var message = Message().apply {
                setRaw(
                    Base64.encodeBase64URLSafeString(
                        messageByteStream.toByteArray()
                    )
                )
            }
            try {
                // Create send message
                logger.info("Tryna send the message.")
                message = service.users().messages().send("me", message).execute()
                logger.info("Message id: " + message.id)
                logger.info(message.toPrettyString())
                return message
            } catch (e: GoogleJsonResponseException) {
                // TODO(developer) - handle error appropriately
                logger.info("There was an error: ${e.message}")
                e.printStackTrace()
                val error = e.details
                if (error.code == 403) {
                    System.err.println("Unable to send message: " + e.details)
                } else {
                    logger.info("${e.message}, details: ${e.details}")
                    throw e
                }
            } catch (ex: Exception) {
                logger.info("Unknown error ${ex.message}")
                ex.printStackTrace()
            }
            return null
        }
    }
}

enum class ServiceAccountKey(val value: String) {
    TYPE("type"),
    PROJECTID("project_id"),
    PRIVATE_KEY_ID("private_key_id"),
    PRIVATE_KEY("private_key"),
    CLIENT_EMAIL("client_email"),
    CLIENT_ID("client_id"),
    AUTH_URI("auth_uri"),
    TOKEN_URI("token_uri"),
    AUTH_PROVIDER_URL("auth_provider_x509_cert_url"),
    CLIENT_URL("client_x509_cert_url"),
    UNIVERSE_DOMAIN("universe_domain")
}