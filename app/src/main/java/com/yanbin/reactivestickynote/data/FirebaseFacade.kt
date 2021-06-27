package com.yanbin.reactivestickynote.data

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFacade {

    private var firestore: FirebaseFirestore? = null

    fun getFirestore(): FirebaseFirestore {
        return if (firestore == null) {
            firestore = FirebaseFirestore.getInstance()

            firestore!!
        } else {
            firestore!!
        }
    }
}