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

class MipmapExt {

    //基础缩放mipmap
    String designDensity

    //图片资源密度
    Set<String> convertDensity = new HashSet<>()

    boolean auto = false

    def designDensity(String designDensity){
        this.designDensity = designDensity
    }

    def mipmapDensity(String... density){
        this.convertDensity.addAll(density)
    }

    def auto(boolean auto){
        this.auto = auto
    }

    @Override
    String toString() {
        return "auto: $auto, designDensity: $designDensity, corvent density: ${convertDensity.join(",")}"
    }
}
