package com.example.sharewishes.interfaces

import java.io.File

interface ICameraResultCallBack {
    fun onCaptureResult(file: File)
}