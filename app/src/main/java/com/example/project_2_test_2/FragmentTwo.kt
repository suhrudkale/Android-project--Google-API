package com.example.project_2_test_2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class FragmentTwo : Fragment() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_two, container, false)



        // Retrieve and display data
        displayData(view)

        return view
    }

    private fun displayData(view: View) {
        val textViewData = view.findViewById<TextView>(R.id.textViewData)

        db.collection("Malls").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("Firestore", "No documents found")
                    textViewData.text = "No documents found"
                } else {
                    for (document in documents) {
                        val data = document.data
                        val mallName = data["mallName"] as String?
                        var maxFieldName = ""
                        var maxValue = Int.MIN_VALUE

                        // Iterate through the fields to find the one with the maximum value
                        for ((key, value) in data) {
                            if (key != "mallName" && value is Number && value.toInt() > maxValue) {
                                maxValue = value.toInt()
                                maxFieldName = key
                            }
                        }

                        // Format and display the mall name and the field with the maximum value
                        val displayText = "$mallName:\n$maxFieldName: $maxValue\n\n"
                        textViewData.append(displayText)

                        Log.d("Firestore", "Mall Name: $mallName")
                        Log.d("Firestore", "Max Field: $maxFieldName with value $maxValue")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
                textViewData.text = "Error getting documents: $exception"
            }
    }



}

