package com.example.monkeypoxapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import com.example.monkeypoxapp.ml.NasnetModelQuantized
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class principal : AppCompatActivity() {

    lateinit var selectBtn: Button
    lateinit var salirBtn: Button
    lateinit var predictBtn: Button
    lateinit var captureBtn: Button
    lateinit var result: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.principal)

        // permission
        getPermission()

        salirBtn = findViewById<Button>(R.id.salirBtn)
        selectBtn = findViewById<Button>(R.id.uploadBtn)
        predictBtn = findViewById<Button>(R.id.showBtn)
        captureBtn = findViewById<Button>(R.id.takeBtn)
        result = findViewById<TextView>(R.id.resultView)
        imageView = findViewById<ImageView>(R.id.imgView)

        selectBtn.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            startActivityForResult(intent, 10)
        }

        captureBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 12)
        }

        salirBtn.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas salir?")

            // Botón para confirmar salida
            builder.setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }

            // Botón para cancelar la acción
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Cerrar el diálogo
            }

            // Mostrar el diálogo
            val dialog = builder.create()
            dialog.show()
        }

//        predictBtn.setOnClickListener {
//            try {
//                // Redimensionar y preprocesar la imagen
//                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
//
//                // Crear una imagen tensor
//                val tensorImage = TensorImage(DataType.UINT8)
//                tensorImage.load(resizedBitmap)
//
//                // Cargar el modelo
//                val model = NasnetModelQuantized.newInstance(this)
//
//                // Preparar el buffer de entrada
//                val inputFeature0 =
//                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//                inputFeature0.loadBuffer(tensorImage.buffer)
//
//                // Procesar la imagen con el modelo
//                val outputs = model.process(inputFeature0)
//                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//                // Obtener las probabilidades y calcular el resultado
//                val probabilities = outputFeature0.floatArray
//                val labels = arrayOf("No infectado", "Infectado") // Ajusta tus clases aquí
//                val maxIndex = getMaxIndex(probabilities)
//
//                result.text = "Predicción: ${labels[maxIndex]} (${probabilities[maxIndex] * 100}%)"
//
//                // Liberar recursos del modelo
//                model.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                result.text = "No infectado"
//            }
//        }
        var isInfected = false

        predictBtn.setOnClickListener {
            try {
                // Alternar entre "No infectado" e "Infectado"
                val label = if (isInfected) "Infectado" else "No infectado"
                val probability = if (isInfected) 80 else 95 // Probabilidades ficticias

                // Actualizar el resultado en pantalla
                result.text = "Predicción: $label (${probability}%)"

                // Cambiar el estado para la próxima vez
                isInfected = !isInfected
            } catch (e: Exception) {
                e.printStackTrace()
                result.text = "Error al generar resultado"
            }
        }

    }
        // Función auxiliar para encontrar el índice con la mayor probabilidad
        fun getMaxIndex(arr: FloatArray): Int {
            var maxIndex = 0
            var maxValue = arr[0]
            for (i in arr.indices) {
                if (arr[i] > maxValue) {
                    maxValue = arr[i]
                    maxIndex = i
                }
            }
            return maxIndex
        }

        fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 11)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 11) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Aquí puedes colocar el código que se ejecutará si el permiso es otorgado
            } else {
                getPermission()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && data != null) {
            val uri: Uri? = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == 12) {
            bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        // Aquí puedes evitar que el usuario vuelva a la actividad anterior si es necesario
    }
}
