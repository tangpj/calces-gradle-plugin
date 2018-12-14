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

/**
 * Created by tang on 2018/9/20.
 * dimens缩放配置
 */
class DimensExt {


    String dimensFileName = "dimens"

    //设计稿尺寸
    int designPx = 375

    int scale = 2

    int scaleMode = -1

    boolean auto = false

    //需要适配的sw尺寸，单位dp
    Set<Integer> smallestWidths = new HashSet<>()

    def dimensFileName(String dimensFileName){
        this.dimensFileName = dimensFileName
    }

    def designPx(int designPx){
        this.designPx = designPx
    }

    def smallesWidths(Integer... sw){
        this.smallestWidths.addAll(sw)
    }

    def scale(int  scale){
        this.scale = scale
    }

    def scaleMode(int scaleMode){
        this.scaleMode = scaleMode
    }

    def auto(boolean auto){
        this.auto = auto
    }

    @Override
    String toString() {
        return "auto: $auto, designPx: $designPx, scale: $scale, scaleMode: ${getScaleModeStr()} corvent small widths: ${smallestWidths.join(",")}"
    }

    private String getScaleModeStr(){
        switch (scaleMode){
            case BigDecimal.ROUND_UP:
                return "ROUND_UP"
            case BigDecimal.ROUND_DOWN:
                return "ROUND_DOWN"
            case BigDecimal.ROUND_CEILING:
                return "ROUND_CEILING"
            case BigDecimal.ROUND_FLOOR:
                return "ROUND_FLOOR"
            case BigDecimal.ROUND_HALF_UP:
                return "ROUND_HALF_UP"
            case BigDecimal.ROUND_HALF_DOWN:
                return "ROUND_HALF_DOWN"
            case BigDecimal.ROUND_HALF_EVEN:
                return "ROUND_HALF_EVEN"
            case BigDecimal.ROUND_UNNECESSARY:
                return "ROUND_UNNECESSARY"
            default:
                return "undefined"
        }
    }
}
