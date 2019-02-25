package com.example.dam2alu4.crronobackground

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.util.*
import android.content.Context.BIND_AUTO_CREATE
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.*
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var txtSeg2 : TextView
    private lateinit var myTime : Thread
    private var estado = -1
    private var secondss = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>( R.id.btnStart ).setOnClickListener{ btnStart() }
        findViewById<Button>( R.id.btnStop).setOnClickListener{ btnStop() }
        findViewById<Button>( R.id.btnPause ).setOnClickListener{ btnPause() }
        txtSeg2 = findViewById( R.id.txtSeg2 )
    }

    private fun btnStart(  ){
        var txtSeg = findViewById<EditText>( R.id.txtSeg )
        if ( txtSeg.text.toString() != ""){
            //startService( Intent( this, CrronoService::class.java ).putExtra( "time", Integer.parseInt( txtSeg.text.toString() ) * 1000 ) )
            // Create and send a message to the service, using a supported 'what' value
            //val msg = Message.obtain(null, 1, 0, 0)//what value have to be an integer :(
            try {
                //msg.arg1 = 5000
                //startService( Intent(this, CrronoService::class.java).putExtra("time", txtSeg.text.toString().toInt() ) )
                //println( txtSeg!!.text.toString() )
                if ( secondss == -1 ) {
                    secondss = Integer.parseInt(txtSeg!!.text.toString())
                    runThread()
                }
                else if ( estado == 0 && secondss != -1 ) runThread()
                else if ( estado == 0 && secondss == -1 ){
                    secondss = Integer.parseInt(txtSeg!!.text.toString())
                    runThread()
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            txtSeg.focusable = View.NOT_FOCUSABLE
            txtSeg.visibility = View.VISIBLE
        }
    }

    private fun btnStop(  ){
        //stopService( Intent( this, CrronoService::class.java ) )
        secondss = -1
        txtSeg2!!.visibility = View.GONE
    }

    private fun btnPause(  ){
        estado = 0
    }

    private fun runThread(  ) {
        estado = 1
        var ok = object : Thread() {
            override fun run() {
                while ( secondss > -1 && estado == 1 ) {
                    runOnUiThread {
                        txtSeg2!!.visibility = View.VISIBLE
                        txtSeg2!!.text = secondss.toString()
                        secondss--
                    }
                    Thread.sleep(1000)
                }
            }
        }
        myTime = ok
        myTime.start()
        //Thread hola = new Thread() { public void run() { while (i++ < 1000) { try { runOnUiThread(new Runnable() { @Override public void run() { btn.setText("#" + i); } }); Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); } } }
    }
    //BIND SERVICE
    /*
    /** Messenger for communicating with the service.  */
    var mService: Messenger? = null

    /** Flag indicating whether we have called bind on the service.  */
    var mBound: Boolean = false

    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = Messenger(service)
            mBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service
        bindService(
            Intent(this, CrronoService::class.java), mConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
    }*/
}
