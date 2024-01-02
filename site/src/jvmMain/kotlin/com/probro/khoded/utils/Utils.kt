package com.probro.khoded.utils/*
package com.probro.khoded.utils

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.security.GeneralSecurityException


*/
/**
 *
 * @author yccheok
 *//*

object Utils {
    */
/** Global instance of the JSON factory.  *//*

    private val JSON_FACTORY: GsonFactory = GsonFactory.getDefaultInstance()

    */
/** Global instance of the HTTP transport.  *//*

    private var httpTransport: HttpTransport? = null
    private val log: Log = LogFactory.getLog(Utils::class.java)

    init {
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        } catch (ex: IOException) {
            log.error(null, ex)
        } catch (ex: GeneralSecurityException) {
            log.error(null, ex)
        }
    }

    private val gmailDataDirectory: File
        private get() = File(org.yccheok.jstock.gui.Utils.getUserDataDirectory() + "authentication" + File.separator + "gmail")

    */
/**
 * Send a request to the UserInfo API to retrieve the user's information.
 *
 * @param credentials OAuth 2.0 credentials to authorize the request.
 * @return User's information.
 * @throws java.io.IOException
 *//*

    @Throws(IOException::class)
    fun getUserInfo(credentials: Credential?): Userinfoplus {
        val userInfoService: Oauth2 =
            Builder(httpTransport, JSON_FACTORY, credentials).setApplicationName("JStock").build()
        return userInfoService.userinfo().get().execute()
    }

    fun loadEmail(dataStoreDirectory: File?): String? {
        val file = File(dataStoreDirectory, "email")
        return try {
            String(Files.readAllBytes(Paths.get(file.toURI())), charset("UTF-8"))
        } catch (ex: IOException) {
            log.error(null, ex)
            null
        }
    }

    fun saveEmail(dataStoreDirectory: File?, email: String?): Boolean {
        val file = File(dataStoreDirectory, "email")
        return try {
            //If the constructor throws an exception, the finally block will NOT execute
            val writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file), "UTF-8"))
            try {
                writer.write(email)
            } finally {
                writer.close()
            }
            true
        } catch (ex: IOException) {
            log.error(null, ex)
            false
        }
    }

    fun logoutGmail() {
        val credential = File(gmailDataDirectory, "StoredCredential")
        val email = File(gmailDataDirectory, "email")
        credential.delete()
        email.delete()
    }

    @Throws(Exception::class)
    fun authorizeGmail(): Pair<Pair<Credential, String>, Boolean> {
        // Ask for only the permissions you need. Asking for more permissions will
        // reduce the number of users who finish the process for giving you access
        // to their accounts. It will also increase the amount of effort you will
        // have to spend explaining to users what you are doing with their data.
        // Here we are listing all of the available scopes. You should remove scopes
        // that you are not actually using.
        val scopes: MutableSet<String> = HashSet()

        // We would like to display what email this credential associated to.
        scopes.add("email")
        scopes.add(GmailScopes.GMAIL_SEND)

        // load client secrets
        val clientSecrets: GoogleClientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY,
            InputStreamReader(Utils::class.java.getResourceAsStream("/assets/authentication/gmail/client_secrets.json"))
        )
        return authorize(clientSecrets, scopes, gmailDataDirectory)
    }

    */
/** Authorizes the installed application to access user's protected data.
 * @return
 * @throws java.lang.Exception
 *//*

    @Throws(Exception::class)
    private fun authorize(
        clientSecrets: GoogleClientSecrets,
        scopes: Set<String>,
        dataStoreDirectory: File
    ): Pair<Pair<Credential, String>, Boolean> {
        // Set up authorization code flow.
        val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, scopes
        )
            .setDataStoreFactory(FileDataStoreFactory(dataStoreDirectory))
            .build()
        // authorize
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    fun getGmail(credential: Credential?): Gmail {
        return Gmail.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("JStock").build()
    }
}*/
