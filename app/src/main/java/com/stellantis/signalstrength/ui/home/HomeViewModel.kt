package com.stellantis.signalstrength.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.*
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var result: String

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    lateinit var telephonyManager: TelephonyManager
    var psListener: MyPhoneStateListener? = null
    private lateinit var list: ArrayList<CellInfo>


    private val _text = MutableLiveData<String>().apply {
        value = getSignalStrength() + " dBm"
    }
    val text: LiveData<String> = _text

    @SuppressLint("MissingPermission")
    private fun getSignalStrength(): String {
        psListener = MyPhoneStateListener()
        telephonyManager =
            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?)!!
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        return telephonyManager.signalStrength?.cellSignalStrengths?.get(0)?.dbm.toString()
    }

    @SuppressLint("MissingPermission")
    val textOperatorName: LiveData<String> = MutableLiveData<String>().apply {
        psListener = MyPhoneStateListener()
        telephonyManager =
            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?)!!
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        value = telephonyManager.networkOperatorName
    }
}

class MyPhoneStateListener() : PhoneStateListener() {
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
        Log.i(
            TAG, "onSignalStrengthsChanged: "
                    + "cellSignalStrengths : ${signalStrength.cellSignalStrengths}"
        )
    }
}
