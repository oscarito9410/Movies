package com.aboolean.movies.utils.recyclerview


import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by oscar on 04/05/19
 * operez@na-at.com.mx
 */
class InfiniteScrollProvider {

    /**
     * [RecyclerView] that we want to provide infinite scrolling behavior for it
     */
    private var recyclerView: RecyclerView? = null

    /**
     * used to determine currently user is waiting for loading items or not
     */
    private var isLoading = false

    /**
     * listener that trigger when user reach end of list.
     */
    private var onLoadMoreListener: OnLoadMoreListener? = null

    /**
     * [GridLayoutManager] that is attached to [.recyclerView]
     *
     *
     * used to determine [.lastVisibleItem],[.totalItemCount],[.previousItemCount]
     */
    private var layoutManager: RecyclerView.LayoutManager? = null

    /**
     * position of last visible item
     */
    private var lastVisibleItem: Int = 0

    /**
     * total items count of [.recyclerView]
     */
    private var totalItemCount: Int = 0

    /**
     * total items count, relate to before last [.onLoadMoreListener] call.
     */
    private var previousItemCount = 0

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    /**
     * this function attach [.recyclerView] to provide infinite scroll for it
     *
     * @param recyclerView       see [.recyclerView] for more information
     * @param onLoadMoreListener callback for notifying when user reach list ends.
     */
    fun attach(recyclerView: RecyclerView, onLoadMoreListener: OnLoadMoreListener) {
        this.recyclerView = recyclerView
        this.onLoadMoreListener = onLoadMoreListener
        layoutManager = recyclerView.layoutManager
        setInfiniteScrollGrid(layoutManager)
    }

    /**
     * this function get scrolling control of [.recyclerView] and whenever
     * user reached list ends, [.onLoadMoreListener] will be called
     */
    private fun setInfiniteScrollGrid(layoutManager: RecyclerView.LayoutManager?) {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalItemCount = layoutManager!!.itemCount
                if (previousItemCount > totalItemCount) {
                    previousItemCount = totalItemCount - THRESHOLD
                }
                if (layoutManager is GridLayoutManager) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                } else if (layoutManager is LinearLayoutManager) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                } else if (layoutManager is StaggeredGridLayoutManager) {
                    val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager?
                    val spanCount = staggeredGridLayoutManager!!.spanCount
                    val ids = IntArray(spanCount)
                    staggeredGridLayoutManager.findLastVisibleItemPositions(ids)
                    var max = ids[0]
                    for (i in 1 until ids.size) {
                        if (ids[1] > max) {
                            max = ids[1]
                        }
                    }
                    lastVisibleItem = max
                }
                if (totalItemCount > THRESHOLD) {
                    if (previousItemCount <= totalItemCount && isLoading) {
                        isLoading = false
                        Log.i("InfiniteScroll", "Data fetched")
                    }
                    if (!isLoading && lastVisibleItem > totalItemCount - THRESHOLD && totalItemCount > previousItemCount) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener!!.onLoadMore()
                        }
                        Log.i("InfiniteScroll", "End Of List")
                        isLoading = true
                        previousItemCount = totalItemCount
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    companion object {

        /**
         * [.onLoadMoreListener] called when [.recyclerView] reach to item with position [.totalItemCount] - {@value THRESHOLD}
         */
        private val THRESHOLD = 3
    }

}
