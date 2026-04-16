package com.zachstopper.tagmusicplayer.ui.tagselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zachstopper.tagmusicplayer.model.TagGroupWithTags

@Composable
fun AssignTagGroupDialog(
    tagGroupsWithTags: List<TagGroupWithTags>,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var selectedGroupId by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Assign Tag Group") },
        text = {
            Column {
                tagGroupsWithTags.forEach { tagGroupWithTags ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedGroupId = tagGroupWithTags.tagGroup.id }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedGroupId == tagGroupWithTags.tagGroup.id,
                            onClick = { selectedGroupId = tagGroupWithTags.tagGroup.id }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tagGroupWithTags.tagGroup.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedGroupId?.let { onConfirm(it) }
                },
            ) {
                Text("Assign")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}