package com.treasure.restart.func.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.treasure.restart.R
import com.treasure.restart.base.BaseActivity
import kotlin.getValue

class PublishActivity : BaseActivity() {

    companion object {
        private const val MAX_IMAGE_COUNT = 9

        const val EXTRA_IMAGES =
            "extra_images"

        fun start(
            activity: AppCompatActivity,
            images: List<Uri> = emptyList()
        ) {
            val intent =
                Intent(
                    activity,
                    PublishActivity::class.java
                )

            intent.putStringArrayListExtra(
                EXTRA_IMAGES,
                ArrayList(
                    images.map {
                        it.toString()
                    }
                )
            )

            activity.startActivity(intent)
        }
    }

    private val uploadViewModel: UploadViewModel by viewModels()

    private lateinit var rvImages: RecyclerView
    private lateinit var etContent: EditText
    private lateinit var tvPublish: TextView
    private lateinit var tvCancel: TextView

    private lateinit var adapter: PublishImageAdapter

    private val images = ArrayList<Uri>()

    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) { uris ->

            if (uris.isEmpty()) {
                return@registerForActivityResult
            }

            val remain =
                MAX_IMAGE_COUNT - images.size

            if (remain <= 0) {
                return@registerForActivityResult
            }

            images.addAll(
                uris.take(remain)
            )

            adapter.setImages(images)
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_publish)

        initImages()

        initView()
        initRecyclerView()
        initClick()
    }

    /**
     * 接收外部传入的图片
     */
    private fun initImages() {

        val imageStrings = intent.getStringArrayListExtra(EXTRA_IMAGES) ?: return
        images.clear()
        images.addAll(
            imageStrings
                .take(MAX_IMAGE_COUNT)
                .mapNotNull {
                    runCatching {
                        Uri.parse(it)
                    }.getOrNull()
                }
        )
    }

    private fun initView() {

        rvImages =
            findViewById(R.id.rvImages)

        etContent =
            findViewById(R.id.etContent)

        tvPublish =
            findViewById(R.id.tvPublish)

        tvCancel =
            findViewById(R.id.tvCancel)
    }

    private fun initRecyclerView() {

        adapter = PublishImageAdapter(

            onClick = { position ->
                openPreview(position)
            },

            onDelete = { position ->

                if (position in images.indices) {

                    images.removeAt(position)

                    adapter.setImages(images)
                }
            },

            onAdd = {

                if (images.size >= MAX_IMAGE_COUNT) {
                    return@PublishImageAdapter
                }

                imagePicker.launch("image/*")
            }
        )

        rvImages.layoutManager =
            GridLayoutManager(
                this,
                3
            )

        rvImages.adapter = adapter

        // 初始化已有图片
        adapter.setImages(images)
    }

    private fun initClick() {

        tvCancel.setOnClickListener {
            finish()
        }

        tvPublish.setOnClickListener {
            publish()
        }
    }

    private fun openPreview(
        position: Int
    ) {

        val intent =
            Intent(
                this,
                ImagePreviewActivity::class.java
            )

        intent.putStringArrayListExtra(
            ImagePreviewActivity.EXTRA_IMAGES,
            ArrayList(
                images.map {
                    it.toString()
                }
            )
        )

        intent.putExtra(
            ImagePreviewActivity.EXTRA_POSITION,
            position
        )

        startActivity(intent)
    }

    private fun publish() {

        val content = etContent.text.toString().trim()
        uploadViewModel.uploadImage(images)
    }
}