package com.example.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class SubActivity : AppCompatActivity() {
    // 初回起動時のバックグラウンド処理
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LifeCycle", "Sub onCreate() called.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
    }

    // アクティビティ(=画面)の起動
    public override fun onStart() {
        Log.i("LifeCycle", "Sub onStart() called.")
        super.onStart()
    }

    // アクティビティの再開
    public override fun onRestart() {
        Log.i("LifeCycle", "Sub onRestart() called.")
        super.onRestart()
    }

    // ユーザによる操作の入力受付が可能
    public override fun onResume() {
        Log.i("LifeCycle", "Sub onResume() called.")
        super.onResume()
    }

    // アクティビティの一時停止
    public override fun onPause() {
        Log.i("LifeCycle", "Sub onPause() called.")
        super.onPause()
    }

    // アクティビティの停止
    public override fun onStop() {
        Log.i("LifeCycle", "Sub onStop() called.")
        super.onStop()
    }

    // アクティビティの終了
    public override fun onDestroy() {
        Log.i("LifeCycle", "Sub onDestroy() called.")
        super.onDestroy()
    }

    // 「戻る」ボタンの"タップ"イベント検知時の処理
    fun onButtonClick(view: View) {
        // アクティビティの終了
        finish()
    }
}