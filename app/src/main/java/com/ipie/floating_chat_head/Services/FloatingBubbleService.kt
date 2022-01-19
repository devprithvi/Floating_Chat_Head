package com.ipie.floating_chat_head.Services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.*
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.ipie.floating_chat_head.MainActivity
import com.ipie.floating_chat_head.R
import com.ipie.floating_chat_head.SimpleService
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

open class FloatingBubbleService : Service() {

    val TAG = FloatingBubbleService::class.java.simpleName
    val CHANNEL_ID = "ForegroundServiceChannel"


    var serviceStatus = false

    // Constructor Variable
    var logger: FloatingBubbleLogger? = null

    // The Window Manager View
    private var windowManager: WindowManager? = null

    // The layout inflater
    var inflater: LayoutInflater? = null

    // Window Dimensions
    val windowSize = Point()

    // The Views
    var bubbleView: View? = null
    var removeBubbleView: View? = null
    var expandableView: View? = null

    var bubbleParams: WindowManager.LayoutParams? = null
    var removeBubbleParams: WindowManager.LayoutParams? = null
    var expandableParams: WindowManager.LayoutParams? = null


    var physics: FloatingBubblePhysics? = null
    var touch: FloatingBubbleTouch? = null
    var secondsinmillis: Long = 0
    var countDownTimer: CountDownTimer? = null

    //    var mAPIService: APIService? = null
    var DamageScreenUrl: String? = null
    var notification: Notification? = null
    var manager: NotificationManager? = null
    val mHandler = Handler()

//    String sharedData;


