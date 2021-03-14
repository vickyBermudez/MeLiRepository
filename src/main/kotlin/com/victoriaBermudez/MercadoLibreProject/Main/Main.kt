package com.victoriaBermudez.MercadoLibreProject.Main
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


import java.sql.Connection


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("hello world")
        val conn: Connection? = Connect().connecting()
        val opcion: Int
        println("si desea averiguar un usuario presione 1, sino presione 2")
        opcion = readLine()?.toInt() as Int
        println(opcion)


        println("ingrese el numero de ID que desea buscar")
        val sellerid: Int
        sellerid = readLine()?.toInt() as Int
        println(sellerid)
        val url: String
        url = "https://api.mercadolibre.com/sites/MLA/search?seller_id=" + sellerid + "/"
        println(url)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: MercadoLibreService = retrofit.create(MercadoLibreService::class.java)





        embeddedServer(Netty, 8080) {
            install(ContentNegotiation) {
                jackson {
                }
            }
            routing {

                get("/items") {
                    if (conn != null) {

                        val statement = conn.createStatement()
                        val resultSet = statement.executeQuery("SELECT * FROM items")
                        println("items")
                        val items = arrayListOf<Item>()
                        while (resultSet.next()) {
                            val idItem = resultSet.getInt("idItem")
                            val title = resultSet.getString("title")
                            val category_id = resultSet.getString("category_id")
                            val name = resultSet.getString("name")

                            items.add(
                                Item(
                                    idItem,
                                    title,
                                    category_id,
                                    name
                                )
                            )
                        }
                        call.respond(items)
                        println("connected to database successfully")
                    } else {
                        println("could not connect to database due error")
                    }
                }


                //val service: CocochesService = retrofit.create(CocochesService::class.java)
            }
        }
    }
}

