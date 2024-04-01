package com.example.puresip_new

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ThingSpeakDataFetcher(private val listener: OnDataFetchedListener) : AsyncTask<Void, Void, String>() {

    interface OnDataFetchedListener {
        fun onDataFetched(data: String?)
    }

    private val API_KEY = "DJ7O2PY21AQUJ5X0"
    private val CHANNEL_ID = "2413615"
    private val THINGSPEAK_URL = "https://api.thingspeak.com/channels/$CHANNEL_ID/feeds.json?api_key=$API_KEY"

    override fun doInBackground(vararg params: Void?): String? {
        try {
            val url = URL(THINGSPEAK_URL)
            val urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(response: String?) {
        listener.onDataFetched(response)
    }
}
