package com.projecthit.fyp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.projecthit.fyp.R

import com.projecthit.fyp.functions.readHumidity
import com.projecthit.fyp.functions.readState
import com.projecthit.fyp.functions.readTemp
import com.projecthit.fyp.functions.readTimer

import com.projecthit.fyp.functions.writeState
import com.projecthit.fyp.functions.writeTimer
import com.projecthit.fyp.ui.theme.BlueCustom
import com.projecthit.fyp.ui.theme.ClassicBlue
import com.projecthit.fyp.ui.theme.ClassicDarkBlue
import com.projecthit.fyp.ui.theme.GreyCustom

import com.projecthit.fyp.ui.theme.NavyCustom
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    ControlScreen()
}


class Item(status: Boolean,timer: Int, val path: String, clock: String) {
    var status = mutableStateOf(status)
        private set
    var timer = mutableStateOf(timer)
        private set
    var clock = mutableStateOf(clock)
        private set
}



@Composable
fun ControlScreen() {

    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {

        val light_icon = R.drawable.bulb
        val plug_icon = R.drawable.plug

            TempAndHumidity()
            ControlWidget("i1", "Light 1", light_icon)
            ControlWidget("i2", "Light 2", light_icon)
            ControlWidget("i3", "Plug", plug_icon)

    }
}

@Composable
fun TempAndHumidity() {

    var temp by remember { mutableStateOf(0.0f)}
    var humidity by remember { mutableStateOf(0.0f)}

    LaunchedEffect(true){
        readHumidity {
            humidity = it
        }
        readTemp {
            temp = it
        }
    }

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth())  {
        Row{
            Icon(painter = painterResource(id = R.drawable.temp), contentDescription = "", tint = Color.White)
            TextCustom(text = "$temp °C")
        }
        Row{
            Icon(painter = painterResource(id = R.drawable.humid), contentDescription = "", tint = Color.White)
            TextCustom(text = "$humidity %")
        }
    }

}

@Composable
fun ControlWidget(path: String, text: String, icon: Int) {

    var showDialog by remember { mutableStateOf(false) }




    val item = remember{ Item( false, 0, path, "") }

    LaunchedEffect(true) {
        readState("${item.path}/status") {
            item.status.value = it
        }
        readTimer("${item.path}/timer") {
            item.timer.value = it
        }
    }

    Card(colors = CardDefaults.cardColors(containerColor = ClassicDarkBlue)) {

        val modifier = Modifier.align(Alignment.CenterHorizontally)

        TextCustom(text, modifier)

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                    writeState(item.path, !item.status.value)
                    item.clock.value = getCurrentTime()
                },
                tint = if(item.status.value) Color.Yellow else Color.LightGray
            )


        Row(modifier = Modifier.padding(5.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = ClassicBlue),
                modifier = Modifier
                    .padding(3.dp)
                    .weight(1f)
            ) {
                ButtonCustomTimer(
                    item = item,
                    modifier = modifier,
                    showDialog = showDialog,
                    onShowDialogChange = { showDialog = it },
                    onTimerValueConfirm = { timerValue ->
                        writeTimer(item.path, timerValue)
                    }
                )
                TextCustom(text = if (item.timer.value==0) "Not Set"  else "Set for", modifier)
                TextCustom(text = if (item.timer.value==0) ""  else (item.timer.value).toString() + " secs", modifier)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = ClassicBlue),
                modifier = Modifier
                    .padding(3.dp)
                    .weight(1f)
            )  {

                ButtonCustomTurn(item, modifier = modifier)
                TextCustom(text = "Since", modifier)
                TextCustom(text = item.clock.value, modifier)
            }

        }


    }

    if (showDialog) {
        TimerValueDialog(
            onDismiss = { showDialog = false },
            onConfirm = { timerValue ->
                writeTimer(item.path, timerValue)
                showDialog = false
            }
        )
    }

}

@Composable
fun TextCustom(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun ButtonCustomTimer(
    item: Item,
    modifier: Modifier,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onTimerValueConfirm: (Int) -> Unit
) {
    Button(
        onClick = { onShowDialogChange(true) },
        colors = ButtonDefaults.buttonColors(containerColor = ClassicDarkBlue),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(3.dp, 0.dp)
    ) {
        TextCustom(text = "TIMER", modifier = modifier)
    }
}

@Composable
fun ButtonCustomTurn(item: Item, modifier: Modifier) {
    Button(
        onClick = { writeState(item.path, !item.status.value)
                  item.clock.value = getCurrentTime()
                  },
        colors = ButtonDefaults.buttonColors(containerColor = ClassicDarkBlue),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(3.dp, 0.dp)
    ) {
        TextCustom(text = if (item.status.value) "ON" else "OFF", modifier = modifier)
    }
}



fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return currentTime.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerValueDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val (text, setText) = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            TextCustom("Set Timer Value")
        },
        text = {
            TextField(
                value = text,
                onValueChange = setText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val timerValue = text.toIntOrNull() ?: 0
                    onConfirm(timerValue)
                }
            ) {
                TextCustom("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                TextCustom("Cancel")
            }
        }
    )
}