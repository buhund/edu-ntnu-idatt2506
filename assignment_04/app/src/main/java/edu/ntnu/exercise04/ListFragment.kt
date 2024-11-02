package edu.ntnu.exercise04

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listener: OnItemSelectedListener

    private val itemList = listOf(
        Item(R.drawable.film01, "Romnudler", "Romnudler is a sci-fi comedy set in space, where a group of astronauts discover that instant noodles hold the key to intergalactic peace."),
        Item(R.drawable.film02, "Atuin Origins", "Atuin Origins is a romantic space comedy about a turtle who falls in love with a space station and how the circular world came to be."),
        Item(R.drawable.film03, "Trolltarm", "Trolltarm is a thrilling fantasy epic about a magical troll living in the Norwegian mountains with the power to control weather.")
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnItemSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(itemList) { item ->
            listener.onItemSelected(item)
        }
        return view
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: Item)
    }
}
