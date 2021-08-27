package com.example.course_android.fragments.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.adapters.AdapterNews
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.databinding.FragmentNewsBinding
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.utils.toast
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class NewsFragment : ScopeFragment(R.layout.fragment_news), BaseMvvmView {

    private var binding: FragmentNewsBinding? = null
    private val mCompositeDisposable = CompositeDisposable()
    var adapterNews = AdapterNews()
    private val viewModel: NewsViewModel by stateViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        viewModel.getNewsFlow().asLiveData(lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Outcome.Failure -> {
                        showError()
                    }
                    is Outcome.Next -> {
                        Log.e("hz", "")
                    }
                    is Outcome.Progress -> {
                        if (it.loading) showProgress() else hideProgress()
                    }
                    is Outcome.Success -> {
                        showNews(it.data.toMutableList())
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
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