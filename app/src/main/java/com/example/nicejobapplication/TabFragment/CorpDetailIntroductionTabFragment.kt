package com.example.nicejobapplication.TabFragment

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import com.example.nicejobapplication.R


class CorpDetailIntroductionTabFragment(private var bundle: Bundle) : Fragment() {

    private lateinit var corpDesTV : TextView
    private lateinit var corpAddressTV : TextView
    private lateinit var corpWebsiteTV : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_corp_detail_introduc_tab, container, false)

        val corpDescription = bundle?.getString("corpDescription")
        val corpAddress = bundle?.getString("corpAddress")
        val corpWebsite = bundle?.getString("corpWebsite")

        corpDesTV = view.findViewById(R.id.introduceContent)
        corpDesTV.text = corpDescription

        corpAddressTV = view.findViewById(R.id.address)
        corpAddressTV.text = corpAddress

        corpWebsiteTV = view.findViewById(R.id.website)
        corpWebsiteTV.text = corpWebsite


        return view
    }

  }
