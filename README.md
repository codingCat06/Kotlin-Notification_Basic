# Kotlin-Notification_Basic
## 1. build.gradle ( 펄미션 )
    dependencies {
        implementation 'io.github.ParkSangGwon:tedpermission-normal:3.3.0'
        ....
    }

## 2. manifests
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

## 3. import
###   1) Notification 필수
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import androidx.core.app.NotificationCompat
    import android.os.Build

###   2) Notification Attribute 부과 설정용
    import android.graphics.Color
    import android.media.AudioAttributes
    import android.media.RingtoneManager
    import android.net.Uri
    import android.nfc.Tag

###   3) Others..
    import com.gun0912.tedpermission.PermissionBuilder
    import com.gun0912.tedpermission.PermissionListener
    import com.gun0912.tedpermission.normal.TedPermission
    import android.Manifest
    import android.widget.Toast

## 4. Make Chaneel
#### 채널을 만들기 위해서 ( ID, NAME ) 그리고 ( Notification ) 의 중요도가 필요함.
    val channel = NotificationChannel( channel_ID, channel_Name, NotificationManager.IMPORTANCE_HIGH )

#### 아래 코드를 통해 화면에 보이는 앱 배너 알림이 왔다는 표시가 뜸 ( ex: 카톡 아이콘 옆에 숫자... )
    channel.SetShowBadge( Boolean )

## 5. Attribution ( 추가 설정 )

#### 1) RingtoneManager
##### RingtoneManager의 Uri 와 audioAtrribute가 필요함.
##### Uri 는 RingtoneManager의 Type에 따른 Uri를 getDefaultUri( Type ) 을 통해 얻을 수 있음.
##### audioAtrribute는 AudioAttribute.Builder() 를 통해 생성하고 setContentType, setUsage 등을 통해 설정할 수 있음
##### 또한 Builder로 정의되었기에 마지막에 .builder() 로 마무리함.
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
    

### 전체 코드
    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val builder: NotificationCompat.Builder

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel_ID = "one-channel"
        val channel_Name = "My Channel One"
        val channel = NotificationChannel( channel_ID, channel_Name, NotificationManager.IMPORTANCE_HIGH )
        var channel_description = "my channel one description"
        channel.setShowBadge(true)
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        channel.setSound(uri, audioAttributes)
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

        manager.createNotificationChannel(channel)
        builder = NotificationCompat.Builder(this, channel_ID)
    } else {
        builder = NotificationCompat.Builder(this)
    }
