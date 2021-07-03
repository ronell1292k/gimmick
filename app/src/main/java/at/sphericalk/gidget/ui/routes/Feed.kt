package at.sphericalk.gidget.ui.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Composable
fun Feed(navController: NavController, viewModel: FeedViewModel) {
    val auth = FirebaseAuth.getInstance()

    // check if current user is signed in
    if (auth.currentUser == null) {
        navController.navigate("welcome")
    }
    val datastore = LocalActivity.current.dataStore
    val username = runBlocking { datastore.data.map { it[Constants.USERNAME] ?: "" }.first() }

    viewModel.fetchEvents()

    Scaffold(topBar = {
        TopAppBar(
            elevation = 0.dp,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.background,
            title = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "@$username's feed")
                }
            },
        )
    }) {
        LazyColumn(Modifier.padding(horizontal = 16.dp)) {
            items(viewModel.events) { event ->
                Text(text = "${event.actor.login} ${event.type} ${event.repo.name}")
            }
        }
    }
}
