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

    // 緯度プロパティ(初期値: 0.0)
    private var _latitude = 0.0
    // 経度プロパティ(初期値: 0.0)
    private var _longitude = 0.0

    // FusedLocationProviderClientプロパティ
    private lateinit var _fusedLocationClient: FusedLocationProviderClient

    // LocationRequestプロパティ
    private lateinit var _locationRequest: LocationRequest

    //
    private lateinit var _onUpdateLocation: OnUpdateLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FusedLocationProviderClientオブジェクトの取得
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        // LocationRequestオブジェクトの生成
        _locationRequest = LocationRequest.create()

        // LocationRequestオブジェクトがnullでない場合の処理
        _locationRequest?.let {
            //
            it.interval = 5000

            //
            it.fastestInterval = 1000

            //
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        //
        _onUpdateLocation = OnUpdateLocation()
    }

    //
    override fun onResume() {
        super.onResume()

        // ACCESS_FINE_LOCATIONが許可されていない場合の処理
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

            //
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                1000
            )

            // onResume()メソッドの終了
            return
        }
        _fusedLocationClient.requestLocationUpdates(
            _locationRequest,
            _onUpdateLocation,
            mainLooper
        )
    }

    override fun onPause() {
        super.onPause()

        //
        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //
                _fusedLocationClient.requestLocationUpdates(
                    _locationRequest,
                    _onUpdateLocation,
                    mainLooper
                )
            }
        }
    }

    //
    private inner class OnUpdateLocation: LocationCallback() {
        //
        override fun onLocationResult(p0: LocationResult?) {
            //
            p0?.let {
                // LocationResultオブジェクトが保持する直近の位置情報
                val location = it.lastLocation

                location?.let {
                    // Locationオブジェクトが保持する経緯度
                    _latitude = it.latitude
                    _longitude = it.longitude

                    // 取得した経緯度を表示するTextView
                    val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
                    val tvLongitude = findViewById<TextView>(R.id.tvLongitude)

                    // TextViewへの経緯度の反映
                    tvLatitude.text = _latitude.toString()
                    tvLongitude.text = _longitude.toString()
                }
            }
        }
    }

    // Findボタンが押された場合の処理
    fun onMapSearchButtonClick(view: View) {
        // EditTextビュー
        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)

        // EditTextビューに入力されたString型文字列
        var searchWord = etSearchWord.text.toString()

        // 文字エンコードを指定したURLエンコーディング
        // URLEncoder.encode(s:enc:): 文字エンコードを指定したURLエンコーディング
        // URLEncoder: URLエンコーディングを行うメソッドを定義するクラス
        // s: String型文字列
        // enc: 文字エンコード
        searchWord = URLEncoder.encode(searchWord, "UTF-8")

        // 地図アプリと連携するString型URI
        // geo:y,x?q=<検索文字列>: 地図アプリで指定した文字列を検索するURI
        // geo:: 地図アプリを表すURI
        // y: 緯度の初期値
        // x: 経度の初期値
        val uriStr = "geo:0,0?q=${searchWord}"

        // String型 → Uri型 への変換
        val uri = Uri.parse(uriStr)

        // 暗黙的インテント(=Intentオブジェクト)の生成
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // アクティビティの起動
        startActivity(intent)
    }

    // Showボタンが押された場合の処理
    fun onMapShowCurrentButtonClick(view: View) {
        // 地図アプリと連携するString型URI
        val uriStr = "geo:${_latitude},${_longitude}"

        // String型 → Uri型 への変換
        val uri = Uri.parse(uriStr)

        // 暗黙的インテント(=Intentオブジェクト)の生成
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // アクティビティの起動
        startActivity(intent)
    }
}