## spring web mvc

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
```
-- 查询入库数据
select *
from tb_score;