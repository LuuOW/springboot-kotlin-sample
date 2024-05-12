import com.cropyback.cropyback.repository.ImageRepository
import com.cropyback.cropyback.repository.UserRepository
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.*
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.awt.Image


@Configuration
@EnableMongoRepositories(basePackageClasses = [UserRepository::class, ImageRepository::class])
class MongoDatabase: AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String {
        return "cropy_db"
    }

    @Bean
    override fun mongoClient(): com.mongodb.client.MongoClient {
        val connectionString = "mongodb+srv://lkempe:gu6CFjrUYtEUX9ek@cluster0.zvnm2t9.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()

        val mongoClient = com.mongodb.client.MongoClients.create(mongoClientSettings)
        val database = mongoClient.getDatabase("cropy_db")
        runBlocking {
            database.runCommand(Document("ping", 1))
            println("Pinged your deployment. You successfully connected to MongoDB!")
        }
        return mongoClient
    }

}

