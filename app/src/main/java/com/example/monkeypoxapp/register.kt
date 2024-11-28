package com.example.monkeypoxapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.monkeypoxapp.data.model.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register)

        val txtempresa = findViewById<EditText>(R.id.nombreEmpresa)
        val txtcod=findViewById<EditText>(R.id.codRegistro)
        val txtemail=findViewById<EditText>(R.id.correo)
        val txtdireccion=findViewById<EditText>(R.id.direccion)
        val txttelefono=findViewById<EditText>(R.id.telefono)
        val txtpassword=findViewById<EditText>(R.id.contraseña)
        val txtpassword1=findViewById<EditText>(R.id.contraseña1)
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("usuario")

        val btncancel = findViewById<Button>(R.id.cancelar_btn)
        val btnregistro = findViewById<TextView>(R.id.registrar_btn)

        btncancel.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
        btnregistro.setOnClickListener {
            val nombre = txtempresa.text.toString().trim()
            val direccion = txtdireccion.text.toString().trim()
            val correo = txtemail.text.toString().trim()
            val codigo = txtcod.text.toString().trim()
            val telefono = txttelefono.text.toString().trim()
            val contraseña = txtpassword.text.toString().trim()

            // Check if any of the fields are empty
            if (nombre.isEmpty() || direccion.isEmpty() || correo.isEmpty() || codigo.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()
            ){
                // Display an error AlertDialog if any required field is empty
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Por favor, complete todos los campos.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }
            // Validate field "Organizacion" to allow only text
            if (!nombre.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+".toRegex())) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ingrese un nombre válido.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }
            // Validate field "RUC" to allow only numbers with a maximum of 11 digits
            if (!codigo.matches("\\d{11}".toRegex())) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ingrese un código válido (11 dígitos).")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }
            // Validate field "email" to allow only email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ingrese un correo electrónico válido.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }
            // Validate field "txtpassword" to ensure it contains at least one uppercase letter,
            // one lowercase letter, one special character, and one digit
            val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}\$".toRegex()
            if (!contraseña.matches(passwordPattern)) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ingrese una contraseña válida de por lo menos 6 digitos. Debe contener al menos una letra mayúscula, una letra minúscula, un carácter especial y un número.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            val nuevoUsuario = Usuario(nombre, codigo,direccion, correo, telefono, contraseña)


            // Adding the newOrganizacion to the "organizacion" collection in Firestore
            collectionRef.add(nuevoUsuario)
                .addOnSuccessListener { documentReference ->
                    // On success, display an AlertDialog
                    AlertDialog.Builder(this)
                        .setTitle("Registro exitoso")
                        .setMessage("Se ha creado el registro correctamente.")
                        .setPositiveButton("OK") { _, _ ->
                            // Start the LoginActivity after clicking OK
                            startActivity(Intent(this, login::class.java))
                        }
                        .setCancelable(false)
                        .show()
                }
                .addOnFailureListener { error ->
                    // On failure, showing a Snackbar with the error message
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error al registrar: $error",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
        }

    }
}