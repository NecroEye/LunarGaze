package com.muratcangzm.lunargaze.helper

interface Downloader {

    fun downloadFile(url:String, imageType: String, imageName:String): Long
}