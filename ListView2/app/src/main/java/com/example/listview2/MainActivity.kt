package com.example.listview2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adapterがリストデータを紐づけるオブジェクト(=ListView)の定義
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // MutableList型リストデータの定義
        var menuList = mutableListOf("deep-fried", "hamburg", "ginger fried", "steak",
            "stir-fried", "pork cutlet", "mapo tofu", "twice cooked pork", "minced meat cutlet",
            "chicken cutlet", "grilled fish", "chop suey", "fried rice")

        // 定義したリストデータをもつAdapterオブジェクトを生成
        // ArrayAdapter(context:resource:objects:): Array, MutableList型データを利用するAdapterクラス
        // context: アクティビティオブジェクト
        // resource: ListViewの各Itemのレイアウトを表現するR値
        // objects: リストデータ
        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, menuList)

        // ListViewにAdapterオブジェクトをセット(adapterプロパティにAdapterオブジェクトを代入)
        lvMenu.adapter = adapter

        // ListViewオブジェクトをリスナとして設定
        lvMenu.onItemClickListener = ListItemClickListener()
    }

    // ListViewのItemの"タップ"イベントを検知するリスナクラス(リスナ)
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {

        // "タップ"イベント検知時の処理(イベントハンドラ)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // ダイアログ生成クラスのインスタンス化(実体化)
            val dialogFragment = OrderConfirmDialogFragment()

            // 生成したダイアログの表示
            // DialogFragment.show(@NonNull manager: FragmentManager, @Nullable tag: String?): Unit
            // manager: FragmentManagerオブジェクト(=supportFragmentManagerプロパティ)
            // tag: ダイアログを識別するための文字列(任意)
            dialogFragment.show(supportFragmentManager, "OrderConfirmDialogFragment")
        }
    }
}