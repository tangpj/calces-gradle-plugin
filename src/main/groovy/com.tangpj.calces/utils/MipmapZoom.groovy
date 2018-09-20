package com.tangpj.calces.utils

import org.gradle.api.Project


/**
 * Created by tang on 2018/9/19.
 * 把高分辨率的图片转换成低分辨率的
 */
class MipmapZoom {

    String designDensity
    Set<String> mipmapDensity = new ArrayList<>()
    Project project

    MipmapZoom(Project project,String designDensity, Set<String> mipmapDensity = new ArrayList<>()){
        this.designDensity = designDensity
        this.mipmapDensity = mipmapDensity
        this.project = project
    }

    
}
