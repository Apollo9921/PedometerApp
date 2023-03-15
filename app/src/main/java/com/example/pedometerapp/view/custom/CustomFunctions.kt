package com.example.pedometerapp.view.custom

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pedometerapp.R
import com.example.pedometerapp.view.theme.Black
import com.example.pedometerapp.view.theme.Purple40
import com.example.pedometerapp.view.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlin.math.ln
import kotlin.math.pow

val small = 600.dp
val normal = 840.dp

var isLoaded = mutableStateOf(false)

@Composable
fun mediaQueryWidth(): Dp {
    return LocalContext.current.resources.displayMetrics.widthPixels.dp / LocalDensity.current.density
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission() {
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    } else {
        rememberPermissionState("")
    }
    if (permission.permission.isNotBlank()) {
        if (!permission.status.isGranted) {
            AlertDialog(
                onDismissRequest = { },
                containerColor = Black,
                title = {
                    val textToShow = if (permission.status.shouldShowRationale) {
                        stringResource(id = R.string.appAuthorization)
                    } else {
                        stringResource(id = R.string.appAuthorization)
                    }
                    Text(
                        text = textToShow,
                        color = White,
                        fontSize =
                        if (mediaQueryWidth() <= small) {
                            16.sp
                        } else if (mediaQueryWidth() <= normal) {
                            20.sp
                        } else {
                            24.sp
                        },
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (!permission.status.shouldShowRationale) {
                                    permission.launchPermissionRequest()
                                } else {
                                    context.startActivity(
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = Uri.fromParts(
                                                "package",
                                                context.packageName,
                                                null
                                            )
                                        }
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40,
                                contentColor = White,
                                disabledContentColor = White,
                                disabledContainerColor = Purple40
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.acceptPermission),
                                color = White,
                                fontSize =
                                if (mediaQueryWidth() <= small) {
                                    13.sp
                                } else if (mediaQueryWidth() <= normal) {
                                    17.sp
                                } else {
                                    21.sp
                                },
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            )
        }
    }
}

fun formatTotalCount(count: Float): String {
    if (count < 1000) return "%.2f".format(count)
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        count / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}