package com.moove.design_system.compose.moove

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.moove.design_system.R
import com.moove.design_system.compose.AppTypography
import com.moove.design_system.compose.TopAppBarTypography

internal val mooveTypography: AppTypography
    @Composable
    get() = AppTypography(
        material = MooveTextStyles.run {
            Typography(
                h1 = H1,
                h2 = H2,
                h3 = H3,
                h4 = H4,
                subtitle1 = BodyHighlighted,
                subtitle2 = ButtonLabel,
                body1 = Body,
                body2 = BodySmall,
                button = ButtonLabel,
                caption = BodySmall,
                overline = LabelSuperSmall,
            )
        },
        appBar = MooveTextStyles.run {
            TopAppBarTypography(
                title = H4,
                titleExpanded = H1,
            )
        }
    )

private object MooveFontFamilies {

    val Quicksand = FontFamily(
        Font(R.font.quicksand_bold, weight = FontWeight.Bold)
    )

    val SourceSansPro = FontFamily(
        Font(R.font.source_sans_pro),
        Font(R.font.source_sans_pro_semibold, weight = FontWeight.SemiBold),
    )
}

private object MooveTextStyles {

    private val commonBoldTextStyle = TextStyle(
        fontFamily = MooveFontFamilies.Quicksand,
        fontWeight = FontWeight.Bold,
    )

    private val commonRegularTextStyle = TextStyle(
        fontFamily = MooveFontFamilies.SourceSansPro,
        fontWeight = FontWeight.Normal,
    )

    val H1 = commonBoldTextStyle.copy(
        fontSize = 30.sp,
        lineHeight = 45.sp,
    )

    val H2 = commonBoldTextStyle.copy(
        fontSize = 26.sp,
        lineHeight = 39.sp,
    )

    val H3 = commonBoldTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 33.sp,
    )

    val H4 = commonBoldTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 27.sp,
    )

    val Body = commonRegularTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val BodyHighlighted = Body.copy(
        fontWeight = FontWeight.SemiBold,
    )

    val BodySmall = Body.copy(
        fontSize = 14.sp,
        lineHeight = 21.sp,
    )

    val LabelSuperSmall = commonRegularTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp,
    )

    val ButtonLabel = commonBoldTextStyle.copy(
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
    )
}
