package es.jac.roncafit.managers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitObject {

    companion object{
        private var instance: Retrofit? = null

        val baseUrl = "http://192.168.56.1:5000/"

        fun getInstance(): Retrofit {
            synchronized(this){
                if(instance==null){
                    instance = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return instance!!
        }
    }
}