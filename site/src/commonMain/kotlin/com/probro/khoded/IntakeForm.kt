package com.probro.khoded

import kotlinx.serialization.Serializable

interface IntakeFormSectionUiModel {
    val id: SectionID
    val title: String
    val description: String
    val questionList: List<Question>?
}

data class IntakeFormSubmission(
    val answers: Map<String, Pair<String, IntakeFormAnswer>>
)

enum class IntakeFormMode {
    IDLE, EDIT, REVIEW, SEND, THANKS
}

@Serializable
data class IntakeForm(
    val sections: List<Section>
)

@Serializable
data class Section(
    val uiModel: IntakeFormSectionUiModel
)

data class Question(
    val id: String = "",
    val questionText: String,
    val questionType: QuestionType,
    val options: List<String>? = null,
    var answer: IntakeFormAnswer? = null
)

sealed class IntakeFormAnswer {
    data class TextBasedAnswer(
        val answer: String = ""
    ) : IntakeFormAnswer()

    data class MultiSelectionAnswer(
        val answerList: List<String> = emptyList()
    ) : IntakeFormAnswer()
}

enum class SectionID {
    EXPLANATION, CONTACT_INFO, PROJECT_OVERVIEW, DESIGN_BRANDING,
    STRUCTURE_FUNCTIONALITY, CONTENT_IMAGERY, TIMELINE_BUDGET,
    MAINTENANCE_UPDATES, ADDITIONAL_INFO
}

enum class QuestionType {
    A_B, PICK_ONE, PICK_MULTIPLE, OPEN_SHORT, OPEN_LONG
}

