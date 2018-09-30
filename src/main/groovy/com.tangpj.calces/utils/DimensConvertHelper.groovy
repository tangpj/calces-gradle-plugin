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

import com.tangpj.calces.extensions.DimensExt
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.gradle.api.Project

class DimensConvertHelper {

    private String designDimensPath
    private String outputGroup
    private Project project
    private DimensExt dimensExt


    DimensConvertHelper(Project project, DimensExt dimensExt){
        this.project = project
        this.dimensExt = dimensExt
        this.designDimensPath = "${project.getBuildFile().getParent()}/src/main/res/values/dimens.xml"
        this.outputGroup = "${project.getBuildFile().getParent()}/src/main/res"
    }

    def createSwDimens(){
        File designDimens = new File(designDimensPath)
        if (!designDimens.exists()){
            return
        }
        if (!designDimens.getParentFile().exists() && !designDimens.getParentFile().mkdirs()){
            println "Unable to find dimens and create fail, please manually create"
        }
        dimensExt.smallestWidths.forEach{
            StringWriter outSw = convertSwDimens(it, dimensExt.designPx, designDimens)
            outputSwDimens(outSw, it)
        }
    }

    private StringWriter convertSwDimens(int targetSw, int designPx, File designDimens){
        Map<String,String> dimensMap =  new LinkedHashMap<>()
        GPathResult resources = new XmlSlurper(false,false).parse(designDimens)
        resources.dimen.forEach{ GPathResult it ->
            String node = it.text()
            if (node.endsWith("dp") || node.endsWith("sp")){
                String unit = node.substring(node.length() - 2, node.length())
                int value = node.substring(0, node.length() - 2).toInteger()
                BigDecimal covert = BigDecimal.valueOf(targetSw / designPx * value)
                def dimens
                if (dimensExt.scale != -1){
                    dimens = covert.setScale(0, dimensExt.scale).intValue()
                }else{
                    dimens = covert.doubleValue()
                }

                String key = it.@name.text()
                dimensMap.put(key,dimens + unit)
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
