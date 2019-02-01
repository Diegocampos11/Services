package com.example.dam2alu4.crronobackground

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import android.R.attr.start
import android.content.Context
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.os.HandlerThread
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.EditText


class CrronoService : Service() {

    var mStartMode: Int = 0       // indicates how to behave if the service is killed
    var mBinder: IBinder? = null      // interface for clients that bind
    var mAllowRebind: Boolean = false // indicates whether onRebind should be used

    //Params by myself/on my own
    var time : Int ? = null

    /*SERVICE INDEPENDIENTE*/
    private var mServiceLooper: Looper? = null
    private var mServiceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                //Toast.makeText(baseContext, "hello!" + msg.arg2, Toast.LENGTH_SHORT).show()
                Log.d("Depu", msg.arg2.toString())
                when (msg.what) {
                    1 ->{
                        var contador = msg.arg2
                        while( contador-- > 0 ){
                            Thread.sleep( 1000 )
                            Log.d("Depu", contador.toString()+"WHEN")
                            //Toast.makeText(applicationContext, "hello!" + msg.arg2, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else ->{
                        super.handleMessage(msg)
                        //Toast.makeText(applicationContext, "super!" + msg.arg2, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        val thread = HandlerThread(
            "ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND
        )
        thread.start()

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.looper
        mServiceHandler = ServiceHandler(mServiceLooper!!)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    /*SERVICE INDEPENDIENTE*/

    /*BIND SERVICE*/
    /*
    /**
     * Handler of incoming messages from clients.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 ->{
                    Thread.sleep( (msg.arg1 * 2).toLong() )
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show()
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    val mMessenger = Messenger(IncomingHandler())

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        return mMessenger.getBinder()
    }

    override fun onCreate() {
        // The service is being created
    }*/

    /*BIND SERVICE*/

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //params sent
        //Log.d("Depu",  )
        //Toast.makeText( baseContext, "asdf " + activity!!.findViewById<EditText>( R.id.txtSeg ), Toast.LENGTH_LONG ).show()
        time = Integer.parseInt( intent.extras["time"].toString() )//solo pasa una vez por start command :o
        //var myThread = MyThread( time!! )
        //myThread.start()
        //
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        val msg = mServiceHandler!!.obtainMessage()
        msg.arg1 = startId
        msg.what = 1
        msg.arg2 = time!!
        mServiceHandler!!.sendMessage(msg)
        Log.d( "Depu", time.toString() )
        return START_NOT_STICKY//mStartMode
    }

    /*override fun onBind(intent: Intent): IBinder? {
        // A client is binding to the service with bindService()
        return mBinder
    }*/

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return mAllowRebind
    }

    override fun onRebind(intent: Intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
    }

    class MyThread( ii : Int ) : Thread(){
        private var i = ii

        override fun run( ){
            //while( i > 0  ) {
            Log.d( "Depu", "Thread" + i )
            Thread.sleep(i.toLong())
            Log.d( "Depu", "Finished" )
            //}

        }
    }
}