sealed class IntakeFormUIModels {
    object IntroUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.EXPLANATION
        override val title = "Project Intake Form"
        override val description =
            "Please fill out this form with as much detail as possible to help us understand your " +
                    "needs and deliver a website that meets your expectations. Once completed, we will review the" +
                    " information and contact you to discuss the project further.\n Thank you for choosing us to " +
                    "build your website!"
        override val questionList: List<Question>? = null
    }

    @Serializable
    object ContactUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.CONTACT_INFO
        override val title = "Contact Information"
        override val description =
            "Please double check spellings as these responses will be used for client contact as " +
                    "well as on the contact page unless otherwise specified."
        override val questionList = listOf(
            Question(questionText = "First Name:", questionType = QuestionType.OPEN_SHORT),
            Question(questionText = "Last Name:", questionType = QuestionType.OPEN_SHORT),
            Question(questionText = "Company/Organization Name:", questionType = QuestionType.OPEN_SHORT),
            Question(questionText = "Email Address:", questionType = QuestionType.OPEN_SHORT),
            Question(questionText = "Phone Number:", questionType = QuestionType.OPEN_SHORT),
            Question(
                questionText = "Preferred Method of Communication",
                questionType = QuestionType.OPEN_SHORT
            ),
        )
    }

    object ProjectOverviewUiModel : IntakeFormSectionUiModel {
        override val id = SectionID.PROJECT_OVERVIEW
        override val title = "Project Overview"
        override val description =
            "Give us a breakdown of who you are, what you're thinking, and what you'd like to" +
                    " portray. Think of it as fodder for further down the process so the more detailed the better!"
        override val questionList = listOf(
            Question(
                questionText = "Describe your business or organization in a few sentences",
                questionType = QuestionType.OPEN_LONG
            ),
            Question(
                questionText = "What are your goals and objectives for the website?",
                questionType = QuestionType.OPEN_LONG
            ),
            Question(
                questionText = "Do you have an existing website? If yes, what do you like and dislike about it?",
                questionType = QuestionType.OPEN_LONG
            ),
            Question(
                questionText = "Who are your target audience or customers?",
                questionType = QuestionType.OPEN_LONG
            )
        )
    }

    object DesignBrandingUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.DESIGN_BRANDING
        override val title = "Design and Branding"
        override val description =
            "If you need the website to fall under certain branding guidelines please provide a copy" +
                    " of the guidelines along with any of the media and content provided."
        override val questionList = listOf(
            Question(
                questionText = "Do you have a logo? If yes, please provide a high resolution version via email",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes",
                    "No",
                    "Yes but I would like to discuss having one created",
                    "No and I would like to discuss having one created"
                )

            ),
            Question(
                questionText = "Do you have a preferred color scheme or existing brand guidelines?" +
                        " Get as specific as you can.",
                questionType = QuestionType.OPEN_LONG
            ),
            Question(
                questionText = "Are there any specific design preferences or inspirations you have in " +
                        "mind for the website?",
                questionType = QuestionType.OPEN_LONG
            ),
        )
    }

    object StructureFunctionalityUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.STRUCTURE_FUNCTIONALITY
        override val title = "Website Structure and Functionality"
        override val description = ""
        override val questionList = listOf(
            Question(
                questionText = "What are the main sections/pages you envision for your website?",
                questionType = QuestionType.PICK_MULTIPLE,
                options = listOf(
                    "Home", "About", "Contact Us", "Product Page(s)", "Pop ups", "Other"
                )
            ),
            Question(
                questionText = "Do you require any special features or functionality?",
                questionType = QuestionType.PICK_MULTIPLE,
                options = listOf(
                    "E-commerce", "Blog", "User Registration", "Other"
                )
            ),
            Question(
                questionText = "Are there any specific third-party integrations or platforms you need to " +
                        "integrate with?",
                questionType = QuestionType.PICK_MULTIPLE,
                options = listOf(
                    "Payment Gateway", "Customer Relationship Management (CRM)", "Other",
                )
            ),
        )
    }

    object ContentImageryUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.CONTENT_IMAGERY
        override val title = "Content and Imagery"
        override val description = ""
        override val questionList = listOf(
            Question(
                questionText = "Have you prepared written content/copy for the website? Will you need " +
                        "assistance with copy editing?",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "I have prepared site content/copy",
                    "I will need content/copy edting services"
                )
            ),
            Question(
                questionText = "Do you have high-quality images and other media assets for the website?",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes", "No"
                )
            ),
            Question(
                questionText = "Is there any existing content that needs to be migrated from an old website?",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes", "No"
                )
            ),
        )
    }

    object TimelineBudgetUiModel : IntakeFormSectionUiModel {
        override val id = SectionID.TIMELINE_BUDGET
        override val title = "Timeline and Budget"
        override val description = ""
        override val questionList = listOf(
            Question(
                questionText = "Do you have a specific deadline for the website launch?",
                questionType = QuestionType.OPEN_SHORT
            ),
            Question(
                questionText = "What is your approximate budget for the website development project?",
                questionType = QuestionType.OPEN_SHORT
            ),
        )
    }

    object MaintenanceUpdatesUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.MAINTENANCE_UPDATES
        override val title = "Maintenance and Updates"
        override val description = ""
        override val questionList = listOf(
            Question(
                questionText = "Will you require ongoing website maintenance and updates after initial" +
                        " development?",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes", "No", "Maybe"
                )
            ),
            Question(
                questionText = "Do you have staff or resources available to manage content updates? ",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes", "No"
                )
            ),
            Question(
                questionText = "Will you require web hosting services (how the site makes it onto the" +
                        " internet) for this site? ",
                questionType = QuestionType.PICK_ONE,
                options = listOf(
                    "Yes, I do not currently have a web hosting servicer",
                    "No, I am happy with my current web hosting servicer",
                    "Maybe, I'd like to discuss switching web hosting servicers",
                    "I don't know - lets chat!"
                )
            ),
        )
    }

    @Serializable
    object AdditionalInfoUIModel : IntakeFormSectionUiModel {
        override val id = SectionID.ADDITIONAL_INFO
        override val title = "Additional Information"
        override val description = "This section is your catch- all. Is there anything you think we missed?"
        override val questionList = listOf(
            Question(
                questionText = "Is there any additional information or specific requirements you would like " +
                        "to share?",
                questionType = QuestionType.OPEN_LONG
            ),
        )
    }

}

val intakeForm: IntakeForm = IntakeForm(
    listOf(
        Section(IntakeFormUIModels.IntroUIModel),
        Section(IntakeFormUIModels.ContactUIModel),
        Section(IntakeFormUIModels.ProjectOverviewUiModel),
        Section(IntakeFormUIModels.DesignBrandingUIModel),
        Section(IntakeFormUIModels.StructureFunctionalityUIModel),
        Section(IntakeFormUIModels.ContentImageryUIModel),
        Section(IntakeFormUIModels.TimelineBudgetUiModel),
        Section(IntakeFormUIModels.MaintenanceUpdatesUIModel),
        Section(IntakeFormUIModels.AdditionalInfoUIModel)
    )
)