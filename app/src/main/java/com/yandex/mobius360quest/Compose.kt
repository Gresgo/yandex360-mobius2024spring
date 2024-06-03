package com.yandex.mobius360quest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yandex.mobius360quest.core.BaseComposeFragment
import com.yandex.mobius360quest.core.clickable

/**
 * Пользователь должен пройти проверку на робота
 * Для этого нужно 1 раз нажать на первую кнопку и 2 раза на вторую
 */
class Compose : BaseComposeFragment() {

    @Composable
    override fun ComposeContent() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RestoreScreen(Modifier.padding(innerPadding))
        }
    }

    @Composable
    fun RestoreScreen(modifier: Modifier = Modifier) {
        val enabled1 = remember { mutableStateOf(false) }
        val enabled2 = remember { mutableStateOf(false) }

        Column(modifier = modifier) {
            Box(Modifier.weight(0.1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Red)
                        .clickable(enabled = enabled2.value) {
                            Log.e("CLICK", "CLICK1")
                            enabled1.value = !enabled1.value
                        }
                        .padding(all = TOTALLY_NORMAL_PADDING)
                        .background(color = Blue)
                )
                Text(text = "Докажите что вы не робот", modifier = Modifier.align(Alignment.Center), )
            }

            Box(Modifier.weight(0.1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Blue)
                        .padding(all = TOTALLY_NORMAL_PADDING)
                        .background(color = Red)
                        .clickable { _ ->
                            Log.e("CLICK", "CLICK2")
                            enabled1.value = !enabled1.value
                        }

                )
                Text(text = "и сделайте это дважды", modifier = Modifier.align(Alignment.Center))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navigateToNext() },
                    enabled = enabled1.value && enabled2.value
                ) {
                    Text("Дальше")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewToggleButtons() {
        RestoreScreen()
    }

    private companion object {
        val TOTALLY_NORMAL_PADDING = 500.dp
    }
}