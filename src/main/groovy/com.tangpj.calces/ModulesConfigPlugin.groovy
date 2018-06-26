package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.tools.r8.logging.Log
import com.tangpj.calces.extensions.AppConfigExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.stringtemplate.v4.ST

/**
 * Created by tang on 2018/6/16.
 */
class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions = getAppConfigExtension(project)
        configModules(project,appConfigExtensions)
        project.afterEvaluate {

        }
    }


    static void configModules(Project project,  NamedDomainObjectContainer<AppConfigExtension>  appConfigExtensions){
        if (appConfigExtensions == null){
            throw new NullPointerException("app is empty")
        }
        appConfigExtensions.forEach{
            String appName = it.name.startsWith(':') ? it.name : new String(":" + it.name)
            if (appName == ":$project.name"){
                project.plugins.apply(AppPlugin)
                dependModules(project, it, appName)
            }else {
                project.plugins.apply(LibraryPlugin)
            }
        }

    }

    static void dependModules(Project project, AppConfigExtension appExtension, String appName){
        if (appExtension.modules != null && appExtension.modules.size() > 0){
            List<String> modulesList = appExtension.modules.stream().map{
                project.dependencies.add(appExtension.dependMethod, project.project(it.name))
                it.name
            }.collect()

            println("build app: [$appName] , depend modules: $modulesList")
        }

    }

    NamedDomainObjectContainer<AppConfigExtension> getAppConfigExtension(Project project){
        try{
            return project.parent.extensions.getByName(PARENT_EXTENSION_NAME) as
                    NamedDomainObjectContainer<AppConfigExtension>
        }catch (UnknownDomainObjectException ignored){
            if (project.parent != null){
                getAppConfigExtension(project.parent)
            }else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }
}
