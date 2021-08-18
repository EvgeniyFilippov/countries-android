package com.example.course_android.fragments.allCapitals

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.adapters.AdapterCapitals
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.base.mvvm.Outcome
import com.example.course_android.databinding.FragmentAllCapitalsBinding
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.utils.toast
import com.example.domain.dto.model.CapitalItemDto
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class AllCapitalsFragment : ScopeFragment(R.layout.fragment_all_capitals), BaseMvvmView {

    private var binding: FragmentAllCapitalsBinding? = null
    private val mCompositeDisposable = CompositeDisposable()
    var adapterCapitals = AdapterCapitals()
    private val viewModel: AllCapitalsViewModel by stateViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllCapitalsBinding.bind(view)

        viewModel.allCapitalsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> {
                    showProgress()
                }
                is Outcome.Failure -> {
                    showError()
                }
                is Outcome.Success -> {
                    showCapitals(it.data)
                    hideProgress()
                }

                is Outcome.Next -> {
                    showCapitals(it.data)
                    hideProgress()
                }
                else -> {

                }
            }
        }

        viewModel.getCapitalsFromApi()
        binding?.recyclerView?.setHasFixedSize(true)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = adapterCapitals
    }


    private fun showCapitals(listCapitalsDto: MutableList<CapitalItemDto>) {
        adapterCapitals.repopulate(
            listCapitalsDto
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