package com.github.sagiri_kawaii01.blenh.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.ui.theme.Blue20
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray0
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import kotlin.math.min
import kotlin.math.pow



@Composable
fun IOCard(
    label: String,
    text: String,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    DashCard(label, modifier) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = Typography.labelLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (null != description) {
            Text(
                text = description,
                style = Typography.labelSmall,
                color = Gray20
            )
        }
    }
}

@Composable
fun TendencyCard(
    label: String,
    keyName: String,
    keys: List<String>,
    values: List<Double>,
    modifier: Modifier = Modifier
) {
    DashCard(label, modifier) {

        val textMeasurer =  rememberTextMeasurer()
        val lines = 6

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val h = size.height / lines
            val yOffset = 30f
            val minValue = (values.minOrNull() ?: 0.0).roundOff()
            val maxValue = values.maxOrNull() ?: 0.0

            val inc = ((maxValue - minValue) / (lines - 1)).roundOff()

            for (i in 0 until lines) {
                drawLine(
                    start = Offset(0f, h * i + yOffset),
                    end = Offset(size.width, h * i + yOffset),
                    color = Gray0,
                    strokeWidth = 1.dp.toPx()
                )

                if (i < lines - 1) {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = formatFloatNumber(minValue + inc.toFloat() * (lines - 1 - i)),
                        topLeft = Offset(0f, h * i + 15 + yOffset),
                        style = Typography.labelSmall.copy(color = Gray20),
                    )
                } else {
                    val elements = selectElements(keys, 8)
                    val texts = keys.filterIndexed { index, _ -> elements.contains(index) }
                    drawTextsWithEqualSpacing(
                        texts = listOf("$keyName ${texts[0]}") + texts.subList(1, texts.size),
                        yOffset = h * i + 15 + yOffset,
                        width = size.width,
                        style = Typography.labelSmall.copy(color = Gray20),
                        textMeasurer
                    ) { textWidths, spacing ->
                        val path = Path()
                        val points: MutableList<Offset> = mutableListOf()
                        var lastKey = 0
                        var m = 1
                        var lastX = textWidths[0] / 2
                        points.add(Offset(lastX, pointHeight(values[lastKey].toFloat(), minValue.toFloat(), maxValue.toFloat(), h * (lines - 1)) + yOffset))
                        for (k in 1 until keys.size) {
                            if (elements.contains(k)) {
                                if (k - lastKey > 1) {
                                    val inx = (spacing + textWidths[m - 1] / 2 + textWidths[m] / 2) / (k - lastKey)
                                    for (j in 1 until  k - lastKey) {
                                        points[lastKey + j] = points[lastKey + j].copy(x = j * inx + lastX)
                                    }
                                }
                                lastX += spacing + textWidths[m - 1] / 2 + textWidths[m++] / 2
                                lastKey = k
                                points.add(Offset(lastX, pointHeight(values[lastKey].toFloat(), minValue.toFloat(), maxValue.toFloat(), h * (lines - 1) + yOffset)))
                            } else {
                                points.add(Offset(0f, pointHeight(values[k].toFloat(), minValue.toFloat(), maxValue.toFloat(), h * (lines - 1)) + yOffset))
                            }
                        }

                        path.moveTo(points[0].x, points[0].y)
                        for (k in 1 until points.size) {
                            path.lineTo(points[k].x, points[k].y)
                        }

                        drawPath(
                            path = path,
                            color = Blue20,
                            style = Stroke(
                                width = 10f,
                                join = StrokeJoin.Round
                            )
                        )

                        drawCircle(
                            color = Blue20,
                            center = points.last(),
                            radius = 12f,
                        )

                        drawCircle(
                            color = Blue20,
                            center = points.last(),
                            radius = 32f,
                            alpha = 0.12f
                        )
                    }
                }
            }

        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatFloatNumber(number: Float): String {
    return when {
        number >= 1000 -> "${formatFloatNumber(number / 1000)}k"
        number >= 10 -> String.format("%.0f", number)
        number >= 1 -> String.format("%.1f", number)
        number >= 0 -> String.format("%.2f", number)
        else -> number.toString()
    }
}

private fun pointHeight(value: Float, minValue: Float, maxValue: Float, height: Float): Float {
    return height - (value - minValue) / (maxValue - minValue) * height
}

private fun selectElements(array: List<String>, n: Int): Set<Int> {
    val k = array.size
    val minN = minOf(n, k)

    if (minN == 1) return setOf(array.size - 1)

    val step = (k - 1).toDouble() / (minN - 1)
    val result = mutableSetOf<Int>()

    for (i in 0 until minN - 1) {
        result.add((i * step).toInt())
    }
    result.add(array.size - 1)

    return result
}

private fun Double.roundOff(): Int {
    val tmp = this.toInt().toString()
    return (tmp[0] - '0') * 10.0.pow((tmp.length - 1)).toInt() + if (this < 100) {
        0
    } else {
        (tmp[1] - '0') * 10.0.pow((tmp.length - 2)).toInt()
    }
}


private fun DrawScope.drawTextsWithEqualSpacing(
    texts: List<String>,
    yOffset: Float,
    width: Float,
    style: TextStyle,
    textMeasurer: TextMeasurer,
    drawPath: (List<Float>, Float) -> Unit
) {
    val textWidths = texts.map { textMeasurer.measure(
        it, style
    ).size.width.toFloat() }
    val totalTextWidth = textWidths.sum()
    val spacing = (width - totalTextWidth) / (texts.size - 1)
    var currentX = 0f
    texts.forEachIndexed { index, text ->
        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = Offset(currentX, yOffset),
            style = style
        )
        currentX += textWidths[index] + spacing
    }
    drawPath(textWidths, spacing)
}

//@Composable
//@Preview
//fun TendencyCardPreview() {
//    BlenhTheme {
//        TendencyCard(
//            label = "Input",
//            keyName = "Jun",
//            keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"),
//            values = listOf(1100f, 200f, 300f, 100f, 150f, 120f, 560f, 400f, 1200f, 1000f, 50f, 90f, 100f, 200f, 300f),
//            modifier = Modifier
//                .width(400.dp)
//                .background(Color.White)
//                .height(320.dp)
//        )
//    }
//}