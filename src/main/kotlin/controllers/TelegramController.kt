package controllers

import org.telegram.telegrambots.api.objects.Update
import telegram.MessagesReceiver
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/tl")
@Produces(MediaType.APPLICATION_JSON)
class TelegramController(
    private val messagesReceiver: MessagesReceiver,
    private val executor: ExecutorService
) {
    @POST
    @Path("/webhook")
    @Consumes(MediaType.APPLICATION_JSON)
    fun webhook(update: Update): Response {
        executor.submit {
            messagesReceiver.handle(update)
        }

        return Response.ok().build()
    }
}