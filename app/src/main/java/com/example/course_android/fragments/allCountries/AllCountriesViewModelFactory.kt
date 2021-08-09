package com.example.course_android.fragments.allCountries

//class AllCountriesViewModelFactory(
//    private var sortStatus:  Int,
//    private val mSearchSubject: BehaviorSubject<String>,
//    private val savedStateHandle: SavedStateHandle,
//    private val networkRepository: NetworkRepository
//): ViewModelProvider.Factory {

//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AllCountriesViewModel::class.java)) {
//            return AllCountriesViewModel(
//                sortStatus, mSearchSubject, savedStateHandle, networkRepository
//            ) as T
//        }
//        throw IllegalArgumentException("Error class. Get ${modelClass.canonicalName}, required AllCountriesViewModel")

//}