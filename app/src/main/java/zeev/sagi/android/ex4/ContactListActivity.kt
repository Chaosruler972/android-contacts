package zeev.sagi.android.ex4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.contact_list_content.view.*

import kotlinx.android.synthetic.main.contact_list.*
import zeev.sagi.android.ex4.list_item.Contact
import zeev.sagi.android.ex4.list_item.ContactsList
import java.util.*


class ContactListActivity(private var permission_code: Int = 0, private var mTwoPane: Boolean = false) : AppCompatActivity() {


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


        setSupportActionBar(toolbar)
        toolbar.title = title


        if (contact_detail_container != null) {

            mTwoPane = true
        }

        setupRecyclerView(contact_list)
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
                }
                else
                {
                    Toast.makeText(this,getString(R.string.insufficnet_permissions),Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, ContactsList.vector, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ContactListActivity,
                                        private val mValues: Vector<Contact>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as Contact
                if (mTwoPane) {
                    val fragment = ContactDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ContactDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.contact_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ContactDetailActivity::class.java).apply {
                        putExtra(ContactDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.id
            holder.mContentView.text = item.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
