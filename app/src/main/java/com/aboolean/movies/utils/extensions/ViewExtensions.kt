package com.aboolean.movies.utils.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun FragmentActivity.snack(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
    val view: View = this.window.decorView.findViewById(android.R.id.content)
    Snackbar.make(view, this.getString(message), duration).show()
}

fun Fragment.snack(@StringRes message: Int) {
    requireActivity().snack(message)
}

/**
 * Default short toast
 */
fun Context.toast(any: Any, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, any.toString(), duration).show()
}

/**
 * Default short toast
 */
fun Context.toast(@StringRes resString: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(resString), duration)
}

/**
 * Long duration toast
 */
fun Context.longToast(any: Any) {
    toast(any.toString(), Toast.LENGTH_LONG)
}

/**
 * Long duration toast
 */

fun Context.longToast(@StringRes stringRes: Int) {
    toast(getString(stringRes), Toast.LENGTH_LONG)
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, duration)
}

fun Fragment.toast(@StringRes resString: Int, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(getString(resString), duration)
}

fun Fragment.longToast(@StringRes stringRes: Int) {
    context?.toast(getString(stringRes), Toast.LENGTH_LONG)
}
