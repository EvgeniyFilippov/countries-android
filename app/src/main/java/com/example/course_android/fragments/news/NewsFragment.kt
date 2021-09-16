package com.example.course_android.fragments.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.adapters.AdapterNews
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.databinding.FragmentNewsBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.utils.toast
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class NewsFragment : ScopeFragment(R.layout.fragment_news), BaseMvvmView {

    private var binding: FragmentNewsBinding? = null
    var adapterNews = AdapterNews()
    private val viewModel: NewsViewModel by stateViewModel()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)
        setHasOptionsMenu(true)

        adapterNews.setItemClick {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(it.url)
            startActivity(openURL)
        }

        viewModel.getNewsFlow().asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Failure -> {
                        showError()
                    }

                    is Outcome.Progress -> {
                        if (it.loading) showProgress() else hideProgress()
                    }
                    is Outcome.Success -> {
                        showNews(it.data.toMutableList())
                    }
                    else -> {
                        showError()
                    }
                }
            })

        viewModel.getNewsFromSearch().asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Success -> {
                        showNews(it.data.toMutableList())
                    }
                    else -> {
                        showError()
                    }
                }
            })

        binding?.recyclerNews?.setHasFixedSize(true)
        binding?.recyclerNews?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerNews?.adapter = adapterNews

    }

    private fun showNews(listNewsDto: MutableList<NewsItemDto>) {
        adapterNews.repopulate(
            listNewsDto
        )
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @ObsoleteCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news_sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val menuSearchItem = menu.findItem(R.id.news_search_button)
        val mSvMenu = menuSearchItem.actionView as SearchView
        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchText.value = newText
                viewModel.getNewsFromSearch()
                return true
            }
        })

        mSvMenu.setOnCloseListener {
            CoroutineScope(lifecycleScope.coroutineContext).launch {
                viewModel.getNewsFlow().collect {
                    if (it is Outcome.Success<List<NewsItemDto>>) {
                        adapterNews.repopulate(it.data.toMutableList())
                    }
                }
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun showError() {
        hideProgress()
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