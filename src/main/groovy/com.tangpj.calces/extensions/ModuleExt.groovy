package com.tangpj.calces.extensions

/**
 * Created by tang on 2018/6/24.
 */
class ModuleExt {
    String name
    boolean isRunAlone = false
    String runAloneId
    String runAloneSuper
    String runAloneActivity

    ModuleExt(String name){
        this.name = name
    }

    def name(String name){
        this.name = name
    }


    def isRunAlone(boolean isRunAlone){
        this.isRunAlone = isRunAlone
    }


    def runAloneId(String runAloneId){
        this.runAloneId = runAloneId
    }

    def runAloneSuper(String runAloneSuper){
        this.runAloneSuper = runAloneSuper
    }

    def runAloneActivity(String runAloneActivity){
        this.runAloneActivity = runAloneActivity
    }

    @Override
    String toString() {
        return "name = $name, isRunAlone = $isRunAlone, runAloneId = $runAloneId, " +
                "runAloneSuper = $runAloneSuper, runAloneActivity = $runAloneActivity"
    }
}
