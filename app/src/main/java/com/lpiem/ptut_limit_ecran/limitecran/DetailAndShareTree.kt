package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_and_share_tree.*

class DetailAndShareTree : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_and_share_tree)

        val filePath = intent.extras["filePath"]

        Glide.with(detailTreeImage)
            .load(Environment.getExternalStorageDirectory().absolutePath+"/LimitEcran/"+filePath)
            .into(detailTreeImage)
    }
}
