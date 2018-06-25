package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tangpj.calces.extensions.AppConfigExtension
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
        AppConfigExtension appConfigExtension = getAppConfigExtension(project)
        configModules(project,appConfigExtension)
        project.afterEvaluate {
            println project.dependencies

        }
    }


    static void configModules(Project project, AppConfigExtension appConfigExtension){
        if (appConfigExtension == null){
            throw new NullPointerException("appConfigExtension is empty")
        }
        if (appConfigExtension.app == ":$project.name"){
            project.plugins.apply(AppPlugin)
            dependModules(project, appConfigExtension)
        }else {
            project.plugins.apply(LibraryPlugin)
        }
    }

    static void dependModules(Project project, AppConfigExtension appExtension){

        if (appExtension.modules != null && appExtension.modules.size() > 0){
            appExtension.modules.forEach{
                project.dependencies.add(appExtension.dependMethod, project.project(it.name))
            }
        }

    }

    AppConfigExtension getAppConfigExtension(Project project){
        println "project name = " + project.name
        try{
            return project.parent.extensions.getByName(PARENT_EXTENSION_NAME) as AppConfigExtension
        }catch (UnknownDomainObjectException ignored){
            if (project.parent != null){
                getAppConfigExtension(project.parent)
            }else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }
}
