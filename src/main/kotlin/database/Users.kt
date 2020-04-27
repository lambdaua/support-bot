package database

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*

class Users(database: MongoDatabase) {
    private val collection: MongoCollection<User> = database.getCollection()

    fun create(id: Int, username: String, firstName: String): User {
        val user = User(id, username, firstName)
        collection.insertOne(user)
        return user
    }

    fun getByTelegramId(userId: Int): User? {
        return collection.findOne(User::userId eq userId)
    }

}

data class User(
    val userId: Int,
    val username: String = "",
    val firstName: String
)