    //    String sharedData;
    override fun onCreate() {
        super.onCreate()
        logger = FloatingBubbleLogger().setDebugEnabled(true).setTag(TAG)
//        mAPIService = ApiUtils.getAPIService()
        //sharedData = SharedPref.getInstance(getContext()).getData("CancelMethod");
        // sharedData = SharedPref.getInstance(getContext()).getData1("CancelMethod");
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.e(FloatingBubbleService::class.java.name, " onStart() ======== " + " Called ")
        val counter = 60
        secondsinmillis = TimeUnit.SECONDS.toMillis(counter.toLong())
        Log.e(
            FloatingBubbleService::class.java.name,
            "secondsinmillis in first time == $secondsinmillis"
        )
        countDownTimer = object : CountDownTimer(secondsinmillis, 1000) {
            override fun onFinish() {
//                SharedPref.cancelMethod = "notCancel"
//                Log.e(
//                    FloatingBubbleService::class.java.name,
//                    "onFinish() ===" + "Called " + SharedPref.cancelMethod
//                )
            }

            override fun onTick(millisUntilFinished: Long) {
                val sharedData = ""
//                val sharedData: String = SharedPref.cancelMethod
                Log.d(TAG, "onTickData:------------------------------------ $sharedData")
                if (sharedData === "cancel") {
                    Log.d(TAG, "onTick-----if-----: $sharedData")
                    countDownTimer!!.cancel()
                    Log.d(TAG, "onTick-----if----: $sharedData")
                    Log.e(FloatingBubbleService::class.java.name, " Status:----- sharedData")
                    countDownTimer!!.onFinish()
                    bubbleView!!.visibility = View.GONE
                    removeBubbleView!!.visibility = View.GONE
                    //SharedPref.getInstance(getContext()).saveData1("CancelMethod",false);
//                    SharedPref.cancelMethod = "notCancel"
                    val dialogIntent =
                        Intent(this@FloatingBubbleService, MainActivity::class.java)
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    dialogIntent.putExtra("action", "in_process")
                    startActivity(dialogIntent)
                    stopSelf()
                } else {
                    Log.d(TAG, "onTick-----else---: $sharedData")
                    secondsinmillis = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    Log.e(
                        FloatingBubbleService::class.java.name,
                        "millisUntilFinished in onTick == $millisUntilFinished"
                    )
                    Log.e(
                        FloatingBubbleService::class.java.name,
                        "seconds == $secondsinmillis"
                    )
                    if (secondsinmillis == 20L || secondsinmillis == 10L || secondsinmillis == 0L) {
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " Inside Condition Seconds ======== $secondsinmillis"
                        )
                        if (secondsinmillis == 0L) {
                            countDownTimer!!.cancel()
                        }
                        val ScreenUrl =
                            "https://api1.altruistsecure.com/api/statusinfo/v1/damagedscreenstatus/"
                        DamageScreenUrl = ScreenUrl +
                                config?.userID.toString() +
                                "/source/" +
                                "1/" + "qrcodetoken/" + config?.qrCodeToken.toString() +
                                "?jwtToken=" +
                                config!!.token
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            "DamageScreenUrl====$DamageScreenUrl"
                        )
//                        sendPost(DamageScreenUrl)
                    }
                }
            }
        }.start()
    }

    /*fun sendPost(url: String?) {
        mAPIService.damageScreenApi(url).enqueue(object : Callback<DamageScreenStatus?>() {
            fun onResponse(
                call: Call<DamageScreenStatus?>?,
                response: Response<DamageScreenStatus?>
            ) {
                Log.e("Response in the api", "" + response.body().toString())
                Log.e(
                    "Response  getErrorCode",
                    "" + response.body().getStatusDescription().getErrorCode()
                )
                Log.e(
                    "Response  getStatus",
                    "" + response.body().getDeviceDetailsUploads().getStatus()
                )
                if (response.body().getStatusDescription().getErrorCode() === 200) {
                    Log.e(
                        "Response  200",
                        "getStatus() == " + response.body().getDeviceDetailsUploads().getStatus()
                    )
                    if (response.body().getDeviceDetailsUploads()
                            .getStatus() != null && response.body().getDeviceDetailsUploads()
                            .getStatus().equals("1")
                    ) {
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " Status inside 1" + response.body().toString()
                        )
                        countDownTimer!!.cancel()
                        bubbleView!!.visibility = View.GONE
                        removeBubbleView!!.visibility = View.GONE
                        val dialogIntent =
                            Intent(this@FloatingBubbleService, FloatingButton::class.java)
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        dialogIntent.putExtra("action", "1")
                        startActivity(dialogIntent)
                        stopSelf()
                    } else if (response.body().getDeviceDetailsUploads().getStatus().equals("2")) {
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " Status inside 2" + response.body().toString()
                        )
                        countDownTimer!!.cancel()
                        bubbleView!!.visibility = View.GONE
                        removeBubbleView!!.visibility = View.GONE
                        val dialogIntent =
                            Intent(this@FloatingBubbleService, FloatingButton::class.java)
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        dialogIntent.putExtra("action", "2")
                        startActivity(dialogIntent)
                        stopSelf()
                    } else if (response.body().getDeviceDetailsUploads().getStatus().equals("0")) {
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " Status inside 0" + response.body().toString()
                        )
                        if (secondsinmillis == 0L) {
                            Log.e(
                                FloatingBubbleService::class.java.name,
                                " Status inside 0 counterForApiCalled" + response.body().toString()
                            )
                            countDownTimer!!.cancel()
                            bubbleView!!.visibility = View.GONE
                            removeBubbleView!!.visibility = View.GONE
                            val dialogIntent = Intent(
                                this@FloatingBubbleService,
                                FloatingButton::class.java
                            )
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            dialogIntent.putExtra("action", "in_process")
                            startActivity(dialogIntent)
                            stopSelf()
                        }
                        //countDownTimer.start();
                    }
                } else {
                    Log.e(
                        FloatingBubbleService::class.java.name,
                        " Status 201 inside" + response.body().toString()
                    )
                    if (secondsinmillis == 0L) {
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " Status inside 201 counterForApiCalled" + response.body().toString()
                        )
                        countDownTimer!!.onFinish()
                        bubbleView!!.visibility = View.GONE
                        removeBubbleView!!.visibility = View.GONE
                        val dialogIntent =
                            Intent(this@FloatingBubbleService, FloatingButton::class.java)
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        dialogIntent.putExtra("action", "3")
                        startActivity(dialogIntent)
                        stopSelf()
                    }
                }
            }

            fun onFailure(call: Call<DamageScreenStatus?>?, t: Throwable?) {
                Log.e(TAG, "Unable to submit post to API.")
            }
        })
    }*/

    override fun onDestroy() {
        serviceStatus = false
        countDownTimer!!.cancel()
        super.onDestroy()
        stopSelf()
    }

    fun mTimer() {
        Thread {
            while (true) {
                try {
                    Thread.sleep(10000)
                    mHandler.post { // Write your code here to update the UI.
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            " =====   " + "MEthod Called"
                        )
                        val ScreenUrl =
                            "https://api1.altruistsecure.com/api/statusinfo/v1/damagedscreenstatus/"
                        DamageScreenUrl = ScreenUrl +
                                config?.userID.toString() +
                                "/source/" +
                                "1" +
                                "?jwtToken=" +
                                config!!.token
                        Log.e(
                            FloatingBubbleService::class.java.name,
                            "DamageScreenUrl====$DamageScreenUrl"
                        )
//                        sendPost(DamageScreenUrl)
                    }
                } catch (e: Exception) {
                }
            }
        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceStatus = true
        if (intent == null || !onGetIntent(intent)) {
            return Service.START_NOT_STICKY
        }
        //        logger.log("Start with START_STICKY");
        stopForeground(true)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service") //                .setContentText(input)
            .setSmallIcon(R.drawable.triangle_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)


        // Remove existing views
        removeAllViews()

        // Load the Window Managers
        setupWindowManager()
        setupViews()
        setTouchListener()
        Log.e(FloatingBubbleService::class.java.name, "onStartCommand == " + "Called")
        return super.onStartCommand(intent, flags, Service.START_NOT_STICKY)
//          return START_NOT_STICKY;
    }

    open fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager = getSystemService<NotificationManager>(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
            manager!!.cancel(1)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        serviceStatus = false
        countDownTimer!!.cancel()
        val serviceIntent = Intent(this, SimpleService::class.java)
        stopService(serviceIntent)
        super.onTaskRemoved(rootIntent)
    }


