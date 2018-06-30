package com.tangpj.calces.utils

import com.tangpj.calces.extensions.ModuleExt
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

/**
 * Created by tang on 2018/6/30.
 */
abstract class ManifestStrategy {

    protected String path
    boolean edit = false

    ManifestStrategy(String path){
        this.path = path
    }

    abstract void setMainIntentFilter(def activity, boolean isFindMain)

    void resetManifest(ModuleExt moduleExt, def manifest){
        if(manifest.@package != moduleExt.runAloneId){
            manifest.@package = moduleExt.runAloneId
            edit = true
        }

        if (!moduleExt.runAloneActivity.isEmpty()){
            manifest.application.activity.each { activity ->
                if (activity.@'android:name' == moduleExt.runAloneActivity){
                    def filter = activity.'intent-filter'.find{
                        it.action.@'android:name' == "android.intent.action.MAIN"
                    }
                    setMainIntentFilter(activity, filter != null && filter.size() > 0)
                }else {
                    throw new IllegalArgumentException("can not found runAloneActivity: $moduleExt.runAloneActivity")
                }
            }
        }

        if (edit){
            buildModulesManifest(manifest)
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

    AppManifestStrategy(String path) {
        super(path)
    }

    @Override
    void setMainIntentFilter(def activity, boolean isFindMain) {
        if (!isFindMain){
            println "build app"
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

    LibraryManifestStrategy(String path) {
        super(path)
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
