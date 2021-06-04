package com.example.intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MenuThanksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_thanks)

        // Intentオブジェクト(=Activity.intent)が保持するデータの取得
        // intent: Activityを起動したIntentオブジェクトを指すプロパティ
        // getStringExtra(name:): String型データの取得
        // name: 格納されたデータの名称
        val menuName = intent.getStringExtra("menuName")
        val menuPrice = intent.getStringExtra("menuPrice")

        // ビューの定義
        val tvMenuName = findViewById<TextView>(R.id.tvMenuName)
        val tvMenuPrice = findViewById<TextView>(R.id.tvMenuPrice)

        // ビューの値(=TextView.text)の変更
        tvMenuName.text = menuName
        tvMenuPrice.text = menuPrice
    }

    // "activity_menu_thanks.xml"で"android:onClick"属性を追加したボタン(=リスナ)の
    // "タップ"イベントを検知した場合の処理(イベントハンドラ)
    fun onBackButtonClick(view: View) {
        // アクティビティの終了
        finish()
    }
}