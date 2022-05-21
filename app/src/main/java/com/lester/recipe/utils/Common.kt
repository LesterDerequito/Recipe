package com.lester.recipe.utils

import android.app.Activity
import android.app.Dialog
import com.lester.recipe.R
import com.lester.recipe.databinding.LayoutConnectionBinding

object Common {

    fun showNetworkError(activity: Activity) {
        val bindingConnection = LayoutConnectionBinding.inflate(activity.layoutInflater)
        val dialog = Dialog(activity, R.style.Theme_Recipe)
        dialog.setContentView(bindingConnection.root)
        bindingConnection.buttonRetry.setOnClickListener {
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(activity.intent);
            activity.overridePendingTransition(0, 0);
        }
        dialog.show()
    }
}