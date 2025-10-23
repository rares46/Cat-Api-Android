package com.example.catapi.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.catapi.data.model.CatImage
import com.example.catapi.ui.common.UiState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox // New import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    navController: NavController,
    viewModel: CatListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Determine if the UI is currently refreshing.
    // This can be when a network request is loading OR when the user has just initiated a pull.
    val isRefreshing = uiState is UiState.Loading

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cats!") })
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.fetchCats() }, // Directly call the refresh action
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            val cats = (uiState as? UiState.Success)?.data
            if (!cats.isNullOrEmpty()) {
                CatGrid(cats = cats) { catId ->
                    navController.navigate("detail?imageId=$catId")
                }
            }
            // The content of the Box scrollable.
            when (val state = uiState) {
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No cats found!", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
                is UiState.Loading -> {
                    // The spinner is up
                }
            }
        }
    }
}

@Composable
fun CatGrid(cats: List<CatImage>, onCatClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(cats, key = { it.id }) { cat ->
            CatGridItem(cat = cat, onClick = {
                onCatClick(cat.id) // Doar trimitem ID-ul simplu
            })
        }
    }
}

@Composable
fun CatGridItem(cat: CatImage, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = cat.url,
            contentDescription = cat.breeds.firstOrNull()?.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), // Square
            contentScale = ContentScale.Crop
        )
    }
}
