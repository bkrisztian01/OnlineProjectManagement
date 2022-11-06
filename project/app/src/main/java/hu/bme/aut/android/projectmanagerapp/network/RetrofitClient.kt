package hu.bme.aut.android.projectmanagerapp.network

import hu.bme.aut.android.projectmanagerapp.BuildConfig
import hu.bme.aut.android.projectmanagerapp.network.milestone.MilestoneAPI
import hu.bme.aut.android.projectmanagerapp.network.project.ProjectAPI
import hu.bme.aut.android.projectmanagerapp.network.singleproject.SingleProjectAPI
import hu.bme.aut.android.projectmanagerapp.network.singletask.SingleTaskAPI
import hu.bme.aut.android.projectmanagerapp.network.task.TaskAPI
import hu.bme.aut.android.projectmanagerapp.network.user.UserAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object  RetrofitClient {
    const val MainServer="https://opmapi.azurewebsites.net/";
    val retrofitClient: Retrofit.Builder by lazy {
        val levelType: HttpLoggingInterceptor.Level = if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(MainServer)
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create())
    }

    val projectApiInterface: ProjectAPI by lazy {
        retrofitClient
            .build()
            .create(ProjectAPI::class.java)
    }

    val singletaskApiInterface: SingleTaskAPI by lazy {
        retrofitClient
            .build()
            .create(SingleTaskAPI::class.java)
    }
    val singleprojectApiInterface: SingleProjectAPI by lazy {
        retrofitClient
            .build()
            .create(SingleProjectAPI::class.java)
    }
    val taskApiInterface: TaskAPI by lazy {
        retrofitClient
            .build()
            .create(TaskAPI::class.java)
    }
    val milestoneApiInterface: MilestoneAPI by lazy {
        retrofitClient
            .build()
            .create(MilestoneAPI::class.java)
    }
    val userApiInterface: UserAPI by lazy {
        retrofitClient
            .build()
            .create(UserAPI::class.java)
    }
}
