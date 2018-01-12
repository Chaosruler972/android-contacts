@file:Suppress("ClassName")

package zeev.sagi.android.ex4


import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.contact_list.*
import kotlinx.android.synthetic.main.contact_list_content.view.*
import zeev.sagi.android.ex4.list_item.Contact
import zeev.sagi.android.ex4.list_item.ContactsList
import java.util.*


class list_fragment : Fragment()
{


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v:View =  inflater.inflate(R.layout.fragment_list_fragment, container, false)
        val contact_list:RecyclerView = get_view_by_id(v,R.id.contact_list) as RecyclerView
        setupRecyclerView(contact_list)
        return v
    }

    /*
    get view by id, for array adapter usage
 */
    fun get_view_by_id(convertView: View, id: Int): View = convertView.findViewById(id) // grabs the correpsonding view by id from layout


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(activity as ContactListActivity, ContactsList.vector, ContactListActivity.mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ContactListActivity,
                                        private val mValues: Vector<Contact>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as Contact
                if (ContactListActivity.mTwoPane) {
                    val fragment = ContactDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ContactDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout2, fragment)
                            .commit()
                }
                else
                {
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

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView)
        {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
    companion object {

        fun newInstance(): list_fragment = list_fragment()
    }
}// Required empty public constructor
