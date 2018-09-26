package com.tangpj.calces.extensions

class MipmapExt {

    //基础缩放mipmap
    String designDensity

    //图片资源密度
    Set<String> convertDensity = new ArrayList<>()

    boolean auto = false

    def designDensity(String designDensity){
        this.designDensity = designDensity
    }

    def mipmapDensity(String... density){
        this.convertDensity.addAll(density)
    }

    def auto(boolean auto){
        this.auto = auto
    }

    @Override
    String toString() {
        return "auto: $auto, designDensity: $designDensity, corvent density: ${convertDensity.join(",")}"
    }
}
