package com.albs.challenge.search_meanings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.albs.challenge.R
import com.albs.challenge.adapter.CustomAdapter
import com.albs.challenge.databinding.ActivitySearchMeaningsBinding
import com.albs.challenge.model.Lfs
import com.albs.challenge.utils.Resource
import com.albs.challenge.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMeaningsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMeaningsBinding
    private lateinit var viewModel: SearchMeaningsViewModel
    private lateinit var adapter: CustomAdapter
    private val lfs: ArrayList<Lfs> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_meanings)

        init()
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_meanings)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMeaning.layoutManager = layoutManager
        binding.rvMeaning.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        setupViewModel()
        adapter = CustomAdapter(lfs)
        binding.rvMeaning.adapter = adapter

        binding.btnSubmit.setOnClickListener {
            lfs.clear()
            Utils.hideKeyboard(application, view = it)
            adapter.notifyDataSetChanged()
            val acronym: String = binding.editAcronym.text.toString().trim()
            binding.shimmerViewContainer.visibility = View.VISIBLE
            viewModel.getMeanings(acronym)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SearchMeaningsViewModel::class.java]
        getMeanings()
    }

    private fun getMeanings() {
        viewModel.meaningsData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.shimmerViewContainer.visibility = View.GONE
                    if (response.data != null) {
                        lfs.addAll(response.data.lfs)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    binding.shimmerViewContainer.visibility = View.GONE
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}