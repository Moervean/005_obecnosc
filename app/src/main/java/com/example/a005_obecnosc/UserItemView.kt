package com.example.a005_obecnosc

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.a005_obecnosc.databinding.UserRowBinding

class UserItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context,attrs,defStyleAttr) {

    private val binding: UserRowBinding? =  DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.user_row,
        this,
        true
    )

    fun assign(name: String, status: String){
        binding?.nameTV?.text = name
        if ( status == "offline")
            binding?.statusImage?.setBackgroundResource(android.R.drawable.presence_busy)
        else
            binding?.statusImage?.setBackgroundResource(android.R.drawable.presence_online)
    }


}