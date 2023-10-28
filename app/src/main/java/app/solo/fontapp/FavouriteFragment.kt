package app.solo.fontapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.solo.fontapp.databinding.FragmentFavouriteBinding
import app.solo.fontapp.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds


class FavouriteFragment : Fragment(),View.OnClickListener {
    lateinit var adRequest: AdRequest
    private lateinit var binding: FragmentFavouriteBinding
    var selectedFontType=true // Font=true , MonoFont=false

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

    private fun init(){

        listeners()

    }
    fun adManager(){
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



    private fun listeners(){
        binding.clSelector1.setOnClickListener (this)
        binding.clSelector2.setOnClickListener (this)
        binding.clFont.setOnClickListener (this)
        binding.exploreFont.setOnClickListener (this)
    }

    private fun simpleFont(){
        binding.clFontBar.setBackgroundResource(R.color.color_black_shade_1)
        binding.clMonogramFontBar.setBackgroundResource(R.color.white)
        selectedFontType=true


    }
    private fun monoFont(){
        selectedFontType=false
        binding.clFontBar.setBackgroundResource(R.color.white)
        binding.clMonogramFontBar.setBackgroundResource(R.color.color_black_shade_1)

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
                R.id.cl_font->{
                    findNavController().popBackStack()
                }
                R.id.explore_font->{
                    findNavController().popBackStack()
                }
            }
        }
    }

}