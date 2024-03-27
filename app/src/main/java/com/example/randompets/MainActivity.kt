package com.example.randompets

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var petImageURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.petbutton)
        val image = findViewById<ImageView>(R.id.imageView3)

        getNextImage(button, image)
        Log.d("petImageURL", "pet image URL set")
        //petbutton.setOnClickListener({getDogImageURL()})
    }

    private fun getDogImageURL() {
        val client = AsyncHttpClient()
        client["https://dog.ceo/api/breeds/image/random", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful$json")
                petImageURL = json.jsonObject.getString("message")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }

    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            val choice = Random.nextInt(2)
            if (choice == 0) {
                getDogImageURL()
            }
            else {
                getCatImageURL()
            }

            Glide.with(this)
                .load(petImageURL)
                .fitCenter()
                .into(imageView)
        }

    }

    private fun getCatImageURL() {
        val client = AsyncHttpClient()
        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Cat", "response successful$json")
                var resultsJSON = json.jsonArray.getJSONObject(0)
                petImageURL = resultsJSON.getString("url")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Cat Error", errorResponse)
            }
        }]
    }


}