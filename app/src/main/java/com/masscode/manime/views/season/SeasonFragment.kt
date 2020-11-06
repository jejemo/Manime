package com.masscode.manime.views.season

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.masscode.manime.R
import com.masscode.manime.databinding.FragmentSeasonBinding
import com.masscode.manime.viewmodel.ViewModelFactory
import java.util.*

class SeasonFragment : Fragment() {

    private lateinit var binding: FragmentSeasonBinding
    private lateinit var viewModel: SeasonViewModel
    private lateinit var adapterSeason: SeasonAdapter
    private var thisYear = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeasonBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[SeasonViewModel::class.java]
        adapterSeason = SeasonAdapter()
        thisYear = Calendar.getInstance()[Calendar.YEAR]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.season_menu, menu)
        binding.loading.visibility = View.VISIBLE

        viewModel.setSeason(thisYear, "spring")
        setTitleSeason("spring")
        viewModel.animeSeason.observe(viewLifecycleOwner, { anime ->
            if (anime.isNotEmpty()) {
                adapterSeason.setData(anime)
                binding.loading.visibility = View.GONE
            }
        })
        with(binding.rvAnimeSeason) {
            setHasFixedSize(true)
            adapter = adapterSeason
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_spring -> {
                refreshList("spring")
            }
            R.id.menu_summer -> {
                refreshList("summer")
            }
            R.id.menu_fall -> {
                refreshList("fall")
            }
            R.id.menu_winter -> {
                refreshList("winter")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshList(season: String) {
        binding.loading.visibility = View.VISIBLE
        viewModel.setSeason(thisYear, season)
        setTitleSeason(season)
        viewModel.animeSeason.observe(viewLifecycleOwner, { anime ->
            if (anime.isNotEmpty()) {
                adapterSeason.setData(anime)
                binding.loading.visibility = View.GONE
            }
        })
    }

    private fun setTitleSeason(season: String) {
        val icon: String
        when (season) {
            "spring" -> {
                icon = String(Character.toChars(0x1F331))
                (activity as AppCompatActivity).supportActionBar?.title = "Spring$icon ~ $thisYear"
            }
            "summer" -> {
                icon = String(Character.toChars(0x1F31E))
                (activity as AppCompatActivity).supportActionBar?.title = "Summer$icon ~ $thisYear"
            }
            "fall" -> {
                icon = String(Character.toChars(0x1F342))
                (activity as AppCompatActivity).supportActionBar?.title = "Fall$icon ~ $thisYear"
            }
            "winter" -> {
                icon = String(Character.toChars(0x2744))
                (activity as AppCompatActivity).supportActionBar?.title = "Winter$icon ~ $thisYear"
            }
        }
    }
}