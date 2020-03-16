package com.aboolean.movies.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aboolean.movies.ui.adapter.MoviesAdapter

abstract class BaseFragment : Fragment() {

    abstract fun getLayoutView(): Int
    abstract fun initView()
    abstract fun attachObservers()

    protected val moviesAdapter = MoviesAdapter(mutableListOf())
    protected val moviesLayoutManager by lazy {
        StaggeredGridLayoutManager(RECYCLER_VIEW_SPAN_COUNT_PORTRAIT,
                StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    protected fun getColumnsByOrientation(isLandScape: Boolean) = if (isLandScape) RECYCLER_VIEW_SPAN_COUNT_LANDSCAPE
    else RECYCLER_VIEW_SPAN_COUNT_PORTRAIT

    companion object {
        const val RECYCLER_VIEW_SPAN_COUNT_PORTRAIT = 2
        const val RECYCLER_VIEW_SPAN_COUNT_LANDSCAPE = 3
        const val SPACE_ITEM_DECORATION = 12
    }
}