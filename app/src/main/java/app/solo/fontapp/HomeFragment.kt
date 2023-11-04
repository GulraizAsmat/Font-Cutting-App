package app.solo.fontapp

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.system.Os.read
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.solo.fontapp.Const.fontType
import app.solo.fontapp.adapter.FontAdapter
import app.solo.fontapp.adapter.MonoFontAdapter
import app.solo.fontapp.databinding.FragmentHomeBinding
import app.solo.fontapp.listeners.AdapterListener
import app.solo.fontapp.models.FontData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset


class HomeFragment : Fragment(), AdapterListener, View.OnClickListener {
    lateinit var interstitialAd: InterstitialAd
    lateinit var adRequest: AdRequest
    private lateinit var binding: FragmentHomeBinding
    var fontList = ArrayList<FontData>()
    var displayList = ArrayList<FontData>()
    var searchStart = false
    var selectedFontType = true // Font=true , MonoFont=false

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
    private lateinit var editor: Editor

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
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        adManager()

    }

    @SuppressLint("MissingPermission")
    private fun adManager() {
        MobileAds.initialize(requireContext())

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

    @SuppressLint("MissingPermission")
    private fun interstitialAdManager() {
        interstitialAd = InterstitialAd(requireContext())
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        interstitialAd.loadAd(adRequest)

        interstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                // on below line we are calling display
                // ad function to display interstitial ad.
                displayInterstitialAd(interstitialAd)
            }
        })
    }

    private fun displayInterstitialAd(interstitialAd: InterstitialAd) {
        // on below line we are
        // checking if the ad is loaded
        if (interstitialAd.isLoaded) {
            // if the ad is loaded we are displaying
            // interstitial ad by calling show method.
            interstitialAd.show()
        }
    }


    private fun init() {
        sharedPreferences = requireContext().getSharedPreferences("FONT_APP", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        getFavourites()

        fontData()
        listeners()
        defaultState()
        recyclerViewManager()
        searchBar()
    }

    private fun getFavourites() {
        val gson = Gson()
        val jsonFont = sharedPreferences.getString("FONTLIST",null)
        val jsonMono = sharedPreferences.getString("MONOLIST",null)
        val type = object : TypeToken<ArrayList<FontData>>(){}.type

        if(jsonFont != null) {
            favouriteFontList = gson.fromJson(jsonFont, type)
        }
        if(jsonMono != null) {
            favouriteMonoList = gson.fromJson(jsonMono, type)
        }
    }

    private fun addToFavourites(position: Int, typeStr: String) {
        if(typeStr == FONTS) {

            if(displayList[position].selected) {
                //delete from list
                displayList[position].selected = false
                val iterator = favouriteFontList.iterator()
                while(iterator.hasNext()) {
                    val item = iterator.next()
                    if(item.assetName == displayList[position].assetName) {
                        iterator.remove()
                    }
                }
                Toast.makeText(requireContext(), "Removed from Favourites", Toast.LENGTH_LONG).show()
            } else {
                displayList[position].selected = true
                favouriteFontList.add(displayList[position])
                Toast.makeText(requireContext(), "Added to Favourites", Toast.LENGTH_LONG).show()
            }

            val gson = Gson()
            val json = gson.toJson(favouriteFontList)
            editor.putString("FONTLIST", json)
            editor.commit()

        } else {
            if(displayList[position].selected) {
                //delete from list
                displayList[position].selected = false
                val iterator = favouriteMonoList.iterator()
                while(iterator.hasNext()) {
                    val item = iterator.next()
                    if(item.assetName == displayList[position].assetName) {
                        iterator.remove()
                    }
                }
                Toast.makeText(requireContext(), "Removed from Favourites", Toast.LENGTH_LONG).show()
            } else {
                displayList[position].selected = true
                favouriteMonoList.add(displayList[position])
                Toast.makeText(requireContext(), "Added to Favourites", Toast.LENGTH_LONG).show()
            }

            val gson = Gson()
            val json = gson.toJson(favouriteMonoList)
            editor.putString("MONOLIST", json)
            editor.commit()
        }
    }


    fun defaultState() {
        if (selectedFontType) {
            fontData()
        } else {
            monoFontData()
        }

    }

    private fun fontData() {
        fontList.clear()
        fontType = true
        fontList.add(FontData("Roboto", false, "roboto_regular.ttf"))
        fontList.add(FontData("CocoBiker Regular Trial", false, "coco.ttf"))
        fontList.add(FontData("gladifilthefte", false, "glad.ttf"))
        fontList.add(FontData("Body-Grotesque-Slim-Bold", false, "body_grotesque.ttf"))
        fontList.add(FontData("3M Trislan", false, "trislan.ttf"))
        fontList.add(FontData("AgendaKing", false, "agendaking.ttf"))
        fontList.add(FontData("AntipastoPro-Medium", false, "atipastopromedium.ttf"))
        fontList.add(FontData("Ballerina", false, "ballerina.ttf"))
        fontList.add(FontData("Cafe Francoise", false, "cafe_francoise.ttf"))
        fontList.add(FontData("Beatrix Signature", false, "beatrix.ttf"))
        fontList.add(FontData("Astaghfirulloh", false, "astaghfirulloh.ttf"))
        fontList.add(FontData("Buljirya", false, "buljirya.ttf"))
        fontList.add(FontData("Black Note", false, "blacknote.ttf"))
        fontList.add(FontData("Candy Qelling", false, "candy_qelling.ttf"))
        fontList.add(FontData("Catterpillar", false, "catterpillar.ttf"))
        fontList.add(FontData("Elegano Display", false, "eleganodisplay.ttf"))
        fontList.add(FontData("Godlike-PersonalUse", false, "godlike_personal_use.ttf"))
        fontList.add(FontData("Heal the World", false, "heal_the_world.ttf"))
        fontList.add(FontData("Honuzima Regular", false, "honuzimaregular.ttf"))
        fontList.add(FontData("Lovely Girl", false, "lovely_girl.ttf"))
        fontList.add(FontData("Midway", false, "midway.ttf"))
        fontList.add(FontData("Milchella", false, "milchella.ttf"))
        fontList.add(FontData("Nozomi", false, "nozomi.ttf"))

        favouriteFontList.forEach { fav ->
            fontList.forEach {
                if(fav.assetName == it.assetName) {
                    it.selected = true
                }
            }
        }

        displayList.clear()
        displayList.addAll(fontList)
        fontAdapter.notifyDataSetChanged()

    }

    private fun monoFontData() {
        fontType = false
        fontList.clear()
        fontList.add(FontData("Adore Free", false, "adorefree.ttf"))
        fontList.add(FontData("Honey Bear", false, "honeybear.ttf"))
        fontList.add(FontData("KEYZHA", false, "keyzha.ttf"))
        fontList.add(FontData("Margarethy", false, "margarethy.ttf"))
        fontList.add(FontData("TendrilsDemo", false, "tendrils.ttf"))
        fontList.add(FontData("Yumeira Monogram", false, "yumeira_monogram.ttf"))
        fontList.add(FontData("Mady Risaw", false, "mady_risaw.ttf"))
        fontList.add(FontData("Sun Flower", false, "sun_flower.ttf"))
        fontList.add(FontData("CoffeeMocca", false, "coffeemocca.ttf"))
        fontList.add(FontData("Spring Break", false, "spring.ttf"))
        fontList.add(FontData("Ziviliam Monogram", false, "ziviliammonogram.ttf"))

        favouriteMonoList.forEach { fav ->
            fontList.forEach {
                if(fav.assetName == it.assetName) {
                    it.selected = true
                }
            }
        }

        displayList.clear()
        displayList.addAll(fontList)
        fontAdapter.notifyDataSetChanged()

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


    private fun listeners() {
        binding.clSelector1.setOnClickListener(this)
        binding.clSelector2.setOnClickListener(this)
        binding.clFavourites.setOnClickListener(this)
        binding.searchIcon.setOnClickListener(this)
    }


    private fun simpleFont() {
        binding.clFontBar.setBackgroundResource(R.color.color_black_shade_1)
        binding.clMonogramFontBar.setBackgroundResource(R.color.white)
        selectedFontType = true
        fontData()
        recyclerViewManager()
    }

    private fun monoFont() {
        selectedFontType = false
        binding.clFontBar.setBackgroundResource(R.color.white)
        binding.clMonogramFontBar.setBackgroundResource(R.color.color_black_shade_1)
        monoFontData()
        recyclerViewManager()

    }

    private fun filterData(query: String) {
        val filteredData = fontList.filter { it.name.contains(query, ignoreCase = true) }
        displayList.clear()
        displayList.addAll(filteredData)
        fontAdapter.notifyDataSetChanged()
    }

    private fun searchBar() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                Log.e("Tag213", "Search " + s)
                binding.searchIcon.setImageResource(R.drawable.cancel_icon)
                searchStart = true
                filterData(s.toString())
            }
        })
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

                R.id.cl_favourites -> {
                    findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
                }

                R.id.search_icon -> {
                    if (searchStart) {
                        binding.search.setText("")
                        binding.searchIcon.setImageResource(R.drawable.ic_search)
                        displayList.clear()
                        displayList.addAll(fontList)
                        fontAdapter.notifyDataSetChanged()

                    } else {

                    }
                }
            }
        }
    }

    private fun saveToDevice(fileName: String, dir: String) {
        if (!isPermissionGranted()) {
            requestPermission(WRITE_EXTERNAL_STORAGE, 10000)
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
            ActivityCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    override fun onAdapterItemClicked(key: String, position: Int) {
        when (key) {
            "font_selected" -> {
                addToFavourites(position, FONTS)

                fontAdapter.notifyDataSetChanged()
            }

            "mono_selected" -> {
                addToFavourites(position, MONO)

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
                Const.fontName = fontList[position].name
                Const.fontName = fontList[position].name
                val intent = Intent(requireContext(), TextEditorActivity::class.java)
                startActivity(intent)
            }
        }
    }


}