package com.huangliang.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Map;

import com.huangliang.api.exception.ServiceException;
import org.yaml.snakeyaml.Yaml;

public class YmlUtils {
    public static void updateYamlFile(String path,String key,String value) {
//        String src = "src/main/resources/application.yml";
        Yaml yaml = new Yaml();
        FileWriter fileWriter = null;
        //层级map变量
        Map<String, Object>  resultMap;
        try {
            //读取yaml文件，默认返回根目录结构
            resultMap = yaml.load(new FileInputStream(new File(path)));
            resultMap.put(key,value);
            //字符输出
            fileWriter = new FileWriter(new File(path));
            //用yaml方法把map结构格式化为yaml文件结构
            fileWriter.write(yaml.dumpAsMap(resultMap));
            //刷新
            fileWriter.flush();
            //关闭流
            fileWriter.close();
        } catch (Exception e) {
            throw new ServiceException("yaml文件初始化失败！");
        }
    }
}
