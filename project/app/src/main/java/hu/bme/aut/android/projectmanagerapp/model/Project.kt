package hu.bme.aut.android.projectmanagerapp.model

class Project(val id: Int, val name: String, val desc: String, val client: String, val startDate: String,
              val endDate: String, val length: Int) {

    companion object {
        private var lastProject = 0
        fun createProjectList(numProjects: Int): ArrayList<Project> {
            val projects = ArrayList<Project>()
            for (i in 1..numProjects) {
                projects.add(
                    Project(
                        ++lastProject, "Project " + lastProject, "random project", "random client",
                        "2", "2", 4
                    )
                )
            }
            return projects
        }//leírás, ügyfél, start, end date, becsült hossz,
    }
}