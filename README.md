# CS309-OOAD-ProjectHelper
## 前端: vue+quasar
### 开发规范

每次开始工作前，请git pull main，每次结束工作后**至少**进行git commit，如果完成了可以git commit后进行git push 到自己的分支，之后**需要上github**来request pull到main branch

所有vue组件的命名必须多余两个单词且每个单词开头必须大写：

​	**错误示范**：登录组件，取名为~~Login.vue~~

​	**正确示范**：登录组件，取名为UserLogin.vue

vue组件存在**不同层级**：layout，page，component，请大家根据自己所写vue组件层次，在**对应目录下**创建vue文件。为了方便**代码的复用**，各位可以对一个功能进行细分，拆到不同的组件中。例如，登录页面中，短信、学号、邮箱登录样式基本一致，可以创建一个组件叫做LoginCardComponent.Vue，切换到不同模式下，只需要修改inputbox中相应的文字即可。

在写不同页面时，请注意为自己写的部分**创一个文件夹**，例如写登录页面，会需要有UserLogin.Vue和UserRegister.Vue组件，这时候，你需要在pages目录下创建一个叫做LoginPages的目录，来存这两个文件。


在router里写网址时，请一律采取小写
### 页面

大页面，例如登录页面，主页面等,目前计划设计如下页面，英文表示组件名字： 

#### 登录部分(黄硕)：
- login 用于登录，提供多种登录方式，短信/邮件/密码，在该页面中提供切换登录方式的选项 
- register 用于注册，界面风格要求与login保持一致，用户可以提供短信（可选），必须提供学号，名字和密码，学号需要检验是否符合南科大学号规范。 
10月1日  完成
效果如下图： 
![登录效果图](image/login效果图.jpg)
#### 主要页面（汤玉磊）: 
- info 两个用途：1. 用于展示个人信息，至少需要展示名字，学号，权限组（学生/教师/助教等，可扩展），手机号（可选），允许修改除了学号和姓名外的所有信息
- main 主页面，类似sakai的风格，提供各种project和个人信息的入口

#### 组管理页面（李伟浩）：
- groups 展示所有的group列表，允许点击进入对应的group页面，教师允许编辑信息（划分小组，create multiple groups based on group size, deadline, number and other information.等，具体看project要求）。 
- group 展示group成员，技术栈及leader等信息，学生在该页面只允许浏览、加入和退出，教师允许编辑信息（分配组员，设置leader等，具体看project要求）， 
10月3日 至10月6日未完成
#### 作业页面（李伟浩）：
- ass-list 展示所有作业，但是可以有分类，一个卡片下面是个人作业，一个卡片是下面是group work，支持点击去到ass-info，教师允许发布作业，教师支持查看布置的所有作业（设计ui的时候条例清晰） 
- ass-info 展示作业具体信息，允许在当前页面提交信息，支持在线浏览，在提交后支持修改，同时教师允许在该页面打分 （汤玉磊完成PDF版本）  
10月7日 
#### 打分页面（黄硕）：
- grade 查看自己作业的分数，以及最终总分
- grade-list教师支持查看各种分类的得分（按照个人，按照小组），排序也支持按照字母升序/降序，按照分数  
10月10日 
## 后端：springboot+mybatis-plus+postgresql
### https的配置
0. 确保已经pull最新版gitignore
1. 请将`genshin2.fun.jks`放置在resource目录下（`genshin2.fun.jks`已放至qq群）
2. 请在application.properties中增加如下信息
   ```properties
   # https
   server.port=8443
   server.ssl.key-store-type=JKS
   server.ssl.key-store=classpath:genshin2.fun.jks
   server.ssl.key-store-password=9wbhq255k7
   ```
3. 现在，访问后端的端口变成了https://localhost:8443/
4. 使用Google Chrome（或其他现代浏览器）访问localhost时，浏览器会弹出类似下图的安全警告，不用理会，部署到云上面就能够解决这个问题
   ![访问效果图](image/访问图.png)
