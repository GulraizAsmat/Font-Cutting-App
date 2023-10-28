//package com.employee.employeejobbie.activites.videoEditor
//
//
//import android.annotation.SuppressLint
//import android.app.ProgressDialog
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.SurfaceTexture
//import android.graphics.Typeface
//import android.media.AudioManager
//import android.media.MediaMetadataRetriever
//import android.media.MediaPlayer
//import android.media.ThumbnailUtils
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.os.Handler
//import android.os.Looper
//import android.provider.MediaStore
//import android.util.DisplayMetrics
//import android.util.Log
//import android.view.Surface
//import android.view.TextureView
//import android.view.View
//import android.webkit.MimeTypeMap
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.content.res.ResourcesCompat
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.DefaultItemAnimator
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.employee.employeejobbie.R
//import com.employee.employeejobbie.activites.MainActivity
//import com.employee.employeejobbie.activites.ResumeUploadActivity
//import com.employee.employeejobbie.activites.videoCapture.PortraitVideoActivity
//import com.employee.employeejobbie.activites.videoEditor.fragments.PropertiesBSFragment
//import com.employee.employeejobbie.activites.videoEditor.fragments.StickerBSFragment
//import com.employee.employeejobbie.activites.videoEditor.fragments.TextEditorDialogFragment
//import app.solo.fontapp.videoEditor.model.FontStyle
//import com.employee.employeejobbie.helper.*
//import com.employee.employeejobbie.listeners.AdapterListener
//import com.employee.employeejobbie.listeners.DialogBoxListener
//import com.employee.employeejobbie.models.user.Intro
//import com.employee.employeejobbie.models.videoUpload.VideoUploadResponse
//import com.employee.employeejobbie.network.NetworkResult
//import com.employee.employeejobbie.viewmodel.BuildProfileViewModel
//import com.employee.employeejobbie.viewmodel.ResumeViewModel
//import com.employee.videoeditor.adapters.photoeditor.*
//import com.employee.videoeditor.adapters.utils.DimensionData
//import com.employee.videoeditor.adapters.utils.Utils.getScaledDimension
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg
//import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler
//import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
//import com.mohammedalaa.seekbar.OnRangeSeekBarChangeListener
//import com.mohammedalaa.seekbar.RangeSeekBarView
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_preview.*
//import okhttp3.MediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import yuku.ambilwarna.AmbilWarnaDialog
//import java.io.*
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//@AndroidEntryPoint
//class PreviewVideoActivity : AppCompatActivity(),
//    OnPhotoEditorListener,
//    PropertiesBSFragment.Properties,
//    View.OnClickListener,
//    StickerBSFragment.StickerListener, AdapterListener, DialogBoxListener {
//
//    private var textAllign = 0
//    private var mPhotoEditor: PhotoEditor? = null
//    private val globalVideoUrl = ""
//    private var propertiesBSFragment: PropertiesBSFragment? = null
//    private var mStickerBSFragment: StickerBSFragment? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private var videoPath: String? = ""
//    private var imagePath = ""
//    private var exeCmd: ArrayList<String?>? = null
//    var fFmpeg: FFmpeg? = null
//    private lateinit var newCommand: Array<String?>
//    private var progressDialog: ProgressDialog? = null
//    private var originalDisplayWidth = 0
//    private var originalDisplayHeight = 0
//    private var newCanvasWidth = 0
//    private var newCanvasHeight = 0
//    private var DRAW_CANVASW = 0
//    private var DRAW_CANVASH = 0
//
//    private var videoHeight = 0f
//    private var videoWidth = 0f
//
//
//    var buttonsClickable = true
//    var colorCodeTemp = 0
//    var bgStatus = false
//    var bgColor = 0
//    lateinit var fontTypeTemp: Typeface
//    var inputTypeTemp = ""
//    var tempPos = 0
//    var textSize: Int = 46
//    var changeTextStatus = false
//    private var mDefaultColor = 0
//
//    private var videoEditingToolUse = false
//    var videoUploadCalling = false
//    var compressingVideoComplete = false
//
//
//
//
//
//    var fontList = ArrayList<FontStyle>()
//
//    private val fontAdapter by lazy {
//        FontAdapter(this,
//            this,
//            fontList)
//    }
//
//    var fileToUpload1 = ""
//    var thumbnail1 = ""
//    var fileName1 = ""
//
//
//    private val resumeViewModel by viewModels<ResumeViewModel>()
//    private val buildProfileViewModel by viewModels<BuildProfileViewModel>()
//
//
//    private val onCompletionListener =
//        MediaPlayer.OnCompletionListener { mediaPlayer -> mediaPlayer.start() }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preview)
//
//
//        Glide.with(this).load(R.drawable.trans).centerCrop()
//            .into(ivImage!!.source)
//        videoPath = intent.getStringExtra("DATA")
//        val retriever = MediaMetadataRetriever()
//
//
//
//        retriever.setDataSource(videoPath)
//        val metaRotation =
//            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
//        val rotation = metaRotation?.toInt() ?: 0
//        if (rotation == 90 || rotation == 270) {
//            DRAW_CANVASH =
//                Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
//            DRAW_CANVASW =
//                Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
//        } else {
//            DRAW_CANVASW =
//                Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
//            DRAW_CANVASH =
//                Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
//        }
//        setCanvasAspectRatio()
//        videoSurface!!.layoutParams.width = newCanvasWidth
//        videoSurface!!.layoutParams.height = newCanvasHeight
//        ivImage!!.layoutParams.width = newCanvasWidth
//        ivImage!!.layoutParams.height = newCanvasHeight
//
//
//        Logger.e("fff",
//            "width>> " + newCanvasWidth + "height>> " + newCanvasHeight + " rotation >> " + rotation)
//
//
//        if (newCanvasHeight < 1000) {
//            videoWidth = 1280.0F
//            videoHeight = 720.0F
//
//
//        } else {
//            videoWidth = 720.0F
//            videoHeight = 1280.0F
//
//
//        }
//
//
//
//        initViews()
//        fontRvHandler()
//        fontList()
//        textChangeSize()
//        videoCompressor()
//        viewModelManager()
//    }
//
//
//    private fun fontRvHandler() {
//        rv_fonts!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rv_fonts!!.itemAnimator = DefaultItemAnimator()
//        rv_fonts!!.adapter = fontAdapter
//
//    }
//
//    private fun viewModelManager() {
//        resumeViewModel.videoUploadResponseLiveData.observe(this, videoUploadObserver)
//    }
//
//    private fun setCanvasAspectRatio() {
//        originalDisplayHeight = getDisplayHeight()
//        originalDisplayWidth = getDisplayWidth()
//        val displayDiamenion: DimensionData = getScaledDimension(
//            DimensionData(DRAW_CANVASW,
//                DRAW_CANVASH),
//            DimensionData(
//                originalDisplayWidth,
//                originalDisplayHeight))
//        newCanvasWidth = displayDiamenion.width
//        newCanvasHeight = displayDiamenion.height
//    }
//
//    private fun getDisplayWidth(): Int {
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.widthPixels
//    }
//
//    private fun getDisplayHeight(): Int {
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.heightPixels
//    }
//
//
//    private fun initViews() {
//
//        fFmpeg = FFmpeg.getInstance(this)
//        progressDialog = ProgressDialog(this)
//        mStickerBSFragment = StickerBSFragment()
//        mStickerBSFragment!!.setStickerListener(this)
//        propertiesBSFragment = PropertiesBSFragment()
//        propertiesBSFragment!!.setPropertiesChangeListener(this)
//        mPhotoEditor = PhotoEditor.Builder(this, ivImage)
//            .setPinchTextScalable(true) // set flag to make text scalable when pinch
//            .setDeleteView(imgDelete) //.setDefaultTextTypeface(mTextRobotoTf)
//            //.setDefaultEmojiTypeface(mEmojiTypeFace)
//            .build() // build photo editor sdk
//        mPhotoEditor!!.setOnPhotoEditorListener(this)
//        imgClose.setOnClickListener(this)
//        imgDone.setOnClickListener(this)
//        imgDraw.setOnClickListener(this)
//        imgText.setOnClickListener(this)
//        imgUndo.setOnClickListener(this)
//        imgSticker.setOnClickListener(this)
//        cl_done!!.setOnClickListener(this)
//        ivbtnUpload.setOnClickListener(this)
//        color_picker!!.setOnClickListener(this)
//        text_justify!!.setOnClickListener(this)
//        cl_bg_text_color!!.setOnClickListener(this)
//        bacK_btn!!.setOnClickListener(this)
//        ivbtnRetake!!.setOnClickListener(this)
//
//        videoSurface!!.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
//            override fun onSurfaceTextureAvailable(
//                surfaceTexture: SurfaceTexture,
//                i: Int,
//                i1: Int,
//            ) {
////                activityHomeBinding.videoSurface.getLayoutParams().height=640;
////                activityHomeBinding.videoSurface.getLayoutParams().width=720;
//                val surface = Surface(surfaceTexture)
//                try {
//                    mediaPlayer = MediaPlayer()
//                    //                    mediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");
//                    Log.d("VideoPath>>", videoPath!!)
//                    mediaPlayer!!.setDataSource(videoPath)
//                    mediaPlayer!!.setSurface(surface)
//                    mediaPlayer!!.prepare()
//                    mediaPlayer!!.setOnCompletionListener(onCompletionListener)
//                    mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                    mediaPlayer!!.start()
//
//
//
//
//                    Logger.e("video_durration", "time :: " + mediaPlayer!!.duration)
//                    updateProgress(mediaPlayer!!.duration.toLong())
//
//                } catch (e: IllegalArgumentException) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace()
//                } catch (e: SecurityException) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace()
//                } catch (e: IllegalStateException) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace()
//                } catch (e: IOException) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onSurfaceTextureSizeChanged(
//                surfaceTexture: SurfaceTexture,
//                i: Int,
//                i1: Int,
//            ) {
//            }
//
//            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
//                return false
//            }
//
//            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
//        }
//        exeCmd = java.util.ArrayList()
//        try {
//            fFmpeg!!.loadBinary(object : FFmpegLoadBinaryResponseHandler {
//                override fun onFailure() {
//                    Log.d("binaryLoad", "onFailure")
//                }
//
//                override fun onSuccess() {
//                    Log.d("binaryLoad", "onSuccess")
//                }
//
//                override fun onStart() {
//                    Log.d("binaryLoad", "onStart")
//                }
//
//                override fun onFinish() {
//                    Log.d("binaryLoad", "onFinish")
//                }
//            })
//        } catch (e: FFmpegNotSupportedException) {
//            e.printStackTrace()
//        }
//    }
//
//
//    private fun updateProgress(videoDuration: Long) {
//
//        var timeLeft = videoDuration
//
//        Logger.e("Time", " timeLeft $timeLeft")
//
//        val totalTime = timeLeft + 1000
//
//        Logger.e("Time", " totalTime $totalTime")
//
//
//        if (totalTime > 0) {
//
//            if (totalTime > 61000) {
//                tvTimer.text = "01:00"
//            } else {
//                tvTimer.text = "0" + calculateTimeLeft(totalTime)
//            }
//
//
//        }
//    }
//
//    private fun calculateTimeLeft(timeLeft: Long): String {
//        return String.format(
//            "%d:%02d",
//            TimeUnit.MILLISECONDS.toMinutes(timeLeft) % TimeUnit.HOURS.toMinutes(1),
//            TimeUnit.MILLISECONDS.toSeconds(timeLeft) % TimeUnit.MINUTES.toSeconds(1)
//        )
//    }
//
//
//    private fun setDrawingMode() {
//        if (mPhotoEditor!!.brushDrawableMode) {
//            mPhotoEditor!!.setBrushDrawingMode(false)
//            imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp))
//        } else {
//            mPhotoEditor!!.setBrushDrawingMode(true)
//            imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
//            propertiesBSFragment!!.show(supportFragmentManager, propertiesBSFragment!!.tag)
//        }
//    }
//
//    private fun fontList() {
//        fontList.clear()
//        fontList.add(FontStyle("Josefinsans", false))
//        fontList.add(FontStyle("Wonderland", false))
//        fontList.add(FontStyle("Cinzel", false))
//        fontList.add(FontStyle("Emojione", false))
//        fontList.add(FontStyle("Merriweather", false))
//        fontList.add(FontStyle("Raleway", false))
//        fontList.add(FontStyle("Roboto", false))
//
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun saveImage() {
//        val file = File(Environment.getExternalStorageDirectory()
//            .toString() + File.separator + "" + System.currentTimeMillis() + ".png")
//        try {
//            file.createNewFile()
//            val saveSettings =
//                SaveSettings.Builder().setClearViewsEnabled(true).setTransparencyEnabled(false)
//                    .build()
//            mPhotoEditor!!.saveAsFile(file.absolutePath, saveSettings, object :
//                PhotoEditor.OnSaveListener {
//                override fun onSuccess(imagePath: String) {
//                    this@PreviewVideoActivity.imagePath = imagePath
//                    Log.d("imagePath>>", imagePath)
//                    Log.d("imagePath2>>", Uri.fromFile(File(imagePath)).toString())
//                    ivImage!!.source.setImageURI(Uri.fromFile(File(imagePath)))
////                    Toast.makeText(this@PreviewVideoActivity,
////                        "Saved successfully...",
////                        Toast.LENGTH_SHORT).show()
//                    applayWaterMark()
//                }
//
//                override fun onFailure(exception: Exception) {
//                    Toast.makeText(this@PreviewVideoActivity,
//                        "Saving Failed...",
//                        Toast.LENGTH_SHORT).show()
//                }
//            })
//        } catch (e: IOException) {
//            e.printStackTrace()
//
//            Logger.e("Tag124", "Crash ...")
//            if (compressingVideoComplete) {
//                Logger.e("Video", "vIDOE aREADY COMPRESS jUST apI cALL")
//
//                withOutEditing()
//            }
//
//        }
//    }
//
//
//    private fun applayWaterMark() {
//
////        imagePath = generatePath(Uri.fromFile(new File(imagePath)),PreviewVideoActivity.this);
//
////        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////        retriever.setDataSource(videoPath);
////        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
////        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//        /*if (width > height) {
//            int tempWidth = width;
//            width = height;
//            height = tempWidth;
//        }*/
//
////        Log.d(">>", "width>> " + width + "height>> " + height);
////        retriever.release();
//        val output = File(Environment.getExternalStorageDirectory()
//            .toString() + File.separator + "" + System.currentTimeMillis() + ".mp4")
//
//
//        Log.e("compress_video", " water mark view path :: $videoPath")
//
//
//
//        try {
//            output.createNewFile()
//            exeCmd!!.add("-y")
//            exeCmd!!.add("-i")
//            exeCmd!!.add(videoPath)
//            //            exeCmd.add("-framerate 30000/1001 -loop 1");
//            exeCmd!!.add("-i")
//            exeCmd!!.add(imagePath)
//            exeCmd!!.add("-filter_complex")
//            //            exeCmd.add("-strict");
////            exeCmd.add("-2");
////            exeCmd.add("-map");
////            exeCmd.add("[1:v]scale=(iw+(iw/2)):(ih+(ih/2))[ovrl];[0:v][ovrl]overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2");
////            exeCmd.add("[1:v]scale=720:1280:1823[ovrl];[0:v][ovrl]overlay=x=0:y=0");
//            exeCmd!!.add("[1:v]scale=$DRAW_CANVASW:$DRAW_CANVASH[ovrl];[0:v][ovrl]overlay=x=0:y=0")
//            exeCmd!!.add("-c:v")
//            exeCmd!!.add("libx264")
//            exeCmd!!.add("-preset")
//            exeCmd!!.add("ultrafast")
//            exeCmd!!.add(output.absolutePath)
//            newCommand = arrayOfNulls(exeCmd!!.size)
//            for (j in exeCmd!!.indices) {
//                newCommand[j] = exeCmd!![j]
//            }
//            for (k in newCommand.indices) {
//                Log.d("CMD==>>", newCommand[k] + "")
//            }
//
////            newCommand = new String[]{"-i", videoPath, "-i", imagePath, "-preset", "ultrafast", "-filter_complex", "[1:v]scale=2*trunc(" + (width / 2) + "):2*trunc(" + (height/ 2) + ") [ovrl], [0:v][ovrl]overlay=0:0" , output.getAbsolutePath()};
//            executeCommand(newCommand, output.absolutePath)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//    fun executeCommand(command: Array<String?>?, absolutePath: String?) = try {
//        fFmpeg!!.execute(command, object : FFmpegExecuteResponseHandler {
//            override fun onSuccess(s: String) {
////                Log.("CommandExecute", "onSuccess  $"+absolutePath.toString())
////                Toast.makeText(applicationContext, "Sucess", Toast.LENGTH_SHORT).show()
//
//                uploadVideo(absolutePath.toString())
////                    val i = Intent(this@PreviewVideoActivity, VideoPreviewActivity::class.java)
////                    i.putExtra("DATA", absolutePath)
////                    startActivity(i)
//            }
//
//            override fun onProgress(s: String) {
//
//
//                Logger.e("TAG1214", "onProgress :: $s")
//
//
////                progressDialog!!.setMessage(s)
//                Log.d("CommandExecute", "onProgress  $s")
//            }
//
//            override fun onFailure(s: String) {
//                Log.d("CommandExecute", "onFailure  $s")
////                progressDialog!!.hide()
//            }
//
//            override fun onStart() {
////                progressDialog!!.setTitle("Preccesing")
////                progressDialog!!.setMessage("Starting")
////                progressDialog!!.show()
//            }
//
//            override fun onFinish() {
////                progressDialog!!.hide()
//            }
//        })
//    } catch (e: FFmpegCommandAlreadyRunningException) {
//        e.printStackTrace()
//    }
//
//    override fun onColorChanged(colorCode: Int) {
//        mPhotoEditor!!.brushColor = colorCode
//    }
//
//    override fun onOpacityChanged(opacity: Int) {
//
//    }
//
//    override fun onBrushSizeChanged(brushSize: Int) {
//
//    }
//
//    override fun onStickerClick(bitmap: Bitmap?) {
//        mPhotoEditor!!.setBrushDrawingMode(false)
//        imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp))
//        mPhotoEditor!!.addImage(bitmap)
//    }
//
//    override fun onEditTextChangeListener(
//        rootView: View?,
//        text: String?,
//        colorCode: Int,
//        pos: Int,
//    ) {
//        val textEditorDialogFragment = TextEditorDialogFragment.show(this,
//            text!!, colorCode, pos)
//        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode, position ->
//
//
//            Logger.e("TAG123", "inputText " + inputText)
//
//            if (inputText == "onCancel--001") {
//                Logger.e("TAG123", " Call Data Empty satya")
//                done_bar_group!!.visibility = View.GONE
//                upload_bar_group.visibility = View.VISIBLE
//                imgText!!.visibility = View.VISIBLE
//                range_seekbar!!.visibility = View.GONE
//                cl_fonts!!.visibility = View.GONE
//                val styleBuilder =
//                    TextStyleBuilder()
//                styleBuilder.withTextColor(colorCode)
//                val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//                    TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//                styleBuilder.withTextFont(typeface!!)
//                mPhotoEditor!!.editText(rootView!!, "", styleBuilder, position)
//            } else {
//                upload_bar_group.visibility = View.GONE
//                skipBtnLayout!!.visibility = View.VISIBLE
//                cl_fonts!!.visibility = View.VISIBLE
//                done_bar_group!!.visibility = View.VISIBLE
////                    seekBar!!.visibility = View.VISIBLE
//                range_seekbar!!.visibility = View.VISIBLE
//                imgText!!.visibility = View.GONE
//
//
//                colorCodeTemp = colorCode
//                inputTypeTemp = inputText
//
//                val styleBuilder =
//                    TextStyleBuilder()
//                styleBuilder.withTextColor(colorCode)
//                val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//                    TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//                styleBuilder.withTextFont(typeface!!)
//                mPhotoEditor!!.editText(rootView!!, inputText, styleBuilder, position)
//
//            }
//
//
////
////
////            Logger.e("TAF123", "inputText  $inputText")
////
//        }
//    }
//
//    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
//
//    }
//
//    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
//
//    }
//
//    override fun onStartViewChangeListener(viewType: ViewType?) {
//
//    }
//
//    override fun onStopViewChangeListener(viewType: ViewType?) {
//
//    }
//
//
//    private fun withoutBackgroundColor() {
//
//        unselected_color_picker.visibility = View.VISIBLE
//        selected_color_picker.visibility = View.GONE
//        mPhotoEditor!!.clearBrushAllViews()
//        mPhotoEditor!!.undo()
//
//        val styleBuilder = TextStyleBuilder()
//        styleBuilder.withTextColor(colorCodeTemp)
//        val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//            TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[tempPos])
//        bgStatus = false
//        bgColor
//        fontTypeTemp = typeface!!
//        styleBuilder.withTextFont(fontTypeTemp)
//
//        mPhotoEditor!!.addText(inputTypeTemp,
//            styleBuilder,
//            0,
//            changeTextStatus,
//            textSize,
//            bgStatus,
//            bgColor,
//            textAllign)
//    }
//
//
//    private fun colorPicker() {
//
//        // the AmbilWarnaDialog callback needs 3 parameters
//        // one is the context, second is default color,
//        val colorPickerDialogue =
//            AmbilWarnaDialog(this, mDefaultColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
//                override fun onCancel(dialog: AmbilWarnaDialog?) {
//                    // leave this function body as
//                    // blank, as the dialog
//                    // automatically closes when
//                    // clicked on cancel button
//                }
//
//                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
//                    // change the mDefaultColor to
//                    // change the GFG text color as
//                    // it is returned when the OK
//                    // button is clicked from the
//                    // color picker dialog
//                    mDefaultColor = color
//                    Log.e("Tag12", "Color Picker $mDefaultColor")
//                    changeTextColor(tempPos, mDefaultColor)
//                    // now change the picked color
//                    // preview box to mDefaultColor
////                    mColorPreview.setBackgroundColor(mDefaultColor)
//                }
//            })
//        colorPickerDialogue.show()
//    }
//
//
//    fun changeTextColor(position: Int, color: Int) {
//        mPhotoEditor!!.clearBrushAllViews()
//        mPhotoEditor!!.undo()
//        colorCodeTemp = color
//        val styleBuilder = TextStyleBuilder()
//        styleBuilder.withTextColor(colorCodeTemp)
//        val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//            TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//
//        fontTypeTemp = typeface!!
//        styleBuilder.withTextFont(fontTypeTemp)
//
//        mPhotoEditor!!.addText(inputTypeTemp,
//            styleBuilder,
//            0,
//            changeTextStatus,
//            textSize,
//            bgStatus,
//            bgColor,
//            textAllign)
//    }
//
//    private fun setBackgroundColor() {
//
//
//        val colorPickerDialogue = AmbilWarnaDialog(this, mDefaultColor,
//            object : AmbilWarnaDialog.OnAmbilWarnaListener {
//                override fun onCancel(dialog: AmbilWarnaDialog?) {
//
//                }
//
//                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
//                    unselected_color_picker.visibility = View.GONE
//                    selected_color_picker.visibility = View.VISIBLE
//                    mDefaultColor = color
//                    Log.e("Tag12", "Color Picker $mDefaultColor")
//                    changeBackgroundColor(tempPos, mDefaultColor)
//
//                }
//            })
//        colorPickerDialogue.show()
//    }
//
//
//    private fun changeBackgroundColor(position: Int, color: Int) {
//        mPhotoEditor!!.clearBrushAllViews()
//        mPhotoEditor!!.undo()
//
//        val styleBuilder = TextStyleBuilder()
//        styleBuilder.withTextColor(colorCodeTemp)
//        val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//            TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//        bgStatus = true
//        bgColor = color
//        fontTypeTemp = typeface!!
//        styleBuilder.withTextFont(fontTypeTemp)
//
//        mPhotoEditor!!.addText(inputTypeTemp,
//            styleBuilder,
//            0,
//            changeTextStatus,
//            textSize,
//            bgStatus,
//            bgColor,
//            textAllign)
//
//    }
//
//    private fun changeTextAlgin(position: Int) {
//        mPhotoEditor!!.clearBrushAllViews()
//        mPhotoEditor!!.undo()
//
//        val styleBuilder = TextStyleBuilder()
//        styleBuilder.withTextColor(colorCodeTemp)
//        val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//            TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//        bgStatus = true
//
//        fontTypeTemp = typeface!!
//        styleBuilder.withTextFont(fontTypeTemp)
//
//        mPhotoEditor!!.addText(inputTypeTemp,
//            styleBuilder,
//            0,
//            changeTextStatus,
//            textSize,
//            bgStatus,
//            bgColor,
//            textAllign
//        )
//
//    }
//
//
//    private fun changeFontFamily(position: Int) {
//        mPhotoEditor!!.clearBrushAllViews()
//        mPhotoEditor!!.undo()
//
//        val styleBuilder = TextStyleBuilder()
//        styleBuilder.withTextColor(colorCodeTemp)
//        val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//            TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])
//        changeTextStatus
//        fontTypeTemp = typeface!!
//        styleBuilder.withTextFont(fontTypeTemp)
//
//        mPhotoEditor!!.addText(inputTypeTemp,
//            styleBuilder,
//            0,
//            changeTextStatus,
//            textSize,
//            bgStatus,
//            bgColor,
//            textAllign)
//    }
//
//
//    private fun textChangeSize() {
//
//
//        range_seekbar.setOnRangeSeekBarViewChangeListener(object : OnRangeSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: RangeSeekBarView?,
//                progress: Int,
//                fromUser: Boolean,
//            ) {
//
//
//                Logger.e("Taf23", " progress bar $progress")
//
////                text_1.textSize = progress.toFloat();
//
//                textSize = progress
//                mPhotoEditor!!.clearBrushAllViews()
//                mPhotoEditor!!.undo()
//                val styleBuilder = TextStyleBuilder()
//                styleBuilder.withTextColor(colorCodeTemp)
//                val typeface = ResourcesCompat.getFont(this@PreviewVideoActivity,
//                    TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[tempPos])
//                changeTextStatus = true
//                fontTypeTemp = typeface!!
//                styleBuilder.withTextFont(fontTypeTemp)
//                mPhotoEditor!!.addText(inputTypeTemp,
//                    styleBuilder,
//                    0,
//                    changeTextStatus,
//                    textSize,
//                    bgStatus,
//                    bgColor,
//                    textAllign)
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: RangeSeekBarView?, progress: Int) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: RangeSeekBarView?, progress: Int) {
//
//            }
//
//
//        })
//
//
//    }
//
//
//    private fun videoCompressor() {
//        /**
//         * 1 is stand for video record from camera
//         * 2 is stand for video pick from gallery
//         * 0 is stand for empty
//         * */
//
//        val list = java.util.ArrayList<Uri>()
//
//        if (SharedPref.getInt(this, Constants.VIDEO_RECORD_BY_CAMERA, 0) == 1) {
//            Logger.e("Video",
//                "PATh  form camera : " + SharedPref.getString(this,
//                    Constants.VIDEO_FILE_URI,
//                    ""))
//
//
//            val uri = Uri.parse(SharedPref.getString(this, Constants.VIDEO_FILE_URI, ""))
//
//
//
//            Logger.e("Video",
//                "URi form camera : $uri")
//            list.add(uri)
//
//        } else if (SharedPref.getInt(this, Constants.VIDEO_RECORD_BY_CAMERA, 0) == 2) {
//            Logger.e("Video",
//                "URi form gallery : " + SharedPref.getString(this,
//                    Constants.VIDEO_FILE_URI,
//                    ""))
//            list.add(Uri.parse(SharedPref.getString(this, Constants.VIDEO_FILE_URI, "")))
//        }
//
//
//        VideoCompressorHelper.videoCompress(this,
//            this,
//            list,
//            videoHeight.toDouble(),
//            videoWidth.toDouble())
//
//    }
//
//    private var videoUploadObserver = Observer<NetworkResult<VideoUploadResponse>> { response ->
//        cl_upload_progress_bar.visibility = View.GONE
//        mediaPlayer!!.stop()
//        when (response) {
//            is NetworkResult.Success -> {
//                Logger.e("Resume", "Upload Video Sucessfull  " + response.data!!.data.id)
//                SharedPref.setString(this, Constants.VIDEO_FILE_PATH, videoPath)
//                video_upload_successful.visibility = View.VISIBLE
//                video_upload_text.text = getString(R.string.app_record_successfully)
//
//                SharedPref.setBoolean(this,
//                    Constants.VIDEO_SECTION_COMPLETED,
//                    true)
//
//                SharedPref.setInt(this, Constants.USER_SELECTED_VIDEO, response.data.data.id)
//
//                if (SharedPref.getInt(this,
//                        Constants.USER_SELECTED_VIDEO,
//                        -1)
//                    == -1
//                ) {
//                    Logger.e("TAGq12",
//                        " User Selected Video First Time  Store " + response.data.data.id)
//
//
//                    SharedPref.setInt(this, Constants.USER_SELECTED_VIDEO, response.data.data.id)
//                } else {
//                    Logger.e("TAGq12", " User Selected Video Already Store ")
//                    SharedPref.setInt(this, Constants.USER_SELECTED_VIDEO, response.data.data.id)
//                }
//
////                CustomView.dialogBox(this, this, getString(R.string.app_record_successfully), false)
//
//                Handler(Looper.getMainLooper()).postDelayed({
//                    if (Constants.openProfileScreen) {
//                        videoUploadDataLocallyStore(response)
//
//                    } else if (Constants.openVideoGalleryScreen) {
//                        Constants.openVideoGalleryToVideoUpload = true
//                        videoUploadDataLocallyStore(response)
//                    } else {
//
//                        Constants.INTRO_ID = response.data.data.id
//                        videoUploadDataLocallyStore(response)
//                        openReviewJobPost()
//                    }
//
//                    video_upload_successful.visibility = View.GONE
//                }, 2000)
//
//
//            }
//            is NetworkResult.Error -> {
//                Logger.e("Resume", "Upload Error")
//                CustomView.dialogBox(this, this, response.message.toString(), true)
//
//            }
//
//            is NetworkResult.Loading -> {
//
//                Logger.e("Resume", "Upload Loading")
//            }
//
//
//        }
//
//    }
//
//    private fun videoUploadDataLocallyStore(response: NetworkResult<VideoUploadResponse>) {
//        val result =
//            SharedPref.getString(this, Constants.USER_RESUME_VIDEO_DETAILS, "")
//        if (result.isNotEmpty()) {
//
//            val list = buildProfileViewModel.stringToDataArrayForVideo(result)
//            Logger.e("Resume", " List Video Size " + list!!.size)
//
//
//            list.add(Intro(
//                created_at = response.data!!.data.created_at,
//                file = response.data.data.file,
//                file_name = response.data.data.file_name,
//                file_type = response.data.data.file_type,
//                id = response.data.data.id,
//                status = response.data.data.status,
//                thumb = response.data.data.thumb,
//                thumbnail = response.data.data.thumbnail.toString(),
//                type = response.data.data.type.toString(),
//                updated_at = response.data.data.updated_at,
//                user_id = response.data.data.user_id,
//            ))
//
//
//            SharedPref.setString(this,
//                Constants.USER_RESUME_VIDEO_DETAILS,
//                buildProfileViewModel.dataArrayToStringForVideo(list))
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//
//
//        } else {
//            val list = java.util.ArrayList<Intro>()
//            list.add(Intro(
//                created_at = response.data!!.data.created_at,
//                file = response.data.data.file,
//                file_name = response.data.data.file_name,
//                file_type = response.data.data.file_type,
//                id = response.data.data.id,
//                status = response.data.data.status,
//                thumb = response.data.data.thumb,
//                thumbnail = response.data.data.thumbnail.toString(),
//                type = response.data.data.type.toString(),
//                updated_at = response.data.data.updated_at,
//                user_id = response.data.data.user_id,
//            ))
//
//
//            SharedPref.setString(this,
//                Constants.USER_RESUME_VIDEO_DETAILS,
//                buildProfileViewModel.dataArrayToStringForVideo(list))
//
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//
//    }
//
//
//    private fun openReviewJobPost(
//
//    ) {
//        Constants.openReviewScreen = false
//        Constants.videoPath = SharedPref.getString(this, Constants.VIDEO_FILE_PATH, "")
//        Constants.fileToUpload = fileToUpload1
//        Constants.thumbnail = thumbnail1
//        Constants.fileName = fileName1
//        Constants.firstTimeVideoRecord = true
//        val intent = Intent(this, ResumeUploadActivity::class.java)
//
//        startActivity(intent)
//
//    }
//
//
//    private fun uploadVideo(path: String) {
//
//
//        try {
//            val pdfname = Calendar.getInstance().timeInMillis.toString()
//
//            //Create a file object using file path
//
//            val uri = Uri.parse(path)
//
//            val file = File(getRealPathFromURI(uri)!!)
//
//
////            val file = File(path)
//            // Parsing any Media type file
//            val requestBody = RequestBody.create(MediaType.parse("*video/mp4*"), file)
//            var fileToUpload = MultipartBody.Part.createFormData("intro", file.name, requestBody)
//
//
//            MimeTypeMap.getFileExtensionFromUrl(file.toURL().toString())
//            var filename = RequestBody.create(MediaType.parse("video/mp4"), pdfname)
////            Cons.VideoUpload.fileToUpload = fileToUpload.toString()
////            Cons.VideoUpload.filename = filename.toString()
////            Cons.VideoUpload.path = path
//
//
//            val bitmap =
//                ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND)
//
//            //bitmap?.scale(350, 460, false)
//
//            val converetdImage: Bitmap = getResizedBitmap(bitmap!!, 500)!!
//
//            val out = ByteArrayOutputStream()
//
//            var compressedBitmap = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//
//            /*saveToInternalStorage(bitmap!!)*/
//
//            val imageOpts: BitmapFactory.Options = BitmapFactory.Options()
//            imageOpts.inSampleSize = 2 // for 1/2 the image to be loaded
//
//            /*val thumb =
//                    Bitmap.createScaledBitmap(BitmapFactory.decodeFile(Cons.VideoUpload.path, imageOpts), 360, 460, false)*/
//
//
//            val imagePath = getImageFilePath()
//            val uriImage = Uri.parse(imagePath)
//            val fileImage = File(getRealPathFromURI(uriImage))
//
//            val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/jpeg*"), fileImage)
//            val thumbnail = MultipartBody.Part.createFormData("thumbnail", fileImage.name, reqFile)
//
//
////            Cons.VideoUpload.video_thumbnail_path = imagePath!!
////            Cons.VideoUpload.video_thumbnail = imagePath!!
//            Logger.e("Tag1234",
//                "vedio path ${MimeTypeMap.getFileExtensionFromUrl(file.toURL().toString())}")
//            Logger.e("Tag1234", "thumbnail path ${fileImage.name}")
//
////
////
//            saveAsPngImage(converetdImage, imagePath)
//            exportPngToGallery(applicationContext, imagePath!!)
//
//            videoPath = path
//            fileToUpload1 = fileToUpload.toString()
//            fileName1 = filename.toString()
//            thumbnail1 = imagePath
//            if (com.employee.employeejobbie.helper.Utils.checkInternet(this, this)) {
//
//
//                if (videoEditingToolUse) {
//                    video_upload_msg.text = "Uploading..."
//                } else {
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        video_upload_msg.text = "Uploading..."
//                    }, 3000)
//                }
//
//
//                buttonsClickable = false
//                resumeViewModel.uploadVideoApi(fileToUpload, thumbnail)
//            }
//
////                        openReviewJobPost(path,fileToUpload.toString(), filename.toString(), imagePath.toString())
//        } catch (e: Exception) {
//            Logger.e("tag12", "crash $e")
//            e.printStackTrace()
//        }
//    }
//
//    private fun getRealPathFromURI(contentURI: Uri): String? {
//        val result: String?
//        val cursor: Cursor? = this.contentResolver.query(
//            contentURI,
//            null,
//            null,
//            null,
//            null
//        )
//        if (cursor == null) { //checking
//            result = contentURI.path
//        } else {
//            cursor.moveToFirst()
//            val idx: Int = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
//            result = cursor.getString(idx)
//            cursor.close()
//        }
//        return result
//    }
//
//    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
//        var width = image.width
//        var height = image.height
//        val bitmapRatio = width.toFloat() / height.toFloat()
//        if (bitmapRatio > 1) {
//            width = maxSize
//            height = (width / bitmapRatio).toInt()
//        } else {
//            height = maxSize
//            width = (height * bitmapRatio).toInt()
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true)
//    }
//
//
//    private fun exportPngToGallery(context: Context, filePath: String) {
//        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val f = File(filePath)
//        val contentUri = Uri.fromFile(f)
//        mediaScanIntent.data = contentUri
//        context.sendBroadcast(mediaScanIntent)
//    }
//
//
//    private fun saveAsPngImage(bitmap: Bitmap, filePath: String?) {
//        try {
//            val file = File(filePath)
//
//            val outStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
//            outStream.close()
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun getAndroidImagesFolder(): File? {
//        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//    }
//
//    private fun getImageFilePath(): String? {
//        return getAndroidImagesFolder()?.absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(
//            Date()) + "cameraRecorder.jpeg"
//    }
//
//    private fun uploadBarShow() {
//        done_bar_group!!.visibility = View.GONE
//        upload_bar_group!!.visibility = View.VISIBLE
//        imgText!!.visibility = View.VISIBLE
//        seekBar!!.visibility = View.GONE
//        range_seekbar.visibility = View.GONE
//        cl_fonts!!.visibility = View.GONE
//    }
//
//
//    private fun withOutEditing() {
//
//
//        Logger.e("videoUpload", " withOutEditing $videoPath")
//
//        uploadVideo(videoPath.toString())
//
//
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.imgClose -> onBackPressed()
//            R.id.ivbtnUpload -> {
//
//
//                if (buttonsClickable) {
//                    buttonsClickable = false
//                    videoUploadCalling = true
//                    cl_upload_progress_bar.visibility = View.VISIBLE
//
//                    if (videoUploadCalling) {
//
//                        Logger.e("Video", "Video Button Press ")
//
//
//                        if (videoEditingToolUse) {
//                            if (compressingVideoComplete) {
//                                Logger.e("Video", "vIDOE aREADY COMPRESS + SAVEiAMGE")
//                                saveImage()
//                            }
//
//                        } else {
//
//                            if (compressingVideoComplete) {
//                                Logger.e("Video", "vIDOE aREADY COMPRESS jUST apI cALL")
//                                saveImage()
////                                withOutEditing()
//                            }
////
//                        }
//
//
//                    }
//                }
//            }
//            R.id.imgDraw -> setDrawingMode()
//            R.id.bacK_btn -> {
//                uploadBarShow()
//            }
//            R.id.ivbtnRetake -> {
//
//                if (buttonsClickable) {
//
//                    val intent = Intent(this, PortraitVideoActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//
//
//            R.id.imgText -> {
//
//                if (buttonsClickable) {
//
//                    upload_bar_group!!.visibility = View.GONE
//                    videoEditingToolUse = true
//                    val textEditorDialogFragment: TextEditorDialogFragment =
//                        TextEditorDialogFragment.show(this, 0)
//
//
//
//                    textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode, position ->
//
//                        Logger.e("TAG123", " inputText " + inputText)
//                        if (inputText == "onCancel--001") {
//                            Logger.e("TAG123", " Call Data Empty satya")
//                            done_bar_group!!.visibility = View.GONE
//                            upload_bar_group.visibility = View.VISIBLE
//                            imgText!!.visibility = View.VISIBLE
//                            range_seekbar!!.visibility = View.GONE
//                            cl_fonts!!.visibility = View.GONE
//                        } else {
//                            upload_bar_group.visibility = View.GONE
//                            skipBtnLayout!!.visibility = View.VISIBLE
//                            cl_fonts!!.visibility = View.VISIBLE
//                            done_bar_group!!.visibility = View.VISIBLE
////                    seekBar!!.visibility = View.VISIBLE
//                            range_seekbar!!.visibility = View.VISIBLE
//                            imgText!!.visibility = View.GONE
//
//
//                            colorCodeTemp = colorCode
//                            inputTypeTemp = inputText
//
//                            val styleBuilder =
//                                TextStyleBuilder()
//                            styleBuilder.withTextColor(colorCode)
//                            val typeface: Typeface =
//                                ResourcesCompat.getFont(this@PreviewVideoActivity,
//                                    TextEditorDialogFragment.getDefaultFontIds(this@PreviewVideoActivity)[position])!!
//                            styleBuilder.withTextFont(typeface)
//
//                            fontTypeTemp = typeface
//                            styleBuilder.withTextFont(fontTypeTemp)
//
//
//                            mPhotoEditor!!.addText(inputTypeTemp,
//                                styleBuilder,
//                                position,
//                                changeTextStatus,
//                                textSize,
//                                bgStatus,
//                                bgColor,
//                                textAllign)
//
//
//                        }
//
//
//                    }
//                }
//            }
//            R.id.imgUndo -> {
//                Log.d("canvas>>", mPhotoEditor!!.undoCanvas().toString())
//                mPhotoEditor!!.clearBrushAllViews()
//            }
//            R.id.imgSticker -> mStickerBSFragment!!.show(supportFragmentManager,
//                mStickerBSFragment!!.tag)
//
//            R.id.cl_done -> {
//                uploadBarShow()
//            }
//            R.id.color_picker -> {
//                colorPicker()
//            }
//            R.id.cl_bg_text_color -> {
//                if (bgStatus) {
//
//                    withoutBackgroundColor()
//                } else {
//
//                    setBackgroundColor()
//                }
//            }
//
//            R.id.text_justify -> {
//
//                when (textAllign) {
//                    0 -> {
//                        textAllign = 1
//                        changeTextAlgin(tempPos)
//                    }
//                    1 -> {
//                        textAllign = 2
//                        changeTextAlgin(tempPos)
//                    }
//                    2 -> {
//                        textAllign = 0
//                        changeTextAlgin(tempPos)
//                    }
//                }
//
//            }
//
//
//        }
//    }
//
//    override fun onAdapterItemClicked(key: String, position: Int) {
//        tempPos = position
//        fontList.forEach {
//            it.fontSelected = false
//        }
//        fontList[position].fontSelected = true
//        fontAdapter.notifyDataSetChanged()
//        changeFontFamily(position)
//    }
//
//    override fun dialogBoxData(key: String, msg: String) {
//        when (key) {
//            "compress_video" -> {
//                Logger.e("Video", "PATh : $msg")
//
//                SharedPref.setString(this, Constants.VIDEO_FILE_PATH, msg)
////                uploadVideo(SharedPref.getString(this, Constants.VIDEO_FILE_PATH, ""))
//                videoPath = msg
//
//                Logger.e("Video", "videoUploadCalling $videoUploadCalling")
//                compressingVideoComplete = true
//                if (videoUploadCalling) {
//
//                    Logger.e("Video", "Already btn press + vd status$videoEditingToolUse")
////                    uploadVideo(videoPath!!)
//                    if (videoEditingToolUse) {
//                        if (compressingVideoComplete) {
//
//                            Logger.e("Video", "compressingVideoComplete $compressingVideoComplete")
//                            saveImage()
//                        }
//
//                    } else {
//
//                        if (compressingVideoComplete) {
//                            Logger.e("Video",
//                                "+compressingVideoComplete  With out Edting+ $compressingVideoComplete")
//                            saveImage()
////                            withOutEditing()
//                        }
////
//                    }
//
//
//                    videoUploadCalling = false
//                }
//
//
//            }
//            "export_percent_value" -> {
//                Logger.e("compress_video", "Percent value  : $msg")
//
//
//            }
//        }
//    }
//
//    override fun onBackPressed() {
////        super.onBackPressed()
//        val intent = Intent(this, PortraitVideoActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onPause() {
//
//        mediaPlayer!!.stop()
//        super.onPause()
//    }
//
//
//}