package com.tangpj.calces

import com.tangpj.calces.extensions.ScreenExt
import com.tangpj.calces.utils.DimensConvert
import org.gradle.api.Plugin
import org.gradle.api.Project

class ScreenPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "screen"

    private designMap

    @Override
    void apply(Project target) {

        ScreenExt screenExt = target.extensions.create(EXTENSION_NAME,ScreenExt)
        designMap = new LinkedHashMap<String,String>()
        target.afterEvaluate {
            new DimensConvert(target, screenExt.dimensExt).createSwDimens()
            println(screenExt)
        }
    }
}