//        @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//        countDownTimer.cancel();
//    }


//    @Override
//    public boolean onUnbind(Intent intent) {
//
//        Log.e("onUnbind()", ""  +"Method Called");
//        return super.onUnbind(intent);
//
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.e("onDestroy()", ""  +"Method Called");
//        logger.log("onDestroy");
//        countDownTimer.cancel();
//        removeAllViews();
//    }


    //        @Override
    //    public void onTaskRemoved(Intent rootIntent) {
    //        super.onTaskRemoved(rootIntent);
    //        countDownTimer.cancel();
    //    }
    //    @Override
    //    public boolean onUnbind(Intent intent) {
    //
    //        Log.e("onUnbind()", ""  +"Method Called");
    //        return super.onUnbind(intent);
    //
    //    }
    //    @Override
    //    public void onDestroy() {
    //        super.onDestroy();
    //        Log.e("onDestroy()", ""  +"Method Called");
    //        logger.log("onDestroy");
    //        countDownTimer.cancel();
    //        removeAllViews();
    //    }
    open fun removeAllViews() {
        if (windowManager == null) {
            return
        }
        if (bubbleView != null) {
            windowManager!!.removeView(bubbleView)
            bubbleView = null
        }
        if (removeBubbleView != null) {
            windowManager!!.removeView(removeBubbleView)
            removeBubbleView = null
        }
        if (expandableView != null) {
            windowManager!!.removeView(expandableView)
            expandableView = null
        }
    }

