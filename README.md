# magic
通过一个接口去封装一个module对外提供的所有功能，这个接口的实现也是在自己的module中实现，这样使的每个module的能力更清晰明确。
在其他的组件中可以通过本项目提供的库去直接调用这个接口的实现。


## 特点
简单便捷的调用其他module的功能接口。

## 使用:
#### 在需要使用的module的build gradle中增加moduleName和两个依赖
```java
    defaultConfig 
          javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
                arguments = [moduleName: project.getName()]
            }
        }
    }
    
    dependencies {
    implementation 'com.leaf:magic-provider:1.0.1'
    implementation 'com.leaf:magic-compiler:1.0.3'
    }
```
   如果是kotlin的module：
```java
   dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${KOTLIN_VERSION}"
    implementation 'com.leaf:magic-provider:1.0.1'
    kapt 'com.leaf:magic-compiler:1.0.3'
}
    
```
#### 1. 需要注解实现的接口

```java
public interface MyTestProvider {
    String getCount();
}

//kotlin：
interface DemoProvider {
    fun getDemoName(): String
}

```

#### 2. 注解接口的实现 可以通过type区分相同接口的不同实例

```java
import com.leaf.magic.annotation.Provider;
@Provider(provider = MyTestProvider.class)
public class MyTestProviderImpl implements MyTestProvider {
    private int count = 100;

    @Override
    public String getCount() {
        count += 100;
        return "MyTestProvider count = " + count;
    }
}

//同一个接口 可以有不同的实现 需要通过type区分
@Provider(provider = MyTestProvider.class，type=100)
public class MyTestProviderImpl implements MyTestProvider {
    private int count = 100;

    @Override
    public String getCount() {
        count += 100;
        return "MyTestProvider count = " + count;
    }
}

//kotlin
import com.leaf.magic.annotation.Provider
@Provider(provider = DemoProvider::class)
class DemoProviderImpl : DemoProvider {
    override fun getDemoName(): String {
        return "module name is demo"
    }
}

```

#### 3. 在需要的地方调用

##### 1.每次都需要新的实例：generateServiceInstance(clazz)
##### 2.没有实例就新实例化一个 有就直接返回已经存在的：getServiceInstance(clazz)
##### 3.同一个接口，通过type区分，可以产生不同的实例：generateServiceInstance(clazz,type) getServiceInstance(clazz,type)

#### 4. 调用的实例

```java
 MyTestProvider myTestProvider1 = (MyTestProvider) Magic.getInstance().getServiceInstance(MyTestProvider.class);
        if (myTestProvider1 != null) {
            Log.i("Magic", "myTestProvider1: " + myTestProvider1.getCount());
            textView1.setText(myTestProvider1.getCount());
        }

MyTestProvider myTestProvider2 = (MyTestProvider) Magic.getInstance().getServiceInstance(MyTestProvider.class);
        if (myTestProvider2 != null) {
            Log.i("Magic", "myTestProvider2: " + myTestProvider2.getCount());
            textView2.setText(myTestProvider2.getCount());

        }

MyTestProvider myTestProvider3 = (MyTestProvider) Magic.getInstance().generateServiceInstance(MyTestProvider.class);
        if (myTestProvider3 != null) {
            Log.i("Magic", "myTestProvider3: " + myTestProvider3.getCount());
            textView3.setText(myTestProvider2.getCount());

        }

// 获取type=100的MyTestProvider接口实例
MyTestProvider myTestProvider4 = (MyTestProvider) Magic.getInstance().generateServiceInstance(MyTestProvider.class, 100);
        if (myTestProvider4 != null) {
            Log.i("Magic", "myTestProvider4: " + myTestProvider4.getCount());
            textView4.setText(myTestProvider4.getCount());

        }
       
 DemoProvider demoProvider = (DemoProvider) Magic.getInstance().getServiceInstance(DemoProvider.class);
        if (demoProvider != null) {
            Log.i("Magic", "demo module: " + demoProvider.getDemoName());
            textView5.setText(demoProvider.getDemoName());
        }

```

###### 结果：
```java
I/Magic: myTestProvider1: MyTestProvider count = 200
I/Magic: myTestProvider2: MyTestProvider count = 400
I/Magic: myTestProvider3: MyTestProvider count = 200
I/Magic: myTestProvider4: MyTestProviderImpl2 count = 10000
I/Magic: demo module: module name is demo

```

 








