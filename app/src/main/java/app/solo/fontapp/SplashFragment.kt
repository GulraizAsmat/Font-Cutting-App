package app.solo.fontapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToNextScreen()
    }

    private fun moveToNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkFirebase()
        }, 1500)


    }

    private fun checkFirebase() {
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("isContinue")
        dbRef.get().addOnSuccessListener {
            Log.e("Splash", it.value.toString())
            val value = it.value as Boolean
            if(value) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                val intent = Intent(requireContext(), ErrorActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }.addOnFailureListener {
            Log.e("Splash", it.message.toString())
        }
    }
}