//    fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//    }

    open fun setupWindowManager() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        setLayoutInflater()
        windowManager!!.defaultDisplay.getSize(windowSize)
    }

    fun setLayoutInflater(): LayoutInflater? {
        inflater =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        return inflater
    }

    /**
     * Creates the views
     */
    fun setupViews() {
        config = config
        Log.e(FloatingBubbleService::class.java.name, " Method === " + "Called")
        Log.e(FloatingBubbleService::class.java.name, " config === " + config.toString())
        val padding: Int = dpToPixels(config!!.paddingDp)
        val iconSize: Int = dpToPixels(config!!.bubbleIconDp)
        val bottomMargin: Int = getExpandableViewBottomMargin()
        // config.getUserName();
        // Setting up view
        bubbleView = inflater?.inflate(R.layout.user_layout, null)
        //  ImageView imageView = (ImageView) bubbleView.findViewById(R.id.IV_layout);
        val userName = bubbleView!!.findViewById<View>(R.id.tv_userName) as TextView
        val userNumber = bubbleView!!.findViewById<View>(R.id.tv_userNumber) as TextView
        userName.setText(config!!.userID)
        Log.e("config.getUserName()", " ===" + config!!.userID)

        // imageView.setBackground(getDrawable(R.drawable.ic_download));
        removeBubbleView = inflater?.inflate(R.layout.floating_remove_bubble_view, null)
        expandableView = inflater?.inflate(R.layout.floating_expandable_view, null)

        // Setting up the Remove Bubble View setup
        removeBubbleParams = getDefaultWindowParams()
        removeBubbleParams!!.gravity = Gravity.TOP or Gravity.START
        removeBubbleParams!!.width = dpToPixels(config!!.removeBubbleIconDp)
        removeBubbleParams!!.height = dpToPixels(config!!.removeBubbleIconDp)
        removeBubbleParams!!.x = (windowSize.x - removeBubbleParams!!.width) / 2
        removeBubbleParams!!.y = windowSize.y - removeBubbleParams!!.height - bottomMargin
        removeBubbleView!!.visibility = View.GONE
        removeBubbleView!!.alpha = config!!.removeBubbleAlpha
        windowManager!!.addView(removeBubbleView, removeBubbleParams)

        // Setting up the Expandable View setup
        expandableParams = getDefaultWindowParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        expandableParams!!.height = windowSize.y - iconSize - bottomMargin
        expandableParams!!.gravity = Gravity.TOP or Gravity.START
        expandableView!!.visibility = View.GONE
        (expandableView as LinearLayout?)!!.gravity = config!!.gravity
        expandableView!!.setPadding(padding, padding, padding, padding)
        windowManager!!.addView(expandableView, expandableParams)

        // Setting up the Floating Bubble View
        bubbleParams = getDefaultWindowParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        bubbleParams!!.gravity = Gravity.TOP or Gravity.START
        // bubbleParams.width = iconSize;
        bubbleParams!!.height = iconSize
        windowManager!!.addView(bubbleView, bubbleParams)

        // Setting the configuration
        if (config!!.removeBubbleIcon != null) {
            (removeBubbleView as ImageView?)!!.setImageDrawable(config!!.removeBubbleIcon)
        }
        if (config!!.bubbleIcon != null) {
            (bubbleView as ImageView?)!!.setImageDrawable(config!!.bubbleIcon)
        }

        //    CardView card = (CardView) expandableView.findViewById(R.id.expandableViewCard);
        // card.setRadius(dpToPixels(config.getBorderRadiusDp()));
//
//        ImageView triangle = (ImageView) expandableView.findViewById(R.id.expandableViewTriangle);
//        LinearLayout container = (LinearLayout) expandableView.findViewById(R.id.expandableViewContainer);
//        if (config.getExpandableView() != null) {
//            triangle.setColorFilter(config.getTriangleColor());
//            ViewGroup.MarginLayoutParams params =
//                    (ViewGroup.MarginLayoutParams) triangle.getLayoutParams();
//            params.leftMargin = dpToPixels((config.getBubbleIconDp() - 16) / 2);
//            params.rightMargin = dpToPixels((config.getBubbleIconDp() - 16) / 2);
//
//            triangle.setVisibility(View.VISIBLE);
//            container.setVisibility(View.VISIBLE);
//          //  card.setVisibility(View.VISIBLE);
//
//            container.setBackgroundColor(config.getExpandableColor());
//            container.removeAllViews();
//            container.addView(config.getExpandableView());
//        } else {
//            triangle.setVisibility(View.GONE);
//            container.setVisibility(View.GONE);
//           // card.setVisibility(View.GONE);
//        }
    }

    /**
     * Get the Bubble config
     *
     * @return the config
     */

