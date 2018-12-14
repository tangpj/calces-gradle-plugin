/*
 * Copyright 2018, The TangPj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
