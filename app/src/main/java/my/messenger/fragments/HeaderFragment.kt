package my.messenger.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import my.messenger.APP_NAME
import my.messenger.R

class HeaderFragment(
    private var title: String = APP_NAME
) : Fragment(R.layout.fragment_header) {
    private lateinit var textView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView = view.findViewById(R.id.title)
        textView.text = title
    }

    fun setTitle(newTitle: String) {
        title = newTitle
        if (this::textView.isInitialized) {
            textView.text = newTitle
        }
    }
}
