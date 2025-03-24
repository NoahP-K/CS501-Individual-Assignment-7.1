package com.example.individualassignment_71

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.individualassignment_71.ui.theme.IndividualAssignment_71Theme
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndividualAssignment_71Theme {
                RecipeScreen()
            }
        }
    }
}

@Composable
fun RecipeScreen(viewModel: RecipeSearchViewModel = viewModel()) {
    var search by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Search for a recipe", fontSize = 20.sp) }
            )
            Button(onClick = {
                if (search.isNotEmpty()) {
                    viewModel.fetchRecipes(search)
                }
            }) {
                Text("Get Recipes", fontSize = 20.sp)
            }

            when (searchState) {
                RecipeSearchViewModel.SearchState.Initial -> {}
                RecipeSearchViewModel.SearchState.Loading -> {
                    CircularProgressIndicator()
                }

                is RecipeSearchViewModel.SearchState.Success -> {
                    val searchResponse =
                        (searchState as RecipeSearchViewModel.SearchState.Success).searchResponse
                    ShowRecipes(searchResponse)
                }

                is RecipeSearchViewModel.SearchState.Error -> {
                    val errorMessage =
                        (searchState as RecipeSearchViewModel.SearchState.Error).errorMessage
                    Text("Error: $errorMessage", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ShowRecipes(searchResponse: SearchResponse){
    val scrollState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    LazyColumn(
        state = scrollState,
        userScrollEnabled = true
    ){
        if(searchResponse.meals == null){
            item(){ Text(text = "No recipes found :(") }
        } else {
            items(searchResponse.meals.size) { i ->
                val recipe = searchResponse.meals[i]
                val painter = rememberAsyncImagePainter(model = recipe.imageUrl)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "picture of " + recipe.name,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                    )
                    Spacer(Modifier.size(12.dp))
                    Column() {
                        Text(
                            text = recipe.name,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "Origin: " + recipe.area,
                            fontSize = 25.sp,
                            fontStyle = FontStyle.Italic
                        )
                        Text(
                            text = "Category: " + recipe.category,
                            fontSize = 25.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                Spacer(Modifier.size(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IndividualAssignment_71Theme {

    }
}