package zeev.sagi.android.ex4

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_contact_detail.*
import kotlinx.android.synthetic.main.contact_detail.view.*
import zeev.sagi.android.ex4.list_item.Contact
import zeev.sagi.android.ex4.list_item.ContactsList
import android.content.Intent
import android.util.Log


class ContactDetailFragment : Fragment() {


    private var mItem: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID))
            {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                mItem = ContactsList.vector[it.getString(ARG_ITEM_ID).toInt()-1]
                //activity?.toolbar_layout?.title = mItem?.toString()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.contact_detail, container, false)

        // Show the dummy content as text in a TextView.
        mItem?.let {
            rootView.name.text = (context.getString(R.string.name_name).replace("name",it.name?:"") + "\n" + context.getString(R.string.phone_number_num).replace("num",it.phone_num?:""))
            val phone_no = it.phone_num
            rootView.name.setOnClickListener {
                if(phone_no==null)
                    return@setOnClickListener
                if(context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {
                    try {
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.tel) + phone_no))
                        startActivity(intent)
                    }
                    catch (e:Exception)
                    {
                        Log.d("ContactDetail","Was Unable to parse & call phone number")
                    }
                }
                else
                {
                    Toast.makeText(context,context.getString(R.string.insufficnet_permissions),Toast.LENGTH_SHORT).show()
                }
            }
            if(it.pic_uri!=null)
            {
                rootView.imageView.setImageURI(Uri.parse(it.pic_uri))
            }
            else
                rootView.imageView.visibility = ImageView.GONE
        }

        return rootView
    }

    companion object {

        const val ARG_ITEM_ID = "item_id"
    }
}
