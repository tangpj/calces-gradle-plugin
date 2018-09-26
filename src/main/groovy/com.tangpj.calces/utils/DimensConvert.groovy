package com.tangpj.calces.utils

import com.tangpj.calces.extensions.DimensExt
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.gradle.api.Project

class DimensConvert {

    private String fileGroupFormat = "values-sw%ddp"
    private String designDimensPath
    private String outputGroup
    private Project project
    private DimensExt dimensExt



    DimensConvert( Project project, DimensExt dimensExt){
        this.project = project
        this.dimensExt = dimensExt
        this.designDimensPath = "${project.getBuildFile().getParent()}/src/main/res/values/dimens.xml"
        this.outputGroup = "${project.getBuildFile().getParent()}/src/main/res"
    }

    def createSwDimens(){
        File designDimens = new File(designDimensPath)
        if (!designDimens.getParentFile().exists() && !designDimens.getParentFile().mkdirs()){
            println "Unable to find dimens and create fail, please manually create"
        }
        dimensExt.smallestWidths.forEach{
            StringWriter outSw = convertSwDimens(it, dimensExt.designPx, designDimens)
            outputSwDimens(outSw, it)
        }
    }

    private static StringWriter convertSwDimens(int targetSw, int designPx, File designDimens){
        Map<String,String> dimensMap =  new LinkedHashMap<>()
        GPathResult resources = new XmlSlurper(false,false).parse(designDimens)
        resources.dimen.forEach{ GPathResult it ->
            String node = it.text()
            if (node.endsWith("dp") || node.endsWith("sp")){
                String unit = node.substring(node.length() - 2, node.length())
                int value = node.substring(0, node.length() - 2).toInteger()
                def covert = targetSw / designPx * value
                String key = it.@name.text()
                dimensMap.put(key,covert + unit)
            }
        }
        def out =new StringWriter()
        def xml = new MarkupBuilder(out)
        xml.resources{
            mkp.yieldUnescaped "\n  <!-- sw${targetSw}dp -->"
            dimensMap.forEach{ String key, String value ->
                dimen(name:key, value)
            }
        }
        return out
    }

    private void outputSwDimens(StringWriter sw, int targetSw){
        def outputGroupPath = "$outputGroup/values-sw${targetSw}dp"
        def outputSwPath = "$outputGroup/values-sw${targetSw}dp/dimens.xml"
        def outputGroupFile = new File(outputGroupPath)
        if (!outputGroupFile.exists()) {
            outputGroupFile.mkdirs()
        }
        File outputSwFile = new File(outputSwPath)
        outputSwFile.text = sw.toString()
    }
}
