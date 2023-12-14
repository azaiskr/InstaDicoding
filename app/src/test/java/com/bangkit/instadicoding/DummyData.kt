package com.bangkit.instadicoding

import com.bangkit.instadicoding.data.remote.response.ListStoryItem

object DummyData {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                "id $i",
                "photoUrl $i",
                "createdAt $i",
                "name $i",
                "description $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}