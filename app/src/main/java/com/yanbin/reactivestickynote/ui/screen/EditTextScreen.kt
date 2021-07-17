package com.yanbin.reactivestickynote.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.yanbin.reactivestickynote.R
import com.yanbin.reactivestickynote.domain.EditTextViewModel
import com.yanbin.reactivestickynote.ui.theme.TransparentBlack
import com.yanbin.utils.subscribeBy
import com.yanbin.utils.toMain

@Composable
fun EditTextScreen(
    editTextViewModel: EditTextViewModel,
    onLeaveScreen: () -> Unit,
) {
    val text by editTextViewModel.text.subscribeAsState(initial = "")
    editTextViewModel.leavePage
        .toMain()
        .subscribeBy( onNext = { onLeaveScreen() })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .background(TransparentBlack)
    ) {
        TextField(
            value = text,
            onValueChange = editTextViewModel::onTextChanged,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(fraction = 0.8f),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.White
            ),
            textStyle = MaterialTheme.typography.h5
        )

        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = editTextViewModel::onCancelClicked
        ) {
            val painter = painterResource(id = R.drawable.ic_close)
            Icon(painter = painter, contentDescription = "Close", tint = Color.White)
        }

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = editTextViewModel::onConfirmClicked
        ) {
            val painter = painterResource(id = R.drawable.ic_check)
            Icon(painter = painter, contentDescription = "Delete", tint = Color.White)
        }
    }
}
