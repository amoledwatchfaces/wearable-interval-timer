package com.appspell.sportintervaltimer.timersetup

import com.appspell.sportintervaltimer.db.SavedInterval
import com.appspell.sportintervaltimer.db.SavedIntervalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val DEFAULT_SETS = 4
private const val DEFAULT_SECONDS = 60

// TODO replace it when implement a list
private const val SAVED_DEFAULT_NAME = "Previously saved"

class TimeSetupRepository @Inject constructor(
    private val intervalsDao: SavedIntervalDao
) {

    fun observeSavedInterval(): Flow<TimerSetupDataState> = flow {
        val savedData = intervalsDao.fetchByName(SAVED_DEFAULT_NAME)
        val defaultState = savedData?.toDataState() ?: DEFAULT_STATE
        if (savedData == null) {
            addNewInterval(defaultState)
        }
        emit(defaultState)
    }

    fun saveInterval(data: TimerSetupDataState) {
        intervalsDao.updateByName(
            name = SAVED_DEFAULT_NAME,
            sets = data.sets,
            workSeconds = data.workSeconds,
            restSeconds = data.restSeconds
        )
    }

    fun addNewInterval(data: TimerSetupDataState) {
        intervalsDao.insert(
            SavedInterval(
                id = null,
                order = 0,
                name = SAVED_DEFAULT_NAME,
                sets = data.sets,
                workSeconds = data.workSeconds,
                restSeconds = data.restSeconds
            )
        )
    }

    private fun SavedInterval.toDataState() =
        TimerSetupDataState(
            sets = this.sets,
            workSeconds = this.workSeconds,
            restSeconds = this.restSeconds
        )

    companion object {
        val DEFAULT_STATE = TimerSetupDataState(
            sets = DEFAULT_SETS,
            workSeconds = DEFAULT_SECONDS,
            restSeconds = DEFAULT_SECONDS,
        )
    }
}