/*
 * Copyright 2018, The TangPj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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