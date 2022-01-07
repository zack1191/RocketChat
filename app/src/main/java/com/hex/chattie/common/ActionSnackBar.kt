package com.hex.chattie.common

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.hex.chattie.R

class ActionSnackBar private constructor(
        parentViewGroup : ViewGroup, content :
        View, contentViewCallback : com.google.android.material.snackbar.ContentViewCallback
                                        ) : BaseTransientBottomBar<ActionSnackBar>(parentViewGroup, content, contentViewCallback)
{

    companion object
    {
        fun make(parentViewGroup : ViewGroup, content : String = "") : ActionSnackBar
        {
            val context = parentViewGroup.context
            val view = LayoutInflater.from(context).inflate(R.layout.message_action_bar, parentViewGroup, false)
            val actionSnackbar = ActionSnackBar(parentViewGroup, view, CallbackImpl(view))
            with(view) {
                actionSnackbar.getView().setPadding(0)
                actionSnackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                actionSnackbar.cancelView = findViewById(R.id.ivCloseActionBar)
                actionSnackbar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE

            }
            return actionSnackbar
        }
    }

    lateinit var cancelView : View
    private lateinit var messageTextView : TextView
    private lateinit var titleTextView : TextView

    override fun dismiss()
    {
        super.dismiss()

    }

    class CallbackImpl(val content : View) : com.google.android.material.snackbar.ContentViewCallback
    {

        override fun animateContentOut(delay : Int, duration : Int)
        {
            content.scaleY = 1f
            ViewCompat.animate(content)
                    .scaleY(0f)
                    .setDuration(duration.toLong())
                    .startDelay = delay.toLong()
        }

        override fun animateContentIn(delay : Int, duration : Int)
        {
            content.scaleY = 0f
            ViewCompat.animate(content)
                    .scaleY(1f)
                    .setDuration(duration.toLong())
                    .startDelay = delay.toLong()
        }
    }
}