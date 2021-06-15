package com.example.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Intentオブジェクトからデータを取得
        val fromNotification = intent.getBooleanExtra("fromNotification", false)

        // Intentオブジェクトのデータが存在する場合の処理
        // <- 通知ドロワーからアクティビティが起動された場合
        if (fromNotification) {
            // 各種ボタン
            val btPlay = findViewById<Button>(R.id.btPlay)
            val btStop = findViewById<Button>(R.id.btStop)

            // 各種ボタンの設定
            btPlay.isEnabled = false
            btStop.isEnabled = true
        }
    }

    // 再生ボタンがタップされた場合の処理(イベントハンドラ)
    fun onPlayButtonClick(view: View) {
        // Intentオブジェクト
        val intent = Intent(this@MainActivity, SoundManageService::class.java)

        // サービスの起動
        startService(intent)

        // 各種ボタン
        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)

        // 各種ボタンの設定
        btPlay.isEnabled = false
        btStop.isEnabled = true
    }

    // 停止ボタンがタップされた場合の処理(イベントハンドラ)
    fun onStopButtonClick(view: View) {
        // Intentオブジェクト
        val intent = Intent(this@MainActivity, SoundManageService::class.java)

        // アクティビティによるサービスの終了
        stopService(intent)

        // 各種ボタン
        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)

        // 各種ボタンの設定
        btPlay.isEnabled = true
        btStop.isEnabled = false
    }
}