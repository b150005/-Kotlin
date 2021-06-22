package com.example.coordinatorlayoutcollapsed

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // ロゴの表示
        toolbar.setLogo(R.mipmap.ic_launcher)

        // ツールバーをアクションバーとして指定
        setSupportActionBar(toolbar)

        // CollapsingToolbarLayout
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)

        // タイトルの文字列
        toolbarLayout.title = getString(R.string.toolbar_title)

        // 通常サイズ時のタイトルの文字色
        toolbarLayout.setExpandedTitleColor(Color.WHITE)

        // 縮小サイズ時のタイトルの文字色
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY)
    }
}