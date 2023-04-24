package dev.app.mobileapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.app.mobileapplication.R.*
import kotlinx.android.synthetic.main.activity_ports_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var usernameET: EditText
    lateinit var passwordET: EditText
    lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        // Get the references to the views
        this.usernameET = findViewById(id.etUsername)
        this.passwordET = findViewById(id.etPassword)
        this.loginButton = findViewById(id.btnLogin)

        usernameET.setText("admin")
        passwordET.setText("admin")



        // Set the click listener to the login button
        this.loginButton.setOnClickListener {
            this.loginButton.isEnabled = false
            // Get the text from the views
            val username = usernameET.text.toString()
            val password = passwordET.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                this.loginButton.isEnabled = true
            }else{
                this.login(username, password)

                loginButton.isEnabled = true
            }
        }
    }

    private fun login(username: String, password: String) {
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        retIn.loginUser(User(username,password)).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed"+t.message,
                    Toast.LENGTH_SHORT
                ).show()


            }
            @SuppressLint("CommitPrefEdits")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) =
                if (response.code() == 201) {
                    // creating a session
                    val gson = Gson()
                    val jsonSTRING = response.body()?.string()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    val id = jsonObject.get("id").asString
                    val username = jsonObject.get("nom").asString
                    var secname = ""
                    if (!jsonObject.get("prenom").isJsonNull()){secname = jsonObject.get("prenom").asString}
                    val num_carte = jsonObject.get("num_carte").asString
                    saveUser(num_carte, username,secname, id)
                    Toast.makeText(this@LoginActivity, "Welcome! "+username, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, PortsListActivity::class.java)
                        startActivity(intent)
                        finish()
                } else if(response.code() == 401){
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
                else if(response.code() == 404){
                    Toast.makeText(this@LoginActivity, "User does not exist" , Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
        })
    }

    fun saveUser(cardNumber: String,username: String,secanme : String, id: String){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply{
            putString("CARD_NUMBER", cardNumber)
            putString("USERNAME", username)
            putString("SECNAME", secanme)
            putString("ID", id)
        }.apply()
        editor.apply()
    }
}


