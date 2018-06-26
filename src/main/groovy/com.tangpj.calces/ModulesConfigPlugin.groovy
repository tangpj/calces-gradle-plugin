package com.tangpj.calces

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tangpj.calces.extensions.AppConfigExtension
import com.tangpj.calces.extensions.ModuleExtension
import kotlin.Unit
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

/**
 * Created by tang on 2018/6/16.
 */
class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    boolean isDebug = false

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions = getAppConfigExtension(project)
        configModules(project,appConfigExtensions)
    }

    void configModules(Project project,  NamedDomainObjectContainer<AppConfigExtension>  appConfigExtensions){
        if (appConfigExtensions == null){
            throw new NullPointerException("app is empty")
        }
        String appName = ""
        List<AppConfigExtension> filterList = appConfigExtensions.stream().filter(){
            appName = it.name.startsWith(':') ? it.name : new String(":" + it.name)
            isDebug = isDebug || it.debug
            appName.endsWith(project.name)
        }.collect()

        if (filterList != null && filterList.size() > 0){
            AppConfigExtension appConfigExtension = filterList.get(0)
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(appConfigExtension.applicationId)
            dependModules(project, appConfigExtension, appName)
        }else if (!(isDebug && modulesRunAlone(project,appConfigExtensions))){
            project.plugins.apply(LibraryPlugin)
        }

    }

    void dependModules(Project project, AppConfigExtension appExtension, String appName){
        if (isDebug){
            println("build debug app: [$appName]")
            return
        }

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

    private static boolean modulesRunAlone(Project project, NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions){
        Long count = appConfigExtensions
                .stream()
                .flatMap{it.modules.stream()}
                .filter{
                    boolean filter = it.name.endsWith(project.name)
                    filter
                }
                .filter{ it.isRunAlone }.map{
                    AppPlugin appPlugin = project.plugins.apply(AppPlugin)
                    appPlugin.extension.defaultConfig.setApplicationId(it.runAloneId)
                    println("build run alone modules: [$it.name]")
                    it.name
                }.count()
        return count > 0L

    }
}
