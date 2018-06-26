package com.tangpj.calces.extensions

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/14.
 */
class AppConfigExtension {
    String name
    String applicationId
    String dependMethod = "implementation"
    boolean debug = false
    NamedDomainObjectContainer<ModuleExtension> modules

    AppConfigExtension(String name){
        this.name = name
    }

    AppConfigExtension(Project project){
        modules = project.container(ModuleExtension)
    }

    def name(String name){
        this.name = name
    }

    def applicationId(String applicationId){
        this.applicationId = applicationId
    }

    def dependMethod(String dependMethod){
        this.dependMethod = dependMethod
    }

    def debug(boolean debug){
        this.debug = debug
    }

    def modules(Closure closure){
        this.modules.configure(closure)
    }

    @Override
    String toString() {
        return "app = $name, applicationId = $applicationId, dependMethod = $dependMethod  debug = $debug\n" +
                "modules: ${modules.isEmpty()? "is empty" : "$modules"}"
    }
}
