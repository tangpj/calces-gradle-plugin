package com.tangpj.calces.extensions

/**
 * Created by tang on 2018/6/24.
 */
class LibraryExt extends ModulesExt{
    boolean isRunAlone = false
    String runAloneSuper

    LibraryExt(String name) {
        super(name)
    }

    def name(String name){
        this.name = name
    }


    def isRunAlone(boolean isRunAlone){
        this.isRunAlone = isRunAlone
    }


    def applicationId(String applicationId){
        this.applicationId = applicationId
    }

    def runAloneSuper(String runAloneSuper){
        this.runAloneSuper = runAloneSuper
    }

    def mainActivity(String mainActivity){
        this.mainActivity = mainActivity
    }

    @Override
    String toString() {
        return "name = $name, isRunAlone = $isRunAlone, applicationId = $applicationId, " +
                "runAloneSuper = $runAloneSuper, mainActivity = $mainActivity"
    }
}
