package com.application.theme.token


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.microsoft.fluentui.theme.token.AliasTokens
import com.microsoft.fluentui.theme.token.FluentAliasTokens
import com.microsoft.fluentui.theme.token.FluentGlobalTokens
import com.microsoft.fluentui.theme.token.TokenSet

class MyAliasTokens : AliasTokens() {
    override val brandColor: TokenSet<FluentAliasTokens.BrandColorTokens, Color> by lazy {
        TokenSet { token ->
            when (token) {
                FluentAliasTokens.BrandColorTokens.Color10 -> Color(0xFF443168)
                FluentAliasTokens.BrandColorTokens.Color20 -> Color(0xFF584183)
                FluentAliasTokens.BrandColorTokens.Color30 -> Color(0xFF430E60)
                FluentAliasTokens.BrandColorTokens.Color40 -> Color(0xFF7B64A3)
                FluentAliasTokens.BrandColorTokens.Color50 -> Color(0xFF8c043d)
                FluentAliasTokens.BrandColorTokens.Color60 -> Color(0xFF8c043d)
                FluentAliasTokens.BrandColorTokens.Color70 -> Color(0xFF9D87C4)
                FluentAliasTokens.BrandColorTokens.Color80 -> Color(0xFFCA0357)
                FluentAliasTokens.BrandColorTokens.Color90 -> Color(0xFF862EB5)
                FluentAliasTokens.BrandColorTokens.Color100 -> Color(0xFFC0AAE4)
                FluentAliasTokens.BrandColorTokens.Color110 -> Color(0xFFCEBBED)
                FluentAliasTokens.BrandColorTokens.Color120 -> Color(0xFFDCCDF6)
                FluentAliasTokens.BrandColorTokens.Color130 -> Color(0xFFA864CD)
                FluentAliasTokens.BrandColorTokens.Color140 -> Color(0xFFEADEFF)
                FluentAliasTokens.BrandColorTokens.Color150 -> Color(0xFFD1ABE6)
                FluentAliasTokens.BrandColorTokens.Color160 -> Color(0xFFE6D1F2)
            }
        }
    }
//    override val typography: TokenSet<TypographyTokens, TextStyle> by lazy {
//        TokenSet { token ->
//            when (token) {
//                TypographyTokens.Display ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size900),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size900),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = -0.5.sp
//                    )
//                TypographyTokens.LargeTitle ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size900),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size900),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = -0.25.sp
//                    )
//                TypographyTokens.Title1 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size800),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size800),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Bold),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Title2 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size700),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size700),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Medium),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Title3 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size600),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size600),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Medium),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Body1Strong ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size500),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size500),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.SemiBold),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Body1 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size500),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size500),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Body2Strong ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size400),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size400),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Medium),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Body2 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size400),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size400),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Caption1Strong ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size300),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size300),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Medium),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Caption1 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size300),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size300),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = 0.sp
//                    )
//                TypographyTokens.Caption2 ->
//                    TextStyle(
//                        fontSize = GlobalTokens.fontSize(GlobalTokens.FontSizeTokens.Size200),
//                        lineHeight = GlobalTokens.lineHeight(GlobalTokens.LineHeightTokens.Size200),
//                        fontWeight = GlobalTokens.fontWeight(GlobalTokens.FontWeightTokens.Regular),
//                        letterSpacing = 0.sp
//                    )
//            }
//        }
//    }
}


