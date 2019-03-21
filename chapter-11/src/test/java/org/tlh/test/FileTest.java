package org.tlh.test;

import java.io.File;

/**
 * Created by 离歌笑tlh/hu ping on 2019/3/21
 * <p>
 * Github: https://github.com/tlhhup
 */
public class FileTest {


    public static void main(String[] args) {
        String path="/Users/huping/Downloads/CentOS-7-x86_64-Minimal-1810.iso";
        File file=new File(path);
        System.out.println(file.exists());
    }


}
