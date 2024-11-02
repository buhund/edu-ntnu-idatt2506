package edu.ntnu.exercise04

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), ListFragment.OnItemSelectedListener {

    private var currentIndex = 0
    private val itemList = listOf(
        Item(R.drawable.film01, "Romnudler", "Romnudler is a sci-fi comedy set in The Space, where a group of astronauts discover that instant noodles hold the key to intergalactic peace."),
        Item(R.drawable.film02, "Atuin Origins", "Atuin Origins is a romantic space comedy about a turtle who falls in love with a space station and how the circular world came to be."),
        Item(R.drawable.film03, "Trolltarm", "Trolltarm is a thrilling fantasy epic about a magical troll living in the Norwegian mountains with the power to control weather.")
    )

    override fun onItemSelected(item: Item) {
        showItemDetails(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.listFragmentContainer, ListFragment())
                .commit()
        }

        showItemDetails(itemList[currentIndex])

        findViewById<Button>(R.id.previous_button).setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showItemDetails(itemList[currentIndex])
            }
        }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            if (currentIndex < itemList.size - 1) {
                currentIndex++
                showItemDetails(itemList[currentIndex])
            }
        }
    }

private fun showItemDetails(item: Item) {
    val fragmentManager = supportFragmentManager
    val detailFragment = fragmentManager.findFragmentById(R.id.detailFragmentContainer) as? DetailFragment

    detailFragment?.let {
        it.updateDetails(item.imageResId, item.description)
    } ?: run {
        val bundle = Bundle().apply {
            putInt("imageResId", item.imageResId)
            putString("description", item.description)
        }
        val fragment = DetailFragment()
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .replace(R.id.detailFragmentContainer, fragment)
            .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentIndex", currentIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt("currentIndex", 0)
        showItemDetails(itemList[currentIndex])
    }
}

