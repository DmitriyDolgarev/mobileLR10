package com.example.lr4_second.domain.model

class ImageModel(var id: Int, var uri: String) {

    fun isEmpty(): Boolean
    {
        if (id == null || uri.isEmpty()) return true
        else return false
    }
}