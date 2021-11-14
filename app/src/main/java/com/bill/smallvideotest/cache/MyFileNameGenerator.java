package com.bill.smallvideotest.cache;

import com.bill.videocache.file.FileNameGenerator;

/**
 * Created by Bill on 2021/11/14.
 */

public class MyFileNameGenerator implements FileNameGenerator {

    public String generate(String url) {
        return url.substring(url.lastIndexOf("/"));
    }
}
