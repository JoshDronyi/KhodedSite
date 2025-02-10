package com.probro.khoded.styles.textStyles

import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.addVariant
import org.jetbrains.compose.web.css.px

val TeamBioParagraphVaraiant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

val JobTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontSize(FontSize.Large)
            .fontWeight(FontWeight.Bold)
            .height(Height.FitContent)
            .margin(10.px)
    }
}

val JobDescriptionVariant = BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontSize(FontSize.Large)
            .height(Height.FitContent)
    }
}
