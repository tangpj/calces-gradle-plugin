package com.tangpj.calces.extensions


/**
 * Created by tang on 2018/9/20.
 * dimens缩放配置
 */
class DimensExt {

    //设计稿尺寸
    int designPx = 375

    //需要适配的sw尺寸，单位dp
    Set<Integer> smallestWidths = new HashSet<>()

    DimensExt(){
        //默认至少会生成一个sw尺寸(360)
        smallestWidths.add(360)
    }

    def designPx(int designPx){
        this.designPx = designPx
    }

    def smallesWidths(Integer... sw){
        this.smallestWidths.addAll(sw)
    }

    @Override
    String toString() {
        return "designPx: $designPx, corvent small widths: ${smallestWidths.join(",")}"
    }
}
