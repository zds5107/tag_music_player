package com.zachstopper.tagmusicplayer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zachstopper.tagmusicplayer.data.repository.SongRepository
import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagGroupRepository
import com.zachstopper.tagmusicplayer.data.repository.TagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagTagGroupRepository
import com.zachstopper.tagmusicplayer.data.service.AutoTagService
import com.zachstopper.tagmusicplayer.data.service.LovedTagService
import com.zachstopper.tagmusicplayer.ui.player.PlayerScreen
import com.zachstopper.tagmusicplayer.ui.tagselection.TagSelectionScreen
import com.zachstopper.tagmusicplayer.viewmodel.PlayerScreenViewModel
import com.zachstopper.tagmusicplayer.viewmodel.PlayerScreenViewModelFactory

@Composable
fun AppNavHost(songRepository: SongRepository, tagRepository: TagRepository, songTagRepository: SongTagRepository, tagTagGroupRepository: TagTagGroupRepository, tagGroupRepository: TagGroupRepository) {

    val navController = rememberNavController()
    val context = LocalContext.current



    val autoTagService = AutoTagService(
        tagRepository = tagRepository,
        songTagRepository = songTagRepository
    )

    val lovedTagService = LovedTagService(
        tagRepository = tagRepository,
        songTagRepository = songTagRepository
    )



    val playerScreenViewModel: PlayerScreenViewModel = viewModel(
        factory = PlayerScreenViewModelFactory(context, songTagRepository, songRepository, autoTagService, lovedTagService)
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "player",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("player") { PlayerScreen(playerScreenViewModel) }
            composable("tags") { TagSelectionScreen(tagTagGroupRepository, tagGroupRepository, playerScreenViewModel, onGenerate = { navController.navigate("player") {
                popUpTo("player") {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } }) }
        }

    }


}