package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ListViewの定義
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // リストデータをもつMutableListオブジェクトの生成
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()

        // リストデータ(MutableMap)の作成
        // リストデータ(MutableMap)の定義
        var menu = mutableMapOf("name" to "item 1", "price" to "$8")
        // MutableListへの追加
        menuList.add(menu)
        // 以降リストデータの定義・リストへの追加
        menu = mutableMapOf("name" to "item 2", "price" to "$7")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 3", "price" to "$2")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 4", "price" to "$6")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 5", "price" to "$12")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 6", "price" to "$20")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 7", "price" to "$9")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 8", "price" to "$8")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 9", "price" to "$3")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 10", "price" to "$4")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 11", "price" to "$25")
        menuList.add(menu)

        // リストデータ(MutableMap)のキーを格納する配列
        val from = arrayOf("name", "price")
        // リストデータ(MutableMap)を埋め込む、ListViewのItem内のTextViewを格納する配列
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        // Adapterオブジェクトの生成
        // SimpleAdapter(context:data:resource:from:to:): Adapterの定義・生成
        // context: Adapterを生成するアクティビティオブジェクト(=コンテキスト)
        // data: Adapterに紐づけるリストデータ
        // resource: リスト形式ビュー(ListView)のItemのレイアウトを表現するR値
        // from: リストデータ(MutableMap)のキー
        // to: リストデータ(MutableMap)の値を埋め込むTextViewのR値(ID)
        val adapter = SimpleAdapter(
            this@MainActivity,
            menuList,
            android.R.layout.simple_list_item_2,
            from,
            to
        )
        // ListViewへのAdapterの紐づけ
        lvMenu.adapter = adapter

        // ListView(の各Item)をリスナとしてセット
        lvMenu.onItemClickListener = ListItemClickListener()
    }

    // ListViewのItemの"タップ"イベントを検知するリスナクラス(リスナ)
    // AdapterView: Adapterに読み込まれるリストデータを表示するビュー(=ListViewのItem)
    // OnClickListener: "タップ"イベントを検知するメンバインタフェース
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {

        // "タップ"イベント検知時の処理(イベントハンドラ)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            // "タップ"されたItemの定義
            // parent: AdapterView
            // getItemAtPosition(): 指定したIndex(position)のtext値を取得(返り値はAny型)
            val item = parent?.getItemAtPosition(position) as MutableMap<String, String>

            // 指定したキーの値
            val menuName = item["name"]
            val menuPrice = item["price"]

            // 画面遷移を実現するIntentオブジェクトの生成
            // Intent(packageContext:cls:): Intentオブジェクトの生成
            // packageContext: 遷移元のアクティビティオブジェクト(=コンテキスト)
            // cls: Javaクラス化した遷移先アクティビティ
            val intent2MenuThanks = Intent(
                this@MainActivity,
                MenuThanksActivity::class.java
            )

            // Intentオブジェクトへのデータの格納
            // putExtra(name:value:): Intentオブジェクトにデータを格納
            // name: 格納するデータの名称
            // value: 格納するデータ
            intent2MenuThanks.putExtra("menuName", menuName)
            intent2MenuThanks.putExtra("menuPrice", menuPrice)

            // 遷移先アクティビティの起動
            // Context.startActivity(intent:): Intentオブジェクトによる画面遷移の実行
            // intent: Intentオブジェクト
            startActivity(intent2MenuThanks)
        }
    }
}