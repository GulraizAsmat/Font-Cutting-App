package app.solo.fontapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.solo.fontapp.adapter.FontAdapter
import app.solo.fontapp.adapter.MatchAdapter
import app.solo.fontapp.adapter.MonoFontAdapter
import app.solo.fontapp.databinding.ActivityTextEditorBinding
import app.solo.fontapp.listeners.AdapterListener
import app.solo.fontapp.models.ColorData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class TextEditorActivity : AppCompatActivity(),AdapterListener {
    var TAG = "TextEditorActivity"
    private val REQUEST_PERMISSION_CODE = 101
    lateinit var adRequest: AdRequest
    private var offsetX = 0f
    private var offsetY = 0f
    private lateinit var binding: ActivityTextEditorBinding
    private var isDragging = false
    private var selectedType=true // draw =true ,cut =false
    private var data = false


        var colorList=ArrayList<ColorData>()

    private val matchAdapter by lazy {
        MatchAdapter(
            this,
            this,
            colorList
        )
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        adManager()
        init()

        listeners()
        outlineText()
        seekBar()

    }

    private fun  colorRecyclerView(){
        binding.rvColors.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvColors.itemAnimator = DefaultItemAnimator()
        binding.rvColors.adapter=matchAdapter
    }



    private fun capitalizeWords(input: String): String {
        val words = input.split(" ") // Split the input into words
        val capitalizedWords = words.map { it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        } } // Capitalize each word
        return capitalizedWords.joinToString(" ") // Join the words back together with spaces
    }

    @RequiresApi(Build.VERSION_CODES.O)


    fun defaultData(){
    binding.screenTitle.text=    Const.fontName
        Log.e(" ","Const.fontType"+Const.fontType)
        if(Const.fontType){

            selectedFont()
            binding.outlineTextView.text=Const.fontName
            binding.outlineTextView.typeface = selectedFont()
        }
        else {


            selectedMonoFont()
            binding.textView.text=Const.fontName
            binding.textView.typeface =  resources.getFont(R.font.adorefree)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectedFont() :Typeface{
        when(Const.fontPos){
            0->{
                val customFont = resources.getFont(R.font.roboto_regular)

             return customFont

            }
            1->{
                val customFont = resources.getFont(R.font.coco)

                   return customFont
            }
            2->{
                val customFont = resources.getFont(R.font.glad)

                   return customFont
            }
            3->{
                val customFont = resources.getFont(R.font.body_grotesque)

                   return customFont
            }
            4->{
                val customFont = resources.getFont(R.font.trislan)

                   return customFont
            }
            5->{
                val customFont = resources.getFont(R.font.agendaking)

                   return customFont
            }
            6->{
                val customFont = resources.getFont(R.font.atipastopromedium)

                   return customFont
            }
            7->{
                val customFont = resources.getFont(R.font.ballerina)

                   return customFont
            }
            8->{
                val customFont = resources.getFont(R.font.cafe_francoise)

                   return customFont
            }
            9->{
                val customFont = resources.getFont(R.font.beatrix)

                   return customFont
            }
            10->{
                val customFont = resources.getFont(R.font.astaghfirulloh)

                   return customFont
            }
            11->{
                val customFont = resources.getFont(R.font.buljirya)

                   return customFont
            }
            12->{
                val customFont = resources.getFont(R.font.blacknote)

                   return customFont
            }
            13->{
                val customFont = resources.getFont(R.font.candy_qelling)

                   return customFont
            }
            14->{
                val customFont = resources.getFont(R.font.catterpillar)

                   return customFont
            }
            15->{
                val customFont = resources.getFont(R.font.eleganodisplay)

                   return customFont
            }
            16->{
                val customFont = resources.getFont(R.font.godlike_personal_use)

                   return customFont
            }
            17->{
                val customFont = resources.getFont(R.font.heal_the_world)

                   return customFont
            }
            18->{
                val customFont = resources.getFont(R.font.honuzimaregular)

                   return customFont
            }
            19->{
                val customFont = resources.getFont(R.font.lovely_girl)

                   return customFont
            }
            20->{
                val customFont = resources.getFont(R.font.midway)

                   return customFont
            }
            21->{
                val customFont = resources.getFont(R.font.milchella)

                   return customFont
            }
            22->{
                val customFont = resources.getFont(R.font.nozomi)

                   return customFont
            }
        }

        return  resources.getFont(R.font.roboto_regular)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun selectedMonoFont( ):Typeface {


        when (Const.fontPos) {
            0 -> {
                val customFont = resources.getFont(R.font.adorefree)

                return customFont

            }
            1 -> {
                val customFont = resources.getFont(R.font.honeybear)

                return customFont
            }
            2 -> {
                val customFont = resources.getFont(R.font.keyzha)

                return customFont
            }
            3 -> {
                val customFont = resources.getFont(R.font.margarethy)

                return customFont
            }
            4 -> {
                val customFont = resources.getFont(R.font.tendrils)

                return customFont
            }
            5 -> {
                val customFont = resources.getFont(R.font.yumeira_monogram)

                return customFont
            }
            6 -> {
                val customFont = resources.getFont(R.font.mady_risaw)

                return customFont
            }
            7 -> {
                val customFont = resources.getFont(R.font.sun_flower)

                return customFont
            }
            8 -> {
                val customFont = resources.getFont(R.font.coffeemocca)

                return customFont
            }
            8 -> {
                val customFont = resources.getFont(R.font.spring)

                return customFont
            }
            9 -> {
                val customFont = resources.getFont(R.font.ziviliammonogram)

                return customFont
            }


        }
        return   resources.getFont(R.font.adorefree)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun listeners() {

        binding.drawBtn.setOnClickListener {


            binding.drawBtn.setBackgroundResource(R.drawable.bg_green_btn)
            binding.cut.setBackgroundResource(R.drawable.bg_white_btn)

            binding.drawBtn.setTextColor(getResources().getColor(R.color.white));
            binding.cut.setTextColor(getResources().getColor(R.color.black));

            selectedType=true
            binding.textView.visibility = View.GONE
            binding.outlineTextView.text=Const.fontName
            binding.outlineTextView.visibility = View.VISIBLE


            binding.outlineTextView.typeface = selectedFont()

        }




        binding.cut.setOnClickListener {
            selectedType=false
            binding.drawBtn.setBackgroundResource(R.drawable.bg_white_btn)
            binding.cut.setBackgroundResource(R.drawable.bg_green_btn)

            binding.drawBtn.setTextColor(getResources().getColor(R.color.black));
            binding.cut.setTextColor(getResources().getColor(R.color.white));


            binding.textView.typeface =  selectedFont()
            binding.textView.text=Const.fontName

            cutFont()



        }

        binding.bacKBtn.setOnClickListener {
            onBackPressed()
        }



        binding.clDefault.setOnClickListener(View.OnClickListener {

            binding.clDefault.setBackgroundResource(com.employee.videoeditor.R.color.orange_color_picker)
            binding.clUpperCase.setBackgroundResource(com.employee.videoeditor.R.color.white)
            binding.clLowerCase.setBackgroundResource(com.employee.videoeditor.R.color.white)

            binding.upper.setTextColor(getResources().getColor(R.color.black));
            binding.default1.setTextColor(getResources().getColor(R.color.white));
            binding.lower.setTextColor(getResources().getColor(R.color.black));



            if(selectedType){
                binding.outlineTextView.text =
                    capitalizeWords(binding.outlineTextView.text.toString()
                        .lowercase(Locale.getDefault()))
            }else {
                binding.textView.text =
                    capitalizeWords(binding.outlineTextView.text.toString()
                        .lowercase(Locale.getDefault()))
            }



        })

        binding.clUpperCase.setOnClickListener {

            binding.clDefault.setBackgroundResource(com.employee.videoeditor.R.color.white)
            binding.clUpperCase.setBackgroundResource(com.employee.videoeditor.R.color.orange_color_picker)
            binding.clLowerCase.setBackgroundResource(com.employee.videoeditor.R.color.white)

            binding.upper.setTextColor(getResources().getColor(R.color.white));
            binding.default1.setTextColor(getResources().getColor(R.color.black));
            binding.lower.setTextColor(getResources().getColor(R.color.black));

            if(selectedType){
                binding.outlineTextView.text = binding.outlineTextView.text.toString()
                    .uppercase(Locale.getDefault())
            }else {
                binding.textView.text = binding.outlineTextView.text.toString()
                    .uppercase(Locale.getDefault())
            }

        }


        binding.clLowerCase.setOnClickListener {

            binding.clDefault.setBackgroundResource(com.employee.videoeditor.R.color.white)
            binding.clUpperCase.setBackgroundResource(com.employee.videoeditor.R.color.white)
            binding.clLowerCase.setBackgroundResource(com.employee.videoeditor.R.color.orange_color_picker)

            binding.upper.setTextColor(getResources().getColor(R.color.black));
            binding.default1.setTextColor(getResources().getColor(R.color.black));
            binding.lower.setTextColor(getResources().getColor(R.color.white));
            if(selectedType){
                binding.outlineTextView.text = binding.outlineTextView.text.toString()
                    .lowercase(Locale.getDefault())
            }else {
                binding.textView.text = binding.outlineTextView.text.toString()
                    .lowercase(Locale.getDefault())
            }



        }
        binding.colorPicker.setOnClickListener {
            colorPicker()

        }

        binding.downloadFontBtn.setOnClickListener {

                        val constraintLayoutBitmap = captureConstraintLayoutAsBitmap()
//             Do something with the captured bitmap

            binding.imageView.setImageBitmap(constraintLayoutBitmap)
            saveBitmapImage(constraintLayoutBitmap!!)

        }


    }
    @SuppressLint("MissingPermission")
    private fun adManager(){
        MobileAds.initialize(this@TextEditorActivity)

        // on below line we are initializing
        // our ad view with its id


        // on below line we are
        // initializing our ad request.
        adRequest = AdRequest.Builder().build()

        // on below line we are loading our
        // ad view with the ad request
        binding.adView.loadAd(adRequest)
//        interstitialAdManager()


    }
    fun cutFont() {



        binding.textView.visibility = View.VISIBLE
        binding.outlineTextView.visibility = View.GONE
        binding.textView.toggleOutline()

        binding.textView.setOutlineWidth(TypedValue.COMPLEX_UNIT_PX, 3f)
        binding.textView.setOutlineColor(R.color.white)
    }

    private fun seekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Handle progress changes here
                Log.e("Tag12", "onProgressChanged $progress")


                binding.outlineTextView.textSize = progress.toFloat()
                binding.textView.textSize = progress.toFloat()


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Called when the user starts interacting with the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Called when the user stops interacting with the SeekBar
            }
        })
    }


    private fun outlineText() {
        binding.textView.setOutlineWidth(TypedValue.COMPLEX_UNIT_PX, 0f)
        binding.textView.setOutlineColor(R.color.white)
    }


    private fun captureConstraintLayoutAsBitmap(): Bitmap? {


        // Measure and layout the ConstraintLayout
        binding.outlineTextView.measure(
            View.MeasureSpec.makeMeasureSpec(
                binding.outlineTextView.width,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                binding.outlineTextView.height,
                View.MeasureSpec.EXACTLY
            )
        )
        binding.outlineTextView.layout(
            0,
            0,
            binding.outlineTextView.measuredWidth,
            binding.outlineTextView.measuredHeight
        )

        // Create a Bitmap and a Canvas to draw the layout onto
        val bitmap =
            Bitmap.createBitmap(
                binding.outlineTextView.width,
                binding.outlineTextView.height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)

        // Draw the layout onto the Canvas
        binding.outlineTextView.draw(canvas)
        return bitmap
    }


    /**Save Bitmap To Gallery
     * @param bitmap The bitmap to be saved in Storage/Gallery*/
    private fun saveBitmapImage(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "FOntAPp")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e(TAG, "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(uri, values, null, null)

                    Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder =
                File(Environment.getExternalStorageDirectory().toString() + '/' + "FOntAPp")
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "saveBitmapImage: ", e)
            }
        }
    }


    private fun init() {
        colorRecyclerView()
        colorList()
        defaultData()
        draggableTextView()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun draggableTextView() {


        val draggableTextView = binding.outlineTextView



        draggableTextView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDragging = true
                    offsetX = event.rawX - view.x
                    offsetY = event.rawY - view.y
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        view.x = event.rawX - offsetX
                        view.y = event.rawY - offsetY

                        if (view.x < 0) {
                            view.x = 0f
                        } else if (view.x > binding.clMainTextLayer.width - view.width) {
                            view.x = (binding.clMainTextLayer.width - view.width).toFloat()
                        }
                        if (view.y < 0) {
                            view.y = 0f
                        } else if (view.y > binding.clMainTextLayer.height - view.height) {
                            view.y = (binding.clMainTextLayer.height - view.height).toFloat()
                        }

                        // Toggle the visibility of viewBlock based on the TextView's position
//                        if (view.x == 0f) {
//                            binding.viewBlock.visibility = View.VISIBLE
//                        } else {
//                            binding.viewBlock.visibility = View.GONE
//                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isDragging = false
                }
            }
            true
        }
    }



    private fun colorList(){


        colorList.add(ColorData("black","0xFF000000",R.color.color_black))
        colorList.add(ColorData("color_green_shade_1","0xFF3C8D7D",R.color.color_green_shade_1))
        colorList.add(ColorData("color_purple_shade_1","0xFF01A368",R.color.color_purple_shade_1))
        colorList.add(ColorData("color_purple_shade_2","0xFF898B9C",R.color.color_purple_shade_2))
        colorList.add(ColorData("color_blue_shade_2","0xFF347AE2",R.color.color_blue_shade_2))
        colorList.add(ColorData("color_orange_shade_2","0xFFFD7B02",R.color.color_orange_shade_2))
        matchAdapter.notifyDataSetChanged()
    }
    override fun onAdapterItemClicked(key: String, position: Int) {

        if(selectedType){

            binding.outlineTextView.setTextColor(getResources().getColor(colorList[position].color));


        }else {


            if (colorList[position].name == "black") {
                binding.textView.setNewColor(0xFF000000.toInt())
            } else if (colorList[position].name == "color_green_shade_1") {
                binding.textView.setNewColor(0xFF3C8D7D.toInt())
            } else if (colorList[position].name == "color_purple_shade_1") {
                binding.textView.setNewColor(0xFF01A368.toInt())
            } else if (colorList[position].name == "color_purple_shade_2") {
                binding.textView.setNewColor(0xFF898B9C.toInt())
            } else if (colorList[position].name == "color_orange_shade_2") {
                binding.textView.setNewColor(0xFFFD7B02.toInt())
            } else if (colorList[position].name == "color_blue_shade_2") {
                binding.textView.setNewColor(0xFF347AE2.toInt())
            }


            cutFont()
        }

    }


    private fun colorPicker() {

        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        val colorPickerDialogue =
            AmbilWarnaDialog(this, 0, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // leave this function body as
                    // blank, as the dialog
                    // automatically closes when
                    // clicked on cancel button
                    -3931646
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    // change the mDefaultColor to
                    // change the GFG text color as
                    // it is returned when the OK
                    // button is clicked from the
                    // color picker dialog

                    Log.e("Tag12", "Color Picker $color")
                    if(  selectedType){
                        val colorValue = color // Your original color value
                        val alpha = (colorValue shr 24) and 0xFF
                        val red = (colorValue shr 16) and 0xFF
                        val green = (colorValue shr 8) and 0xFF
                        val blue = colorValue and 0xFF

                        val hexColor = String.format("#%02X%02X%02X%02X", alpha, red, green, blue)

                        binding.outlineTextView.setTextColor(Color.parseColor(hexColor))
//                        binding.outlineTextView.setTextColor(getResources().getColor(color));
                    }else {
                        binding.textView.setNewColor(color)
                        cutFont()
                    }

//                    changeTextColor(tempPos, mDefaultColor)
                    // now change the picked color
                    // preview box to mDefaultColor
//                    mColorPreview.setBackgroundColor(mDefaultColor)
                }
            })
        colorPickerDialogue.show()
    }


}

