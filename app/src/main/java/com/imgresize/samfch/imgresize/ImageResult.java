package com.imgresize.samfch.imgresize;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by samfch on 8/2/16.
 */
class ImageResult implements Serializable, Comparable {
    private String imgUrl;
    private String percentSimilarity;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPercentSimilarity() {
        return percentSimilarity;
    }

    public void setPercentSimilarity(String percentSimilarity) {
        this.percentSimilarity = percentSimilarity;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareage= Integer.parseInt(((ImageResult) o).getPercentSimilarity());
        /* For Ascending order*/
//        return Integer.parseInt(this.percentSimilarity) - compareage;

        /* For Descending order do like this */
        return compareage - Integer.parseInt(this.percentSimilarity);
    }
}
