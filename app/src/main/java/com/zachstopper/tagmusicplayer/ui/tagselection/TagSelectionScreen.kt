package com.zachstopper.tagmusicplayer.ui.tagselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zachstopper.tagmusicplayer.data.repository.TagGroupRepository
import com.zachstopper.tagmusicplayer.data.repository.TagTagGroupRepository
import com.zachstopper.tagmusicplayer.model.TagSelectionState
import com.zachstopper.tagmusicplayer.viewmodel.PlayerScreenViewModel
import com.zachstopper.tagmusicplayer.viewmodel.TagSelectionScreenViewModel
import com.zachstopper.tagmusicplayer.viewmodel.TagSelectionScreenViewModelFactory
import com.zachstopper.tagmusicplayer.viewmodel.TagUiModel


@Composable
fun TagSelectionScreen(
    tagTagGroupRepository: TagTagGroupRepository,
    tagGroupRepository: TagGroupRepository,
    playerScreenViewModel: PlayerScreenViewModel,
    onGenerate: () -> Unit
) {



    val viewModel: TagSelectionScreenViewModel = viewModel(
        factory = TagSelectionScreenViewModelFactory( tagTagGroupRepository, tagGroupRepository)
    )

    val isSelectionMode by viewModel.isSelectionMode.collectAsState()

    val selectedTagsForEdit by viewModel.selectedTagsForEdit.collectAsState()

    val selectedTags by viewModel.selectedTags.collectAsState()

    val allTagGroupsWithTags by viewModel.allTagGroupsWithTags.collectAsState()

    val expandedGroups  by viewModel.expandedGroups.collectAsState()

    var showAssignDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {




        Surface(
            tonalElevation = 2.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val textColor = if (isSelectionMode) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    Text(
                        text = if (isSelectionMode) "Organize Mode" else "Filter Mode",
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor
                    )

                    IconButton(
                        onClick = { viewModel.cycleMode() },
                        modifier = Modifier.size(58.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelectionMode) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isSelectionMode) "Exit selection mode" else "Enter selection mode",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                HorizontalDivider()
            }
        }



        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(allTagGroupsWithTags) { tagGroupsWithTags ->

                val tagUiModelsForGroup = tagGroupsWithTags.tags.map { tagEntity ->
                    TagUiModel(
                        id = tagEntity.id,
                        name = tagEntity.name,
                        selectionState = selectedTags[tagEntity.id] ?: TagSelectionState.UNSELECTED
                    )
                }

                val isExpanded = expandedGroups[tagGroupsWithTags.tagGroup.id] ?: false

                CollapsibleSection(
                    title = tagGroupsWithTags.tagGroup.name,
                    isExpanded = isExpanded,
                    onToggle = { viewModel.toggleGroup(tagGroupsWithTags.tagGroup.id)}
                ) {
                    TagChipGrid(
                        tags = tagUiModelsForGroup,
                        isSelectionMode = isSelectionMode,
                        selectedTagsForEdit = selectedTagsForEdit,
                        onTagClick = { id ->
                            if (isSelectionMode) {
                                viewModel.toggleTagSelection(id)
                            } else {
                                viewModel.cycleTagState(id)
                            }
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (isSelectionMode) {

                OutlinedButton(
                    onClick = { showAssignDialog = true },
                    modifier = Modifier.weight(1f),
                    enabled = selectedTagsForEdit.isNotEmpty()
                ) {
                    Text("Assign")
                }

                OutlinedButton(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Create")
                }

            } else {

                Button(
                    onClick = {
                        val rules = viewModel.buildFilterRules()
                        playerScreenViewModel.generatePlaylist(rules)
                        onGenerate()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Generate")
                }

                OutlinedButton(
                    onClick = { viewModel.clearTags() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }
        }


    }



    if (showCreateDialog) {
        CreateTagGroupDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name ->
                viewModel.createTagGroup(name)
                showCreateDialog = false
            }
        )
    }

    if (showAssignDialog) {
        AssignTagGroupDialog(
            tagGroupsWithTags = allTagGroupsWithTags,
            onDismiss = { showAssignDialog = false },
            onConfirm = { groupId ->
                viewModel.assignSelectedTagsToGroup(groupId)
                showAssignDialog = false
            }
        )
    }


}