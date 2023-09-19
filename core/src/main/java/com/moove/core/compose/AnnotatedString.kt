package com.moove.core.compose

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

object Html {
    fun fromHtml(source: String): AnnotatedString {
        val textWithSymbols = replaceSymbols(source)
        return HtmlCompat.fromHtml(textWithSymbols, HtmlCompat.FROM_HTML_MODE_LEGACY).toAnnotatedString()
    }

    private fun replaceSymbols(source: String): String {
        return source.replace("<li>", "<li>•\t\t")
    }

    private fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
        val spanned = this@toAnnotatedString
        append(spanned.toString())
        getSpans(0, spanned.length, Any::class.java).forEach { span ->
            val start = getSpanStart(span)
            val end = getSpanEnd(span)
            when (span) {
                is StyleSpan -> addStyleSpan(span, start, end)
                is UnderlineSpan -> addUnderlineSpan(start, end)
                is ForegroundColorSpan -> addForegroundColorSpan(span, start, end)
                is BulletSpan -> addListSpan(start, end)
            }
        }
    }

    private fun AnnotatedString.Builder.addStyleSpan(span: StyleSpan, start: Int, end: Int) {
        when (span.style) {
            Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
            Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
            Typeface.BOLD_ITALIC -> addStyle(
                SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                start,
                end
            )
        }
    }

    private fun AnnotatedString.Builder.addUnderlineSpan(start: Int, end: Int) {
        addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
    }

    private fun AnnotatedString.Builder.addForegroundColorSpan(span: ForegroundColorSpan, start: Int, end: Int) {
        addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
    }

    private fun AnnotatedString.Builder.addListSpan(start: Int, end: Int) {
        addStyle(ParagraphStyle(textIndent = TextIndent(restLine = 14.sp)), start, end)
    }
}
