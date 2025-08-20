/*
package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.IntakeFormAnswer
import com.probro.khoded.Section
import com.probro.khoded.intakeForm
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun IntakeFormReview(
    modifier: Modifier = Modifier,
    onCTAClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .padding(10.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        intakeForm.sections.forEach { section ->
            section.uiModel.questionList?.let {
                IntakeFormSectionReview(section)
            }
        }
        ButtonDisplay(
            state = ButtonState(
                buttonText = "Finish Review",
                onButtonClick = onCTAClicked
            ),
            BlueButtonVariant,
            modifier = Modifier.fillMaxWidth(80.percent)
        )
    }
}

@Composable
fun IntakeFormSectionReview(section: Section, modifier: Modifier = Modifier) = with(section.uiModel) {
    Column(
        modifier = modifier
            .height(Height.MinContent)
            .fillMaxWidth(80.percent)
            .padding(20.px)
            .margin(topBottom = 20.px)
            .border {
                width(1.px)
                color(Colors.DarkGray)
                style(LineStyle.Solid)
            }
            .borderRadius(20.px)
            .boxShadow(
                offsetX = 5.px,
                offsetY = 5.px,
                blurRadius = 5.px,
                color = Colors.DarkGray
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.XXLarge)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(title)
        }
        questionList?.forEach { question ->
            QuestionReviewDisplay(question.questionText, question.answer)
        }
    }
}

@Composable
fun QuestionReviewDisplay(questionText: String, answer: IntakeFormAnswer?) {
    Row(
        modifier = Modifier
            .fillMaxWidth(80.percent)
            .padding(10.px),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth(40.percent)
                .padding(5.px)
                .fontSize(FontSize.Large)
                .textAlign(TextAlign.Start)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(questionText)
        }
        when (answer) {
            is IntakeFormAnswer.MultiSelectionAnswer -> MultiAnswerDisplay(answer.answerList)
            is IntakeFormAnswer.TextBasedAnswer -> TextAnswerDisplay(answer.answer)
            null -> TextAnswerDisplay("No answer selected")
        }
    }
}

@Composable
fun MultiAnswerDisplay(
    answers: List<String>,
    modifier: Modifier = Modifier
) {
    SimpleGrid(
        numColumns(base = 1, sm = 2, md = 4),
        modifier = modifier
            .fillMaxWidth()
    ) {
        answers.forEach {
            TextAnswerDisplay(it)
        }
    }
}

@Composable
fun TextAnswerDisplay(answer: String) {
    P(
        attrs = BaseTextStyle.toModifier()
            .fillMaxWidth()
            .padding(1.px)
            .textAlign(TextAlign.Center)
            .fontSize(FontSize.Large)
            .toAttrs()
    ) {
        Text(answer)
    }
}
*/
