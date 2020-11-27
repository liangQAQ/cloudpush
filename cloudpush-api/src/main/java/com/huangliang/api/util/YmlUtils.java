package com.huangliang.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Map;

import com.huangliang.api.exception.NetException;
import org.yaml.snakeyaml.Yaml;

public class YmlUtils {
    public static void updateYamlFile(String path,String key,String value) throws NetException {
        Yaml yaml = new Yaml();
        FileWriter fileWriter = null;
        //层级map变量
        Map<String, Object>  resultMap;
        try {
            File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());

            //读取yaml文件，默认返回根目录结构
            resultMap = yaml.load(new FileInputStream(file));
            resultMap.put(key,value);
            //字符输出
            fileWriter = new FileWriter(file);
            //用yaml方法把map结构格式化为yaml文件结构
            fileWriter.write(yaml.dumpAsMap(resultMap));
            //刷新
            fileWriter.flush();
            //关闭流
            fileWriter.close();
        } catch (Exception e) {
            throw new NetException("yaml文件初始化失败！");
        }
    }
}
