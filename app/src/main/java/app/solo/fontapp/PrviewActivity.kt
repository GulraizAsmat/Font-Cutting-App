package app.solo.fontapp

import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import app.solo.fontapp.videoEditor.fragments.PropertiesBSFragment
import app.solo.fontapp.videoEditor.fragments.StickerBSFragment
import app.solo.fontapp.videoEditor.fragments.TextEditorDialogFragment
import com.employee.videoeditor.adapters.photoeditor.OnPhotoEditorListener
import com.employee.videoeditor.adapters.photoeditor.PhotoEditor
import com.employee.videoeditor.adapters.photoeditor.TextStyleBuilder
import com.employee.videoeditor.adapters.photoeditor.ViewType
import yuku.ambilwarna.AmbilWarnaDialog

class PrviewActivity : AppCompatActivity(), OnClickListener, OnPhotoEditorListener,
    PropertiesBSFragment.Properties,

    StickerBSFragment.StickerListener {
    private lateinit var binding: app.solo.fontapp.databinding.ActivityPreviewBinding

    var textSizeOpenStatus = false
    private var propertiesBSFragment: PropertiesBSFragment? = null
    private var mStickerBSFragment: StickerBSFragment? = null


    private var mDefaultColor = 0
    var colorCodeTemp = 0
    var tempPos = 0
    var inputTypeTemp = ""
    private var textAllign = 0
    var textSize: Int = 46
    var changeTextStatus = false
    var bgStatus = false
    var bgColor = 0

    private var mPhotoEditor: PhotoEditor? = null
    lateinit var fontTypeTemp: Typeface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = app.solo.fontapp.databinding.ActivityPreviewBinding.inflate(layoutInflater)

        // getting our root layout in our view.

        // getting our root layout in our view.
        val view: View = binding.root

        // below line is to set
        // Content view for our layout.

        // below line is to set
        // Content view for our layout.
        setContentView(view)
        init()
    }


    private fun initViews() {

//        fFmpeg = FFmpeg.getInstance(this)
//        progressDialog = ProgressDialog(this)
        mStickerBSFragment = StickerBSFragment()
        mStickerBSFragment!!.setStickerListener(this)
        propertiesBSFragment = PropertiesBSFragment()
        propertiesBSFragment!!.setPropertiesChangeListener(this)
        mPhotoEditor = PhotoEditor.Builder(this, binding.ivImage)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            .setDeleteView(binding.imgDelete) //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk
        mPhotoEditor!!.setOnPhotoEditorListener(this)
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
        openTextView()

    }


    fun openTextView() {
        val textEditorDialogFragment: TextEditorDialogFragment =
            TextEditorDialogFragment.show(this, 0)




        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode, position ->

            if (inputText == "onCancel--001") {
                Log.e("TAG123", " Call Data Empty satya")
                binding.viewData.visibility = View.GONE
//                val styleBuilder =
//                    TextStyleBuilder()
//                styleBuilder.withTextColor(colorCode)
//                val typeface = ResourcesCompat.getFont(this@PrviewActivity,
//                    TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[position])
//                styleBuilder.withTextFont(typeface!!)
//                mPhotoEditor!!.editText(rootView!!, "", styleBuilder, position)
            } else {
//            upload_bar_group.visibility = View.GONE

                binding.viewData.visibility = View.VISIBLE

                colorCodeTemp = colorCode
                inputTypeTemp = inputText

                val styleBuilder =
                    TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                val typeface: Typeface =
                    ResourcesCompat.getFont(
                        this@PrviewActivity,
                        TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[position]
                    )!!
                styleBuilder.withTextFont(typeface)

                fontTypeTemp = typeface
                styleBuilder.withTextFont(fontTypeTemp)


                mPhotoEditor!!.addText(
                    inputTypeTemp,
                    styleBuilder,
                    position,
                    changeTextStatus,
                    textSize,
                    bgStatus,
                    bgColor,
                    textAllign
                )


            }


        }
    }

    private fun init() {

        listeners()
        seekBar()
//        textView()
        initViews()
    }

    private fun listeners() {
        binding.colorPicker.setOnClickListener(this)
        binding.clBgTextColor.setOnClickListener(this)
        binding.bacKBtn.setOnClickListener(this)
        binding.textSize.setOnClickListener(this)
        binding.capitalAndSmall.setOnClickListener(this)
        binding.downloadBtn.setOnClickListener(this)


    }

    private fun colorPicker() {

        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        val colorPickerDialogue =
            AmbilWarnaDialog(this, mDefaultColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // leave this function body as
                    // blank, as the dialog
                    // automatically closes when
                    // clicked on cancel button
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    // change the mDefaultColor to
                    // change the GFG text color as
                    // it is returned when the OK
                    // button is clicked from the
                    // color picker dialog
                    mDefaultColor = color
                    Log.e("Tag12", "Color Picker $mDefaultColor")
                    changeTextColor(tempPos, mDefaultColor)
                    // now change the picked color
                    // preview box to mDefaultColor
//                    mColorPreview.setBackgroundColor(mDefaultColor)
                }
            })
        colorPickerDialogue.show()
    }

    fun changeTextColor(position: Int, color: Int) {
        mPhotoEditor!!.clearBrushAllViews()
        mPhotoEditor!!.undo()
        colorCodeTemp = color
        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCodeTemp)
        val typeface = ResourcesCompat.getFont(
            this@PrviewActivity,
            TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[position]
        )

        fontTypeTemp = typeface!!
        styleBuilder.withTextFont(fontTypeTemp)

        mPhotoEditor!!.addText(
            inputTypeTemp,
            styleBuilder,
            0,
            changeTextStatus,
            textSize,
            bgStatus,
            bgColor,
            textAllign
        )
    }

    private fun seekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Handle progress changes here
                Log.e("Tag12", "onProgressChanged $progress")


