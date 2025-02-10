/*
package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.*
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.Section
import com.probro.khoded.intakeForm
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.contactSections.QuestionDisplay
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.ComponentStyle
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ClientRequestForm(
    modifier: Modifier = Modifier,
    onCTAClicked: () -> Unit
) {
    var currentSection by remember { mutableStateOf(0) }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        //TODO: ANIMATE SECTION CHANGES
        intakeForm.sections.forEachIndexed { index, section ->
            IntakeSectionDisplay(
                section = section,
                isLastSection = intakeForm.sections.indices.last == index,
                modifier = Modifier
                    .visibility(if (currentSection == index) Visibility.Visible else Visibility.Collapse)
                    .opacity(if (currentSection == index) 100.percent else 0.percent)
                    .transition(
                        CSSTransition(property = "visibility", duration = 500.ms),
                        CSSTransition(property = "opacity", duration = 300.ms)
                    ),
                prevSection = {
                    println("Heading to previous section")
                    currentSection--
                },
                nextSection = {
                    println("Heading to next section")
                    currentSection++
                }
            ) { isLast ->
                if (isLast) onCTAClicked()
                else currentSection = 1
            }
        }
    }
}

val IntakeFormSectionStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(20.px)
            .background(Colors.White)
            .borderRadius(r = 20.px)
            .border(
                width = 2.px,
                style = LineStyle.Solid,
                color = Colors.DarkGray
            )
            .boxShadow(
                color = Colors.DarkGray,
                offsetY = 10.px,
                offsetX = 5.px,
                blurRadius = 10.px
            )
    }
}

@Composable
fun IntakeSectionDisplay(
    section: Section,
    isLastSection: Boolean,
    modifier: Modifier = Modifier,
    prevSection: () -> Unit,
    nextSection: () -> Unit,
    onCTAClicked: (isLastSection: Boolean) -> Unit,
) = with(section.uiModel) {
    println("In the section display.")
    Column(
        modifier = IntakeFormSectionStyle.toModifier().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SectionExplanation(
            title = title,
            description = description
        )
        if (questionList?.isNotEmpty() == true) {
            SectionQuestions(
                section = section,
                isLast = isLastSection,
                prevSection = prevSection,
                nextSection = nextSection,
                onCTAClicked = {
                    onCTAClicked(isLastSection)
                }
            )
        } else {
            ButtonDisplay(
                state = ButtonState(
                    buttonText = "OK",
                    onButtonClick = { onCTAClicked(isLastSection) }
                ),
                BlueButtonVariant
            )
        }
    }
}

@Composable
private fun SectionQuestions(
    section: Section,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    prevSection: () -> Unit,
    nextSection: () -> Unit,
    onCTAClicked: () -> Unit
) = with(section.uiModel) {
    println("IN section Questions, questionlist was ${section.uiModel.questionList}")
    Column(
        modifier = modifier.fillMaxWidth(75.percent),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        questionList?.forEach { question ->
            QuestionDisplay(
                question = question,
            ) {
                //TODO: IMPLEMENT ACTION FOR SAVING QUESTION ANSWER
                println("Answered question -> $question")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ButtonDisplay(
                state = ButtonState(
                    buttonText = "Back",
                    onButtonClick = prevSection
                ),
                BlueButtonVariant
            )
            ButtonDisplay(
                state = ButtonState(
                    buttonText = if (isLast) "Done" else "Next",
                    onButtonClick = if (isLast) onCTAClicked else nextSection
                ),
                BlueButtonVariant
            )
        }
    }
}

@Composable
fun SectionExplanation(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(leftRight = 20.px, top = 10.px)
            .fillMaxWidth(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.XLarge)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(title)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Large)
                .fontStyle(FontStyle.Italic)
                .toAttrs()
        ) {
            Text(description)
        }
    }
}*/
