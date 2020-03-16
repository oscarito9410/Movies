package com.aboolean.movies.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**

Created by oscar on 04/05/19
operez@na-at.com.mx
 */
/**
 * Pagination class to add more items to the list when reach the last item.
 */
abstract class PaginationScrollListener
/**
 * Supporting only LinearLayoutManager for now.
 *
 * @param layoutManager
 */
(var layoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener() {


    abstract fun isLoading(): Boolean


    var pastVisibleItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = layoutManager.childCount
        totalItemCount = layoutManager.itemCount
        var firstVisibleItems: IntArray? = null
        firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
        if (firstVisibleItems != null && firstVisibleItems.size > 0) {
            pastVisibleItems = firstVisibleItems[0]
        }


        if (!isLoading()) {
            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()
}