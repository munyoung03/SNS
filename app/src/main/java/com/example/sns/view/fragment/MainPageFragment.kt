package com.example.sns.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sns.R
import com.example.sns.viewModel.MainPageViewModel

class MainPageFragment : Fragment() {

    private lateinit var mainPageViewModel: MainPageViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainPageViewModel =
                ViewModelProviders.of(this).get(MainPageViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mainpage, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        mainPageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}