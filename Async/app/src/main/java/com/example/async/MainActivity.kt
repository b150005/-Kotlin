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
    // -> 変更の可能性がない部分は定数として定義
    companion object {
        // ログに記載するタグ文字列
        private const val DEBUG_TAG = "Async"
        // 天候情報のURL共通部分
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

        // 表示するリストデータのキーを格納した配列
        val from = arrayOf("name")

        // リストデータを表示するTextViewのIDを格納したInt型配列
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
        // "name": ListViewに表示する都市名
        // "q": URLに挿入する都市名
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

    // "タップ"イベントを検知するリスナクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        // "タップ"イベント検知時の処理(イベントハンドラ)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            // "タップ"されたItem
            val item = _list.get(position)
            // "タップ"されたItemの都市名
            val q = item.get("q")

            // qがnullでない場合の処理
            q?.let {
                // 取得する天候情報のURL
                val urlFull = "$WEATHERINFO_URL&q=$q&appid=$APP_ID"

                // URLを基に天候情報を取得
                receiveWeatherInfo(urlFull)
            }
        }
    }

    // URL情報を基にした天候情報の取得処理
    // -> UIスレッドで動作することを明示的に記述(@UiThreadアノテーション)
    // => 異なるスレッドで動作する場合はコンパイル時にエラーが発生
    @UiThread
    private fun receiveWeatherInfo(urlFull: String) {

        // UIスレッドで動作する処理を管理するHandlerオブジェクトの生成
        // Handler: 特定のスレッドで動作させる処理(=Runnableオブジェクト)を
        //          Looperに管理させるクラス
        // mainLooper: アクティビティ(=UIスレッド)がもつLooperオブジェクト
        // Looper: 特定のスレッド内での処理を管理するクラス
        val handler = HandlerCompat.createAsync(mainLooper)

        // 非同期でWeb APIにアクセスするオブジェクト
        // -> ワーカースレッドで動作させるRunnableオブジェクト
        val backgroundReceiver = WeatherInfoBackgroundReceiver(handler, urlFull)

        // 1スレッド追加したスレッドプールをもつExecutorServiceプロパティ
        // Executors.newSingleThreadExecutor(): スレッドプールに新規スレッドを追加
        // Executors: ExecutorServiceのファクトリクラス
        // Executor: スレッドプールでの処理実行メソッドを抽象的に定義したインタフェース
        val executeService = Executors.newSingleThreadExecutor()

        // 非同期処理の開始
        // ExecutorService.submit(task:): スレッドプールにRunnableオブジェクトを送信
        // -> 待機状態である新規作成したスレッドが処理を実行
        executeService.submit(backgroundReceiver)
    }

    // 非同期でWeb APIにアクセスするクラス
    // -> 非同期処理を行うRunnableオブジェクトを定義するクラス
    // Runnable: 特定スレッド内で行う処理の自動実行メソッド(=run())を抽象的に定義したインタフェース
    // -> Runnableインタフェースを実装するクラスではrun()のオーバーライドが必須
    private inner class WeatherInfoBackgroundReceiver(handler: Handler, url: String): Runnable {

        // クラス内で扱うHandlerオブジェクト(=Handlerプロパティ)
        // -> クラス内での書き換えが発生しないよう、スレッドセーフ(読み込み専用のval)で定義
        private val _handler = handler

        // 選択した都市の天候情報のURLオブジェクト(=URLプロパティ)
        private val _url = url

        // Web APIへのアクセス処理
        // -> ワーカースレッドで動作することを明示的に記述(@WorkerThreadアノテーション)
        // => 異なるスレッドで動作する場合はコンパイル時にエラーが発生
        // -> run()メソッドはスレッドプールによって自動的に実行される
        @WorkerThread
        override fun run() {

            // Web APIを通じて取得するJSON文字列
            // -> エラーに備えて空文字で初期化
            var result = ""

            // URLオブジェクト
            // String型 → URL型 への変換
            val url = URL(_url)

            // HttpURLConnectionオブジェクト
            // URL型 (→ URLConnection) → HttpURLConnection型 への変換
            // HttpURLConnection: HTTP接続を行うクラス
            val con = url.openConnection() as? HttpURLConnection

            // HttpURLConnectionオブジェクトがnullでない場合の処理
            // -> let関数ブロック内では、nullチェック対象(=con)がitで置換
            con?.let {

                // 例外処理(エラーハンドリング)
                // 例外が発生する可能性があるブロック
                try {
                    // -- HTTP接続設定の定義開始 --

                    // 接続がタイムアウトするまでの時間[ミリ秒]
                    // -> タイムアウトした場合はSocketTimeoutExceptionが発生
                    it.connectTimeout = 1000

                    // データ取得がタイムアウトするまでの時間[ミリ秒]
                    // -> タイムアウトした場合はSocketTimeoutExceptionが発生
                    it.readTimeout = 1000

                    // HTTP接続メソッドの指定
                    it.requestMethod = "GET"

                    // -- HTTP接続設定の定義終了 --

                    // HTTP接続の実行
                    // ->場合によってSocketTimeoutExceptionまたはIOExceptionが発生
                    // -> 接続時、HttpURLConnectionオブジェクトのinputStreamプロパティに
                    //    InputStream型のレスポンスデータが自動的に格納
                    // InputStream: 読み込み専用の入力データを抽象的に定義するクラス
                    it.connect()

                    // HttpURLConnectionオブジェクトによるレスポンスデータの取得
                    val stream = it.inputStream

                    // InputStream型のレスポンスデータをString型に変換
                    result = is2String(stream)

                    // InputStreamオブジェクトの解放
                    stream.close()
                }
                // 例外が発生した場合のエラー処理
                catch (ex: SocketTimeoutException) {
                    // Logcatに出力
                    // Log.w(tag:msg:tr): WARNレベルのログメッセージをLogcatに出力
                    // tag: ログに記載するタグ
                    // msg: ログに記載するメッセージ
                    // ex: ログに記載する例外
                    Log.w(DEBUG_TAG, "Connection Timed Out.", ex)
                }

                // HttpURLConnectionオブジェクトの解放
                it.disconnect()
            }

            // 非同期処理の終了後にUIスレッドで行う処理(Runnableオブジェクト)
            val postExecutor = WeatherInfoPostExecutor(result)

            // UIスレッドでの続行処理をUIスレッドHandlerのLooperに送信
            _handler.post(postExecutor)
        }
    }

    // 非同期処理の終了後にUIスレッドで動作する処理(→JSON解析)を定義するクラス
    // -> コンストラクタにWeb APIで取得したJSONデータをセット
    private inner class WeatherInfoPostExecutor(result: String) : Runnable {

        // Web APIから取得したJSON文字列
        private val _result = result

        // JSON解析処理
        // -> UIスレッドで動作することを明示的に記述(@UiThreadアノテーション)
        // => 異なるスレッドで動作する場合はコンパイル時にエラーが発生
        // -> run()メソッドはHandlerによって自動的に実行される
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

    // InputStream型 → String型 への変換処理
    private fun is2String(stream: InputStream?): String {

        // StringBuilderオブジェクト
        // StringBuilder: 文字列を格納する(同一ポインタ内での)可変配列を定義するクラス
        val sb = StringBuilder()

        //　Byteデータ → 文字データ への変換
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))

        // 1行分の文字データ
        // -> 1行目の読み込み
        var line = reader.readLine()

        // 最終行までループさせる処理
        while (line != null) {

            // 読み込んだ1行分の文字データをStringBuilderオブジェクトに格納
            sb.append(line)

            // 次の行を読み込む
            line = reader.readLine()
        }

        // BufferedReaderオブジェクトの解放
        reader.close()

        // 読み込んだ文字データ(StringBuilder型)をString型に変換
        return sb.toString()
    }
}