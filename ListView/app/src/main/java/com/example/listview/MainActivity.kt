package com.example.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // onItemClickListenerプロパティにリスナ(クラスのインスタンス)を代入
        // -> セッタ(=setOnItemClickListener()メソッド)がonItemClickListenerプロパティのセッタ
        //    である場合、プロパティに直接リスナを代入することで自動的にセッタが呼ばれる
        lvMenu.onItemClickListener = ListItemClickListener()
    }

    // ListViewのItemをタップした場合のリスナクラス(リスナ)
    // AdapterView: ListView, Spinnerの親クラス
    // OnItemClickListener: "タップ"イベントを検知するAdapterViewのメンバインタフェース
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {

        // タップ時の処理(イベントハンドラ)
        // view: (タップされた)ビュー(=ListViewのItem(=TextView))
        // position: (タップされた)ItemのIndex
        // id: (SimpleCursorAdapterを使用する場合)DBの主キー
        //     (それ以外の場合)positionと同じ値
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            // ListViewのItem(TextView)の値をIndex番号から取得, String型に変換
            // parent: AdapterView
            // getItemAtPosition(): 指定したIndexのtext値を取得(返り値はAny型)
            val item = parent?.getItemAtPosition(position) as String
            // トーストで表示する文字列
            val show = "you chose: " + item

            // Toast.makeText(context:text:duration:): Toastオブジェクトのインスタンス化
            // context: トーストを表示させるアクティビティオブジェクト(=Activityインスタンス)
            // text: トーストで表示する文字列
            // duration: トーストを表示する時間
            val toast = Toast.makeText(this@MainActivity, show, Toast.LENGTH_LONG)

            // トーストの表示
            toast.show()
        }
    }
}