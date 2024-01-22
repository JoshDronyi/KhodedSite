/*
package com.probro.khoded.pages

import androidx.compose.runtime.*
import com.probro.khoded.*
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.serviceSections.*
import com.probro.khoded.utils.MailClient
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.json
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun Services(modifier: Modifier = Modifier.fillMaxSize()) {
    val ctx = rememberPageContext()
    Scaffold(
        router = ctx.router,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(Height.FitContent)
                .padding(20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Navigator.sections[Navigator.KeySections.Services]?.forEach {
                when (it.id) {
                    Pages.Services_Section.Landing.id -> {
                        ServicesLanding()
                    }

                    Pages.Services_Section.GetStarted.id -> {

                        PricingServiceSlides()
                    }

                    else -> {}
                }
            }
        }
    }
}


@Composable
fun PricingServiceSlides() {
    var intakeFormMode by remember { mutableStateOf(IntakeFormMode.IDLE) }
    when (intakeFormMode) {
        IntakeFormMode.IDLE -> GetStartedService(data = Pages.Services_Section.GetStarted) {
            intakeFormMode = IntakeFormMode.EDIT
        }

        IntakeFormMode.EDIT ->
            ClientRequestForm(
                Modifier
                    .id(Pages.Contact_Section.Landing.id)
                    .height(Height.FitContent)
            ) {
                intakeFormMode = IntakeFormMode.REVIEW
            }

        IntakeFormMode.REVIEW -> IntakeFormReview {
            intakeFormMode = IntakeFormMode.SEND
        }

        IntakeFormMode.SEND -> {
            sendIntakeForm()
            intakeFormMode = IntakeFormMode.THANKS
        }

        IntakeFormMode.THANKS -> ClientRequestThanks(
            uiModel = ClientRequestThanksUiModel(
                thanksMessage = "Thank you for choosing Khoded to handle your web presence.",
                thankssubMessage = "We are happy to receive your message and will address it as soon as possible."
            )
        )
    }
}

val scope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
    throwable.printStackTrace()
})

fun sendIntakeForm() = scope.launch {
    MailClient.sendIntakeForm(getIntakeFormAnswers())
}

fun getIntakeFormAnswers(): JsonObject {
    val form = IntakeFormDTO(formType = "Intake Form").apply {
        intakeForm.sections.map { section ->
            val jawn = section.uiModel.questionList?.map { question ->
                question.answer?.toFormAnswerDTO(question.questionText) ?: FormAnswerDTO(
                    questionText = question.questionText,
                    answerValue = "Question not Answered."
                )
            }
            when (section.uiModel.id) {
                SectionID.CONTACT_INFO -> {
                    if (section.uiModel.id.name == SectionID.CONTACT_INFO.name) {
                        val organization: IntakeFormAnswer? = section.uiModel.questionList?.get(2)?.answer
                        this.organization = organization?.let {
                            when (it) {
                                is IntakeFormAnswer.MultiSelectionAnswer -> it.answerList.joinToString(",")
                                is IntakeFormAnswer.TextBasedAnswer -> it.answer
                            }
                        }
                    }
                    this.contactFormAnswers = jawn
                }

                SectionID.PROJECT_OVERVIEW -> this.projectOverviewAnswers = jawn
                SectionID.DESIGN_BRANDING -> designBrandingAnswers = jawn
                SectionID.STRUCTURE_FUNCTIONALITY -> structureFunctionalityAnswers = jawn
                SectionID.CONTENT_IMAGERY -> contentImageryAnswers = jawn
                SectionID.TIMELINE_BUDGET -> timelineBudgetAnswers = jawn
                SectionID.MAINTENANCE_UPDATES -> maintenanceUpdatesAnswers = jawn
                SectionID.ADDITIONAL_INFO -> additionalInfo = jawn
                else -> {
                    println("Intro section doesnt have a ")
                }
            }
        }
    }
    return json
        .encodeToJsonElement(form)
        .jsonObject
}

fun IntakeFormAnswer.toFormAnswerDTO(questionText: String) = when (this) {
    is IntakeFormAnswer.MultiSelectionAnswer -> FormAnswerDTO(
        questionText = questionText,
        answerValue = answerList.joinToString(",")
    )

    is IntakeFormAnswer.TextBasedAnswer -> FormAnswerDTO(
        questionText = questionText,
        answerValue = answer
    )
}
*/
