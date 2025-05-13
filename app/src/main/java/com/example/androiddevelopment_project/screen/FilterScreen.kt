package com.example.androiddevelopment_project.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androiddevelopment_project.model.MovieFilters
import com.example.androiddevelopment_project.model.availableGenres
import com.example.androiddevelopment_project.model.genreMap
import com.example.androiddevelopment_project.viewmodel.MovieViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackClick: () -> Unit,
    onApplyFilters: () -> Unit,
    viewModel: MovieViewModel = koinViewModel()
) {
    val filters by viewModel.filters.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenreSelector(
                selectedGenre = filters.selectedGenre,
                onGenreSelected = { genre ->
                    viewModel.updateGenreFilter(genre)
                }
            )
            
            HorizontalDivider()
            
            RatingSlider(
                rating = filters.minRating,
                onRatingChanged = { rating ->
                    viewModel.updateMinRatingFilter(rating)
                }
            )
            
            HorizontalDivider()
            
            YearInput(
                year = filters.releaseYear,
                onYearChanged = { year ->
                    viewModel.updateReleaseYearFilter(year)
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.resetFilters()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сбросить")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(
                    onClick = {
                        onApplyFilters()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Применить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSelector(
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = "Жанр",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = genreMap[selectedGenre] ?: "Выберите жанр",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableGenres.forEach { genre ->
                    DropdownMenuItem(
                        text = { 
                            Text(genreMap[genre] ?: genre) 
                        },
                        onClick = {
                            onGenreSelected(genre)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RatingSlider(
    rating: Double,
    onRatingChanged: (Double) -> Unit
) {
    Column {
        Text(
            text = "Минимальный рейтинг: ${String.format("%.1f", rating)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Slider(
            value = rating.toFloat(),
            onValueChange = { newValue ->
                val roundedValue = (newValue * 10).roundToInt() / 10.0
                onRatingChanged(roundedValue)
            },
            valueRange = 0f..10f,
            steps = 100,
            modifier = Modifier.fillMaxWidth()
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0")
            Text("10")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearInput(
    year: String,
    onYearChanged: (String) -> Unit
) {
    Column {
        Text(
            text = "Год выпуска",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = year,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || (newValue.length <= 4 && newValue.all { it.isDigit() })) {
                    onYearChanged(newValue)
                }
            },
            placeholder = { Text("Например: 2020") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
} 