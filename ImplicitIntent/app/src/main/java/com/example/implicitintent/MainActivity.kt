package com.example.implicitintent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    // 経緯度プロパティ
    // -> 位置情報が取得できなかった場合に備えて初期値を設定
    private var _latitude = 0.0
    private var _longitude = 0.0

    // FusedLocationProviderClientプロパティ
    // -> オブジェクトがリソースを大量に消費する場合、
    //    アクティビティ初期化時にオブジェクトを生成(=初期化)できるよう
    //    lateinitキーワードを用いて宣言
    private lateinit var _fusedLocationClient: FusedLocationProviderClient

    // LocationRequestプロパティ
    private lateinit var _locationRequest: LocationRequest

    // OnUpdateLocationプロパティ
    // <- 抽象クラスであるLocationCallbackの実装クラス
    private lateinit var _onUpdateLocation: OnUpdateLocation

    // アクティビティ初期化時に呼び出される処理
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FusedLocationProviderClientプロパティの初期化
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        // LocationRequestプロパティの初期化
        _locationRequest = LocationRequest.create()

        // LocationRequestプロパティがnullでないことを保証
        // <- lateinitで宣言したオブジェクトのプロパティを変更する場合は
        //    オブジェクトがnullでないことの保証が推奨
        _locationRequest?.let {

            // 基本取得間隔[ms]
            it.interval = 5000

            // 最短取得間隔[ms]
            it.fastestInterval = 1000

            // 位置情報の取得精度
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // LocationCallbackプロパティの初期化
        _onUpdateLocation = OnUpdateLocation()
    }

    // アクティビティの表示直前に呼び出される処理
    override fun onResume() {
        super.onResume()

        // アクティビティによるセルフパーミッションチェックの結果、
        // 位置情報の利用が許可されていない場合の処理
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // パーミッションチェックを行うパーミッションリスト
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

            // ユーザに許可を求めるパーミッションダイアログの表示
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                1000
            )

            // onResume()メソッドの終了
            return
        }

        // アクティビティによるセルフパーミッションチェックの結果、
        // 位置情報の利用が許可されている場合の処理
        // -> 位置情報を取得
        _fusedLocationClient.requestLocationUpdates(
            _locationRequest,
            _onUpdateLocation,
            mainLooper
        )
    }

    // パーミッションダイアログが操作された場合に呼び出されるコールバックメソッド
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        // ユーザ操作によって利用許可が下りていた場合
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // アクティビティによるセルフパーミッションチェックの結果、
            // 位置情報の利用が許可されていない場合の処理
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 何も実行せずonRequestPermissionsResult()メソッドを終了
                return
            }

            // アクティビティによるセルフパーミッションチェックの結果、
            // 位置情報の利用が許可されている場合の処理
            // -> 位置情報を取得
            _fusedLocationClient.requestLocationUpdates(
                _locationRequest,
                _onUpdateLocation,
                mainLooper
            )
        }
    }

    // アクティビティの非表示直前に呼び出される処理
    override fun onPause() {
        super.onPause()

        // 位置情報の取得を停止
        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation)
    }

    // 位置情報の取得処理終了時に呼び出される処理の実装クラス
    private inner class OnUpdateLocation: LocationCallback() {

        // 位置情報の取得処理終了時に呼び出される処理
        override fun onLocationResult(result: LocationResult?) {

            // 取得結果がnullでないことを保証
            result?.let {

                // 直近の位置情報を保持するlastLocationプロパティ
                val location = it.lastLocation

                // 直近の位置情報がnullでないことを保証
                location?.let {

                    // 直近の経緯度
                    _latitude = it.latitude
                    _longitude = it.longitude

                    // 反映するTextView
                    val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
                    val tvLongitude = findViewById<TextView>(R.id.tvLongitude)

                    // 取得した経緯度の反映
                    tvLatitude.text = _latitude.toString()
                    tvLongitude.text = _longitude.toString()
                }
            }
        }
    }

    // Findボタンが押された場合に呼び出される処理
    fun onMapSearchButtonClick(view: View) {

        // EditText
        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)

        // EditTextに入力された値
        var searchWord = etSearchWord.text.toString()

        // URLエンコーディング
        searchWord = URLEncoder.encode(searchWord, "UTF-8")

        // String型URI
        val uriStr = "geo:0,0?q=${searchWord}"

        // String型 → Uri型 への変換
        val uri = Uri.parse(uriStr)

        // 指定したURIを表示するIntentオブジェクト
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // 画面遷移の実行
        startActivity(intent)
    }

    // Showボタンが押された場合に呼び出される処理
    fun onMapShowCurrentButtonClick(view: View) {

        // String型URI
        val uriStr = "geo:${_latitude},${_longitude}"

        // String型 → Uri型 への変換
        val uri = Uri.parse(uriStr)

        // 指定したURIを表示するIntentオブジェクト
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // 画面遷移の実行
        startActivity(intent)
    }
}

