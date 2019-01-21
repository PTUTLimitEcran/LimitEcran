package com.lpiem.ptut_limit_ecran.limitecran.Gallery

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lpiem.ptut_limit_ecran.limitecran.R
import kotlinx.android.synthetic.main.activity_detail_and_share_tree.*
import java.io.File


class DetailAndShareTree : AppCompatActivity() {
    private var filePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_and_share_tree)

        filePath = intent.extras["filePath"] as String

        Glide.with(detailTreeImage)
            .load(Environment.getExternalStorageDirectory().absolutePath+"/LimitEcran/"+filePath)
            .into(detailTreeImage)

        floatingActionButton.setOnClickListener { share() }
    }


    private fun share() {
        val myShareIntent = Intent(Intent.ACTION_SEND)
        myShareIntent.type = "image/*"

        val imageFile = File(Environment.getExternalStorageDirectory().absolutePath+"/LimitEcran/"+filePath)

        val imageUri = FileProvider.getUriForFile(
            this@DetailAndShareTree,
            "com.lpiem.ptut_limit_ecran.limitecran.provider",
            imageFile
        )

        myShareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        myShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Look at this Awesome Tree I Won !")
        startActivity(myShareIntent)
    }

}
