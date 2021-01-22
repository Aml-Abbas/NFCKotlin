package com.example.nfckotlin

import android.content.Intent

import android.nfc.NfcAdapter
import android.os.Bundle
import android.app.Activity
import android.app.PendingIntent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import kotlin.text.Charsets.US_ASCII
import kotlin.text.Charsets.UTF_8


class MainActivity : AppCompatActivity() {
    var mNfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        var car = Car(color = "Red", weight = 900, topSpeed = 300)
        car.let {
            it.color = "Blue"
            it.topSpeed = 200
        }


    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtilJava.enableNFCInForeground(it, this, javaClass)
        }

    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtilJava.disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tagContentTextView.text = getPayload(intent)
        writeButtton.setOnClickListener {
            val messageWrittenSuccessfully =
                NFCUtilJava.createNFCMessage(messageEditText.text.toString(), intent, this)
            resultTextView.text = ifElse(
                messageWrittenSuccessfully,
                "Successful Written to Tag",
                "Something went wrong Try Again"
            )
        }

    }

    fun getPayload(intent: Intent?): String {
        val parcelabels: Array<Parcelable>? =
            intent?.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        var string = ""
        if (parcelabels != null && parcelabels.isNotEmpty()) {
            val ndefMessage = parcelabels[0] as NdefMessage

            string = String(ndefMessage.records[0].payload, UTF_8)
        } else {
            string = "No NDEF messages found"
        }
        return string
    }

    fun readTextFromMessage(ndefMessage: NdefMessage): String {
        val ndefRecords = ndefMessage.records
        val tagContent = ""
        if (ndefRecords != null && ndefRecords.isNotEmpty()) {
            val record = ndefRecords[0];

        }
        return tagContent;
    }


    fun <T> ifElse(
        condition: Boolean,
        primaryResult: T,
        secondaryResult: T
    ) = if (condition) primaryResult else secondaryResult


}