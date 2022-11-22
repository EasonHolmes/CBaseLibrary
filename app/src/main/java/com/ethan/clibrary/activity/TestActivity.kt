package com.ethan.clibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ethan.clibrary.R
import com.ethan.clibrary.databinding.ActivityMainBinding
import com.ethan.clibrary.databinding.ActivityTestBinding

/**
 * Created by Ethan Cui on 2022/11/22
 */
class TestActivity : AppCompatActivity() {
    val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lottieTest.imageAssetsFolder = "transition/images"
        binding.lottieTest.setAnimation("transition/data10.json")
    }
}