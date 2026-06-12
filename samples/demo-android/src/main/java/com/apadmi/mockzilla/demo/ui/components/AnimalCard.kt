package com.apadmi.mockzilla.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.android.sample.R
import com.apadmi.mockzilla.demo.engine.AnimalDto

@Composable
private fun AnimalDto.AnimalTypeDto.text() = when (this) {
    AnimalDto.AnimalTypeDto.Cow -> stringResource(R.string.animal_type_cow)
    AnimalDto.AnimalTypeDto.Sheep -> stringResource(R.string.animal_type_sheep)
    AnimalDto.AnimalTypeDto.Pig -> stringResource(R.string.animal_type_pig)
}

@Composable
internal fun AnimalCard(
    animal: AnimalDto
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(12.dp)
        )
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = animal.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(R.string.animal_age, animal.age),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            text = animal.animal.text()
        )
    }

    Text(
        text = animal.biography,
        style = MaterialTheme.typography.bodyMedium
    )

    Row {
        Text(
            text = stringResource(R.string.animal_owner_label),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = animal.owner.ifBlank { stringResource(R.string.animal_owner_placeholder) },
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
        )
    }
}
