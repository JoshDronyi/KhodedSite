package com.probro.khoded.pages.contactSections

import androidx.compose.runtime.*
import com.probro.khoded.IntakeFormAnswer
import com.probro.khoded.Question
import com.probro.khoded.QuestionType
import com.probro.khoded.styles.textStyles.BaseContainerStyle
import com.probro.khoded.styles.textStyles.BaseTextInputStyle
import com.probro.khoded.styles.textStyles.BaseTextStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.RadioInput
import org.jetbrains.compose.web.dom.Text

@Composable
fun QuestionDisplay(
    question: Question,
    modifier: Modifier = Modifier,
    onQuestionAnswered: () -> Unit
) {
    println("In question display, type is ${question.questionType} answer is ${question.answer}")
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Medium)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(question.questionText)
        }
        when (question.questionType) {
            QuestionType.A_B ->
                ABQuestionDisplay(question) { answer ->
                    println("Seleted A/B answeer -> $answer \n from options ${question.options}")
                    //TODO: ANSWER AN A_B QUESTION
                    question.answer = IntakeFormAnswer.TextBasedAnswer(answer)
                    onQuestionAnswered()
                }


            QuestionType.PICK_ONE ->
                PickOneQuestionDisplay(question) { answer ->
                    println("Picked one, picks -> $answer \n from options ${question.options}")
                    //TODO: ANSWER PICK_ONE QUESTION
                    question.answer = IntakeFormAnswer.TextBasedAnswer(answer)
                    onQuestionAnswered()
                }


            QuestionType.PICK_MULTIPLE -> {
                var answers = mutableListOf<String>()
                val selectedAnswers = remember {
                    mutableStateListOf((question.answer as? IntakeFormAnswer.MultiSelectionAnswer)?.answerList)
                }
                PickMultipleQuestionDisplay(
                    question = question,
                    selectedAnswers = selectedAnswers.map { nextAnswers: List<String>? ->
                        nextAnswers?.joinToString(",").apply {
                            this?.let { answers.add(it) }
                        } ?: ""
                    },
                ) { selected ->
                    println("Picked Multiple, picks -> $selected \n from options ${question.options}")
                    answers.apply {
                        toMutableList()
                        if (this.contains(selected)) {
                            println("removing selected item $selected")
                            remove(selected)
                        } else {
                            println("adding selected item $selected")
                            add(selected)
                        }
                    }
                    question.answer = IntakeFormAnswer.MultiSelectionAnswer(answerList = answers)
                    onQuestionAnswered()
                }
            }


            QuestionType.OPEN_SHORT -> OpenQuestionDisplay(question) { answer ->
                println("Short answer question changed: $answer \n from options ${question.options}")
                //TODO: ANSWER OPEN_SHORT QUESTION
                question.answer = IntakeFormAnswer.TextBasedAnswer(answer)
                onQuestionAnswered()
            }

            QuestionType.OPEN_LONG -> OpenQuestionDisplay(question) { answer ->
                println("Long answer question changed: $answer \n from options ${question.options}")
                //TODO: ANSWER OPEN_lONG QUESTION
                question.answer = IntakeFormAnswer.TextBasedAnswer(answer)
                onQuestionAnswered()
            }
        }
    }
}

@Composable
fun PickMultipleQuestionDisplay(
    question: Question,
    selectedAnswers: List<String>,
    modifier: Modifier = Modifier,
    onOptionsSelect: (selected: String) -> Unit
) = with(question) {
    SimpleGrid(
        numColumns = numColumns(base = 2, md = 4),
        modifier = modifier
            .fillMaxWidth()
    ) {
        options?.forEach { option ->
            println("Option $option is in selected ${selectedAnswers.contains(option)}}")
            AnswerOption(
                option = option,
                isSelected = selectedAnswers.contains(option),
                questionType = QuestionType.PICK_MULTIPLE,
                onOptionSelect = { selected ->
                    onOptionsSelect(selected)
                }
            )
        }
    }
}

@Composable
fun PickOneQuestionDisplay(question: Question, onOptionSelect: (option: String) -> Unit) = with(question) {
    var selectedItem by remember {
        mutableStateOf((question.answer as? IntakeFormAnswer.TextBasedAnswer)?.answer ?: "")
    }
    SimpleGrid(
        numColumns = numColumns(base = 1, md = 2),
        modifier = Modifier.fillMaxWidth()
    ) {
        options?.forEach {
            AnswerOption(
                option = it,
                isSelected = selectedItem == it,
                questionType = QuestionType.PICK_ONE,
                onOptionSelect = { selected ->
                    selectedItem = selected
                    onOptionSelect(selected)
                }
            )
        }
    }
}

@Composable
fun ABQuestionDisplay(
    question: Question,
    modifier: Modifier = Modifier,
    onOptionSelect: (option: String) -> Unit
) = with(question) {
    println("")
    var selected by remember {
        mutableStateOf((question.answer as? IntakeFormAnswer.TextBasedAnswer)?.answer ?: "")
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        options?.forEach {
            AnswerOption(
                option = it,
                isSelected = selected == it,
                questionType = QuestionType.A_B
            ) { answer ->
                selected = answer
                onOptionSelect(answer)
            }
        }
    }
}

val TextInputVariant = BaseTextInputStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(r = 8.px)
    }
}

@Composable
fun OpenQuestionDisplay(
    question: Question,
    modifier: Modifier = Modifier,
    onValueChange: (textAnswer: String) -> Unit
) {
    var textValue by remember { mutableStateOf((question.answer as? IntakeFormAnswer.TextBasedAnswer)?.answer ?: "") }
    Input(
        type = InputType.Text,
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChange(it)
        },
        modifier = BaseTextInputStyle.toModifier(TextInputVariant)
            .then(modifier)
    )
}

val AnswerOptionVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Colors.DarkGray
            )
            .borderRadius(r = 20.px)
            .padding(leftRight = 15.px)
            .margin(topBottom = 5.px)
            .minHeight(50.px)
            .boxShadow(
                color = Colors.DarkGray,
                offsetY = 2.px,
                offsetX = 2.px,
                blurRadius = 2.px
            )
        //  .transition(CSSTransition(property = "shadow", duration = 400.ms))
    }
    hover {
        Modifier
            .boxShadow(
                color = Colors.DarkGray,
                offsetY = 4.px,
                offsetX = 4.px,
                blurRadius = 4.px
            )
        //.transition(CSSTransition(property = "shadow", duration = 400.ms))
    }
}

@Composable
fun AnswerOption(
    option: String,
    isSelected: Boolean,
    questionType: QuestionType,
    modifier: Modifier = BaseContainerStyle.toModifier(AnswerOptionVariant),
    onOptionSelect: (option: String) -> Unit
) {
    println("For option $option is selected is $isSelected")
    Row(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .onClick { onOptionSelect(option) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (questionType == QuestionType.PICK_ONE) {
            RadioInput(
                checked = isSelected,
                attrs = Modifier
                    .margin(right = 5.px)
                    .toAttrs()
            )
        } else {
            CheckboxInput(
                checked = isSelected,
                attrs = Modifier
                    .margin(right = 5.px)
                    .toAttrs()
            )
        }
        P(
            attrs = Modifier
                .padding(0.px)
                .margin(0.px)
                .fontSize(FontSize.Small)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) {
            Text(option)
        }
    }
}