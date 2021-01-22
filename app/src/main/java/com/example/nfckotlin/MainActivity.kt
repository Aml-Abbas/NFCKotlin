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
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var mNfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        writeButtton.setOnClickListener {
            val messageWrittenSuccessfully =
                NFCUtil.createNFCMessage(messageEditText.text.toString(), intent, this)
            resultTextView.text = ifElse(
                messageWrittenSuccessfully,
                "Successful Written to Tag",
                "Something went wrong Try Again"
            )
        }

    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtil.enableNFCInForeground(it, this, javaClass)
        }

    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtil.disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tagContentTextView.text = NFCUtil.retrieveNFCMessage(this.intent)
    }


    fun <T> ifElse(
        condition: Boolean,
        primaryResult: T,
        secondaryResult: T
    ) = if (condition) primaryResult else secondaryResult


}