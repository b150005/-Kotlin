package com.example.async

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.os.HandlerCompat
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    // private定数
    companion object {
        // ログに記載するタグ文字列
        private const val DEBUG_TAG = "Async"
        // 天候情報のURL
        private const val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        // APIキー
        private const val APP_ID = "2243f50c3b5e4bd5689ce9642a3c436b"
    }

    // リストデータを格納するリスト
    private var _list: MutableList<MutableMap<String, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // リストデータの生成
        _list = createList()

        // 表示するListView
        val lvCityList = findViewById<ListView>(R.id.lvCityList)

        // 表示するリストデータ
        val from = arrayOf("name")

        // リストデータを表示するTextView
        val to = intArrayOf(android.R.id.text1)

        // リストデータとListViewを紐づけるAdapterオブジェクト
        val adapter = SimpleAdapter(
            this@MainActivity,
            _list,
            android.R.layout.simple_list_item_1,
            from,
            to
        )

        // ListViewにAdapterオブジェクトをセット
        lvCityList.adapter = adapter

        // ListViewのItemをリスナとしてセット
        lvCityList.onItemClickListener = ListItemClickListener()
    }

    // リストデータの生成処理
    private fun createList(): MutableList<MutableMap<String, String>> {
        // リストデータを格納するリスト
        var list: MutableList<MutableMap<String, String>> = mutableListOf()

        // リストデータ
        var city = mutableMapOf("name" to "Osaka", "q" to "Osaka")
        list.add(city)
        city = mutableMapOf("name" to "Kobe", "q" to "Kobe")
        list.add(city)
        city = mutableMapOf("name" to "Tokyo", "q" to "Tokyo")
        list.add(city)
        city = mutableMapOf("name" to "Hiroshima", "q" to "Hiroshima")
        list.add(city)
        city = mutableMapOf("name" to "Fukuoka", "q" to "Fukuoka")
        list.add(city)
        city = mutableMapOf("name" to "Nagoya", "q" to "Nagoya")
        list.add(city)
        city = mutableMapOf("name" to "Sapporo", "q" to "Sapporo")
        list.add(city)

        // 定義したリストデータの返却
        return list
    }

    // URL情報を基にした天候情報の取得処理
    @UiThread
    private fun receiveWeatherInfo(urlFull: String) {

        // Handlerオブジェクトの生成
        // Handler: スレッド間の通信を行うクラス
        // mainLooper: アクティビティがもつLooperオブジェクト
        // Looper:
        val handler = HandlerCompat.createAsync(mainLooper)

        // 非同期でWeb APIにアクセスするオブジェクト
        val backgroundReceiver = WeatherInfoBackgroundReceiver(handler, urlFull)

        // 別スレッドで動作するインスタンス
        // Executors.newSingleThreadExecutor():
        // -> 別スレッドで動作するインスタンス(ExecutorServiceインスタンス)の生成
        // Executors:
        // Executor:
        val executeService = Executors.newSingleThreadExecutor()

        // 別スレッドでの処理(=非同期処理)の実行
        executeService.submit(backgroundReceiver)
    }

    // 非同期でWeb APIにアクセスするクラス
    // -> Runnableインスタンスを実装するクラス
    // Runnable: ~~するインタフェース
    private inner class WeatherInfoBackgroundReceiver(handler: Handler, url: String): Runnable {

        // Handlerオブジェクト
        // -> スレッドセーフで定義
        private val _handler = handler

        // 天候情報のURL
        private val _url = url

        // Web APIへのアクセス処理
        @WorkerThread
        override fun run() {

            // Web API空取得したJSON文字列
            // -> 空文字で初期化
            var result = ""

            // URLオブジェクト
            val url = URL(_url)

            // HttpURLConnectionオブジェクト
            // HttpURLConnection: HTTP接続を行うクラス
            val con = url.openConnection() as? HttpURLConnection

            // HttpURLConnectionオブジェクトがnullでない場合の処理
            con?.let {
                //
                try {
                    // 接続がタイムアウトするまでの時間[ミリ秒]
                    // -> タイムアウトした場合はSocketTimeoutExceptionが発生
                    it.connectTimeout = 1000

                    // データ取得がタイムアウトするまでの時間[ミリ秒]
                    // -> タイムアウトした場合はSocketTimeoutExceptionが発生
                    it.readTimeout = 1000

                    // HTTP接続メソッドの指定
                    it.requestMethod = "GET"

                    // HTTP接続の実行
                    // -> 接続時、HttpURLConnectionオブジェクトの
                    //    inputStreamプロパティにレスポンスデータが自動的に格納
                    it.connect()

                    // HttpURLConnectionオブジェクトによるレスポンスデータの取得
                    val stream = it.inputStream

                    // レスポンスデータをString型に変換
                    result = is2String(stream)

                    // InputStreamオブジェクトの解放
                    stream.close()
                }
                // エラーハンドリング
                catch (ex: SocketTimeoutException) {
                    // EventLogに出力
                    Log.w(DEBUG_TAG, "Connection Timed Out.", ex)
                }

                // HttpURLConnectionオブジェクトの解放
                it.disconnect()
            }

            // 非同期処理の終了時にUIスレッドで処理を行うクラスのオブジェクト
            val postExecutor = WeatherInfoPostExecutor(result)

            // 非同期処理の終了時にHandlerオブジェクトをUIスレッドのクラスオブジェクトに送信
            _handler.post(postExecutor)
        }
    }

    // レスポンスデータをString型に変換する処理
    private fun is2String(stream: InputStream?): String {
        //
        val sb = StringBuilder()

        //
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))

        //
        var line = reader.readLine()

        //
        while (line != null) {
            //
            sb.append(line)

            //
            line = reader.readLine()
        }

        //
        reader.close()

        //
        return sb.toString()
    }

    // 非同期処理の終了後にUIスレッドで動作する処理を定義するクラス
    // -> コンストラクタにWeb APIで取得したJSONデータをセット
    private inner class WeatherInfoPostExecutor(result: String) : Runnable {

        // Web APIから取得したJSON文字列
        private val _result = result

        // UIスレッド(メインスレッド)で行う処理
        @UiThread
        override fun run() {
            // ルートJSONオブジェクト
            // JSONObject:　JSONデータをもつオブジェクト
            val rootJSON = JSONObject(_result)

            // 都市名
            val cityName = rootJSON.getString("name")

            // 経緯度が格納されたJSONオブジェクト
            val coordJSON = rootJSON.getJSONObject("coord")

            // 緯度
            val latitude = coordJSON.getString("lat")

            // 経度
            val longitude = coordJSON.getString("lon")

            // 天候情報が格納されたJSON配列オブジェクト
            val weatherJSONArray = rootJSON.getJSONArray("weather")

            // 現在の天候情報が格納されたJSONオブジェクト
            val weatherJSON = weatherJSONArray.getJSONObject(0)

            // 現在の天候情報
            val weather = weatherJSON.getString("description")

            // 取得した都市名を埋め込んだ文字列
            val telop = "Weather for ${cityName}"

            // 取得した天候情報を埋め込んだ文字列
            val desc = "The weather now is ${weather}. \nIt is located approximately at lat. ${latitude} and at long. ${longitude}."

            // 天候情報を表示するTextView
            val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
            val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)

            // 天候情報の表示
            tvWeatherTelop.text = telop
            tvWeatherDesc.text = desc
        }
    }

    // "タップ"イベントを検知するリスナクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        // "タップ"イベント検知時の処理(イベントハンドラ)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            // "タップ"されたItem
            val item = _list.get(position)
            // "タップ"されたItemの都市名
            val q = item.get("q")

            q?.let {
                // 取得する天候情報のURL
                val urlFull = "$WEATHERINFO_URL&q=$q&appid=$APP_ID"

                // URLを基に天候情報を取得
                receiveWeatherInfo(urlFull)
            }
        }
    }
}