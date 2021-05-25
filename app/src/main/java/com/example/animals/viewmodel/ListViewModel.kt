package com.example.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel (application: Application):AndroidViewModel(application){
    /*

    expose a series live data variables, instantiate them with some values
     */
    //lazy means the system is not instantiate this variable unless it's need
    //usually live data is observable, mutable means that you can change the value
    // whenever we want
    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy{MutableLiveData<Boolean>()}
    val loading by lazy {MutableLiveData<Boolean>()}

    private  val disposable = CompositeDisposable()
    //a disposable is a java construct that takes a result of observer
    //and dismiss it when life cycle of program is finished

    private val apiService = AnimalApiService()
    private  val prefs = SharedPreferencesHelper(getApplication())

    private var invalidApiKey = false

    fun refresh(){
        loading.value = true
        invalidApiKey=false
        //check whether there is a key or not
        val key = prefs.getApiKey()

        if(key.isNullOrEmpty()){
            getKey()

        }
       else{
           getAnimals(key)
        }


    }

    fun hardRefresh(){
        loading.value=true
        getKey()
    }
    private fun getKey(){
        //getApikey returns a single
        disposable.add(
            apiService.getApiKey().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>(){
                    override fun onSuccess(key: ApiKey) {

                        if(key.key.isNullOrEmpty()){
                            loadError.value=true
                            loading.value = false
                        }
                        else{
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value= false
                        loadError.value = true
                    }

                })
        )
    }
    private fun getAnimals(key:String){
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>(){
                    override fun onSuccess(list: List<Animal>) {
                      //display in the view
                        loadError.value =false
                        animals.value=list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if(!invalidApiKey){
                            invalidApiKey=true
                            getKey()
                        }
                        else {
                            e.printStackTrace()
                            loading.value = false
                            animals.value = null
                            loadError.value = true
                        }
                    }

                }
        ))

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}