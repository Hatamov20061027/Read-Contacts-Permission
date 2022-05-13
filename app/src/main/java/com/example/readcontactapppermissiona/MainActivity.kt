package com.example.readcontactapppermissiona

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var rvAdapter: RecycleView
    val REQUEST_CALL = 1
    lateinit var contact3: Contact
    lateinit var list: ArrayList<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.rv)
        list = ArrayList()
        rvAdapter = RecycleView(object : RecycleView.OnMyItemClickListener {
            override fun callContact(contact: Contact) {
                contact3 = contact
                makePhoneCall(contact3)
            }

            override fun sendSMSContact(contact: Contact) {

            }
        }, list)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
        } else {
            getContact()
        }


        rv.adapter = rvAdapter
    }


    @SuppressLint("Range")
    private fun getContact() {
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            var name =
                cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var number =
                cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            var contact = Contact("$name              ", number)
            list.add(contact)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContact()

            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(contact3)
            }

        }
    }

    fun makePhoneCall(contact: Contact) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                REQUEST_CALL
            )
        } else {
            var num = "tel:${contact.number}"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(num)))
        }
    }
}