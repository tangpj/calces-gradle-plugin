package com.tangpj.calces

import com.tangpj.calces.extensions.AppConfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Created by tang on 2018/6/12.
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        AppConfigExtension appConfigExtension = new AppConfigExtension(project)
        project.extensions.add(EXTENSION_NAME, appConfigExtension)
        configApp(project, appConfigExtension)
    }

    static void configApp(Project project, AppConfigExtension appConfigExtension) {
        List<String> moduleList = new ArrayList<>()
        project.afterEvaluate {
            appConfigExtension = project.extensions.getByName(EXTENSION_NAME) as AppConfigExtension
            println appConfigExtension

            checkoutModules(appConfigExtension,moduleList)

        }

        initChildModules(moduleList, project)
        println "child modules = $moduleList"
    }

    static void initChildModules(List<String> moduleList ,Project project){

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

    static void checkoutModules(AppConfigExtension appConfigExtension,
                                List<String> moduleList){
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (moduleList != null){
            modulesSet.addAll(moduleList)
        }
        List<String> notFoundList = new ArrayList<>()

        if (appConfigExtension.app != null && !appConfigExtension.app.isEmpty()){
            configSet.add(appConfigExtension.app)
        }else{
            configSet.add(":$appConfigExtension.name")
        }
        List<String> moduleExtensionList = appConfigExtension.modules.
                toList().
                stream().
                map{ it.name }.
                collect()

        if (moduleExtensionList != null && !moduleExtensionList.isEmpty()){
            configSet.addAll(moduleExtensionList)
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
        println "build list = " + configSet
    }



}