//                text_1.textSize = progress.toFloat();

                textSize = progress
                mPhotoEditor!!.clearBrushAllViews()
                mPhotoEditor!!.undo()
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCodeTemp)
                val typeface = ResourcesCompat.getFont(
                    this@PrviewActivity,
                    TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[tempPos]
                )
                changeTextStatus = true
                fontTypeTemp = typeface!!
                styleBuilder.withTextFont(fontTypeTemp)
                mPhotoEditor!!.addText(
                    inputTypeTemp,
                    styleBuilder,
                    0,
                    changeTextStatus,
                    textSize,
                    bgStatus,
                    bgColor,
                    textAllign
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Called when the user starts interacting with the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Called when the user stops interacting with the SeekBar
            }
        })
    }


    private fun textView() {

        val textEditorDialogFragment: TextEditorDialogFragment =
            TextEditorDialogFragment.show(this, 0)



        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode, position ->

            Log.e("TAG123", " inputText " + inputText)
            if (inputText == "onCancel--001") {
                Log.e("TAG123", " Call Data Empty satya")
//                    done_bar_group!!.visibility = View.GONE
//                    upload_bar_group.visibility = View.VISIBLE
//                    imgText!!.visibility = View.VISIBLE
//                    range_seekbar!!.visibility = View.GONE
//                    cl_fonts!!.visibility = View.GONE
            } else {
//                    upload_bar_group.visibility = View.GONE
//                    skipBtnLayout!!.visibility = View.VISIBLE
//                    cl_fonts!!.visibility = View.VISIBLE
//                    done_bar_group!!.visibility = View.VISIBLE
////                    seekBar!!.visibility = View.VISIBLE
//                    range_seekbar!!.visibility = View.VISIBLE
//                    imgText!!.visibility = View.GONE


                colorCodeTemp = colorCode
                inputTypeTemp = inputText

                val styleBuilder =
                    TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                val typeface: Typeface =
                    ResourcesCompat.getFont(
                        this@PrviewActivity,
                        TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[position]
                    )!!
                styleBuilder.withTextFont(typeface)

                fontTypeTemp = typeface
                styleBuilder.withTextFont(fontTypeTemp)


                mPhotoEditor!!.addText(
                    inputTypeTemp,
                    styleBuilder,
                    position,
                    changeTextStatus,
                    textSize,
                    bgStatus,
                    bgColor,
                    textAllign
                )


            }


        }
    }

    private fun withoutBackgroundColor() {

        binding.unselectedColorPicker.visibility = View.VISIBLE
        binding.selectedColorPicker.visibility = View.GONE
        mPhotoEditor!!.clearBrushAllViews()
        mPhotoEditor!!.undo()

        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCodeTemp)
        val typeface = ResourcesCompat.getFont(
            this@PrviewActivity,
            TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[tempPos]
        )
        bgStatus = false
        bgColor
        fontTypeTemp = typeface!!
        styleBuilder.withTextFont(fontTypeTemp)

        mPhotoEditor!!.addText(
            inputTypeTemp,
            styleBuilder,
            0,
            changeTextStatus,
            textSize,
            bgStatus,
            bgColor,
            textAllign
        )
    }

    private fun setBackgroundColor() {


        val colorPickerDialogue = AmbilWarnaDialog(this, mDefaultColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {

                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    binding.unselectedColorPicker.visibility = View.GONE
                    binding.selectedColorPicker.visibility = View.VISIBLE
                    mDefaultColor = color
                    Log.e("Tag12", "Color Picker $mDefaultColor")
                    changeBackgroundColor(tempPos, mDefaultColor)

                }
            })
        colorPickerDialogue.show()
    }

    private fun changeBackgroundColor(position: Int, color: Int) {
        mPhotoEditor!!.clearBrushAllViews()
        mPhotoEditor!!.undo()

        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCodeTemp)
        val typeface = ResourcesCompat.getFont(
            this@PrviewActivity,
            TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[position]
        )
        bgStatus = true
        bgColor = color
        fontTypeTemp = typeface!!
        styleBuilder.withTextFont(fontTypeTemp)

        mPhotoEditor!!.addText(
            inputTypeTemp,
            styleBuilder,
            0,
            changeTextStatus,
            textSize,
            bgStatus,
            bgColor,
            textAllign
        )

    }


    override fun onColorChanged(colorCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onOpacityChanged(opacity: Int) {
        TODO("Not yet implemented")
    }

    override fun onBrushSizeChanged(brushSize: Int) {
        TODO("Not yet implemented")
    }

    override fun onStickerClick(bitmap: Bitmap?) {
        TODO("Not yet implemented")
    }

    override fun onEditTextChangeListener(
        rootView: View?,
        text: String?,
        colorCode: Int,
        pos: Int
    ) {

        val textEditorDialogFragment = TextEditorDialogFragment.show(
            this,
            text!!, colorCode, pos
        )
        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode, position ->

            if (text == "onCancel--001") {
                Log.e("TAG123", "onEditTextChangeListener Empty satya")
                binding.viewData.visibility = View.VISIBLE
                val styleBuilder =
                    TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                val typeface = ResourcesCompat.getFont(
                    this@PrviewActivity,
                    TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[pos]
                )
                styleBuilder.withTextFont(typeface!!)
                mPhotoEditor!!.editText(rootView!!, "", styleBuilder, pos)
            } else {
//            upload_bar_group.visibility = View.GONE

                binding.viewData.visibility = View.VISIBLE

                colorCodeTemp = colorCode
                inputTypeTemp = text.toString()

                val styleBuilder =
                    TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                val typeface = ResourcesCompat.getFont(
                    this@PrviewActivity,
                    TextEditorDialogFragment.getDefaultFontIds(this@PrviewActivity)[pos]
                )
                styleBuilder.withTextFont(typeface!!)
                mPhotoEditor!!.editText(rootView!!, text, styleBuilder, pos)


            }
        }
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

    }

    override fun onStartViewChangeListener(viewType: ViewType?) {

    }

    override fun onStopViewChangeListener(viewType: ViewType?) {

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.color_picker -> {
                binding.clSeekbar.visibility = View.GONE
                colorPicker()
            }
            R.id.cl_bg_text_color -> {
                binding.clSeekbar.visibility = View.GONE
                if (bgStatus) {

                    withoutBackgroundColor()
                } else {

                    setBackgroundColor()
                }
            }
            R.id.bacK_btn -> {
                onBackPressed()
            }
            R.id.text_size -> {
                if (textSizeOpenStatus) {
                    textSizeOpenStatus = false
                    binding.clSeekbar.visibility = View.GONE
                } else {
                    textSizeOpenStatus = true
                    binding.clSeekbar.visibility = View.VISIBLE
                }
            }
            R.id.capital_and_small -> {
                Toast.makeText(
                    this@PrviewActivity,
                    "Text Capital and Small Under Development",
                    Toast.LENGTH_LONG
                ).show()
            }
            R.id.download_btn -> {
                Toast.makeText(
                    this@PrviewActivity,
                    "Under Development",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}