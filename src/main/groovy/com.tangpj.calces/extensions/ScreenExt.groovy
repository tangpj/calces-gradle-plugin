package com.tangpj.calces.extensions

import org.gradle.api.Action

/**
 * Created by tang on 2018/9/17.
 * App屏幕支持配置ext，用于配置图片支持
 */
class ScreenExt {

    DimensExt dimensExt

    MipmapExt mipmapExt

    ScreenExt(){
        dimensExt = new DimensExt()
        mipmapExt = new MipmapExt()
    }

    def dimens(Action<DimensExt> action){
        action.execute(this.dimensExt)
    }

    def mipmap(Action<MipmapExt> action){
        action.execute(this.mipmapExt)
    }

    @Override
    String toString() {
        return "dimens = { $dimensExt }; mipmap = {$mipmapExt }"
    }
}
