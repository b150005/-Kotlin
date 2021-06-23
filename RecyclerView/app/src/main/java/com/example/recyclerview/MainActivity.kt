package com.example.recyclerview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        // RecyclerView
        val lvMenu = findViewById<RecyclerView>(R.id.lvMenu)

        // レイアウト配置とリストアイテムを管理するLinearLayoutManager(レイアウトマネージャ)
        val layout = LinearLayoutManager(this@MainActivity)

        // RecyclerViewにLinearLayoutManagerをセット
        lvMenu.layoutManager = layout

        // リストデータ
        val menuList = createSetMealList()

        // リストデータを紐づけるRecyclerListAdapter
        val adapter = RecyclerListAdapter(menuList)

        // RecyclerViewにRecyclerListAdapterをセット
        lvMenu.adapter = adapter

        // 区切り線を表示するDividerItemDecorationオブジェクト
        val decorator = DividerItemDecoration(this@MainActivity, layout.orientation)

        // リサイクラービューへの区切り線の追加
        lvMenu.addItemDecoration(decorator)
    }

    // 独自定義するアダプタクラス
    // <- RecyclerView.Adapterクラスの実装クラス
    // <- クラスコンストラクタは一度のみ呼ばれ、クラス内でしか利用しない(=機密性を持たせる)ため、
    //    "private val"を付与
    private inner class RecyclerListAdapter(
        private val _listData: MutableList<MutableMap<String, Any>>
    ): RecyclerView.Adapter<RecyclerListViewHolder>() {

        // RecyclerViewによって最初に呼び出される処理
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {

            // LayoutInflaterオブジェクト
            val inflater = LayoutInflater.from(this@MainActivity)

            // レイアウトファイルのインフレート(=アイテムビュー)
            val view = inflater.inflate(R.layout.row, parent, false)

            // インフレートしたアイテムビューをリスナとしてセット
            view.setOnClickListener(ItemClickListener())

            // ビューホルダの生成
            val holder = RecyclerListViewHolder(view)

            // ビューホルダを返却
            return holder
        }

        // RecyclerViewがアダプタからビューホルダを受け取った際にRecyclerViewから呼び出される処理
        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {

            // リストデータのアイテム
            val item = _listData[position]

            // 指定したキーと一致するリストデータ
            val menuName = item["name"] as String
            val menuPrice = item["price"] as Int

            // Int型 → String型 への変換
            val menuPriceStr = menuPrice.toString()

            // ビューホルダが保持するビューにデータを反映
            holder._tvMenuNameRow.text = menuName
            holder._tvMenuPriceRow.text = menuPriceStr
        }

        // リストデータの項目数を取得
        override fun getItemCount(): Int {
            // リストデータの項目数を返却
            // <- リストデータはクラスコンストラクタの引数から取得
            return _listData.size
        }
    }

    // アイテム内ビュー(=TextView)を保持するViewHolder(ビューホルダ)
    // <- RecyclerView.ViewHolderクラスの実装クラス
    private inner class RecyclerListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Item内に表示されるItem名
        var _tvMenuNameRow: TextView

        // Item内に表示される金額
        var _tvMenuPriceRow: TextView

        // 初期化時に呼び出される処理(=イニシャライザ)
        init {
            // Item内のTextView
            // -> アダプタクラスのonBindViewHolder()メソッドによってリストデータが反映される
            _tvMenuNameRow = itemView.findViewById(R.id.tvMenuNameRow)
            _tvMenuPriceRow = itemView.findViewById(R.id.tvMenuPriceRow)
        }
    }

    // リサイクラービューのアイテムの"タップ"イベントを検知するリスナクラス
    // <- View.OnClickListenerインタフェースの実装クラス
    private inner class ItemClickListener: View.OnClickListener {

        // "タップ"イベント検知時に呼び出される処理(イベントハンドラ)
        override fun onClick(view: View?) {
            // タップされたアイテムの「アイテム名」を表すTextView
            val tvMenuName = view?.findViewById<TextView>(R.id.tvMenuNameRow)

            // TextViewの文字列
            val menuName = tvMenuName?.text.toString()

            // トースト表示する文字列
            val msg = getString(R.string.msg_header) + menuName

            // 表示するトースト
            val toast = Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT)

            // トーストの表示
            toast.show()
        }
    }

    // RecyclerViewのItemのリスト定義・作成
    private fun createSetMealList(): MutableList<MutableMap<String, Any>> {

        // MutableList
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()

        // MutableMapの定義
        var menu = mutableMapOf<String, Any>(
            "name" to "deep fried",
            "price" to 800,
            "desc" to "deep fried chicken with salad, rice, and soup"
        )
        // MutableListへMutableMapを格納
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

        // 定義したMutableListを返却
        return menuList
    }
}