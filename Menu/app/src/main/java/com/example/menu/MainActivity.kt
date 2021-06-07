package com.example.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var _menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
    private val _from = arrayOf("name", "price")
    private val _to = intArrayOf(R.id.tvMenuNameRow, R.id.tvMenuPriceRow)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _menuList = createSetMealList()
        val lvMenu = findViewById<ListView>(R.id.lvMenu)

        // Adapterオブジェクトの生成
        // SimpleAdapter(context:data:resource:from:to:): Adapterの定義・生成
        // context: Adapterを生成するアクティビティオブジェクト(=コンテキスト)
        // data: Adapterに紐づけるリストデータ
        // resource: リスト形式ビュー(ListView)のItemのレイアウトを表現するR値
        // from: リストデータ(MutableMap)のキー
        // to: リストデータ(MutableMap)の値を埋め込むTextViewのR値(ID)
        val adapter = SimpleAdapter(
            this@MainActivity,
            _menuList,
            R.layout.row,
            _from,
            _to
        )

        lvMenu.adapter = adapter
        lvMenu.onItemClickListener = ListItemClickListener()

        // 生成したコンテキストメニューを指定したビュー(ListView)に登録
        registerForContextMenu(lvMenu)
    }

    // 定義したオプションメニューの表示
    // onCreateOptionsMenu(menu:): アクティビティ開始時に呼び出されるメソッド
    // menu: Itemを保持するオプションメニュー
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // レイアウトファイル(XML)をJavaオブジェクトとして実体化(=インフレート)
        // MenuInflater.inflate(menuRes:menu:): XMLで定義したメニューのインフレート
        // menuRes: メニューのレイアウトファイル名(R値)
        // menu: インフレートを行うメニュー
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)

        // onCreateOptionsMenu(menu:)をオーバーライドした場合、常にtrueを返却
        return true
    }

    // コンテキストメニューの表示時に呼び出される処理
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, view, menuInfo)

        menuInflater.inflate(R.menu.menu_context_menu_list, menu)

        menu?.setHeaderTitle(R.string.menu_list_context_header)
    }

    // オプションメニューItemの"タップ"イベント検知時の処理(イベントハンドラ)
    // onOptionsItemSelected(item:): オプションメニューのItemがタップされた場合に呼び出されるメソッド
    // -> オプションメニューのItem以外が選択された場合、super.onOptionsItemSelected(item)を返却
    // item: タップされたItem
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 最終的に返却するBool値(初期値: true)
        var returnVal = true

        // タップされたItemに応じた分岐処理
        // MenuItem.itemId: ItemのID(R値)
        when(item.itemId) {
            R.id.menuListOptionSetMeal ->
                // Listの再定義
                _menuList = createSetMealList()

            R.id.menuListOptionCurry ->
                // Listの再定義
                _menuList = createCurryList()

            else ->
                // オプションメニューのItem以外が選択された場合は、
                // 親クラス(super)のonOptionsItemSelected(item:)メソッドの
                // 返り値(デフォルトではfalse)を返却
                returnVal = super.onOptionsItemSelected(item)
        }

        // ListViewの定義
        val lvMenu = findViewById<ListView>(R.id.lvMenu)

        // Adapterオブジェクトの再定義
        val adapter = SimpleAdapter(
            this@MainActivity,
            _menuList,
            R.layout.row,
            _from,
            _to
        )

        // ListViewにAdapterオブジェクトを紐づける
        lvMenu.adapter = adapter

        // 最終的にBool値を返却
        // -> アイテムが選択され、正常に処理を行った場合はtrueを返却
        return returnVal
    }

    // コンテキストメニューItemの"タップ"イベント検知時の処理(イベントハンドラ)
    // onContextItemSelected(item:): オプションメニューのItemがタップされた場合に呼び出されるメソッド
    // -> オプションメニューのItem以外が選択された場合、super.onContextItemSelected(item)を返却
    // item: タップされたItem
    override fun onContextItemSelected(item: MenuItem): Boolean {
        // 最終的に返却するBool値(初期値: true)
        var returnVal = true

        // AdapterContextMenuInfoオブジェクトの定義
        // -> コンテキストメニューを呼び出したItemの情報を格納
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        // コンテキストメニューを呼び出したItemのIndex番号
        val listPosition = info.position

        // コンテキストメニューを呼び出したItemのキーと値を格納するMutableList
        val menu = _menuList[listPosition]

        // タップされたItemに応じた分岐処理
        // MenuItem.itemId: ItemのID(R値)
        when(item.itemId) {
            R.id.menuListContextDesc -> {
                // トースト表示する文字列
                val desc = menu["desc"] as String

                // 表示するトーストの定義
                val toast = Toast.makeText(this@MainActivity, desc, Toast.LENGTH_LONG)

                // トーストの表示
                toast.show()
            }

            R.id.menuListContextOrder ->
                order(menu)

            else ->
                // オプションメニューのItem以外が選択された場合は、
                // 親クラス(super)のonContextItemSelected(item:)メソッドの
                // 返り値(デフォルトではfalse)を返却
                returnVal = super.onContextItemSelected(item)
        }

        return returnVal
    }

    // オプションメニューのItem1のリスト定義・作成
    private fun createSetMealList(): MutableList<MutableMap<String, Any>> {
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()

        var menu = mutableMapOf<String, Any>(
            "name" to "deep fried",
            "price" to 800,
            "desc" to "deep fried chicken with salad, rice, and soup"
        )
        menuList.add(menu)

        menu = mutableMapOf<String, Any>(
            "name" to "hamburg",
            "price" to 850,
            "desc" to "hamburg with salad, rice, and soup"
        )
        menuList.add(menu)

        menu = mutableMapOf<String, Any>(
            "name" to "item 1",
            "price" to 750,
            "desc" to "This description is about item 1."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 2",
            "price" to 900,
            "desc" to "This description is about item 2."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 3",
            "price" to 1200,
            "desc" to "This description is about item 3."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 4",
            "price" to 870,
            "desc" to "This description is about item 4."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 5",
            "price" to 980,
            "desc" to "This description is about item 5."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 6",
            "price" to 490,
            "desc" to "This description is about item 6."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 7",
            "price" to 730,
            "desc" to "This description is about item 7."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 8",
            "price" to 930,
            "desc" to "This description is about item 8."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "item 9",
            "price" to 1020,
            "desc" to "This description is about item 9."
        )
        menuList.add(menu)

        return menuList
    }

    // オプションメニューのItem2のリスト定義・作成
    private fun createCurryList(): MutableList<MutableMap<String, Any>> {
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()

        var menu = mutableMapOf<String, Any>(
            "name" to "beef curry",
            "price" to 520,
            "desc" to "curry with 100 percent domestically-produced beef"
        )
        menuList.add(menu)

        menu = mutableMapOf<String, Any>(
            "name" to "pork curry",
            "price" to 420,
            "desc" to "curry with 100 percent domestically-produced pork"
        )
        menuList.add(menu)

        menu = mutableMapOf<String, Any>(
            "name" to "curry 1",
            "price" to 570,
            "desc" to "this description is about curry 1."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "curry 2",
            "price" to 650,
            "desc" to "this description is about curry 2."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "curry 3",
            "price" to 490,
            "desc" to "this description is about curry 3."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "curry 4",
            "price" to 610,
            "desc" to "this description is about curry 4."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "curry 5",
            "price" to 790,
            "desc" to "this description is about curry 5."
        )
        menuList.add(menu)
        menu = mutableMapOf<String, Any>(
            "name" to "curry 6",
            "price" to 910,
            "desc" to "this description is about curry 6."
        )
        menuList.add(menu)

        return menuList
    }

    // 指定したItemを遷移先アクティビティに渡しながら画面遷移を行う処理
    private fun order(menu: MutableMap<String, Any>) {
        // 指定したキーの値
        val menuName = menu["name"] as String
        val menuPrice = menu["price"] as Int

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
        intent2MenuThanks.putExtra("menuPrice", "$${menuPrice}")

        // 遷移先アクティビティの起動
        // Context.startActivity(intent:): Intentオブジェクトによる画面遷移の実行
        // intent: Intentオブジェクト
        startActivity(intent2MenuThanks)
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
            val item = parent?.getItemAtPosition(position) as MutableMap<String, Any>

            order(item)
        }
    }
}