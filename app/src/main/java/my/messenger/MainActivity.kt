package my.messenger

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import my.messenger.fragments.ChatsListFragment
import my.messenger.fragments.HeaderFragment

class MainActivity : AppCompatActivity() {
    val chatInfoMap: MutableMap<Int, Pair<Int, String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.fragment_container) != null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, ChatsListFragment())
                replace(R.id.header, HeaderFragment())
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.chats_list, ChatsListFragment())
            }
        }
    }
}
