package app.solo.fontapp

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.solo.fontapp.adapter.FontAdapter
import app.solo.fontapp.adapter.MonoFontAdapter
import app.solo.fontapp.databinding.FragmentFavouriteBinding
import app.solo.fontapp.databinding.FragmentHomeBinding
import app.solo.fontapp.listeners.AdapterListener
import app.solo.fontapp.models.FontData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class FavouriteFragment : Fragment(), View.OnClickListener, AdapterListener {
    lateinit var adRequest: AdRequest
    private lateinit var binding: FragmentFavouriteBinding
    var selectedFontType = true // Font=true , MonoFont=false

    var fontList = ArrayList<FontData>()
    var displayList = ArrayList<FontData>()

    val FONTS = "Fonts"
    val MONO = "Monogram"

    private val fontAdapter by lazy {
        FontAdapter(
            requireContext(),
            this,
            displayList
        )
    }

    private val monoFontAdapter by lazy {
        MonoFontAdapter(
            requireContext(),
            this,
            displayList
        )
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    var favouriteFontList = ArrayList<FontData>()
    var favouriteMonoList = ArrayList<FontData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        adManager()
    }

    private fun init() {

        sharedPreferences = requireContext().getSharedPreferences("FONT_APP", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        getFavourites()
        listeners()
        defaultState()
        recyclerViewManager()


    }

    fun defaultState() {
        if (selectedFontType) {
            simpleFont()
        } else {
            monoFont()
        }

    }

    private fun getFavourites() {
        val gson = Gson()
        val jsonFont = sharedPreferences.getString("FONTLIST", null)
        val jsonMono = sharedPreferences.getString("MONOLIST", null)
        val type = object : TypeToken<ArrayList<FontData>>() {}.type

        if (jsonFont != null) {
            favouriteFontList = gson.fromJson(jsonFont, type)
        }
        if (jsonMono != null) {
            favouriteMonoList = gson.fromJson(jsonMono, type)
        }
    }

    private fun addToFavourites(position: Int, typeStr: String) {
        if (typeStr == FONTS) {


            //delete from list
            favouriteFontList.removeAt(position)
            Toast.makeText(requireContext(), "Removed from Favourites", Toast.LENGTH_LONG).show()

            val gson = Gson()
            val json = gson.toJson(favouriteFontList)
            editor.putString("FONTLIST", json)
            editor.commit()

            if(favouriteFontList.size > 0) {
                binding.groupEmpty.visibility = View.GONE
            } else {
                binding.groupEmpty.visibility = View.VISIBLE
            }

        } else {
            //delete from list
            favouriteMonoList.removeAt(position)
            Toast.makeText(requireContext(), "Removed from Favourites", Toast.LENGTH_LONG).show()

            val gson = Gson()
            val json = gson.toJson(favouriteMonoList)
            editor.putString("MONOLIST", json)
            editor.commit()

            if(favouriteMonoList.size > 0) {
                binding.groupEmpty.visibility = View.GONE
            } else {
                binding.groupEmpty.visibility = View.VISIBLE
            }
        }
    }

    fun adManager() {
        MobileAds.initialize(requireContext())

        // on below line we are initializing
        // our ad view with its id


        // on below line we are
        // initializing our ad request.
        adRequest = AdRequest.Builder().build()

        // on below line we are loading our
        // ad view with the ad request
        binding.adView.loadAd(adRequest)


    }


    private fun listeners() {
        binding.clSelector1.setOnClickListener(this)
        binding.clSelector2.setOnClickListener(this)
        binding.clFont.setOnClickListener(this)
        binding.exploreFont.setOnClickListener(this)
    }

    private fun simpleFont() {
        binding.clFontBar.setBackgroundResource(R.color.color_black_shade_1)
        binding.clMonogramFontBar.setBackgroundResource(R.color.white)
        selectedFontType = true

        fontList.clear()
        Const.fontType = true

        fontList.addAll(favouriteFontList)
        fontList.forEach {
            it.selected = true
        }

        displayList.clear()
        displayList.addAll(fontList)
        fontAdapter.notifyDataSetChanged()

        if(favouriteFontList.size > 0) {
            binding.groupEmpty.visibility = View.GONE
        } else {
            binding.groupEmpty.visibility = View.VISIBLE
        }

        recyclerViewManager()
    }

    private fun monoFont() {
        selectedFontType = false
        binding.clFontBar.setBackgroundResource(R.color.white)
        binding.clMonogramFontBar.setBackgroundResource(R.color.color_black_shade_1)

        fontList.clear()
        Const.fontType = true

        fontList.addAll(favouriteMonoList)
        fontList.forEach {
            it.selected = true
        }

        displayList.clear()
        displayList.addAll(fontList)
        monoFontAdapter.notifyDataSetChanged()

        if(favouriteMonoList.size > 0) {
            binding.groupEmpty.visibility = View.GONE
        } else {
            binding.groupEmpty.visibility = View.VISIBLE
        }

        recyclerViewManager()

    }

    private fun recyclerViewManager() {

        binding.rvFont.layoutManager =
            LinearLayoutManager(context)
        binding.rvFont.itemAnimator = DefaultItemAnimator()

        if (selectedFontType) {
            binding.rvFont.adapter = fontAdapter
        } else {
            binding.rvFont.adapter = monoFontAdapter
        }

    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.cl_selector_1 -> {
                    simpleFont()
                }

                R.id.cl_selector_2 -> {
                    monoFont()
                }

                R.id.cl_font -> {
                    findNavController().popBackStack()
                }

                R.id.explore_font -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onAdapterItemClicked(key: String, position: Int) {
        when (key) {
            "font_selected" -> {
                addToFavourites(position, FONTS)
                displayList.removeAt(position)

                fontAdapter.notifyDataSetChanged()
            }

            "mono_selected" -> {
                addToFavourites(position, MONO)
                displayList.removeAt(position)

                monoFontAdapter.notifyDataSetChanged()
            }

            "font_download" -> {
                saveToDevice(displayList[position].assetName, FONTS)
            }

            "mono_download" -> {
                saveToDevice(displayList[position].assetName, MONO)
            }

            "edit" -> {
                Const.fontPos = position
                Const.fontName = displayList[position].name
                Const.fontName = displayList[position].name
                val intent = Intent(requireContext(), TextEditorActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun saveToDevice(fileName: String, dir: String) {
        if (!isPermissionGranted()) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 10000)
            return
        }
        val assetManager = requireContext().assets as AssetManager

        var inStream: InputStream? = null
        var outStream: OutputStream? = null

        try {

            inStream = assetManager.open(fileName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = requireContext().contentResolver
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/x-font-ttf")
                values.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/" + getApplicationName(requireContext()) + "/$dir/"
                )
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
                uri?.let { it ->
                    resolver.openOutputStream(it).use {
                        // Write file
                        val buffer = byteArrayOf(1024.toByte())
                        var read: Int = inStream!!.read(buffer)
                        while (inStream!!.read(buffer).also { read = it } !== -1) {
                            it!!.write(buffer, 0, read)
                        }
//                    it?.write(someText.toByteArray(Charset.defaultCharset()))
                        it?.close()
                    }
                }
            } else {
                val outDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + getApplicationName(
                        requireContext()
                    ) + "/$dir/"

                val outFile = File(outDir, fileName)

                outStream = FileOutputStream(outFile)

                val buffer = byteArrayOf(1024.toByte())
                var read: Int = inStream.read(buffer)
                while (inStream.read(buffer).also { read = it } !== -1) {
                    outStream.write(buffer, 0, read)
                }
            }

            Log.e("HomeFrag", "File saved")
            Toast.makeText(requireContext(), "$fileName saved", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("HomeFrag", "Error: ", e)
        } finally {
            inStream?.close()
            inStream = null
            outStream?.flush()
            outStream?.close()
            outStream = null
        }
    }

    fun getApplicationName(context: Context): String? {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
    }

    private fun isPermissionGranted(): Boolean {
        val permissionCheck =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        if (this::sharedPreferences.isInitialized) {
            getFavourites()
        }
    }

}