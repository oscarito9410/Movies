package com.aboolean.movies.utils.extensions

import com.aboolean.movies.R
import com.aboolean.movies.utils.Constants.URL_IMAGE

val String.posterFullUrl: String
    get() = "${URL_IMAGE}$this"

val Boolean.imageFavoriteResource: Int
    get() = if (this) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border