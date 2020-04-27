package telegram

import org.telegram.telegrambots.api.methods.ParseMode
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.exceptions.TelegramApiException

const val adminGroupId = ""

class PeerExt(private val chatBot: ChatBot, private val chatId: String) {
    @Throws(TelegramApiException::class)
    fun sendText(text: String) {
        chatBot.execute(
            SendMessage()
                .setChatId(chatId)
                .setText(text)
                .setParseMode(ParseMode.MARKDOWN)
                .disableWebPagePreview()
        )
    }
}