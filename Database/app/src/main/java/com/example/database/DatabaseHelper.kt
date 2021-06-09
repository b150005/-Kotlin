package com.example.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.StringBuilder

// SQLiteデータベースを生成するクラス
// -> SQLiteOpenHelperのコンテキストを代入するため、
//    クラスコンストラクタにContextを必要とする

// SQLiteOpenHelper(context:name:factory:version:): SQLiteOpenHelperオブジェクトの生成
// context: DatabaseHelperクラスを利用するアクティビティオブジェクト(コンテキスト)
// -> DatabaseHelperクラス側では用意できないため、クラスコンストラクタの引数を利用
// name: データベース名
// factory: 手動的に作成するCursorFactoryオブジェクト(通常: null)
// version: データベースのバージョン番号
class DatabaseHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    // private定数の定義
    companion object {
        // データベース名
        private const val DATABASE_NAME = "itemmemo.db"
        // データベースのバージョン番号
        private const val DATABASE_VERSION = 1
    }

    // SQLiteDatabaseの初期化時に実行される処理
    override fun onCreate(db: SQLiteDatabase?) {
        // DDL文
        // StringBuilder: 文字列を格納する(同一ポインタ内での)可変配列を定義するクラス
        val sb = StringBuilder()

        // DDL文の定義
        // _id: ListViewの行番号(INTEGER, 主キー)
        // <- カラム名を"_id"にすると、Android OSが自動的に主キーとして判定
        // item: Item名(TEXT型)
        // note: EditTextの値(TEXT型)
        sb.append("CREATE TABLE itemmemos(")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("item TEXT,")
        sb.append("note TEXT")
        sb.append((");"))

        val sql = sb.toString()

        // SQLiteデータベースの生成
        // DDL文: SQLiteデータベースの生成・削除
        // SQLiteDatabase?.execSQL(sql:): DDL文によるSQLiteデータベースの生成・削除
        // sql: DDL文
        db?.execSQL(sql)
    }

    // 端末内部のSQLiteデータベースのバージョンが古い場合に実行される処理
    // -> 抽象メソッドのため、処理が不要でも記述する必要がある
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}