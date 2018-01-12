package zeev.sagi.android.ex4

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.contact_list_content.view.*

import kotlinx.android.synthetic.main.contact_list.*
import zeev.sagi.android.ex4.list_item.Contact
import zeev.sagi.android.ex4.list_item.ContactsList
import java.util.*
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK




class ContactListActivity(private var permission_code: Int = 0) : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        permission_code = resources.getInteger(R.integer.request_permission_code)
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE)

        if(checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
        {
            ContactsList.init_load_contacts(baseContext)
            if(checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissions,permission_code)
        }
        else
            requestPermissions(permissions,permission_code)


        //setSupportActionBar(toolbar)
        //toolbar.title = title
        /*
        if (contact_detail_container != null) {

            mTwoPane = true
        }
        */
        mTwoPane = isTablet(baseContext)

        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, list_fragment.newInstance())
        transaction.commit()
        if(!mTwoPane)
            frameLayout2.visibility = FrameLayout.GONE
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    companion object {
        var mTwoPane: Boolean = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            permission_code->
            {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    ContactsList.init_load_contacts(baseContext)
                    val transaction = fragmentManager.beginTransaction()
                    transaction.add(R.id.frameLayout, list_fragment.newInstance())
                    transaction.commit()
                    if(!mTwoPane)
                        frameLayout2.visibility = FrameLayout.GONE
                }
                else
                {
                    Toast.makeText(this,getString(R.string.insufficnet_permissions),Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }




}
