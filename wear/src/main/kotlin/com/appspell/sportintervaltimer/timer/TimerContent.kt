package com.appspell.sportintervaltimer.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.appspell.sportintervaltimer.R
import com.appspell.sportintervaltimer.R.drawable
import com.appspell.sportintervaltimer.R.string
import com.appspell.sportintervaltimer.theme.PrepareTheme
import com.appspell.sportintervaltimer.theme.RestTheme
import com.appspell.sportintervaltimer.theme.WorkTheme
import com.appspell.sportintervaltimer.timer.TimerType.PREPARE
import com.appspell.sportintervaltimer.timer.TimerType.REST
import com.appspell.sportintervaltimer.timer.TimerType.UNDEFINED
import com.appspell.sportintervaltimer.timer.TimerType.WORK
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun TimerContent(
    viewModel: TimerViewModel = hiltViewModel(), navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.onEach { newNavigationEvent ->
            navController.popBackStack()
            navController.navigate(
                newNavigationEvent.route, NavOptions.Builder().setLaunchSingleTop(true).build()
            )
        }.launchIn(this)
    }

    when (state.type) {
        WORK -> WorkTheme {
            TimerScreenContent(state = state,
                onPause = { viewModel.onPause() },
                onResume = { viewModel.onResume() },
                onSkip = { viewModel.onSkip() })
        }
        REST -> RestTheme {
            TimerScreenContent(state = state,
                onPause = { viewModel.onPause() },
                onResume = { viewModel.onResume() },
                onSkip = { viewModel.onSkip() })
        }
        else -> PrepareTheme {
            TimerScreenContent(state = state,
                onPause = { viewModel.onPause() },
                onResume = { viewModel.onResume() },
                onSkip = { viewModel.onSkip() })
        }
    }
}

@Composable
private fun TimerScreenContent(
    state: TimerUiState, onPause: () -> Unit, onResume: () -> Unit, onSkip: () -> Unit
) {
    var isSquare by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .onSizeChanged {
                isSquare = it.width == it.height
            },
        timeText = {
            if (isSquare) {
                TimeText(
                    timeTextStyle = MaterialTheme.typography.caption2,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                TimeText(
                    timeTextStyle = MaterialTheme.typography.caption3,
                    modifier = Modifier
                )
            }
        },
    ) {
        TimerCountDown(
            setsText = stringResource(
                string.number_of_number, state.currentSet, state.allSets
            ),
            timerText = state.time,
            type = state.type,
            progress = state.progress,
            isPaused = state.isPaused,
            onPause = onPause,
            onResume = onResume,
            onSkip = onSkip
        )
    }
}

@Composable
private fun TimerCountDown(
    setsText: String,
    timerText: String,
    type: TimerType,
    progress: Float,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkip: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val (circularProgress, sets, activity, timer, actionButtons) = createRefs()
        val verticalChain = createVerticalChain(
            sets,
            activity,
            timer,
            actionButtons,
            chainStyle = ChainStyle.Packed
        )

        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 7.dp,
            indicatorColor = MaterialTheme.colors.onSurface,
            trackColor= MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
            modifier = Modifier
                .constrainAs(circularProgress) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .aspectRatio(0.98f)
        )

        // sets left
        Text(
            text = setsText,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.caption1,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 14.dp)
                .constrainAs(sets) {
                    top.linkTo(circularProgress.top, 12.dp)
                    bottom.linkTo(activity.top)
                    start.linkTo(circularProgress.start)
                    end.linkTo(circularProgress.end)
                }
        )

        // type of activity
        Text(
            text = when (type) {
                PREPARE -> stringResource(id = R.string.type_prepare)
                WORK -> stringResource(id = R.string.type_work)
                REST -> stringResource(id = R.string.type_rest)
                UNDEFINED -> ""
            },
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.caption2,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(activity) {
                    top.linkTo(circularProgress.top)
                    bottom.linkTo(activity.top)
                    start.linkTo(circularProgress.start)
                    end.linkTo(circularProgress.end)
                }
        )

        // Time
        Text(
            text = timerText,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.display1,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(timer) {
                    top.linkTo(activity.bottom, 8.dp)
                    bottom.linkTo(actionButtons.top, 8.dp)
                    start.linkTo(circularProgress.start, 8.dp)
                    end.linkTo(circularProgress.end, 8.dp)
                }
        )

        // Action buttons (play & skip)
        TimerActionButtons(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(actionButtons) {
                    top.linkTo(timer.bottom)
                    bottom.linkTo(circularProgress.bottom)
                    start.linkTo(circularProgress.start)
                    end.linkTo(circularProgress.end)
                },
            isPaused = isPaused,
            onPause = onPause,
            onResume = onResume,
            onSkip = onSkip
        )
    }
}

@Composable
private fun TimerActionButtons(
    modifier: Modifier,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        if (!isPaused) {
            Button(
                colors = ButtonDefaults.secondaryButtonColors(),
                onClick = onPause,
                modifier = Modifier
                    .size(46.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .aspectRatio(1f),
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_pause),
                    contentDescription = stringResource(id = string.button_pause)
                )
            }
        } else {
            Button(
                colors = ButtonDefaults.primaryButtonColors(),
                onClick = onResume,
                modifier = Modifier
                    .size(46.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .aspectRatio(1f),
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_play),
                    contentDescription = stringResource(id = string.button_continue)
                )
            }
        }

        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(),
            onClick = onSkip,
            modifier = Modifier
                .size(46.dp)
                .padding(start = 4.dp, end = 4.dp)
                .aspectRatio(1f),
        ) {
            Icon(
                painter = painterResource(id = drawable.ic_skip),
                contentDescription = stringResource(id = string.button_skip)
            )
        }
    }
}