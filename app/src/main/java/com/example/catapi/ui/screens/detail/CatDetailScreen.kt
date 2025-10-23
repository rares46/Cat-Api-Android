package com.example.catapi.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.catapi.data.model.Breed
import com.example.catapi.data.model.CatImage
import com.example.catapi.ui.common.UiState

@Composable
fun CatDetailScreen(
    viewModel: CatDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            is UiState.Success -> {
                // Breed data is needed to show this screen
                val breed = state.data.breeds?.firstOrNull()
                if (breed != null) {
                    CatDetailContent(cat = state.data, breed = breed)
                } else {
                    Text(
                        text = "No breed information available for this cat.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CatDetailContent(cat: CatImage, breed: Breed) {
    // Scrollable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. Big Image
        Card(elevation = CardDefaults.cardElevation(4.dp)) {
            AsyncImage(
                model = cat.url,
                contentDescription = breed.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(16.dp))

        // 2. Basic Info
        Text(breed.name, style = MaterialTheme.typography.headlineLarge)
        Text("Origin: ${breed.origin}", style = MaterialTheme.typography.bodyLarge)
        Text("Life Span: ${breed.lifeSpan} years", style = MaterialTheme.typography.bodyLarge)
        Text(breed.temperament, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        // 3. Star Ratings
        Text("Characteristics", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        RatingBar(label = "Intelligence", rating = breed.intelligence)
        RatingBar(label = "Affection Level", rating = breed.affectionLevel)
        RatingBar(label = "Child Friendly", rating = breed.childFriendly)
        RatingBar(label = "Social Needs", rating = breed.socialNeeds)

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        // 4. Description
        Text("Description", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(breed.description, style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        // 5. Links
        Text("More Info", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        ClickableLink(text = "View Wikipedia Page", url = breed.wikipediaUrl)
        Spacer(Modifier.height(8.dp))
        ClickableLink(text = "View VetStreet Page", url = breed.vetstreetUrl)
    }
}

// Custom Composable to show the stars
@Composable
fun RatingBar(label: String, rating: Int, maxRating: Int = 5) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row {
            repeat(maxRating) { index ->
                val icon = if (index < rating) Icons.Filled.Star else Icons.Outlined.StarOutline
                val tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Gray
                Icon(icon, contentDescription = null, tint = tint)
            }
        }
    }
}

// Helper for the clickable links
@Composable
fun ClickableLink(text: String, url: String?) {
    val uriHandler = LocalUriHandler.current

    if (!url.isNullOrBlank()) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                try {
                    // For bad URLs
                    uriHandler.openUri(url)
                } catch (e: Exception) {
                }
            }
        )
    }
}