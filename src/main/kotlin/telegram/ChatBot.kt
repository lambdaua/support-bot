package telegram

import org.telegram.telegrambots.ApiContext
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import utils.Markdown
import database.User

class ChatBot(
    private val botToken: String
) : DefaultAbsSender(ApiContext.getInstance(DefaultBotOptions::class.java)) {
    override fun getBotToken(): String {
        return botToken
    }

    fun sendQuesToAdminGroup(text: String, from: User): Int? {
        val message = "${Markdown.escape(from.usernameOrIdLink())}\n\n" + Markdown.escape(text)

        val sendMessage = SendMessage(adminGroupId, message)
            .enableMarkdown(true)
            .disableWebPagePreview()

        return try {
            execute(sendMessage).messageId
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sendAnswerToUser(responseText: String, userId: Long) {
        execute(SendMessage(userId, responseText))
    }
}

fun Number.telegramLink(text: String): String {
    return "[$text](tg://user?id=$this)"
}

fun User.usernameOrIdLink(): String {
    if (username.isEmpty()) return this.userId.telegramLink(this.firstName)
    return "@" + this.username
}