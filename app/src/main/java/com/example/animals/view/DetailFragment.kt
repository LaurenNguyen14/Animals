package com.example.animals.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.animals.R
import com.example.animals.databinding.FragmentDetailBinding
import com.example.animals.model.Animal
import com.example.animals.model.AnimalPalette
import com.example.animals.util.getProgressDrawable
import com.example.animals.util.loadImage


class DetailFragment : Fragment() {
    var animal:Animal?=null

    private lateinit var databinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databinding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailFragmentArgs.fromBundle(it).animal
        }
        context?.let {
            databinding.animalImage.loadImage(animal?.imageUrl, getProgressDrawable(it))
        }


        animal?.imageUrl?.let {
            setupBackgroundColor(it)
        }
        databinding.animal=animal
    }

    private fun setupBackgroundColor(url:String){
        Glide.with(this).asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                     Palette.from(resource)
                         .generate(){palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb?:0
                            databinding.pallete = AnimalPalette(intColor)
                         }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }
}