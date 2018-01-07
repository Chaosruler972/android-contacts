package zeev.sagi.android.ex4.list_item


class Contact(val id:String,val name:String?,val phone_num:String?, val pic_uri:String?)
{
    override fun toString():String = (name?:"")
}