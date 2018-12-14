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
package com.tangpj.calces.utils

import com.tangpj.calces.extensions.MipmapExt
import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap

import javax.imageio.ImageIO
import java.awt.Image
import java.awt.image.BufferedImage
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
        this.mipmapGroupPathFormat =  "${project.getBuildFile().getParent()}/src/main/res/$mipmapExt.folder-"

    }

    /**
     * 对外提供图片压缩处理功能
     */
    public void zoom(){
        if (!scaleMap.containsKey(mipmapExt.designDensity)) return
        File designGroup = new File("$mipmapGroupPathFormat$mipmapExt.designDensity")
        if (!designGroup.exists())
            throw new UnknownError("Unable to find ${designGroup.name}, please check your configuration")
        Map<String, Set<String>> mipmapMap = new HashMap<>()
        Set<String> mipmapList = new HashSet<>()
        List<String> covertList = new ArrayList<>(mipmapExt.convertDensity)
        covertList = covertList
                .stream()
                .filter{ scaleMap.get(it) < scaleMap.get(mipmapExt.designDensity) }
                .collect(Collectors.toList()) as List<String>
        mipmapList.addAll(covertList)
        mipmapList.add(mipmapExt.designDensity)
        mipmapList.stream().filter{ scaleMap.containsKey(it) }
                .forEach{
            if (it == mipmapExt.designDensity){
                mipmapMap.put(mipmapExt.designDensity, initImageSet(designGroup))
            }else{
                File convertGroup = new File("$mipmapGroupPathFormat$it")
                if (!convertGroup.exists()) convertGroup.mkdir()
                mipmapMap.put(it, initImageSet(convertGroup))
            }
        }
        realZoom(mipmapMap)

    }

    /**
     * 实际图片缩放方法
     */
    private void realZoom(Map<String, Set<String>> mipmapMap){
        Set<String> designMipmapSet = mipmapMap.get(mipmapExt.designDensity)
        mipmapMap.entrySet()
                .stream()
                .filter{ it.key != mipmapExt.designDensity}
                .forEach{ filterExistImage(it.key, it.value, designMipmapSet)}
    }

    /**
     * 过滤已生成的图片
     */
    private void filterExistImage(String destDensity, Set<String> mipmapSet, Set<String> designMipmapSet){
        designMipmapSet.stream()
                .filter{ !mipmapSet.contains(it) }
                .forEach{ imageZoom(destDensity, it) }
    }

    /**
     * 图片缩放并保存到对应的mipmap文件夹中
     */
    private void imageZoom(String destDensity, String originalImagePath){
        String srcPath = "$mipmapGroupPathFormat$mipmapExt.designDensity/$originalImagePath"
        String destPath = "$mipmapGroupPathFormat$destDensity/$originalImagePath"

        File srcFile = new File(srcPath)
        File destFile = new File(destPath)

        BufferedImage im = ImageIO.read(srcFile)

        int width = im.getWidth()
        int height = im.getHeight()

        BigDecimal srcScale =  new BigDecimal( scaleMap.get(mipmapExt.designDensity))
        BigDecimal destScale =  new BigDecimal( scaleMap.get(destDensity))


        int toWidth = (new BigDecimal(width) / srcScale * destScale).intValue()
        int toHeight = (new BigDecimal(height) / srcScale * destScale).intValue()

        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_ARGB)
        result.getGraphics().drawImage( im.getScaledInstance(toWidth, toHeight,Image.SCALE_SMOOTH), 0, 0, null)
        ImageIO.write(result, "png", destFile)
    }

    /**
     * 读取对应图片目录，把已存在的图片名保存起来
     * 用于后续过滤已存在图片
     */
    private Set<String> initImageSet(File group){
        File[] mipmapArray = group.listFiles()
        Set<String> designMipmap = mipmapArray.toList().stream()
                .filter{ helper.isImage(it) }
                .map{ it.name }
                .collect(Collectors.toSet())
        return designMipmap
    }


    
}