### redis的配置
1. 将application.properties中`#redis`改为如下信息：
   ```properties
   # redis
   spring.data.redis.host=www.genshin2.fun
   spring.data.redis.port=6379
   spring.cache.type=redis
   spring.data.redis.password=G4t!mP8#r2LqX0wV
   ```
2. 如果想要可视化的看到数据源中的信息，请在`IDEA`或`DataGrip`中配置数据源
   - 要下载driver文件
   - url为jdbc:redis://124.71.110.18:6379/0
   - 密码请输入G4t!mP8#r2LqX0wV
   - 不需要账户名
   - 使用test connection尝试连接，确保可行
### springboot：

1. 包名及其意义：
    - config：装在一些配置类的文件，如CROS等
    - controller：控制器，提供restful服务，将各种服务整合在流程中
    - entities：实体，对应数据库中的表
    - mapper：将实体映射到数据库的表中
    - service：服务类，提供某种具体的服务
    - util：效用类，包装一些可复用的代码块，如cookie的设置与检查等
### 前端像后端端点访问需要注意的事情
1. 响应体一定是ResponseResult<T>类，请见`com.example.projecthelper.util.ResponseResult`
2. ResponseResult中有状态码，请以该状态码为准
3. 如果操作失败了，返回的ResponseResult的jwt_token为null。
### 后端已经完成基本测试的接口：
1. /signup POST
   
   请求体
   ```json
   {
      "identity":0,
      "password":"Aa@123456",
      "name":"Andy",
      "gender":"m"
   }
   ```
   响应体
   ```
   "statusCode": 200成功, 405密码太弱或身份不合法/"信息不完整"
   ```
   ```json
   {
      "statusCode":200, 
      "msg":"",
      "jwt_token":"eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxMCIsImV4cCI6MTY5Njk0NDg1MSwiaWRlbnRpdHlDb2RlIjoiMCIsImlhdCI6MTY5Njk0Mzk1MX0.A6hDRUi06U-EmpRHgVNajRubZQzfMDUcOyEr_EoUBN-GR7uHpDkdSyikrKw6TxyE2eEH57xaBAKyJ3oWVkcTqA"
   }
   ```
2. /login POST
   
   请求体
   ```json
   {
      "key":"10",
      "value":"Aa@123456"
   }
   ```
   响应体
   ```
   "statusCode": 200成功,401“认证失败”（用户名密码错误）
   ```
   ```json
   {
       "statusCode": 200,
       "msg": "success",
       "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJudWxsIiwiZXhwIjoxNjk2OTQ1ODQxLCJpZGVudGl0eUNvZGUiOiIwIiwiaWF0IjoxNjk2OTQ0OTQxfQ.Ivu-LmaUnoEJ_tP0vWPnWBcg4w1dNrYliOOMyrZvO-ycXABDJGUxhxb30qyfGxihSjCZsA8rc_ZwnJFhBdvI1g"
   }
   ```
3. /logout DELETE
   请求体:任意
   响应体：
   ```json
   {
       "statusCode": 200,
       "msg": "登出成功"
   }
   ```
   ~~前端要自行将header中的Token删掉~~已经使用redis完善了该功能
