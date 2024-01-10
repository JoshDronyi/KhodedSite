import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.api.json
import com.probro.khoded.email.MailClient
import com.probro.khoded.local.KhodedDB
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.readBodyText
import com.varabyte.kobweb.api.http.setBodyText


@Api("/saveIntakeForm")
suspend fun saveIntakeForm(ctx: ApiContext) {
    val client = MailClient(ctx.logger)
    with(ctx.logger) {
        info("Got the data. the body was ${ctx.req.body}")
        val formString = ctx.req.readBodyText() ?: ""
        val form: IntakeFormDTO = json.decodeFromString<IntakeFormDTO>(formString)
        try {
            info("Attempting to send form.")
            client.sendIntakeForm(form)
            ctx.res.apply {
                status = 200
                setBodyText("Successfully hit endpoint with body ${ctx.req.body?.decodeToString()}")
            }
        } catch (ex: Exception) {
            println("Exception while trying to send intake form")
            error(ex.localizedMessage)
            ex.printStackTrace()
            ctx.res.status = 500
            ctx.res.setBodyText("Unfortunately there was an error sending the message. ${ex.printStackTrace()}")
        }
    }
}