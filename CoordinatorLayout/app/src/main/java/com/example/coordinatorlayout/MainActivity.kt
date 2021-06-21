package com.example.coordinatorlayout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // ロゴの表示
        toolbar.setLogo(R.mipmap.ic_launcher)

        // タイトルの文字列
        toolbar.setTitle(R.string.toolbar_title)

        // タイトルの文字色
        toolbar.setTitleTextColor(Color.WHITE)

        // サブタイトルの文字列
        toolbar.setSubtitle(R.string.toolbar_subtitle)

        // サブタイトルの文字色
        toolbar.setSubtitleTextColor(Color.LTGRAY)

        // ツールバーをアクションバーとして指定
        setSupportActionBar(toolbar)
    }
}