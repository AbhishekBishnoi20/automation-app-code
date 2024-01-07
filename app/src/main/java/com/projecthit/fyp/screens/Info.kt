package com.projecthit.fyp.screens

import android.text.SpannableString
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projecthit.fyp.ui.theme.ClassicBlue
import com.projecthit.fyp.ui.theme.ClassicDarkBlue


@Composable
fun InfoScreen() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ClassicBlue)
                .padding(16.dp), // Padding around the entire screen
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = ClassicBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Final Year Project",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "By",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Group",
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    val studentNames = listOf(
                        "Pragnesh Parmar (U20EE037)",
                        "Avadh Vadhiya (U20EE021)",
                        "Pratik Parmar (U20EE030)",
                        "Abhishek Bishnoi (U20EE050)"
                    )
                    studentNames.forEach { studentName ->
                        StudentNameCard(nameWithId = studentName)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentNameCard(nameWithId: String) {
    val (name, id) = nameWithId.split('(')
    val firstLetter = name.take(1)
    val restOfTheName = name.drop(1)

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
            append(firstLetter)
        }
        append(restOfTheName)
        append(" ($id")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = ClassicDarkBlue),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Text(
            text = annotatedString,
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp
        )
    }
}




