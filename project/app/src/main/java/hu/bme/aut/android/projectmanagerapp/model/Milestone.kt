package hu.bme.aut.android.projectmanagerapp.model

class Milestone (val id: Int, val name: String, val projectID: Int) {
    private lateinit var tasks: ArrayList<Task>
    fun getTasks()=tasks;
    companion object {
        private var lastMilestone = 0
        fun createMileStoneList(numMilestone: Int): ArrayList<Milestone> {
            val milestones = ArrayList<Milestone>()
            for (i in 1..numMilestone) {
                milestones.add(
                    Milestone(
                        ++lastMilestone, "Milestone " + lastMilestone, 3)

                )
            }
            return milestones
        }
    }
}