package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.contactSections.ContactUsSection
import com.probro.khoded.utils.EmailData
import com.probro.khoded.utils.MailClient
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun Contact() {
    val ctx = rememberPageContext()
    val scope = rememberCoroutineScope()
    Scaffold(
        router = ctx.router
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ContactUsSection(
                Modifier
                    .id(Pages.Contact_Section.ContactInfo.id)
                    .height(Height.FitContent)
                    .fillMaxWidth(80.percent)
                    .padding(20.px)
            ) { name, email, organization, message ->
                scope.launch(Dispatchers.Default) {
                    println("Using client for values $name, $email, $organization, $message")
                    MailClient.sendEmail(
                        EmailData(name, email, organization, message)
                    )
                }
            }
        }
    }
}

