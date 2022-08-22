package com.tolgaozgun.sprintplanning.util

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {

    private var FIRESTORE : FirebaseFirestore? = null

    fun getFirestore() : FirebaseFirestore{
        if(FIRESTORE == null){
            FIRESTORE = FirebaseFirestore.getInstance()
        }
        return FIRESTORE as FirebaseFirestore
    }
}