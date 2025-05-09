package my.messenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import my.messenger.fragments.ChatsListFragment
import my.messenger.fragments.HeaderFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            add(R.id.fragment_container, ChatsListFragment())
            replace(R.id.header, HeaderFragment())
        }
    }
}
