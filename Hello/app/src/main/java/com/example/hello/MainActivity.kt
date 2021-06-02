package com.example.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // リスナとするオブジェクトの定義
        val btClick = findViewById<Button>(R.id.btClick)
        // リスナクラスのインスタンス化(実体化)
        val listener = HelloListener()
        // オブジェクトをリスナとして設定
        btClick.setOnClickListener(listener)

        // リスナとするオブジェクトの定義
        val btClear = findViewById<Button>(R.id.btClear)
        // オブジェクトをリスナとして設定
        btClear.setOnClickListener(listener)
    }

    // ボタンをタップした場合のリスナクラス(リスナ)
    // View.OnClickListenerインタフェース: "タップ"イベントを検知するリスナインタフェース
    private inner class HelloListener: View.OnClickListener {
        // タップ時の処理(イベントハンドラ)
        // view: (タップされた)ビュー
        override fun onClick(view: View) {
            // EditText(@+id/etName)に入力された値
            val input = findViewById<EditText>(R.id.etName)
            // TextView(@+id/tvOutput)に出力する値
            val output = findViewById<TextView>(R.id.tvOutput)

            // タップされたボタンに応じた分岐処理
            when(view.id) {

                // "greet"ボタンの場合
                R.id.btClick -> {
                    // EditTextのtextプロパティを「String型」に変換・代入
                    // <- findViewById<EditText>()メソッドの返却型は「Editable型」
                    val inputStr = input.text.toString()

                    // TextView(ラベル)のtextプロパティの上書き
                    output.text = "Hello, $inputStr."
                }

                // "clear"ボタンの場合
                R.id.btClear -> {
                    // EditTextのtextプロパティを空にする
                    // setText(): String型の引数をtextプロパティに代入
                    input.setText("")
                    // TextView(ラベル)のtextプロパティを空にする
                    output.text = ""
                }
            }
        }
    }
}