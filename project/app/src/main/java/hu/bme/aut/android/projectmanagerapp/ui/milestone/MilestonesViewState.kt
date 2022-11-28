package hu.bme.aut.android.projectmanagerapp.ui.milestone

import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone


sealed class MilestonesViewState

object InProgress : MilestonesViewState()
data class MilestoneResponseSuccess(val data: List<Milestone>) : MilestonesViewState()
data class MilestoneResponseError(val exceptionMsg: String) : MilestonesViewState()