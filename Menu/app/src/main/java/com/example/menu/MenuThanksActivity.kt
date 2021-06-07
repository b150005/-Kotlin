package com.example.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        // アクションバー内に「戻る」ボタン(←)を表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // オプションメニューItemの"タップ"イベント検知時の処理
    // onOptionsItemSelected(item:): オプションメニューのItemがタップされた場合に呼び出されるメソッド
    // -> オプションメニューのItem以外が選択された場合、super.onOptionsItemSelected(item)を返却
    // item: タップされたItem
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 最終的に返却するBool値(初期値: true)
        var returnVal: Boolean = true

        // タップされたItemに応じた分岐処理
        // 選択したItemが「戻る」ボタン(←; android.R.id.home)の場合
        if (item.itemId == android.R.id.home) {
            // アクティビティの終了
            finish()
        }
        // それ以外の場合
        else {
            // オプションメニューのItem以外が選択された場合は、
            // 親クラス(super)のonOptionsItemSelected(item:)メソッドの
            // 返り値(デフォルトではfalse)を返却
            returnVal = super.onOptionsItemSelected(item)
        }

        // 最終的にBool値を返却
        // -> アイテムが選択され、正常に処理を行った場合はtrueを返却
        return returnVal
    }

    // "activity_menu_thanks.xml"で"android:onClick"属性を追加したボタン(=リスナ)の
    // "タップ"イベントを検知した場合の処理(イベントハンドラ)
    fun onBackButtonClick(view: View) {
        // アクティビティの終了
        finish()
    }
}