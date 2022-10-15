package hu.bme.aut.android.projectmanagerapp.model

class Task (val id: Int, val name: String, val desc: String, val status: String, val startDate: String,
            val endDate: String, val projectID: Int, val milestoneID: Int) {
    companion object {
        private var lastTask = 0
        fun createTaskList(numTasks: Int): ArrayList<Task> {
            val tasks = ArrayList<Task>()
            for (i in 1..numTasks) {
                tasks.add(
                    Task(
                        ++lastTask, "Task " + lastTask, "random desc", "random status",
                        "2", "2", 4,5
                    )
                )
            }
            return tasks
        }
    }
}