package com.appspell.sportintervaltimer

sealed class Navigation(open val route: String) {

    data object TimerSetup : Navigation(route = "TimerSetup")

    data object Timer : Navigation(route = "Timer")

    data object Finish : Navigation(route = "Finish")

}