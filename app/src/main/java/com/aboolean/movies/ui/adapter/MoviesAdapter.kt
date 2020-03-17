package com.aboolean.movies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboolean.movies.R
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.utils.extensions.imageFavoriteResource
import com.aboolean.movies.utils.extensions.posterFullUrl
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_popular_movie.view.*

class MoviesAdapter(private val movies: MutableList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var itemClickListener: ((Movie) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_popular_movie,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = movies.size

    fun setItemClickListener(listener: (Movie) -> Unit) {
        this.itemClickListener = listener
    }

    fun addNewMovies(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies.apply {
            clear()
            addAll(newMovies)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(movies[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(movie: Movie) {
            itemView.apply {
                with(movie) {
                    tvMovieTitle.text = title
                    tvTotalPointsItem.text = context.getString(
                        R.string.text_placeholder_votes, voteAverage.toString(),
                        voteCount.toString()
                    )
                    Glide.with(context).asDrawable().load(posterPath.posterFullUrl)
                        .placeholder(R.drawable.ic_movie_favorite)
                        .into(ivMoviePoster)
                    ivMovieFavoriteState.setImageResource(isFavorite.imageFavoriteResource)
                }
                setOnClickListener { itemClickListener?.invoke(movie) }
            }
        }
    }
}