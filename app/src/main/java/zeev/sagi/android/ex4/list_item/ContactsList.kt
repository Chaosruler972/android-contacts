package zeev.sagi.android.ex4.list_item

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import java.util.*


object ContactsList
{

    fun init_load_contacts(context: Context)
    {
        if(context.checkSelfPermission(Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
            return
        val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        var i = 0
        while (phones.moveToNext()) {
            val id = (++i).toString()
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val phoo_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            val contact_item = Contact(id,name,phoneNumber,phoo_uri)
            if(phoneNumber!=null && vector.all { it.phone_num!=null && phoneNumber!=it.phone_num })
                vector.addElement(contact_item)
            else
                i--
        }
        phones.close()
    }
    val vector:Vector<Contact> = Vector()
}
