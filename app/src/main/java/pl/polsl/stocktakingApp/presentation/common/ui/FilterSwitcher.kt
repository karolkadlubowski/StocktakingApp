package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import pl.polsl.stocktakingApp.ui.theme.*

@Composable
fun FilterSwitcher(
    selectedTabIndex: Int,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    onTabClick: (Int) -> Unit,
) {
    require(tabs.size in 2..5) {
        "Number of tabs is not in required range 2..5"
    }
    Surface(
        color = Color.Transparent,
        shape = S.rounded,
        modifier = modifier.padding(bottom = D.FilterSwitcher.paddingBottom)
    ) {
        OurTabRow(
            modifier = Modifier
                .clip(S.rounded)
                .fillMaxWidth()
                .height(D.FilterSwitcher.height)
                .background(C.Transparent),
            selectedTabIndex = selectedTabIndex,
            contentColor = C.Transparent,
            backgroundColor = C.BackgroundLighter,
            indicator = { Indicator(it) }
        ) {
            tabs.forEachIndexed { index, item ->
                FilterItem(item, selectedTabIndex, index, onTabClick)
            }
        }
    }
}

@Composable
private fun FilterItem(
    text: String,
    selectedIndex: Int,
    index: Int,
    onClick: (Int) -> Unit
) {
    val filterSwitcherTheme = LocalFilterSwitcherTheme.current
    val selectedTextColor = filterSwitcherTheme.selectedTextColor
    val notSelectedColor = filterSwitcherTheme.textColor
    val isSelected = selectedIndex == index

    val transition = updateTransition(targetState = isSelected, label = "Text style")

    val color by transition.animateColor(label = "text color") {
        if (it) selectedTextColor else notSelectedColor
    }
    val fontWeight by transition.animateValue(
        typeConverter = FontWeight.Converter,
        label = "font weight"
    ) {
        if (it) filterSwitcherTheme.selectedFontWeight else filterSwitcherTheme.fontWeight
    }

    Box(
        modifier = Modifier
            .clip(filterSwitcherTheme.itemShape)
            .fillMaxHeight()
            .clickable(onClick = { onClick(index) })
    ) {
        Text(
            text = text,
            maxLines = 1,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = color,
                fontWeight = fontWeight,
                fontSize = filterSwitcherTheme.fontSize
            )
        )
    }
}


@Composable
private fun Indicator(tabPosition: TabPosition) {
    val ind by animateDpAsState(targetValue = tabPosition.left)

    Box(
        Modifier
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = ind)
            .width(tabPosition.width)
            .clip(S.rounded)
            .background(LocalFilterSwitcherTheme.current.indicatorColor)
            .fillMaxSize()
    )
}


@Composable
private fun OurTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = C.SettingsOrange,
    contentColor: Color = contentColorFor(backgroundColor),
    indicator: @Composable (tabPosition: TabPosition) -> Unit,
    tabs: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.selectableGroup(),
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = LocalFilterSwitcherTheme.current.elevation,
    ) {
        SubcomposeLayout(Modifier.fillMaxWidth()) { constraints ->
            val tabRowWidth = constraints.maxWidth
            val tabMeasurables = subcompose(TabSlots.Tabs, tabs)
            val tabCount = tabMeasurables.size
            val tabWidth = (tabRowWidth / tabCount)
            val tabPlaceables = tabMeasurables.map {
                it.measure(constraints.copy(minWidth = tabWidth, maxWidth = tabWidth))
            }

            val tabRowHeight = tabPlaceables.maxByOrNull { it.height }?.height ?: 0

            val tabPositions = List(tabCount) { index ->
                TabPosition(tabWidth.toDp() * index, tabWidth.toDp())
            }

            layout(tabRowWidth, tabRowHeight) {
                subcompose(TabSlots.Indicator) {
                    indicator(tabPositions[selectedTabIndex])
                }.forEach {
                    it.measure(Constraints.fixed(tabRowWidth, tabRowHeight)).placeRelative(0, 0)
                }

                tabPlaceables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(index * tabWidth, 0)
                }
            }
        }
    }
}

@Immutable
private data class TabPosition(val left: Dp, val width: Dp)

private enum class TabSlots { Tabs, Indicator }

val FontWeight.Companion.Converter: TwoWayConverter<FontWeight, AnimationVector1D>
    get() = TwoWayConverter(
        convertToVector = { fontWeight -> AnimationVector1D(fontWeight.weight.toFloat()) },
        convertFromVector = { vector -> FontWeight(vector.value.toInt()) }
    )


@Preview
@Composable
private fun FilterSwitcherPreview() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    StocktakingAppTheme {
        FilterSwitcher(
            selectedTabIndex,
            listOf(
                "Wszystkie",
                "Środki trwałe",
                "Wyposażenie"
            )
        ) { selectedTabIndex = it }
    }
}