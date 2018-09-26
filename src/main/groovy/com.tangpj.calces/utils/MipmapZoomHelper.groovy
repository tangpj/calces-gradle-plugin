package com.tangpj.calces.utils

import com.google.common.collect.ImmutableMap
import com.tangpj.calces.extensions.MipmapExt
import org.gradle.api.Project

import java.util.stream.Collectors


/**
 * Created by tang on 2018/9/19.
 * 把高分辨率的图片转换成低分辨率的
 */
class MipmapZoomHelper {

    MipmapExt mipmapExt

    Project project

    private String mipmapGroupPathFormat

    ImageCheckHelper helper = new ImageCheckHelper()

    static final Map<String, Double> scaleMap = ImmutableMap.<String, Double>builder()
            .put("ldpi", 0.75)
            .put("mdpi", 1)
            .put( "hdpi", 1.5)
            .put( "xhdpi", 2)
            .put( "xxhdpi", 3)
            .put( "xxxhdpi", 4)
            .build()

    MipmapZoomHelper(Project project, MipmapExt mipmapExt){
        this.mipmapExt = mipmapExt
        this.project = project
        this.mipmapGroupPathFormat =  "${project.getBuildFile().getParent()}/src/main/res/mipmap-"


    }

    public void zoom(){
        File designGroup = new File("$mipmapGroupPathFormat$mipmapExt.designDensity")
        if (!designGroup.exists()) return

        Map<String, Set<String>> mipmapMap = new HashMap<>()
        List<String> mipmapList = new ArrayList<>()
        mipmapList.addAll(mipmapExt.convertDensity)
        mipmapList.add(mipmapExt.designDensity)
        mipmapList.forEach{
            if (it == mipmapExt.designDensity){
                mipmapMap.put(mipmapExt.designDensity, initImageSet(designGroup))
            }else{
                File convertGroup = new File("$mipmapGroupPathFormat$it")
                if (!convertGroup.exists()) convertGroup.mkdir()
                mipmapMap.put(it, initImageSet(convertGroup))
            }
        }

    }

    private void readZoom(Map<String, Set<String>> mipmapMap){
        Set<String> designMipmapSet = mipmapMap.get(mipmapExt.designDensity)
    }

    private Set<String> initImageSet(File group){
        File[] mipmapArray = group.listFiles()
        Set<String> designMipmap = mipmapArray.toList().stream()
                .filter{ helper.isImage(it) }
                .map{ it.name }
                .collect(Collectors.toSet())
        return designMipmap
    }


    
}
