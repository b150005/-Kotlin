package com.example.cameraintent

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // startActivityForResult()による遷移先アクティビティでの処理終了後、
    // 遷移元アクティビティで呼び出される処理(=コールバック処理)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // リクエストコードが一致し、かつ正常に処理が終了していた場合の処理
        if(requestCode == 200 && resultCode == RESULT_OK) {

            // 「カメラ」アプリによって変換されたBitmapオブジェクト(=画像)
            // <- ストレージを利用しない場合はサムネイル画像しか取得できないため、
            //    解像度の低い画像となる
            // val bitmap = data?.getParcelableExtra<Bitmap>("data")

            // 画像を表示するImageView
            val ivCamera = findViewById<ImageView>(R.id.ivCamera)

            // Bitmapデータを指定してImageViewに画像を反映
            // ivCamera.setImageBitmap(bitmap)

            // URIを指定してImageViewに画像を反映
            ivCamera.setImageURI(_imageUri)
        }
    }

    fun onCameraImageClick(view: View) {

        // 日時形式を指定したSimpleDateFormatオブジェクト
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")

        // 現在の時刻を保持するDateオブジェクト
        val now = Date()

        // Date型 -> String型 への変換
        val nowStr = dateFormat.format(now)

        // タイムスタンプを利用したファイル名の指定
        val fileName = "CameraIntentPhoto_${nowStr}.jpg"

        // ContentValuesオブジェクト(=データ情報)
        val values =  ContentValues()

        // ContentValuesオブジェクトに格納するファイル名
        values.put(MediaStore.Images.Media.TITLE, fileName)

        // ContentValuesオブジェクトに格納するMIMEタイプ(=ファイルの種類)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        // データの格納先URIとデータ情報(=ContentValuesオブジェクト)を保持するURI
        _imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // 「カメラ」アプリを起動するインテント
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 画像の出力先をURIで指定
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)

        // 遷移先での処理後に遷移元へ戻る画面遷移の実行
        startActivityForResult(intent, 200)
    }
}