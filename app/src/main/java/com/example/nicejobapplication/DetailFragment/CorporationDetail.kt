package com.example.nicejobapplication.DetailFragment;
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.adapter.CorpViewPagerAdapter
import com.example.nicejobapplication.databinding.FragmentCorporationDetailBinding
import com.example.nicejobapplication.modal.Corporation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class CorporationDetail : Fragment() {

    private lateinit var binding: FragmentCorporationDetailBinding
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var db: FirebaseFirestore

    private lateinit var corpLogoIFV : ImageFilterView
    private lateinit var corpNameTV: TextView

    private lateinit var bundleToTabLayout: Bundle
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_corporation_detail, container, false)
        binding = FragmentCorporationDetailBinding.inflate(layoutInflater)

        navController = findNavController()

        //Set sự kiện cho nút back
        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        val bundle = arguments
        val documentID = bundle?.getString("documentID")

        db = FirebaseFirestore.getInstance()
        db.collection("corporations").document(documentID!!)
            .get()
            .addOnSuccessListener  { document ->
                var corpName = document.data?.get("corpName").toString()
                var corpLogo = "gs://nicejob-367709.appspot.com/corporation_image/" + document.data?.get("corpLogo").toString()

                var corpDescription = document.data?.get("corpDescription").toString()
                var corpWebsite  = if(document.data?.get("website").toString().isEmpty()){
                    document.data?.get("website").toString()
                }else{
                    "Chưa cập nhật!"
                }
                var corpAddress = document.data?.get("corpAddress").toString()



                binding.corpName.text = corpName
                val storageRef = Firebase.storage.getReferenceFromUrl(corpLogo)
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding.corpLogo)
                }

                bundleToTabLayout = bundleOf(
                    "corpId" to documentID,
                    "corpDescription" to corpDescription,
                    "corpAddress" to corpAddress,
                    "corpWebsite" to corpWebsite
                )

                binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Giới thiệu"))
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Tuyển dụng"))

                val adapter = CorpViewPagerAdapter(childFragmentManager, binding.tabLayout.tabCount, bundleToTabLayout)
                binding.viewPager.adapter = adapter



                binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

                binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        binding.viewPager.currentItem = tab!!.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }





        return binding.root
    }


}

