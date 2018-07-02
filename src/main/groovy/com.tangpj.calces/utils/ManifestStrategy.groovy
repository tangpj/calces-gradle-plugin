package com.tangpj.calces.utils

import com.tangpj.calces.extensions.LibraryExt
import com.tangpj.calces.extensions.ModulesExt
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.Project

/**
 * Created by tang on 2018/6/30.
 */
abstract class ManifestStrategy {

    protected String path
    protected GPathResult manifest
    boolean edit = false

    ManifestStrategy(Project project){
        path = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
        File manifestFile = new File(path)
        if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()){
            println "Unable to find AndroidManifest and create fail, please manually create"
        }
        manifest = new XmlSlurper().parse(manifestFile)
    }

    abstract void setMainIntentFilter(def activity, boolean isFindMain)

    void resetManifest(ModulesExt moduleExt){
        if(manifest.@package != moduleExt.applicationId && moduleExt.applicationId != null && !moduleExt.applicationId.isEmpty()){
            manifest.@package = moduleExt.applicationId
            edit = true
        }

        boolean isFindMain = false
        if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()){
            manifest.application.activity.each { activity ->
                if (activity.@'android:name' == moduleExt.mainActivity){
                    def filter = activity.'intent-filter'.find{
                        it.action.@'android:name' == "android.intent.action.MAIN"
                    }
                    isFindMain = true
                    setMainIntentFilter(activity, filter != null && filter.size() > 0)
                }
            }
        }

        manifest.application.activity.each { activity ->
            def filter = activity.'intent-filter'.find{
                it.action.@'android:name' == "android.intent.action.MAIN"
            }
            if (filter != null && activity.@'android:name' != moduleExt.mainActivity){
                filter.replaceNode{}
                edit = true
            }
        }

        if (!isFindMain){
                addMainActivity(manifest.application, moduleExt)
        }

        if (edit){
            buildModulesManifest(manifest)
        }
    }

    void addMainActivity(def application, ModulesExt modulesExt){
        if (modulesExt.mainActivity != null && !modulesExt.mainActivity.isEmpty()){
            application.appendNode{
                activity('android:name': modulesExt.mainActivity){
                    'intent-filter'{
                        action('android:name':"android.intent.action.MAIN")
                        category('android:name':"android.intent.category.LAUNCHER")
                    }
                }
            }
            edit = true
        }

    }

    void buildModulesManifest(def manifest){

        def fileText = new File(path)
        StreamingMarkupBuilder outputBuilder = new StreamingMarkupBuilder()
        def root = outputBuilder.bind{
            mkp.xmlDeclaration()
            mkp.declareNamespace('android':'http://schemas.android.com/apk/res/android')
            mkp.yield manifest
        }
        String result = XmlUtil.serialize(root)
        fileText.text = result

    }
}

class AppManifestStrategy extends ManifestStrategy{

    AppManifestStrategy(Project project) {
        super(project)
    }

    @Override
    void setMainIntentFilter(def activity, boolean isFindMain) {
        if (!isFindMain){
            activity.appendNode{
                'intent-filter'{
                    action('android:name':"android.intent.action.MAIN")
                    category('android:name':"android.intent.category.LAUNCHER")}
            }
            edit = true
        }
    }

}

class LibraryManifestStrategy extends ManifestStrategy{

    LibraryManifestStrategy(Project project) {
        super(project)
    }

    @Override
    void setMainIntentFilter(def activity, boolean isFindMain) {
        if (isFindMain){
            println "build lib"
            activity.'intent-filter'.each{
                if(it.action.@'android:name' == "android.intent.action.MAIN"){
                    it.replaceNode{}
                    edit = true
                }
            }
        }
    }
}
