package com.tangpj.calces.extensions

/**
 * Created by tang on 2018/9/17.
 * App屏幕支持配置ext，用于配置图片支持
 */
class ScreenExt {

    //需要适配的sw尺寸，单位dp
    List<Integer> smallestWidths = new ArrayList<>()

    //图片资源密度
    List<String> mipmapDensity = new ArrayList<>()

    //基础缩放mipmap
    String designDensity

    //设计稿尺寸
    int designPx = 375


    def smallesWidths(Integer... sw){
        this.smallestWidths.addAll(sw)
    }

    def mipmapDensity(String... density){
        this.mipmapDensity.addAll(density)
    }

    def designDensity(String designDensity){
        this.designDensity = designDensity
    }

    def designPx(int designPx){
        this.designPx = designPx
    }



}
