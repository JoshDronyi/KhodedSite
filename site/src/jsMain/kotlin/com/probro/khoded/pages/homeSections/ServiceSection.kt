//package com.probro.khoded.sections
//
//import androidx.compose.runtime.Composable
//import com.probro.khoded.components.ServiceTab
//import com.probro.khoded.models.Res
//import com.probro.khoded.styles.BaseSectionStyles
//import com.probro.khoded.styles.BaseTextStyle
//import com.varabyte.kobweb.compose.css.Height
//import com.varabyte.kobweb.compose.foundation.layout.Arrangement
//import com.varabyte.kobweb.compose.foundation.layout.Column
//import com.varabyte.kobweb.compose.foundation.layout.Row
//import com.varabyte.kobweb.compose.ui.Alignment
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.modifiers.*
//import com.varabyte.kobweb.compose.ui.toAttrs
//import com.varabyte.kobweb.silk.components.style.toModifier
//import org.jetbrains.compose.web.css.percent
//import org.jetbrains.compose.web.css.px
//import org.jetbrains.compose.web.dom.P
//import org.jetbrains.compose.web.dom.Text
//import com.probro.khoded.utils.Constants
//
//@Composable
//fun ServiceSection(
//    modifier: Modifier = Modifier,
//    height: Height = Height.Inherit
//) {
//    Column(
//        modifier = BaseSectionStyles.toModifier()
//            .then(modifier),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//        P(
//            attrs = BaseTextStyle.toModifier()
//                .margin(topBottom = 40.px)
//                .background(Res.Themes.baseTheme.surfaceColor.rgb)
//                .borderRadius(20.px)
//                .fontSize(30.px)
//                .padding(10.px)
//                .toAttrs()
//        ) {
//            Text(Constants.Strings.servicesTitle)
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(90.percent),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val tabModifier = Modifier.maxWidth(400.px)
//                .fillMaxWidth()
//                .height(height = height)
//            ServiceTab(
//                title = Constants.Strings.formTitle,
//                serviceList = Constants.Strings.formStringList,
//                modifier = tabModifier
//            )
//            ServiceTab(
//                title = Constants.Strings.developTitle,
//                serviceList = Constants.Strings.developStringList,
//                modifier = tabModifier
//            )
//            ServiceTab(
//                title = Constants.Strings.ExpandTitle,
//                serviceList = Constants.Strings.expandStringList,
//                modifier = tabModifier
//            )
//        }
//    }
//}