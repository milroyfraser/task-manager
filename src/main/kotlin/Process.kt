import java.util.*

class Process(
    val priority: Priority
) {
    val pid: String = UUID.randomUUID().toString()
    val createdAt: Date = Calendar.getInstance().time

    fun kill() {
        println("RIP    : $pid $priority $createdAt")
    }
}