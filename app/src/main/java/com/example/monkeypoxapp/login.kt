package com.example.monkeypoxapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        var auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef=db.collection("usuario")


        val txtuser = findViewById<EditText>(R.id.txt_usuario)
        val txtpassword = findViewById<EditText>(R.id.txtcontraseña)
        val btnlogin = findViewById<Button>(R.id.login_btn)
        val btnregistro = findViewById<TextView>(R.id.register_btn)


        btnlogin.setOnClickListener {
            val correo = txtuser.text.toString()
            val contraseña = txtpassword.text.toString()
            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth
            collectionRef
                .whereEqualTo("correo", correo)
                .whereEqualTo("contraseña", contraseña)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
//                           nombreVol= querySnapshot.documents[0].getString("nombre")
//                          globalUser.nombre=nombreVol.toString()
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Inicio de sesión exitoso",
                                Snackbar.LENGTH_LONG
                            ).show()
                            val intent = Intent(this, principal::class.java).apply {
                            }
                            startActivity(intent)
                        } else {
                            /*  Snackbar.make(
                                  findViewById(android.R.id.content),
                                  "Credenciales inválidas",
                                  Snackbar.LENGTH_LONG
                              ).show()*/
                            val alertDialog = AlertDialog.Builder(this)
                                .setTitle("Error de inicio de sesión")
                                .setMessage("Credenciales inválidas")
                                .setPositiveButton("Aceptar", null)
                                .create()
                            alertDialog.show()
                        }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Error al iniciar sesión",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
        btnregistro.setOnClickListener {
            startActivity(Intent(this, register::class.java))
        }
    }

}

