package com.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MenuThanksFragment : Fragment() {

    // 10-inchの画面かどうか判定するフラグ
    private var _isLayoutXLarger = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 10-inchの判定を行うためのフラグメント
        // fragmentManager: FragmentManagerオブジェクトを表すプロパティ
        // FragmentManager: アクティビティが保有するフラグメントを管理するクラス
        // FragmentManager.findFragmentById(Id:): 指定したID(R値)に一致するフラグメントの取得
        // id: レイアウトファイル(XML)で記述したフラグメントのID(R値)
        val menuListFragment = fragmentManager?.findFragmentById(R.id.fragmentMenuList)

        // 指定したフラグメントが同一アクティビティ内に存在しない場合
        // -> 10-inchでない場合
        if (menuListFragment == null) {
            _isLayoutXLarger = false
        }
    }

    // フラグメントのレイアウト(XML)のインフレート(フラグメントの生成)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // インフレートしたビュー
        val view = inflater.inflate(R.layout.fragment_menu_thanks, container, false)

        // データが格納されるBundleオブジェクト
        val extras: Bundle?

        // 10-inchの画面である場合
        if (_isLayoutXLarger) {
            // Bundleオブジェクトにデータを格納
            // arguments: データが格納されたBundleオブジェクト
            // <- 追加元でargumentsプロパティにデータを格納済み
            extras = arguments
        }
        // 10-inchの画面でない場合
        else {
            // 遷移元と遷移先を紐づけるIntentオブジェクト
            // activity: フラグメントを表示するアクティビティ(FragmentActivityクラスのプロパティ)
            // intent: Intentオブジェクト(Activityクラスのプロパティ)
            // -> FragmentクラスはActivityクラスを継承していないため、
            //    activityプロパティ(Nullable型)を中継してintentプロパティを利用
            val intent = activity?.intent

            // データが格納されたBundleオブジェクト
            // extras: Intentオブジェクトに格納されたキーと値を格納するBundleオブジェクト
            extras = intent?.extras
        }

        // キーを指定して値を取得
        val menuName = extras?.getString("menuName")
        val menuPrice = extras?.getString("menuPrice")

        // フラグメント内のビュー(TextView)
        val tvMenuName = view.findViewById<TextView>(R.id.tvMenuName)
        val tvMenuPrice = view.findViewById<TextView>(R.id.tvMenuPrice)

        // TextViewの表示文字列
        tvMenuName.text = menuName
        tvMenuPrice.text = menuPrice

        // フラグメント内のビュー(Button)
        val btBack = view.findViewById<Button>(R.id.btBack)

        // ボタンをリスナとして定義
        btBack.setOnClickListener(ButtonClickListener())

        // インフレート済みのビューを返却
        return view
    }

    // "タップ"イベントを検知するするリスナクラス(リスナ)
    private inner class ButtonClickListener: View.OnClickListener {
        // "タップ"イベント検知時の処理(イベントハンドラ)
        override fun onClick(view: View?) {
            // 10-inchの画面である場合
            if (_isLayoutXLarger) {

                // フラグメントトランザクションの編集開始
                // FragmentTransaction: フラグメントトランザクションの制御を行うクラス
                // FragmentManager?.beginTransaction(): FragmentTransactionオブジェクトの取得
                val transaction = fragmentManager?.beginTransaction()

                // フラグメントの削除
                // FragmentTransaction?.remove(fragment:): 指定したフラグメントの削除
                // fragment: 削除するフラグメント
                transaction?.remove(this@MenuThanksFragment)

                // 編集したフラグメントトランザクションを反映(コミット)
                // FragmentTransaction?.commit(): フラグメントトランザクションの編集を反映
                transaction?.commit()
            }
            // 10-inchの画面でない場合
            else {
                // アクティビティの終了
                // -> activityプロパティを中継してfinish()メソッドを利用
                activity?.finish()
            }
        }
    }

}