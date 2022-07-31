package com.yanbin.reactivestickynote.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yanbin.reactivestickynote.R
import com.yanbin.utils.subscribeBy
import com.yanbin.utils.toMain

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    toEditorPage: () -> Unit
) {
    val text by loginViewModel.text.subscribeAsState(initial = "")

    loginViewModel.toEditorPage
        .toMain()
        .subscribeBy (onNext = { toEditorPage() })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = loginViewModel::onTextChanged,
            textStyle = MaterialTheme.typography.button
        )
        
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = loginViewModel::onEnterClicked
        ) {
            Text(
                text = stringResource(id = R.string.login_button_enter),
                style = MaterialTheme.typography.button
            )
        }
    }
}