4. /tea/**
必须以教师身份访问，否则返回的响应体的"statusCode" = 402“权限不够”（比如说学生来发）
5. /tea/postNotice POST 
   
   请求体：
   ```json
   {
      "title":"t1",
      "content":"c1",
      "projectId": 1, 
      "stuView": [3, 4, 5, 6, 7]
   }
   ```
   响应体：
   ```
   "statusCode": 200成功,401“认证失败”（用户名密码错误），
   ```
   ```json
   {
       "statusCode": 200,
       "msg": "success",
       "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJudWxsIiwiZXhwIjoxNjk2OTQ1ODQxLCJpZGVudGl0eUNvZGUiOiIwIiwiaWF0IjoxNjk2OTQ0OTQxfQ.Ivu-LmaUnoEJ_tP0vWPnWBcg4w1dNrYliOOMyrZvO-ycXABDJGUxhxb30qyfGxihSjCZsA8rc_ZwnJFhBdvI1g"
   }
   ```
6. /tea/modifyNotice PUT

   请求体：
   ```json
   {
      "noticeId":1,
      "title":"t2",
      "content": "c2",
      "stuView": [3],
      "toAll": true
   }
   ```
   响应体：
   ```
   "statusCode": 200成功,401“认证失败”（用户名密码错误）,405"无权修改别人发布的公告"
   ```
   ```json
   {
       "statusCode": 200,
       "msg": "success",
       "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJudWxsIiwiZXhwIjoxNjk2OTQ1ODQxLCJpZGVudGl0eUNvZGUiOiIwIiwiaWF0IjoxNjk2OTQ0OTQxfQ.Ivu-LmaUnoEJ_tP0vWPnWBcg4w1dNrYliOOMyrZvO-ycXABDJGUxhxb30qyfGxihSjCZsA8rc_ZwnJFhBdvI1g"
   }
   ```
7. /stu/**必须以学生身份访问，identity为3, 否则权限不够
8. /stu/editPersonInfo PUT
   
   请求体：
   
   ```json
   {
    "technologyStack": "ts1",
    "programmingSkills": "ps1",
    "intendedTeammates": "it1"
   }
   ```
   
   响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
9. /adm/createMultipleUsers

   请求体：

   ```json
   {
    "obj":{
        "identity": 3,
        "password": "Aa@123456",
        "name": "Andy",
        "gender": "m"
    },
    "count": 10
   }
   ```
   `identity, password, name, gender, count`均不能为空

   响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
10. /tea/createProject

   请求体：

   ```json
   {
    "name": "CS309"
   }
   ```
   `name`均不能为空

   响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
11. /tea/addStuToProject

   请求体：
   
   ```json
   {
    "key": 1,
    "value": [3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
   }  
   ```
   `key, value`均不能为空
   
   响应体
   
   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
12. /tea/createGroup

   请求体：

   ```json
   {
    "maxsize": 5,
    "groupName": "gp1",
    "projectId": 1,
    "instructorId": 1
   }
   ```
   `maxsize, groupName, projectId, instructorId`均不能为空

   响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
13. /tea/createMultipleGroups

   请求体：

   ```json
   {
    "obj": {
    "maxsize": 10,
    "groupName":"gp2",
    "projectId": 1,
    "instructorId": 1
    },
    "count": 10
   }
   ```
   `maxsize, groupName, projectId, instructorId, count`均不能为空

   响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```
14. /tea/modifyGroupInfo

   请求体：

   ```json
   {
   "maxsize": 500,
   "groupName": "gp3",
   "instructorId": 1,
   "groupId": 4
   }
   ```
   `maxsize, groupName, groupId, instructorId`均不能为空
   
   响应体
   
   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```

### Controller接口

所有请求地址名字都暂定，后端可以为了统一修改请求地址

#### 校验 （前端10.23提出）

get /getIden

无请求体，仅携带jwt_token

响应体

   ```json
   {
   "statusCode": 200,
   "msg": "Success",
   "data": {
      "username": "123"
      "userid": 12
   }
   "jwt_token": "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyIiwiZXhwIjoxNjk3Mjc3OTgzLCJpZGVudGl0eUNvZGUiOiIzIiwiaWF0IjoxNjk3Mjc0MzgzfQ.Awh6vlRDj3mPQs3T2OAcC5D-2JD7kGX9qBHtVdEohTo6Xnz_B_tMDbAFtTNP9DvF8E6XftkOi-UQ_D4H_NGHug"
   }
   ```

2. project类中至少应该包含project的自增id和project的名字，  
3. ~~用户的id利用cookies查询，现在由于登录的cookies还没写好，请在后端先写死一个id来做样例~~

**用途：** 该接口用于主页上的展示
