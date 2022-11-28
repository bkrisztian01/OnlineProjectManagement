package hu.bme.aut.android.projectmanagerapp.ui.singlemilestone

import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone


sealed class SingleMilestoneViewState

object InProgress : SingleMilestoneViewState()
data class SingleMilestoneResponseSuccess(val data: Milestone) : SingleMilestoneViewState()
data class SingleMilestoneResponseError(val exceptionMsg: String) : SingleMilestoneViewState()