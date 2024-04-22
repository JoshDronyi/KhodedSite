package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.probro.khoded.components.composables.*
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.ConsultationSectionDisplay
import com.probro.khoded.pages.homeSections.DesignSectionDisplay
import com.probro.khoded.pages.homeSections.LandingSectionDisplay
import com.probro.khoded.pages.homeSections.ServicesSectionDisplay
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Strings
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import org.jetbrains.compose.web.css.percent

@Page
@Composable
fun Index() {
    val ctx = rememberPageContext()
    Scaffold(
        onNavigate = { path ->
            ctx.router.navigateTo(path)
        }
    ) { header, footer, modifier ->
        HomePageSections(header, footer, modifier) { page ->
            ctx.router.navigateTo(page.path)
        }
    }
}

@Composable
fun HomePageSections(
    header: @Composable (variant: ComponentVariant?, textVariant: ComponentVariant?) -> Unit,
    footer: @Composable (variant: ComponentVariant?) -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: (page: PageSection) -> Unit
) {
    val popUpUiModel by remember {
        mutableStateOf(
            PopUpScreenUIModel(
                promptText = Strings.consultationThanksMessage,
                buttonState = ButtonState(
                    buttonText = "See you soon.",
                    onButtonClick = {
                        // WILL CHANGE BASED ON WHAT NEEDS TO BE DONE FROM THE MESSAGING RESULT.
                        // ERRORS SHOULD BE DISPLAYED. OTHERWISE, THANKS MESSAGE DISPLAYED.
                    }
                ),
                isShowing = false
            )
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(
            header = header,
            data = Pages.Home_Section.Landing,
            onNavigate = onNavigate
        )
        ServicesSectionDisplay(Pages.Home_Section.Services)
        DesignSectionDisplay(Pages.Home_Section.Design)
        ConsultationSectionDisplay(
            footer = footer,
            data = Pages.Home_Section.Consultation,
        )
//        onPopUpClick = {
//                with(popUpUiModel) {
//                    isShowing = true
//                    messagingState = MessagingStage.SENDING
//                }
//            }
//        ) { response ->
//            //Display the result from the server. Determine result state.
//            with(popUpUiModel) {
//                promptText = response
//                messagingState = MessagingStage.SENT
//            }
//        }
    }
}
