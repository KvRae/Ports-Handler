package dev.app.mobileapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
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

    val portsArrayList= ArrayList<String>()
    var portId: String = ""


    lateinit var button: android.widget.Button
    lateinit var emptyPortsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ports_list)

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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PortsAdapter(portsArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : PortsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                adapter.selectedPosition = position
                adapter.notifyItemChanged(position)
            }
        })



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
        loadCard()
        Log.d("Port ID", portId)
        val user_cart = portId
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retIn.getPortsbUser(user_cart).enqueue(object : Callback<List<Port>> {
            override fun onResponse(call: Call<List<Port>>, response: Response<List<Port>>) {
                if (response.isSuccessful) {
                    val ports = response.body()
                    ports?.forEach {
                        portsArrayList.add(it.nom)
                    }
                    Log.d("PortsListActivity", portsArrayList.toString())
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
        val savedString = sharredPreferences.getString("CARD_NUMBER", "")
        portId = savedString.toString()

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


