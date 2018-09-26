package com.tangpj.calces.utils


import javax.activation.MimetypesFileTypeMap

class ImageCheckHelper {

    private  MimetypesFileTypeMap mtFtp

    public ImageCheckHelper(){
        mtFtp = new MimetypesFileTypeMap()
        mtFtp.addMimeTypes("image png tif jpg jpeg bmp")
    }

    public  boolean isImage(File file){
        String mimeType = mtFtp.getContentType(file)
        String type = mimeType.split("/")[0]
        return type == "image"
    }

}