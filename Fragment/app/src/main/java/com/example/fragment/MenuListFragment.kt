package com.example.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.SimpleAdapter

class MenuListFragment : Fragment() {

    // 10-inchの画面かどうか判定するフラグ
    private var _isLayoutXLarge = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // フラグメントのレイアウト(XML)のインフレート(フラグメントの生成)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // インフレートしたビュー
        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)

        // インフレートしたビュー(view)内のListView
        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)

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

        val from = arrayOf("name", "price")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        // Adapterオブジェクトの生成
        // context: Fragmentがもつactivityプロパティを指定
        // <- FragmentクラスはContextクラスを継承していないため、
        //    "this@...Activity"が使用不可
        val adapter = SimpleAdapter(
            activity,
            menuList,
            android.R.layout.simple_list_item_2,
            from,
            to
        )
        lvMenu.adapter = adapter

        lvMenu.onItemClickListener = ListItemClickListener()

        return view
    }

    // アクティビティ生成時に呼び出される処理
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // アクティビティ内のFrameLayout(View)を取得
        val menuThanksFrame = activity?.findViewById<View>(R.id.menuThanksFrame)

        // 指定したView(FrameLayout)が同一アクティビティ内に存在しない場合
        // -> 10-inchでない場合
        if (menuThanksFrame == null) {
            _isLayoutXLarge = false
        }
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

            // Bundleオブジェクトの生成
            val bundle = Bundle()

            // Bundleオブジェクトへのデータ格納
            bundle.putString("menuName", menuName)
            bundle.putString("menuPrice", menuPrice)

            // 10-inchの画面である場合
            if (_isLayoutXLarge) {

                // フラグメントトランザクションの編集開始
                // FragmentTransaction: フラグメントトランザクションの制御を行うクラス
                // FragmentManager?.beginTransaction(): FragmentTransactionオブジェクトの取得
                val transaction = fragmentManager?.beginTransaction()

                // 追加するフラグメントオブジェクトの生成
                val menuThanksFragment = MenuThanksFragment()

                // 追加するフラグメントのargumentsプロパティにBundleオブジェクトを代入
                // -> 追加フラグメントではargumentsプロパティを用いてデータを取得
                menuThanksFragment.arguments = bundle

                // フラグメントの置換
                // FragmentTransaction?.replace(containerViewId:fragment:): フラグメントの置換
                // containerViewId: 置換先レイアウト部品のID(R値)
                // fragment: 置換するフラグメントオブジェクト
                transaction?.replace(R.id.menuThanksFrame, menuThanksFragment)

                // 編集したフラグメントトランザクションを反映(コミット)
                // FragmentTransaction?.commit(): フラグメントトランザクションの編集を反映
                transaction?.commit()
            }
            // 10-inchの画面でない場合
            else {
                // 画面遷移を実現するIntentオブジェクトの生成
                // Intent(packageContext:cls:): Intentオブジェクトの生成
                // packageContext: 遷移元のアクティビティオブジェクト(=コンテキスト)
                // -> FragmentクラスはActivityクラスを継承していないため、 Fragment.activityプロパティを指定
                // cls: Javaクラス化した遷移先アクティビティ
                val intent2MenuThanks = Intent(activity, MenuThanksActivity::class.java)

                // Bundleオブジェクトへのデータの格納
                intent2MenuThanks.putExtras(bundle)

                // 遷移先アクティビティの起動
                // Context.startActivity(intent:): Intentオブジェクトによる画面遷移の実行
                // intent: Intentオブジェクト
                startActivity(intent2MenuThanks)
            }
        }
    }
}