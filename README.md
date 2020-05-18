# magic
提供了下面两个核心的功能。  
1.获取其他module的功能Provider接口  
2.获取其他module的ViewHolder实例    
组件化开发，一个核心点是每个module的功能和View应该和其他的module解耦。一个module如果想调用另一个module的功能或者复用View，可以通过接口的方式调用。


## 特点
简单便捷的调用其他module的功能接口和ViewHolder实例

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
    implementation 'com.leaf:magic-provider:1.3.0'
    implementation 'com.leaf:magic-compiler:1.3.0'
    }
```
   如果是kotlin的module：
```java
   dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${KOTLIN_VERSION}"
    implementation 'com.leaf:magic-provider:1.3.0'
    kapt 'com.leaf:magic-compiler:1.3.0'
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
###### ViewHolder接口 必须有BaseViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot)构造方法
```java
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {
        super(null);
    }

    public abstract void bindView(Object baseData, int position);

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
###### ViewHolder的实现类 必须有构成方法ViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot)
```java
import com.leaf.magic.annotation.Provider;

@Provider(provider = BaseViewHolder.class, type = 100)
public class MyViewHolder extends BaseViewHolder {
    private TextView titleView;

    public MyViewHolder(LayoutInflater layoutInflater, ViewGroup root, boolean attachToRoot) {
        this(layoutInflater.inflate(R.layout.my_viewholder_layout, root, attachToRoot));
    }

    public MyViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bindView(Object baseData, int position) {
        if (baseData instanceof String) {
            String title = (String) baseData;
            Log.i("magic", "bindView " + title);
            titleView.setText(title);
        }
    }
}

// kotlin的

   constructor(layoutInflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) :
            this(layoutInflater.inflate(R.layout.my_viewholder_layout, root, attachToRoot))
            
```
#### 3. 在需要的地方调用
##### 首先在Application中初始化 Magic.getInstance(applicationContext).init()

##### 1.每次创建新的实例：create(clazz)
##### 2.没有实例就创建新实例 有就直接返回已经存在的：get(clazz)
##### 3.同一个接口，通过type区分，可以产生不同的实例：create(clazz,type) get(clazz,type)
##### 4.创建ViewHolder实例，通过type区分，可以产生不同的实例：createViewHolder(clazz,type)

#### 4. 调用的实例

###### 接口Provider的实例
```java
 MyTestProvider myTestProvider1 = (MyTestProvider) Magic.getInstance().get(MyTestProvider.class);
        if (myTestProvider1 != null) {
            Log.i("Magic", "myTestProvider1: " + myTestProvider1.getCount());
            textView1.setText(myTestProvider1.getCount());
        }

MyTestProvider myTestProvider2 = (MyTestProvider) Magic.getInstance().get(MyTestProvider.class);
        if (myTestProvider2 != null) {
            Log.i("Magic", "myTestProvider2: " + myTestProvider2.getCount());
            textView2.setText(myTestProvider2.getCount());

        }

MyTestProvider myTestProvider3 = (MyTestProvider) Magic.getInstance().create(MyTestProvider.class);
        if (myTestProvider3 != null) {
            Log.i("Magic", "myTestProvider3: " + myTestProvider3.getCount());
            textView3.setText(myTestProvider2.getCount());

        }

// 获取type=100的MyTestProvider接口实例
MyTestProvider myTestProvider4 = (MyTestProvider) Magic.getInstance().create(MyTestProvider.class, 100);
        if (myTestProvider4 != null) {
            Log.i("Magic", "myTestProvider4: " + myTestProvider4.getCount());
            textView4.setText(myTestProvider4.getCount());

        }
       
 DemoProvider demoProvider = (DemoProvider) Magic.getInstance().get(DemoProvider.class);
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

######接口ViewHolder的实例

 ```java
  BaseViewHolder baseViewHolder = (BaseViewHolder) Magic.getInstance().createViewHolder(BaseViewHolder.class, 100, getLayoutInflater(), frameLayout, false);
        if (baseViewHolder != null) {
            baseViewHolder.bindView("titleForTest", 0);
            frameLayout.addView(baseViewHolder.itemView);
        }

```








