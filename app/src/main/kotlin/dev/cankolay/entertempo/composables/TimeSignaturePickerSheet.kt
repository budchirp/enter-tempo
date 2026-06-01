package dev.cankolay.entertempo.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.entertempo.shared.model.TimeSignature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSignaturePickerSheet(
    sheetState: SheetState,
    timeSignatures: List<TimeSignature>,
    selectedTimeSignature: TimeSignature,
    onTimeSignatureSelected: (TimeSignature) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            items(items = timeSignatures) { timeSignature ->
                TimeSignatureOptionRow(
                    timeSignature = timeSignature,
                    selected = selectedTimeSignature == timeSignature,
                    onClick = { onTimeSignatureSelected(timeSignature) }
                )
            }
        }
    }
}
