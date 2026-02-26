package com.next.wallettracker.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.next.wallettracker.ui.theme.WallettrackerTheme
import kotlinx.coroutines.selects.SelectClause

@Preview(showBackground = true)
@Composable
private fun Test() {
    WallettrackerTheme {
        val data = listOf(
            Pair("Jan", 111.45),
            Pair("Fev", 111.0),
            Pair("Mars", 113.45),
            Pair("Avr", 112.25),
            Pair("Mai", 116.45),
            Pair("Juin", 113.35),
            Pair("Juillet", 118.65),
//            Pair("Aout", 118.65),
//            Pair("Sept", 118.65),
//            Pair("Oct", 118.65),
//            Pair("Nov", 118.65),
//            Pair("Dec", 450.98),
//            Pair(13, 110.15),
//            Pair(14, 113.05),
//            Pair(15, 114.25),
//            Pair(16, 116.35),
//            Pair(17, 117.45)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            LineChart(
                data = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .align(CenterHorizontally)
            )

        }
    }
}