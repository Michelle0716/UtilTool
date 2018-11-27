# UtilTool
一些工具类整理：加密、转换、log、sp等


# 集成：
## 项目的根目录
```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
        google()
    }
}
```

## app中
```
dependencies {
    compile 'com.github.Michelle0716:UtilTool:V1.1'
}
```

### 注意
#### 把app里面的编译最小版本和目标版本改为以下情况，以免冲突
```
minSdkVersion 15
targetSdkVersion 26
```

#  调用

![image](https://github.com/Michelle0716/UtilTool/blob/master/pic/method.jpeg)


