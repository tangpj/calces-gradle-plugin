package com.tangpj.calces

import com.tangpj.calces.extensions.ScreenExt
import com.tangpj.calces.utils.DimensConvertHelper
import com.tangpj.calces.utils.MipmapZoomHelper
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
            if (screenExt.dimensExt.auto){
                new DimensConvertHelper(target, screenExt.dimensExt).createSwDimens()
            }
            if (screenExt.mipmapExt.auto){
                new MipmapZoomHelper(target, screenExt.mipmapExt).zoom()
            }
            println(screenExt)


        }
    }
}
