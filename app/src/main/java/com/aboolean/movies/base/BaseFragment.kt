package com.aboolean.movies.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aboolean.movies.ui.adapter.MoviesAdapter

abstract class BaseFragment : Fragment() {
    //Methods will be used for each fragment
    abstract fun getLayoutView(): Int
    abstract fun initView()
    abstract fun attachObservers()

    //Protected fields will be shared in each fragment
    //Verify if the screen orientation is landscape or portrait
    private val isLandScape by lazy {
        context?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
    protected val moviesAdapter = MoviesAdapter(mutableListOf())
    protected val moviesLayoutManager by lazy {
        StaggeredGridLayoutManager(
            getColumnsByOrientation(isLandScape),
                StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * Method to get recyclerview's columns according to the current orientation
     */
    protected fun getColumnsByOrientation(isLandScape: Boolean) = if (isLandScape) RECYCLER_VIEW_SPAN_COUNT_LANDSCAPE
    else RECYCLER_VIEW_SPAN_COUNT_PORTRAIT

    companion object {
        const val RECYCLER_VIEW_SPAN_COUNT_PORTRAIT = 2
        const val RECYCLER_VIEW_SPAN_COUNT_LANDSCAPE = 3
        const val SPACE_ITEM_DECORATION = 12
    }
}