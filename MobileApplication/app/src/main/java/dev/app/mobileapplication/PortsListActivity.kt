package dev.app.mobileapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.app.mobileapplication.APIService.SocketHandler
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortsListActivity : AppCompatActivity() {

    lateinit var portsArrayList: ArrayList<String>
    var portId: String = ""
    var username = ""

    lateinit var usernameTextView: TextView
    lateinit var cardTextView: TextView
    lateinit var button: android.widget.Button
    lateinit var emptyPortsLayout: LinearLayout
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PortsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ports_list)

        // TextViews
        usernameTextView = findViewById(R.id.usernameTV)
        cardTextView = findViewById(R.id.numCarteTV)

        // get username and card number


        // button
        button = findViewById(R.id.button)
        button.visibility = android.view.View.INVISIBLE

        // empty ports layout
        emptyPortsLayout = findViewById(R.id.emptyPortsLayout)

        // socket connection
        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        SocketHandler.establishConnection()

        // recycler view
        initPortsList()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = PortsAdapter(portsArrayList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter.setOnItemClickListener(object : PortsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                adapter.selectedPosition = position
                adapter.notifyItemChanged(position)
            }
        })

        // button listener to send socket
        button.setOnClickListener {
            if (adapter.selectedPosition != -1) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmation")
                builder.setMessage("Voulez-vous vraiment connecter au port ${portsArrayList[adapter.selectedPosition]} ?" )
                builder.setPositiveButton("Oui") { dialog, which ->
                    portId = portsArrayList[adapter.selectedPosition]
                    Log.d("PortsListActivity", portId)
                    sendSocket()
                }
                builder.setNegativeButton("Non") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog: Dialog = builder.create()
                dialog.show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Erreur")
                builder.setMessage("Veuillez sélectionner un port")
                builder.setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog: Dialog = builder.create()
                dialog.show()
            }
        }

        // socket listener
        mSocket.on("connected_port") {
            args ->
            if (args[0] != null) {
                val msg = args[0] as String
                runOnUiThread{
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initPortsList() {
        portsArrayList = ArrayList()
        loadCard()
        val user_cart = portId
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retIn.getPortsbUser(user_cart).enqueue(object : Callback<List<Port>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Port>>, response: Response<List<Port>>) {
                if (response.isSuccessful) {
                    val ports = response.body()
                    ports?.forEach {
                        portsArrayList.add(it.nom)
                    }
                    Log.d("PortsListActivity", portsArrayList.toString())
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("PortsListActivity", "Error")
                }
                if (portsArrayList.size > 0) {
                    button.visibility = android.view.View.VISIBLE
                }
                else {
                    emptyPortsLayout.visibility = android.view.View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Port>>, t: Throwable) {
                Log.d("PortsListActivity", "Error")
            }
        })
    }

    private fun loadCard(){
        val sharredPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        portId = sharredPreferences.getString("CARD_NUMBER", "").toString()
        username = sharredPreferences.getString("USERNAME", "").toString()
        usernameTextView.text = username
        cardTextView.text = portId

    }

    private fun sendSocket(){
        val mSocket = SocketHandler.getSocket()
        mSocket.emit("connect_port", portId)
    }


    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}


