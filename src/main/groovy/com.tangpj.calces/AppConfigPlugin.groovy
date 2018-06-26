package com.tangpj.calces

import com.tangpj.calces.extensions.AppConfigExtension
import com.tangpj.calces.extensions.ModuleExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/12.
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions = project.container(AppConfigExtension)
        appConfigExtensions.all {
            modules = project.container(ModuleExtension)
        }
        project.extensions.add(EXTENSION_NAME, appConfigExtensions)
        configApp(project)
    }

    void configApp(Project project) {
        List<String> moduleList = new ArrayList<>()
        NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions
        project.afterEvaluate {
            appConfigExtensions = project.extensions.getByName(EXTENSION_NAME) as
                    NamedDomainObjectContainer<AppConfigExtension>
            checkRepeatApp(appConfigExtensions)
            checkModules(appConfigExtensions,moduleList)

        }
        initChildModules(moduleList, project)
        println("project child modules: $moduleList")
    }

    void initChildModules(List<String> moduleList ,Project project){

        if (project.childProjects.isEmpty()){
            moduleList.add(project.toString()
                    .replace("project ","")
                    .replace('\'',''))
            return
        }
        project.childProjects.entrySet().forEach{
            initChildModules(moduleList, it.value)
        }

    }

    static void checkRepeatApp(NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions){
        Map<String,List<AppConfigExtension>> groupMap =
                appConfigExtensions.groupBy{ it.name.startsWith(':') ? it.name : new String(":" + it.name)}
        groupMap.forEach{
            k,v ->
                if (v.size() > 1){
                    throw new IllegalArgumentException("app is repeat. app name =  [$k]")
                }
        }

    }

    static void checkModules(NamedDomainObjectContainer<AppConfigExtension> appConfigExtensions,
                             List<String> moduleList){
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (moduleList != null){
            modulesSet.addAll(moduleList)
        }
        List<String> notFoundList = new ArrayList<>()

        List<String> appList = appConfigExtensions
                .stream()
                .map{it.name.startsWith(':') ? it.name : new String(":" + it.name)}.collect()
        appConfigExtensions.forEach{
            String appName = it.name.startsWith(':') ? it.name : new String(":" + it.name)
            configSet.add(appName)
            List<String> moduleExtensionList =
                    it.modules.
                            stream().
                            map{
                                if (appList.contains(it.name)){
                                    throw new IllegalArgumentException("$it.name already configured " +
                                            "as an application, please check appConfig")
                                }
                                it.name
                            }.
                            collect()

            if (moduleExtensionList != null && !moduleExtensionList.isEmpty()){
                configSet.addAll(moduleExtensionList)
            }
        }

        configSet.forEach{
            if(!modulesSet.contains(it)){
                notFoundList.add(it)
            }
        }
        if (notFoundList.size() > 0){
            throw  new IllegalArgumentException(
                    "not fount modules = " + notFoundList
            )
        }
        println("modules: " + configSet)
    }



}
