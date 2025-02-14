<<<<<<<< HEAD:app/src/main/java/top/yogiczy/mytv/activities/LeanbackActivity.kt
package com.zhuangzhi.mytv.activities
========
package com.zhuangzhi.mytv.tv
>>>>>>>> ee27a07f525f5a5f2b5114240b2ba6bfabe66f88:tv/src/main/java/top/yogiczy/mytv/tv/MainActivity.kt

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
<<<<<<<< HEAD:app/src/main/java/top/yogiczy/mytv/activities/LeanbackActivity.kt
import com.zhuangzhi.mytv.ui.LeanbackApp
import com.zhuangzhi.mytv.ui.screens.leanback.toast.LeanbackToastState
import com.zhuangzhi.mytv.ui.theme.LeanbackTheme
import com.zhuangzhi.mytv.ui.utils.HttpServer
import com.zhuangzhi.mytv.ui.utils.SP
import kotlin.system.exitProcess

class LeanbackActivity : ComponentActivity() {
    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        if (!SP.uiPipMode) return

        enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
        )
        super.onUserLeaveHint()
    }

========
import androidx.tv.material3.Surface
import com.zhuangzhi.mytv.tv.ui.App
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme
import com.zhuangzhi.mytv.tv.utlis.HttpServer
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
>>>>>>>> ee27a07f525f5a5f2b5114240b2ba6bfabe66f88:tv/src/main/java/top/yogiczy/mytv/tv/MainActivity.kt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, window.decorView).let { insetsController ->
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // 屏幕常亮
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            MyTVTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App(
                        onBackPressed = {
                            finish()
                            exitProcess(0)
                        },
                    )
                }
            }
        }

        HttpServer.start(applicationContext, showToast = {
            LeanbackToastState.I.showToast(it, id = "httpServer")
        })
    }
}