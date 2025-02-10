package com.probro.khoded.styles.textStyles

import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.extendedBy
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

val CompanyInfoColumnStyle = ColumnStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}


val ContactSectionContainerVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(topBottom = 30.px)
    }
}
val MessagingSectionContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
    }
}
val SpacerContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
    }
}
val CompanyInfoContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier
            .fillMaxHeight()
            .minHeight(50.vh)
    }
}
val ClientInfoContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier
            .height(Height.FitContent)
    }
}


val ClientInfoTextBoxVariant = BaseTextInputStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 5.px)
    }
}

val ClientInfoTextAreaVariant = ClientInfoTextBoxVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    }
}


val CompanyContactTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontSize(FontSize.Larger)
            .fontWeight(FontWeight.Bolder)
            .textAlign(TextAlign.End)
    }
}