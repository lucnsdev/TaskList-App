package lucns.tasklist

import android.app.Application
import lucns.tasklist.repositories.AppContainer
import lucns.tasklist.repositories.AppDataContainer

class TaskApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}