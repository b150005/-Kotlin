package com.example.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private var _itemId = -1

    private var _item = ""

    // DatabaseHelperオブジェクトの生成
    // -> クラスコンストラクタを定義しているため、
    //    DatabaseHelperクラスを利用するコンテキストを指定
    // <- 様々な処理で使用するため、処理直前ではなく事前に生成
    private val _helper = DatabaseHelper(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvItem = findViewById<ListView>(R.id.lvItem)

        lvItem.onItemClickListener = ListItemClickListener()
    }

    override fun onDestroy() {

        // DatabaseHelperオブジェクトの解放
        // SQLiteOpenHelper.close(): Databaseオブジェクトの解放
        _helper.close()

        // アクティビティの終了
        super.onDestroy()
    }

    fun onSaveButtonClick(view: View) {

        // EditTextのビュー
        val etNote = findViewById<EditText>(R.id.etNote)

        // EditTextに入力された値
        val note = etNote.text.toString()

        // SQLiteDatabaseオブジェクト(DB接続オブジェクト)の指定
        // SQLiteOpenHelper.writableDatabase: 読み書き可能なSQLiteDatabaseオブジェクト
        val db = _helper.writableDatabase

        // -- データ更新処理の開始 --

        // 更新前データを削除するSQL文
        // ?: 変数が後から挿入される箇所
        val sqlDelete = "DELETE FROM itemmemos WHERE _id = ?"

        // 更新前データを削除するSQL文のコンパイル
        // SQLiteDatabase.compileStatement(sql:): SQLiteStatementオブジェクトの取得(=コンパイル)
        // sql: コンパイルを行うSQL文
        var stmt = db.compileStatement(sqlDelete)

        // SQL文への値のバインド
        // SQLiteProgram.bind<T>(index:value:):
        // index: SQL文の"?"のインデックス番号
        // -> 1番目は"1", 2番目は"2", ..., n番目は"n"
        // value: SQL文の"?"にバインドする値
        stmt.bindLong(1, _itemId.toLong())

        // (更新・削除を行う)SQL文の実行
        // SQLiteStatement.executeUpdateDelete(): 更新・削除を行うSQL文の実行
        stmt.executeUpdateDelete()

        // 更新後データを挿入するSQL文
        // ?: 変数が後から挿入される箇所
        val sqlInsert = "INSERT INTO itemmemos (_id, item, note) VALUES (?, ?, ?)"

        // 更新後データを挿入するSQL文のコンパイル
        // SQLiteDatabase.compileStatement(sql:): SQLiteStatementオブジェクトの取得(=コンパイル)
        // sql: コンパイルを行うSQL文
        stmt = db. compileStatement(sqlInsert)

        // SQL文への値のバインド
        // SQLiteProgram.bind<T>(index:value:):
        // index: SQL文の"?"のインデックス番号
        // value: SQL文の"?"にバインドする値
        stmt.bindLong(1, _itemId.toLong())
        stmt.bindString(2, _item)
        stmt.bindString(3, note)

        // (挿入を行う)SQL文の実行
        // SQLiteStatement.executeInsert(): 挿入を行うSQL文の実行
        stmt.executeInsert()

        // -- データ更新処理の終了 --

        etNote.setText("")

        val tvItem = findViewById<TextView>(R.id.tvItem)

        tvItem.text = getString(R.string.tv_item)

        val btSave = findViewById<Button>(R.id.btSave)

        btSave.isEnabled = false
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            _itemId = position

            _item = parent?.getItemAtPosition(position) as String

            val tvItem = findViewById<TextView>(R.id.tvItem)

            tvItem.text = _item

            val btSave = findViewById<Button>(R.id.btSave)

            btSave.isEnabled = true

            // SQLiteDatabaseオブジェクト(DB接続オブジェクト)の指定
            // SQLiteOpenHelper.writableDatabase: 読み書きを行うSQLiteDatabaseオブジェクト
            val db = _helper.writableDatabase

            // -- データ取得処理の開始 --

            // データを取得するSQL文
            val sql = "SELECT * FROM itemmemos WHERE _id = ${_itemId}"

            // SQL文の実行結果表を格納するCursorオブジェクト
            // SQLiteDatabase.rawQuery(sql:selectionArgs:): 取得を行うSQL文の実行
            // sql: 実行するSQL文
            // selectionArgs: バインド変数用のString型配列
            // -> バインド変数を利用しない場合はnull
            val cursor = db.rawQuery(sql, null)

            var note = ""

            // ループによるデータ取得
            // Cursor.moveToNext(): 次の行に移動
            // -> 次の行が存在する場合はtrue, 存在しない場合はfalseを返す
            while (cursor.moveToNext()) {

                // 列名を指定してIndex番号を取得
                // Cursor.getColumnIndex(columnName:): 指定した列のIndex番号を取得
                // -> 指定した列が存在しない場合は-1を返却
                val idxNote = cursor.getColumnIndex("note")

                // 文字列の取得
                // Cursor.getString(columnIndex:): Index番号を指定した文字列の取得
                note = cursor.getString(idxNote)
            }

            // -- データ取得処理の終了 --

            // 取得した文字列を反映するEditText
            val etNote = findViewById<EditText>(R.id.etNote)

            // 取得した文字列の代入
            etNote.setText(note)
        }
    }
}