import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import configurations.AppConfiguration
import controllers.TelegramController
import database.Users
import database.SupportHistory
import io.dropwizard.Application
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import org.litote.kmongo.KMongo
import telegram.ChatBot
import telegram.MessagesReceiver
import io.dropwizard.setup.Environment
import org.litote.kmongo.id.jackson.IdJacksonModule
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.TelegramBotsApi
import telegram.LongPolling
import java.util.concurrent.Executors

class App : Application<AppConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ApiContextInitializer.init()
            App().run(*args)
        }
    }

    override fun initialize(bootstrap: Bootstrap<AppConfiguration>?) {
        super.initialize(bootstrap)

        val module = KotlinModule()

        bootstrap!!.objectMapper
            .registerModule(module)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(IdJacksonModule())

        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(
            bootstrap.configurationSourceProvider,
            EnvironmentVariableSubstitutor(false)
        )
    }

    override fun run(conf: AppConfiguration, environment: Environment) {
        val mongoClient = KMongo.createClient(conf.mongoUri)
        val database = mongoClient.getDatabase(conf.mongoUri.database!!)

        val users = Users(database)
        val supportHistory = SupportHistory(database)

        val botName = conf.bot.name
        val botToken = conf.bot.token

        val chatBot = ChatBot(botToken)

        val messagesReceiver = MessagesReceiver(chatBot, users, supportHistory)

        if (conf.isLongpoll()) {
            val botsApi = TelegramBotsApi()
            botsApi.registerBot(LongPolling(botName, botToken, messagesReceiver))
        }

        val executor = Executors.newFixedThreadPool(16)
        environment.jersey().register(TelegramController(messagesReceiver, executor))
    }
}