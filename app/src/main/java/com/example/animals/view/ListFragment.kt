package com.example.animals.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animals.R
import com.example.animals.model.Animal
import com.example.animals.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel:ListViewModel
    private val listAdapter = AnimalListAdapter(arrayListOf())
    private val animalListDataObserver = Observer<List<Animal>>{list ->
        //check if list is not null
        list?.let { //if list not null then do let
            animalList.visibility = View.VISIBLE
            listAdapter.updateAnimalList(it)
        }

    }

    private val loadingLiveDataObserver = Observer<Boolean>{
        isLoading ->
        loadingView.visibility=if(isLoading) View.VISIBLE else View.GONE
        if(isLoading){
            listError.visibility=View.GONE
            animalList.visibility = View.GONE
        }

    }

    private val errorLiveDataObserver = Observer<Boolean> {isError->
        listError.visibility = if(isError) View.VISIBLE else View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModelProviders have a type of View model, attach it to the fragment
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.animals.observe(this, animalListDataObserver )

        viewModel.loading.observe(this,loadingLiveDataObserver)
        viewModel.loadError.observe(this,errorLiveDataObserver )
        viewModel.refresh()

        animalList.apply{
            layoutManager = GridLayoutManager(context,2)
            adapter= listAdapter
        }
        refreshLayout.setOnRefreshListener {
            //refresh the information, have the viewModel refresh its data

            animalList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.hardRefresh()

            refreshLayout.isRefreshing = false
        }
    }




}