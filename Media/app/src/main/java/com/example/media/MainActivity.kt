package com.example.media

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*

class MainActivity : AppCompatActivity() {

    // MediaPlayerプロパティ
    // -> オブジェクト解放時はnullにするため、Nullable型で宣言
    private var _player: MediaPlayer? = null

    // アクティビティ初期化時の処理
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // MediaPlayerオブジェクトの生成
        _player = MediaPlayer()

        // Loopスイッチ(トグルスイッチ)
        val loopSwitch = findViewById<SwitchMaterial>(R.id.swLoop)

        // メディアファイルのURI文字列
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"

        // String型 → URI型 への変換
        val mediaFileUri = Uri.parse(mediaFileUriStr)

        // MediaPlayerがnullでない場合の処理
        _player?.let {

            // メディアファイルの指定
            it.setDataSource(this@MainActivity, mediaFileUri)

            // メディアファイルの準備完了時のリスナ定義
            it.setOnPreparedListener(PlayerPreparedListener())

            // メディアファイルの再生完了時のリスナ定義
            it.setOnCompletionListener(PlayerCompletionListener())

            // 非同期でのMediaPlayerオブジェクトの準備
            it.prepareAsync()
        }

        // トグルスイッチをリスナとしてセット
        loopSwitch.setOnCheckedChangeListener(LoopSwitchChangedListener())
    }

    // 再生ボタンが"タップ"された場合の処理(イベントハンドラ)
    fun onPlayButtonClick(view: View) {

        // MediaPlayerがnullでない場合の処理
        _player?.let {

            // 再生ボタン
            val btPlay = findViewById<Button>(R.id.btPlay)

            // MediaPlayerの再生状態に応じた分岐処理
            // 再生中である場合の処理
            if (it.isPlaying) {
                // MediaPlayerの一時停止
                it.pause()

                // 再生ボタンのラベルを"Play"に変更
                btPlay.setText(R.string.bt_play_play)
            }
            // 再生中でない場合の処理
            else {
                // MediaPlayerの再生
                it.start()

                // 再生ボタンのラベルを"Pause"に変更
                btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    // <<ボタンが"タップ"された場合の処理(イベントハンドラ)
    fun onBackButtonClick(view: View) {

        // 再生位置を先頭に変更
        _player?.seekTo(0)
    }

    // >>ボタンが"タップ"された場合の処理(イベントハンドラ)
    fun onForwardButtonClick(view: View) {

        // MediaPlayerがnullでない場合の処理
        _player?.let {

            // 再生中のメディアファイルの長さ
            val duration = it.duration

            // 再生位置を末尾に変更
            it.seekTo(duration)

            // MediaPlayerが再生中でない場合の処理
            if (!it.isPlaying) {

                // MediaPlayerの再生
                it.start()
            }
        }
    }

    // アクティビティ終了時の処理
    override fun onDestroy() {

        // MediaPlayerがnullでない場合の処理
        // -> let関数ブロック内のitは非Nullable型
        _player?.let {

            // MediaPlayerの再生状態に応じた分岐処理
            // 再生中である場合の処理
            if (it.isPlaying) {
                // MediaPlayerの停止
                it.stop()
            }

            // MediaPlayerオブジェクトの解放
            it.release()
        }

        // MediaPlayerをnullに変更
        _player = null

        // アクティビティの終了
        super.onDestroy()
    }

    // "メディアファイルの準備完了"イベントを検知するリスナクラス
    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        // "メディアファイルの準備"完了時の処理
        override fun onPrepared(mp: MediaPlayer?) {

            // 再生ボタン
            val btPlay = findViewById<Button>(R.id.btPlay)
            // <<ボタン
            val btBack = findViewById<Button>(R.id.btBack)
            // >>ボタン
            val btForward = findViewById<Button>(R.id.btForward)

            // 各種ボタンを利用可能な状態に変更
            btPlay.isEnabled = true
            btBack.isEnabled = true
            btForward.isEnabled = true
        }
    }

    // "メディアファイルの再生完了"イベントを検知するリスナクラス
    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        // "メディアファイルの再生"完了時の処理
        override fun onCompletion(mp: MediaPlayer?) {

            // MediaPlayerがnullでない場合の処理
            _player?.let {

                // ループ状態でない場合の処理
                if (!it.isLooping) {
                    // 再生ボタン
                    val btPlay = findViewById<Button>(R.id.btPlay)

                    // 再生ボタンのラベルを変更
                    btPlay.setText(R.string.bt_play_play)
                }
            }
        }
    }

    // ループスイッチの"状態遷移"イベントを検知するリスナクラス
    private inner class LoopSwitchChangedListener: CompoundButton.OnCheckedChangeListener {
        // "状態遷移"イベント検知時の処理
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            // ループ機能の変更
            _player?.isLooping = isChecked
        }
    }

}