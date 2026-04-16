package com.zachstopper.tagmusicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zachstopper.tagmusicplayer.data.repository.TagGroupRepository
import com.zachstopper.tagmusicplayer.data.repository.TagTagGroupRepository
import com.zachstopper.tagmusicplayer.model.TagFilterRules
import com.zachstopper.tagmusicplayer.model.TagGroupWithTags
import com.zachstopper.tagmusicplayer.model.TagSelectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TagUiModel(
    val id: Long,
    val name: String,
    val selectionState: TagSelectionState = TagSelectionState.UNSELECTED
)

class TagSelectionScreenViewModel(
    private val tagTagGroupRepository: TagTagGroupRepository,
    private val tagGroupRepository: TagGroupRepository
) : ViewModel() {


    // UI State
    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode

    private val _selectedTags = MutableStateFlow<Map<Long, TagSelectionState>>(emptyMap())
    val selectedTags: StateFlow<Map<Long, TagSelectionState>> = _selectedTags

    private val _selectedTagsForEdit = MutableStateFlow<Set<Long>>(emptySet())
    val selectedTagsForEdit: StateFlow<Set<Long>> = _selectedTagsForEdit

    private val _expandedGroups = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val expandedGroups: StateFlow<Map<Long, Boolean>> = _expandedGroups


    // Data / Derived State
    val allTagGroupsWithTags: StateFlow<List<TagGroupWithTags>> =
        tagTagGroupRepository.allTagGroupsWithTags
            .map { groups ->
                groups.map  { group ->
                    group.copy(
                        tags = group.tags.sortedBy { it.name.lowercase() }
                    )

                }

            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Section Logic
    fun cycleTagState(id: Long) {
        _selectedTags.update { current ->
            val nextState = when (current[id] ?: TagSelectionState.UNSELECTED) {
                TagSelectionState.UNSELECTED -> TagSelectionState.INCLUDE_TIER2
                TagSelectionState.INCLUDE_TIER2 -> TagSelectionState.EXCLUDE
                TagSelectionState.EXCLUDE -> TagSelectionState.INCLUDE_TIER1
                TagSelectionState.INCLUDE_TIER1 -> TagSelectionState.UNSELECTED
            }
            current + (id to nextState)
        }
    }
    fun cycleMode() {
        _isSelectionMode.value = !_isSelectionMode.value
        _selectedTagsForEdit.value = emptySet()
    }
    fun toggleTagSelection(id: Long) {
        _selectedTagsForEdit.update { currentSet ->
            if (currentSet.contains(id)) {
                currentSet - id
            } else {
                currentSet + id
            }
        }
    }
    fun clearTags(){
        _selectedTags.value = emptyMap()
    }

    // Group Actions
    fun toggleGroup(groupId: Long) {
        _expandedGroups.update { current ->
            val currentState = current[groupId] ?: false
            current + (groupId to !currentState)
        }
    }
    fun createTagGroup(name: String) {
        viewModelScope.launch {
            tagGroupRepository.insertTagGroup(name)
        }
    }
    fun assignSelectedTagsToGroup(groupId: Long) {
        viewModelScope.launch {
            val selectedIds = _selectedTagsForEdit.value
            tagTagGroupRepository.assignTagsToGroup(groupId, selectedIds)
            _selectedTagsForEdit.value = emptySet()
        }
    }

    // Rule Building
    fun buildFilterRules(): TagFilterRules {

        val selection = _selectedTags.value

        val exclude =
            selection.filterValues { it == TagSelectionState.EXCLUDE }
                .keys
                .toSet()

        val tier1 = selection.filterValues { it == TagSelectionState.INCLUDE_TIER1 }
            .keys
            .toSet()

        val tier2 = selection.filterValues { it == TagSelectionState.INCLUDE_TIER2 }
            .keys
            .toSet()


        return TagFilterRules(
            exclude = exclude,
            includeTier1 = tier1,
            includeTier2 = tier2
        )

    }

}

