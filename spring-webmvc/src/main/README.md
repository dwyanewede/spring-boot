## Spring结合Tomcat和Jndi实现数据源外部化配置

## jndi官方描述

### 什么是jndi

JNDI(Java Naming and Directory Interface,Java命名和目录接口)是SUN公司提供的一种标准的Java命名系统接口，JNDI提供统一的客户端API，通过不同的访问提供者接口JNDI服务供应接口(SPI)的实现，由管理者将JNDI API映射为特定的命名服务和目录系统，使得Java应用程序可以和这些命名服务和目录服务之间进行交互。目录服务是命名服务的一种自然扩展。两者之间的关键差别是目录服务中对象不但可以有名称还可以有属性（例如，用户有email地址），而命名服务中对象没有属性。

### 如何使用jndi创建上下文
JNDI API 定义了一个上下文, 该上下文指定在何处查找对象。初始上下文通常用作起点。在最简单的情况下, 必须使用实现所需的特定实现和额外参数创建初始上下文。初始上下文将用于查找名称。初始上下文类似于文件系统的目录树的根或顶部。
#### 实现方案一
```java
var contextArgs = new Hashtable<String, String>();

// 设置参数，不同的服务提供商有不同的实现
contextArgs.put( Context.INITIAL_CONTEXT_FACTORY, "com.jndiprovider.TheirContextFactory" );

// 设置url明确数据存储位置
contextArgs.put( Context.PROVIDER_URL, "jndiprovider-database" );

// 此处可以实现安全校验 等

// 创建context
Context myCurrentContext = new InitialContext(contextArgs);
```

```java
// 在上行下文昭寻找对应的beanName
MyBean myBean = (MyBean)  myCurrentContext.lookup("com.mydomain.MyBean");
```

#### 实现方案二
```java
// 还可以通过添加 jndi 来配置上下文对象。类路径中包含初始上下文工厂类名和提供程序 URL 的属性文件
// 仅需要创建上下文context，将会去类路径下读取配置文件 
Context myCurrentContext = new InitialContext();
```

```java
// 从上下文中寻找初始化完成的bean信息
MyBean myBean = (MyBean)  myCurrentContext.lookup("com.mydomain.MyBean");
```

## spring结合tomcat实现jndi注入

### 配置 jndi resource数据源
在 `apache-tomcat-8.0.38\conf\context.xml`中配置
```xml
<Resource name="jdbc/sxsDB"       
	 auth="Container"
	 type="javax.sql.DataSource"
	 driverClassName="com.mysql.jdbc.Driver"
	 url="jdbc:mysql://localhost:3306/testdb"    
	 username="root"
	 password="123456"
	 maxIdle="40"
	 maxWait="4000"
	 maxActive="250"
	 removeAbandoned="true" 
	 removeAbandonedTimeout="180"
	 logAbandoned="true"/>

```

### 配置 maven 相关依赖
```xml
    <!-- mybatis -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.3.0</version>
    </dependency>
    
    <!--Spring java数据库访问包，在本例中主要用于提供数据源 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <!--mysql数据库驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.38</version>
    </dependency>
    <!-- mybatis ORM框架 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.4.1</version>
    </dependency>
    
    <!-- fastjson -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.30</version>
    </dependency>
    <!-- druid -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.6</version>
    </dependency>
```


### 配置数据库连接
```java
@Configuration
@MapperScan("com.sxs.web.mapper")
public class MybatisConfiguration {
    @Bean
    public DataSource dataSource() throws ClassNotFoundException, NamingException {
        // 方式一
//        Context initContext = new InitialContext();
//        Context envContext  = (Context)initContext.lookup("java:/comp/env");
//        DataSource dataSource = (DataSource)envContext.lookup("jdbc/sxsDB");

        // 方式二
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
        JndiTemplate jndiTemplate = factoryBean.getJndiTemplate();
        DataSource dataSource = jndiTemplate.lookup("java:/comp/env/jdbc/sxsDB", DataSource.class);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.sxs.web.model");
        //   "classpath*:sqlmap/*-mapper.xml"
        sqlSessionFactoryBean.setMapperLocations(new Resource[]{});
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
```

### 配置映射实体

```java
public class Course implements Serializable {

    private static final long serialVersionUID = 1848918888438459623L;
    private String course_name;
    private String stu_name;
    private double score;
    private String currentThreadName;

    @Override
    public String toString() {
        return "Course{" +
                "course_name='" + course_name + '\'' +
                ", stu_name='" + stu_name + '\'' +
                ", score=" + score +
                ", currentThreadName='" + currentThreadName + '\'' +
                '}';
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCurrentThreadName() {
        return currentThreadName;
    }

    public void setCurrentThreadName(String currentThreadName) {
        this.currentThreadName = currentThreadName;
    }
}
```

### 配置 mapper 映射
```java
@Mapper
@Repository
public interface CourseMapper {

    @Select("select * from tb_score limit 2 offset 1")
    Collection<Course> findAllCourses();

    @Select("select * from tb_score limit 1 offset 1")
    Course findSingleCourse();
}
```

### 配置控制器

```java
@RestController
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/find-all-courses")
    public Collection<Course> findAllCourses(){

        Collection<Course> courses1 = courseMapper.findAllCourses();

        for (Course course : courses1){
            course.setCurrentThreadName(Thread.currentThread().getName());
        }
        return courses1;
    }

    @GetMapping("/find-single-course")
    public String findSingleCourse(){
        Course course = courseMapper.findSingleCourse();
        course.setCurrentThreadName(Thread.currentThread().getName());
        return JSON.toJSONString(course);
    }
}

```
### 添加SQL脚本
```sql
-- 查询所有的数据库
show databases;
-- 使用testdb
use testdb;
-- 查询testdb所有的表
show tables ;
-- 建表前清除
drop table tb_score;
-- 建表语句
create table tb_score(
  id bigint primary key auto_increment,
  course_name varchar(20),
  stu_name varchar(20),
  score double
);
-- 插入数据
insert into tb_score (course_name, stu_name, score) values ('高数','尚先生',100.00);
insert into tb_score (course_name, stu_name, score) values ('英语','尚先生',80.00);
insert into tb_score (course_name, stu_name, score) values ('英语','尚小菲',100.00);
insert into tb_score (course_name, stu_name, score) values ('高数','尚小菲',90.00);
-- 查询入库数据
select *
from tb_score;
```
### 更多优秀文章
> java界的小学生
> https://blog.csdn.net/shang_xs

> 更多Tomcat相关
> 深入学习和理解Servlet（一）
> https://blog.csdn.net/shang_xs/article/details/90371068
> 深入学习和理解Servlet（二）
> https://blog.csdn.net/shang_xs/article/details/90376489

### 完整代码和相关依赖请见GitHub
>https://github.com/dwyanewede/spring-boot/tree/master/spring-webmvc/src/main

### 公众号推荐
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190429165130430.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NoYW5nX3hz,size_16,color_FFFFFF,t_70)
