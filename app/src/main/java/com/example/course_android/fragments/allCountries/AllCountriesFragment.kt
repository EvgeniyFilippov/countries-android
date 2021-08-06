package com.example.course_android.fragments.allCountries

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.DEFAULT_INT
import com.example.course_android.Constants.DEFAULT_SORT_STATUS
import com.example.course_android.Constants.FILE_NAME_PREF
import com.example.course_android.Constants.KEY_SORT_STATUS
import com.example.course_android.Constants.SORT_STATUS_DOWN
import com.example.course_android.Constants.SORT_STATUS_UP
import com.example.course_android.R
import com.example.course_android.adapters.AdapterOfAllCountries
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.base.mvvm.Outcome
import com.example.course_android.databinding.FragmentAllCountriesBinding
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.fragments.filter.FilterViewModel
import com.example.course_android.utils.getCurrentLocation
import com.example.course_android.utils.getResultOfCurrentLocation
import com.example.course_android.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_all_countries.*

class AllCountriesFragment : Fragment(R.layout.fragment_all_countries), BaseMvvmView {

    private var binding: FragmentAllCountriesBinding? = null
    private var sortStatus = DEFAULT_SORT_STATUS
    private lateinit var inet: MenuItem
    private val mCompositeDisposable = CompositeDisposable()
    var adapterOfAllCountries = AdapterOfAllCountries()

    private val mSearchSubject = BehaviorSubject.create<String>()
    private lateinit var viewModel: AllCountriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, AllCountriesViewModelFactory(sortStatus, mSearchSubject))
                .get(AllCountriesViewModel::class.java)
        viewModel.getSearchSubject()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSortStatus()
        binding = FragmentAllCountriesBinding.bind(view)
        context?.let { getCurrentLocation(it) }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<HashMap<String?, Int>>(
            "valueOfFilter"
        )?.observe(viewLifecycleOwner, Observer { map ->
            viewModel.getCountriesFromFilter(map)
        })

        viewModel.mutableCountriesLiveData.observe(
            viewLifecycleOwner,
            Observer { data -> showCountries(data) })


//        viewModel.mutableCountriesFromSearchLiveData.singleObserve(
//            viewLifecycleOwner,
//            Observer { data -> showCountries(data) })


        viewModel.mutableCountriesFromSearchLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> {

                }
                is Outcome.Failure -> {
                    hideProgress()
                }
                is Outcome.Success -> {
                    showCountries(it.data)
                    hideProgress()
                }

                is Outcome.Next -> {
                    showCountries(it.data)
                    hideProgress()
                }
                else -> {

                }
            }
        }



        showProgress()
        viewModel.getCountriesFromApi()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapterOfAllCountries
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_sort_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
        if (sortStatus == SORT_STATUS_UP) {
            menu.findItem(R.id.sort_countries)
                .setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24).isChecked = true
        } else if (sortStatus == SORT_STATUS_DOWN) {
            menu.findItem(R.id.sort_countries).setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
        }

        inet = menu.findItem(R.id.online)
        inet.isVisible = context?.isOnline() != true


        val menuSearchItem = menu.findItem(R.id.menu_search_button)
        val mSvMenu: SearchView = menuSearchItem.actionView as SearchView
        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mSearchSubject.onNext(query) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mSearchSubject.onNext(newText)
                return true
            }
        })

        mSvMenu.setOnCloseListener {
            viewModel.getCountriesFromApi()
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_countries) {
            if (!item.isChecked) {
                adapterOfAllCountries.sortAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24)
                context?.toast(getString(R.string.sort_up))
                item.isChecked = true
                sortStatus = SORT_STATUS_UP
            } else {
                adapterOfAllCountries.sortDescendingAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
                context?.toast(getString(R.string.sort_down))
                item.isChecked = false
                sortStatus = SORT_STATUS_DOWN
            }
        }
        if (item.itemId == R.id.reset_sort) {
            showSortResetDialog()
        }
        if (item.itemId == R.id.filter) {
            findNavController().navigate(R.id.action_allCountriesFragment_to_filterFragment)
        }

        saveSortStatus()
        return super.onOptionsItemSelected(item)
    }

    private fun saveSortStatus() {
        activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
            ?.edit()
            ?.apply { putInt(KEY_SORT_STATUS, sortStatus) }
            ?.apply()
    }

    private fun readSortStatus() {
        val sharedPreference =
            activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
        val reader = sharedPreference?.getInt(KEY_SORT_STATUS, DEFAULT_INT)
        if (reader != null) {
            sortStatus = reader
        }
    }

    private fun showCountries(listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>) {
        adapterOfAllCountries.repopulate(
            listCountriesFromApiDto
        )
        hideProgress()
        adapterOfAllCountries.setItemClick { item ->
            val bundle = Bundle()
            bundle.putString(COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(
                R.id.action_secondFragment_to_countryDetailsFragment,
                bundle
            )
        }
    }

    private fun showSortResetDialog() {
        val alertDialog = context?.let { MaterialAlertDialogBuilder(it) }
            ?.setTitle(getString(R.string.sort))
            ?.setMessage(getString(R.string.reset_sort))
            ?.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            ?.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                sortStatus = DEFAULT_INT
                saveSortStatus()
                adapterOfAllCountries.resetSorting()
            }
        alertDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

    override fun showError(error: String) {
        if (context?.isOnline() == false) {
            context?.toast(getString(R.string.chek_inet))
        } else {
            activity?.showAlertDialog()
        }
    }

    override fun showProgress() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressBar?.visibility = View.GONE
    }

}