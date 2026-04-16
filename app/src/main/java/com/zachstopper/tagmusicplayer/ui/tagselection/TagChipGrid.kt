package com.zachstopper.tagmusicplayer.ui.tagselection
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zachstopper.tagmusicplayer.model.TagSelectionState
import com.zachstopper.tagmusicplayer.viewmodel.TagUiModel

@Composable
fun TagChipGrid( tags: List<TagUiModel>,
                 isSelectionMode: Boolean,
                 selectedTagsForEdit: Set<Long>,
                 onTagClick: (Long) -> Unit
) {
    FlowRow( modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            val isSelected = if (isSelectionMode) {
                selectedTagsForEdit.contains(tag.id)
            } else {
                tag.selectionState != TagSelectionState.UNSELECTED }
            val chipColor = if (isSelectionMode) {
                if (isSelected) Color(0xFF2196F3) else Color.LightGray
            } else {
                when(tag.selectionState) {
                    TagSelectionState.UNSELECTED -> MaterialTheme.colorScheme.surface
                    TagSelectionState.EXCLUDE -> Color(0xFFE53935)
                    TagSelectionState.INCLUDE_TIER1 -> Color(0xFF4055EC)
                    TagSelectionState.INCLUDE_TIER2 -> Color(0xFF4CAF50)
                }
            }
            FilterChip(
                selected = isSelected,
                onClick = { onTagClick(tag.id) },
                label = { Text(tag.name) },
                leadingIcon = {
                    Icon(Icons.Default.Tag, contentDescription = null)
                              },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = chipColor,
                    selectedLabelColor = Color.White,
                    )
            )
        }
    }
}