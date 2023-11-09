package vn.edu.hust.activityexamples

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ActionMode

class MainActivity : AppCompatActivity() {

    lateinit var textResult: TextView
    val items = arrayListOf<String>()

    var actionMode: ActionMode? = null
    val actionModeCallback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.sub_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if (item?.itemId == R.id.action_download) {
                Log.v("TAG", "Download")
            } else if (item?.itemId == R.id.action_share) {
                Log.v("TAG", "Share")
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edit1 = findViewById<EditText>(R.id.edit1)
        val edit2 = findViewById<EditText>(R.id.edit2)
        textResult = findViewById<TextView>(R.id.text_result)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getIntExtra("result", 0)
                textResult.text = "Result: $result"
            } else {
                textResult.text = "Failed"
            }
        }

        findViewById<Button>(R.id.button_open).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("param1", edit1.text.toString())
            intent.putExtra("param2", edit2.text.toString())
            //startActivity(intent)
            //startActivityForResult(intent, 123)
            launcher.launch(intent)
        }

        findViewById<Button>(R.id.button_other).setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:0987654321")
//            startActivity(intent)

//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.422219,-122.08364"))
//            startActivity(intent)

//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hust.edu.vn"))
//            startActivity(intent)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com")) // recipients
            intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            intent.putExtra(Intent.EXTRA_TEXT, "Email message text")
            startActivity(intent)
        }

        repeat(50){items.add("Item $it")}
        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)

        // registerForContextMenu(findViewById(R.id.imageView))
        registerForContextMenu(findViewById(R.id.list_view))

        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            //actionMode = startSupportActionMode(actionModeCallback)

            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.sub_menu)
            popupMenu.setOnMenuItemClickListener {
                true
            }
            popupMenu.show()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.sub_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterContextMenuInfo).position
        if (item.itemId == R.id.action_download) {
            Log.v("TAG", "Download ${items[pos]}")
        } else if (item.itemId == R.id.action_share) {
            Log.v("TAG", "Share ${items[pos]}")
        }
        return super.onContextItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getIntExtra("result", 0)
                textResult.text = "Result: $result"
            } else {
                textResult.text = "Failed"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            Log.v("TAG", "Share")
        } else if (item.itemId == R.id.action_download) {
            Log.v("TAG", "Download")
        } else if (item.itemId == R.id.action_settings) {
            Log.v("TAG", "Settings")
        }
        return super.onOptionsItemSelected(item)
    }
}