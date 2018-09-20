package com.tangpj.calces.extensions

class MipmapExt {

    //基础缩放mipmap
    String designDensity

    //图片资源密度
    Set<String> convertDensity = new ArrayList<>()

    def designDensity(String designDensity){
        this.designDensity = designDensity
    }

    def mipmapDensity(String... density){
        this.convertDensity.addAll(density)
    }

    @Override
    String toString() {
        return "designDensity: $designDensity, corvent density: ${convertDensity.join(",")}"
    }
}