class OutlineTextView : AppCompatTextView {
    private val defaultOutlineWidth = 0F
    private var isDrawing: Boolean = false
    private var outlineColor: Int = 0
    private var outlineWidth: Float = 0.toFloat()
    var setNewColor=0xFF3C8D7D.toInt()
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
            outlineColor = a.getColor(R.styleable.OutlineTextView_outlineColor, currentTextColor)
            outlineWidth =
                a.getDimension(R.styleable.OutlineTextView_outlineWidth, defaultOutlineWidth)
            a.recycle()
        } else {
            outlineColor = currentTextColor
            outlineWidth = defaultOutlineWidth
        }
        setOutlineWidth(TypedValue.COMPLEX_UNIT_PX, outlineWidth)
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
    }

    fun setOutlineWidth(unit: Int, width: Float) {
        outlineWidth = TypedValue.applyDimension(
            unit, width, context.resources.displayMetrics
        )
    }

    private var showOutline: Boolean = true
    fun toggleOutline() {
//        showOutline = !showOutline
        invalidate()
    }

    fun setNewColor(color: Int) {
        setNewColor = color
    }

    override fun onDraw(canvas: Canvas) {

        if (showOutline) {
            showOutline = false
            Log.e("Tag213", "outlineWidth:: $outlineColor")
            isDrawing = true
            paint.style = Paint.Style.FILL
            super.onDraw(canvas)
            val currentTextColor = outlineColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = outlineWidth
            setTextColor(0xFF3C8D7D.toInt())
            super.onDraw(canvas)
            setTextColor(0xFF3C8D7D.toInt())
            super.onDraw(canvas)
//        isDrawing = true

        } else {
setNewColor
            Log.e("Tag213", "else:: " + setNewColor)
            Log.e("Tag213", "currentTextColor:: " + currentTextColor)

            setTextColor(setNewColor) // Set the text color to white

//            val text = "Hello Buddy"
//
//            canvas.drawText(text, x, y, paint)
            super.onDraw(canvas)
        }

    }

}
