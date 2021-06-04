package com.example.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    // 初回起動時のバックグラウンド処理
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LifeCycle", "Main onCreate() called.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // アクティビティ(=画面)の起動
    public override fun onStart() {
        Log.i("LifeCycle", "Main onStart() called.")
        super.onStart()
    }

    // アクティビティの再開
    public override fun onRestart() {
        Log.i("LifeCycle", "Main onRestart() called.")
        super.onRestart()
    }

    // ユーザによる操作の入力受付が可能
    public override fun onResume() {
        Log.i("LifeCycle", "Main onResume() called.")
        super.onResume()
    }

    // アクティビティの一時停止
    public override fun onPause() {
        Log.i("LifeCycle", "Main onPause() called.")
        super.onPause()
    }

    // アクティビティの停止
    public override fun onStop() {
        Log.i("LifeCycle", "Main onStop() called.")
        super.onStop()
    }

    // アクティビティの終了
    public override fun onDestroy() {
        Log.i("LifeCycle", "Main onDestroy() called.")
        super.onDestroy()
    }

    // "Show next view."ボタンの"タップ"イベント検知時の処理
    fun onButtonClick(view: View) {
        // Intentオブジェクトの定義・生成
        val intent = Intent(this@MainActivity, SubActivity::class.java)

        // 遷移先アクティビティの起動
        startActivity(intent)
    }
}