//
//    var config: FloatingBubbleConfig? = null


    protected open var config: FloatingBubbleConfig? =null
        get() {
            return FloatingBubbleConfig.getDefault(getContext())
        }

    /**
     * Sets the touch listener
     */
    fun setTouchListener() {
        physics = FloatingBubblePhysics.Builder()
            .sizeX(windowSize.x)
            .sizeY(windowSize.y)
            .bubbleView(bubbleView)
            .config(config)
            .windowManager(windowManager)
            .build()
        touch = FloatingBubbleTouch.Builder()
            .sizeX(windowSize.x)
            .sizeY(windowSize.y)
            .listener(getTouchListener())
            .physics(physics)
            .bubbleView(bubbleView)
            .removeBubbleSize(dpToPixels(config?.removeBubbleIconDp!!))
            .windowManager(windowManager)
            .expandableView(expandableView)
            .removeBubbleView(removeBubbleView)
            .config(config)
            .marginBottom(getExpandableViewBottomMargin())
            .padding(dpToPixels(config!!.paddingDp))
            .build()
        bubbleView!!.setOnClickListener {
            println("Click")
            bubbleView!!.visibility = View.GONE
            removeBubbleView!!.visibility = View.GONE

            //   Bitmap bitmap_hiddenview = ScreenShott.getInstance().takeScreenShotOfJustView(v);
            // logger.log("Bitmap Image "+bitmap_hiddenview.toString());
            //takeScreenshot();
        }
        // bubbleView.setOnTouchListener(touch);
    }


    /**
     * Gets the touch listener for the bubble
     *
     * @return the touch listener
     */
    fun getTouchListener(): FloatingBubbleTouchListener? {
        return object : DefaultFloatingBubbleTouchListener() {
            override fun onRemove() {
                stopSelf()
            }
        }
    }


    /**
     * Get the default window layout params
     *
     * @return the layout param
     */
    fun getDefaultWindowParams(): WindowManager.LayoutParams? {
        return getDefaultWindowParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Get the default window layout params
     *
     * @return the layout param
     */
    fun getDefaultWindowParams(width: Int, height: Int): WindowManager.LayoutParams? {
        return WindowManager.LayoutParams(
            width,
            height,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
    }

    /**
     * Handles the intent for the service (only if it is not null)
     *
     * @param intent the intent
     */
    fun onGetIntent(intent: Intent): Boolean {
        return true
    }

    /**
     * Get the layout inflater for view inflation
     *
     * @return the layout inflater
     */
    @JvmName("getInflater1")
    fun getInflater(): LayoutInflater? {
        return if (inflater == null) setLayoutInflater() else inflater
    }

    /**
     * Get the context for the service
     *
     * @return the context
     */
    fun getContext(): Context {
        return getApplicationContext()
    }

    /**
     * Sets the state of the expanded view
     *
     * @param expanded the expanded view state
     */
    fun setState(expanded: Boolean) {
        touch?.setState(expanded)
    }

    /**
     * Get the expandable view's bottom margin
     *
     * @return margin
     */
    open fun getExpandableViewBottomMargin(): Int {
        val resources = getContext().resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        var navBarHeight = 0
        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return navBarHeight
    }

    /**
     * Converts DPs to Pixel values
     *
     * @return the pixel value
     */
    open fun dpToPixels(dpSize: Int): Int {
        val displayMetrics: DisplayMetrics = getResources().getDisplayMetrics()
        return Math.round((dpSize * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat())
    }


}
