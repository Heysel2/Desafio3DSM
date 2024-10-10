package udb.edu.sv.dsm.tareas2.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import udb.edu.sv.dsm.tareas2.model.Resource

interface ApiService {
    @GET("api/education/resources/")
    fun getResources(): Call<List<Resource>>

    @GET("api/education/resources/{id}")
    fun getResourceById(@Path("id") id: String): Call<Resource>


    @POST("api/education/resources/")
    fun addResources(@Body resource: Resource) : Call<Resource>

    @PUT("api/education/resources/{id}")
    fun updateResources(@Path("id") id: String, @Body resource: Resource): Call<Resource>

    @DELETE("api/education/resources/{id}")
    fun deleteResources(@Path("id") id:String): Call<Void>
}
