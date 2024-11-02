package edu.ntnu.exercise04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    private var imageResId: Int? = null
    private var description: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        imageView = view.findViewById(R.id.detail_image)
        textView = view.findViewById(R.id.detail_description)

        imageResId?.let { updateDetails(it, description) }

        return view
    }

    fun updateDetails(imageResId: Int, description: String?) {
        if (::imageView.isInitialized && ::textView.isInitialized) {
            imageView.setImageResource(imageResId)
            textView.text = description
        } else {
            this.imageResId = imageResId
            this.description = description
        }
    }
}

