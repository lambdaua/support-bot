package configurations

import com.mongodb.MongoClientURI
import io.dropwizard.Configuration

class AppConfiguration(
    val mongoUri: MongoClientURI,
    val bot: Bot
) : Configuration() {
    private val longpoll = false

    fun isLongpoll(): Boolean {
        return longpoll
    }
}

data class Bot(var name: String, var token: String)