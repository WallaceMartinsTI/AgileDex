package com.wcsm.agiledex.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.agiledex.presentation.ui.theme.AgileDexTheme
import com.wcsm.agiledex.presentation.ui.theme.DarkGrayColor
import com.wcsm.agiledex.presentation.ui.theme.White06Color

@Composable
fun PokemonSearchBar(
    modifier: Modifier = Modifier,
    onFilterValueChange: (textFilter: String) -> Unit
) {
    val textFilterFocusRequester = remember { FocusRequester() }
    var textFilter by remember { mutableStateOf("") }

    LaunchedEffect(textFilter) {
        onFilterValueChange(textFilter)
    }

    OutlinedTextField(
        value = textFilter,
        onValueChange = {
            textFilter = it
        },
        modifier = modifier
            .focusRequester(textFilterFocusRequester)
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = "Digite para filtrar",
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "Ícone de filtrar",
                tint = DarkGrayColor,
            )
        },
        trailingIcon = {
            if(textFilter.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            textFilter = ""
                            textFilterFocusRequester.requestFocus()
                        }
                        .padding(4.dp)
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                textFilterFocusRequester.freeFocus()
            }
        )
    )
}

@Preview
@Composable
private fun PokemonSeachBarPreview() {
    AgileDexTheme {
        PokemonSearchBar {}
    }
}