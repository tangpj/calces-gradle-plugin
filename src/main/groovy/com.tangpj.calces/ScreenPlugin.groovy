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
