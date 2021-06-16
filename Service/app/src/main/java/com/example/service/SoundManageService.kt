package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SoundManageService : Service() {

    // クラス定数
    companion object {
        private const val CHANNEL_ID = "soundmanagerservice_notification_channel"
    }

    // MediaPlayerプロパティ
    private var _player: MediaPlayer? = null

    // Serviceクラスの抽象メソッドの実装(必須)
    // -> サービスをバインドして実行する場合はブロック内{...}に処理を記述
    override fun onBind(intent: Intent): IBinder {}

    // サービスの初期化時に実行する処理
    // <- 親クラスのonCreate()メソッドの呼び出しは不要
    override fun onCreate() {
        // MediaPlayerオブジェクトの生成
        _player = MediaPlayer()

        // 通知チャネル名
        val name = getString(R.string.notification_channel_name)

        // 通知チャネルの重要度
        // -> NotificationManagerのクラス定数を利用
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        // 通知チャネル
        val channel = NotificationChannel(CHANNEL_ID, name, importance)

        // NotificationManagerオブジェクトの取得
        val manager = getSystemService(NotificationManager::class.java)

        // 通知チャネルをセット
        // -> 独自に作成したNotificationChannelオブジェクトは、
        //    NotificationManager内で再生成する必要がある
        manager.createNotificationChannel(channel)
    }

    // サービスの実行開始時に行う処理
    // -> サービスの強制終了時の処理を表すServiceクラス定数を返却
    // <- 親クラスのonStartCommand()メソッドの呼び出しは不要
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // メディアファイルのURI文字列
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"

        // String型 → Uri型 への変換
        val mediaFileUri = Uri.parse(mediaFileUriStr)

        // MediaPlayerがnullでないことを保証
        _player?.let {
            // メディアファイルの指定
            it.setDataSource(this@SoundManageService, mediaFileUri)

            // 再生準備完了時のリスナ定義
            it.setOnPreparedListener(PlayerPreparedListener())

            // 再生完了時のリスナ定義
            it.setOnCompletionListener(PlayerCompletionListener())

            // 非同期での再生準備
            it.prepareAsync()
        }

        // Serviceクラス定数の返却
        // <- サービス強制終了時のリカバリ方法を表すクラス定数
        return START_NOT_STICKY
    }

    // サービスの終了時に実行する処理
    // <- 親クラスのonDestroy()メソッドの呼び出しは不要
    override fun onDestroy() {
        // MediaPlayerがnullでないことを保証
        _player?.let {
            // MediaPlayerが再生中である場合の処理
            if (it.isPlaying) {
                // MediaPlayerの停止
                it.stop()
            }

            // MediaPlayerオブジェクトの解放
            it.release()
        }

        // MediaPlayerプロパティをnullに変更
        // -> MediaPlayerオブジェクトの完全な解放
        _player = null
    }

    // メディアファイルの"準備完了"イベントを検知するリスナクラス
    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        // "準備完了"イベント検知時の処理
        override fun onPrepared(mp: MediaPlayer?) {
            // メディアファイルを再生
            mp?.start()

            // Notificationオブジェクトを作成するBuilderオブジェクト
            val builder = NotificationCompat.Builder(this@SoundManageService, CHANNEL_ID)

            // 通知エリアに表示するアイコン
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)

            // 通知ドロワーに表示するタイトル
            // -> Context.getString(resId:)メソッドを用いてCharSequence型文字列を取得
            // CharSequence: String型やStringBuilder型で実装されるインタフェース
            // => String型やStringBuilder型の文字列が利用可能
            builder.setContentTitle(getString(R.string.msg_notification_title_start))

            // 通知ドロワーに表示するメッセージ
            // -> Context.getString(resId:)メソッドを用いてCharSequence型文字列を取得
            builder.setContentText(getString(R.string.msg_notification_text_start))

            // 起動先アクティビティを指定するIntentオブジェクトの生成
            val intent = Intent(this@SoundManageService, MainActivity::class.java)

            // Intentオブジェクトにデータを格納
            intent.putExtra("fromNotification", true)

            // アクティビティを起動するPendingIntentオブジェクト
            val stopServiceIntent = PendingIntent.getActivity(
                this@SoundManageService,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            // BuilderにPendingIntentオブジェクトをセット
            builder.setContentIntent(stopServiceIntent)

            // タップされた通知メッセージを自動的に消去
            builder.setAutoCancel(true)

            // Notificationオブジェクトの生成
            val notification = builder.build()

            // サービスのフォアグラウンド実行
            startForeground(200, notification)
        }
    }

    // メディアファイルの"再生完了"イベントを検知するリスナクラス
    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {
            // Notificationオブジェクトを生成するBuilderオブジェクト
            val builder = NotificationCompat.Builder(this@SoundManageService, CHANNEL_ID)

            // 通知エリアに表示するアイコン
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)

            // 通知ドロワーに表示するタイトル
            // -> Context.getString(resId:)メソッドを用いてCharSequence型文字列を取得
            builder.setContentTitle(getString(R.string.msg_notification_title_finish))

            // 通知ドロワーに表示するメッセージ
            // -> Context.getString(resId:)メソッドを用いてCharSequence型文字列を取得
            builder.setContentText(getString(R.string.msg_notification_text_finish))

            // Notificationオブジェクトの生成
            val notification = builder.build()

            // サービスクラスからNotificationManagerCompatオブジェクトを取得
            val manager = NotificationManagerCompat.from(this@SoundManageService)

            // 通知を実行
            manager.notify(100, notification)

            // サービスクラス自身によるサービスの終了
            stopSelf()
        }
    }
}