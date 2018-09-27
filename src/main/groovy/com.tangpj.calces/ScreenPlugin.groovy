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

        def dimensTask = target.task("dimensCovert"){
            group 'calces'
            doLast{
                ScreenExt ext = target.extensions.getByName(EXTENSION_NAME) as ScreenExt
                new DimensConvertHelper(target, ext.dimensExt).createSwDimens()
            }
        }
        def mipmapTask = target.task('mipmapZoom') {
            group 'calces'
            doLast {
                ScreenExt ext = target.extensions.getByName(EXTENSION_NAME) as ScreenExt
                new MipmapZoomHelper(target, ext.mipmapExt).zoom()
            }
        }

        target.afterEvaluate {
            println(screenExt)
            if (screenExt.dimensExt.auto){
                new DimensConvertHelper(target, screenExt.dimensExt).createSwDimens()
                println(':calces:dimensCovert')
            }
            if (screenExt.mipmapExt.auto){
                new MipmapZoomHelper(target, screenExt.mipmapExt).zoom()
                println(':calces:mipmapZoom')
            }

        }


    }
}
