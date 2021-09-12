package com.example.course_android.fragments.newsByLocation

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.base.mvi.BaseMviViewModel

class NewsByLocationViewModel (savedStateHandle: SavedStateHandle) :
    BaseMviViewModel<NewsIntent, NewsAction, NewsState>() {
    override fun intentToAction(intent: NewsIntent): NewsAction {
        return when (intent) {
            is NewsIntent.LoadAllCharacters -> NewsAction.AllCharacters
            is NewsIntent.ClearSearch -> NewsAction.AllCharacters
            is NewsIntent.SearchCharacter -> NewsAction.SearchCharacters(intent.name)
        }
    }

    override fun handleAction(action: NewsAction) {
        TODO("Not yet implemented")
    }


//    override fun handleAction(action: NewsAction) {
//        launchOnUI {
//            when (action) {
//                is NewsAction.AllCharacters -> {
//                    dataManager.getAllCharacters().collect {
//                        mState.postValue(it.reduce())
//                    }
//                }
//                is NewsAction.SearchCharacters -> {
//                    dataManager.searchCharacters(action.name).collect {
//                        mState.postValue(it.reduce(true))
//                    }
//                }
//            }
//        }
//    }
}