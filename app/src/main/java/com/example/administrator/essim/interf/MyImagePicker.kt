package com.example.administrator.essim.interf


import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery

import io.reactivex.Observable

interface MyImagePicker {

    @Gallery
    fun openGallery(): Observable<Result>

    @Camera
    fun openCamera(): Observable<Result>
}