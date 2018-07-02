/*
 * Copyright (C) 2018 Tang
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

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tangpj.calces.extensions.AppConfigExt
import com.tangpj.calces.extensions.AppExt
import com.tangpj.calces.extensions.LibraryExt
import com.tangpj.calces.utils.AppManifestStrategy
import com.tangpj.calces.utils.LibraryManifestStrategy
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

/**
 * Created by tang on 2018/6/16.
 */
class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        AppConfigExt appConfigExt = getAppConfigExtension(project)
        configModules(project, appConfigExt)
    }

    static void configModules(Project project, AppConfigExt appConfigExt){
        if (appConfigExt == null){
            throw new NullPointerException("can not find appConfig")
        }
        List<AppExt> filterList = appConfigExt.apps.stream()
                .filter{ (it.name.startsWith(':') ? it.name : new String(":" + it.name)).endsWith(project.name) }
                .skip(0).collect()

        if (filterList != null && filterList.size() > 0){
            AppExt appExt = filterList.get(0)
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(appExt.applicationId)
            new AppManifestStrategy(project).resetManifest(appExt)
            dependModules(project, appExt, appConfigExt)
        }else {
            modulesRunAlone(project,appConfigExt.modules, appConfigExt.debugEnable)
        }

    }

    static void dependModules(Project project, AppExt appExt, AppConfigExt appConfigExt){
        List<LibraryExt> moduleExtList = appConfigExt.modules.stream().filter{
            modules ->
                String modulesName = appExt.modules.stream().find{ it.contains(modules.name) }
                modulesName != null && !modulesName.isEmpty()
        }.skip(0).collect()

        if (appExt.modules != null && appExt.modules.size() > 0){
            List<String> modulesList = appExt.modules.stream()
                    .filter{appConfigExt.debugEnable ? (moduleExtList != null && !moduleExtList.get(0).isRunAlone) : true }
                    .map{
                         project.dependencies.add(appExt.dependMethod, project.project(it))
                         it
            }.collect()
            println("build app: [$appExt.name] , depend modules: $modulesList")
        }
    }

    AppConfigExt getAppConfigExtension(Project project){
        try{
            return project.parent.extensions.getByName(PARENT_EXTENSION_NAME) as AppConfigExt
        }catch (UnknownDomainObjectException ignored){
            if (project.parent != null){
                getAppConfigExtension(project.parent)
            }else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }

    private static void modulesRunAlone(Project project, NamedDomainObjectContainer<LibraryExt> modules, boolean isDebug){
        List<LibraryExt> filterList = modules.stream().filter{ it.name.endsWith(project.name) }.skip(0).collect()
        if (filterList != null && filterList.size() > 0){
            LibraryExt moduleExt = filterList.get(0)

            if (isDebug && moduleExt.isRunAlone){
                AppPlugin appPlugin = project.plugins.apply(AppPlugin)
                appPlugin.extension.defaultConfig.setApplicationId(moduleExt.applicationId)
                if (moduleExt.runAloneSuper != null && !moduleExt.runAloneSuper.isEmpty()){
                    project.dependencies.add("implementation", project.project(moduleExt.runAloneSuper))
                    println("build run alone modules: [$moduleExt.name], runSuper = $moduleExt.runAloneSuper")
                }else{
                    println("build run alone modules: [$moduleExt.name]")
                }
                if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()){
                    new AppManifestStrategy(project).resetManifest(moduleExt)
                }
            }else{
                project.plugins.apply(LibraryPlugin)
                new LibraryManifestStrategy(project).resetManifest(moduleExt)
            }
        }

    }

}

