package lucns.tasklist.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "items")
@Parcelize
class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var timestamp: Long = 0,
    var concluded: Boolean = false
) : Parcelable

/*
@Entity(tableName = "items")
@Parcelize
class Task : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var title: String = ""
        get() = field
        set(value) {
            field = value
        }

    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var timestamp: Long = 0
        get() = field
        set(value) {
            field = value
        }

    var concluded: Boolean = false
        get() = field
        set(value) {
            field = value
        }
}
*/