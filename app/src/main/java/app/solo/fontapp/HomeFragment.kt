package app.solo.fontapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class HomeFragment : Fragment(), AdapterListener,View.OnClickListener {
    lateinit var interstitialAd: InterstitialAd
    lateinit var adRequest: AdRequest
    private lateinit var binding: FragmentHomeBinding
    var fontList=ArrayList<FontData>()
    var displayList=ArrayList<FontData>()
    var searchStart=false
    var selectedFontType=true // Font=true , MonoFont=false

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
    private fun adManager(){
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
    private fun interstitialAdManager(){
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


    private fun init(){
        fontData()
        listeners()
        defaultState()
        recyclerViewManager()
        searchBar()
    }


    fun defaultState(){
        if(selectedFontType){
            fontData()
        }else {
            monoFontData()
        }

    }
    private fun fontData(){
        fontList.clear()
        fontType=true
        fontList.add(FontData("Roboto"))
        fontList.add(FontData("CocoBiker Regular Trial"))
        fontList.add(FontData("gladifilthefte"))
        fontList.add(FontData("Body-Grotesque-Slim-Bold"))
        fontList.add(FontData("3M Trislan"))
        fontList.add(FontData("AgendaKing"))
        fontList.add(FontData("AntipastoPro-Medium"))
        fontList.add(FontData("Ballerina"))
        fontList.add(FontData("Cafe Francoise"))
        fontList.add(FontData("Beatrix Signature"))
        fontList.add(FontData("Astaghfirulloh"))
        fontList.add(FontData("Buljirya"))
        fontList.add(FontData("Black Note"))
        fontList.add(FontData("Candy Qelling"))
        fontList.add(FontData("Catterpillar"))
        fontList.add(FontData("Elegano Display"))
        fontList.add(FontData("Godlike-PersonalUse"))
        fontList.add(FontData("Heal the World"))
        fontList.add(FontData("Honuzima Regular"))
        fontList.add(FontData("Lovely Girl"))
        fontList.add(FontData("Midway"))
        fontList.add(FontData("Milchella"))
        fontList.add(FontData("Nozomi"))
        displayList.clear()
        displayList.addAll(fontList)
        fontAdapter.notifyDataSetChanged()

    }

    private fun monoFontData(){
        fontType=false
        fontList.clear()
        fontList.add(FontData("Adore Free"))
        fontList.add(FontData("Honey Bear"))
        fontList.add(FontData("KEYZHA"))
        fontList.add(FontData("Margarethy"))
        fontList.add(FontData("TendrilsDemo"))
        fontList.add(FontData("Yumeira Monogram"))
        fontList.add(FontData("Mady Risaw"))
        fontList.add(FontData("Sun Flower"))
        fontList.add(FontData("CoffeeMocca"))
        fontList.add(FontData("Spring Break"))
        fontList.add(FontData("Ziviliam Monogram"))

        displayList.clear()
        displayList.addAll(fontList)
        fontAdapter.notifyDataSetChanged()

    }

    private fun recyclerViewManager() {

        binding.rvFont.layoutManager =
            LinearLayoutManager(context)
        binding.rvFont.itemAnimator = DefaultItemAnimator()

        if(selectedFontType){
            binding.rvFont.adapter = fontAdapter
        }else {
            binding.rvFont.adapter = monoFontAdapter
        }

    }


    private fun listeners(){
        binding.clSelector1.setOnClickListener (this)
        binding.clSelector2.setOnClickListener (this)
        binding.clFavourites.setOnClickListener (this)
        binding.searchIcon.setOnClickListener (this)
    }


    private fun simpleFont(){
        binding.clFontBar.setBackgroundResource(R.color.color_black_shade_1)
        binding.clMonogramFontBar.setBackgroundResource(R.color.white)
        selectedFontType=true
        fontData()
        recyclerViewManager()
    }
    private fun monoFont(){
        selectedFontType=false
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

    private fun searchBar(){
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                Log.e("Tag213","Search "+s)
                binding.searchIcon.setImageResource(R.drawable.cancel_icon)
                searchStart=true
                filterData(s.toString())
            }
        })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.cl_selector_1->{
                    simpleFont()
                }
                R.id.cl_selector_2->{
                    monoFont()
                }
                R.id.cl_favourites->{
                    findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
                }
                R.id.search_icon->{
                    if(searchStart){
                        binding.search.setText("")
                        binding.searchIcon.setImageResource(R.drawable.ic_search)
                        displayList.clear()
                        displayList.addAll(fontList)
                        fontAdapter.notifyDataSetChanged()

                    }else {

                    }
                }
            }
        }
    }

    override fun onAdapterItemClicked(key: String, position: Int) {
                when(key){
                    "font_selected"->{
                        fontList[position].selected = !fontList[position].selected

                        fontAdapter.notifyDataSetChanged()
                            monoFontAdapter.notifyDataSetChanged()
                    }
                    "font_download"->{
                        Toast.makeText(requireContext(),"Under Development",Toast.LENGTH_LONG).show()



                    }
                    "edit"->{

                    Const.fontPos=position
                    Const.fontName=fontList[position].name
                    Const.fontName=fontList[position].name
                        val intent = Intent(requireContext(), TextEditorActivity::class.java)
                        startActivity(intent)
                    }
                }
    }


}