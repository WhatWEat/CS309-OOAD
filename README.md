# CS309-OOAD-ProjectHelper
## 前端: vue+quasar
### 开发规范

每次开始工作前，请git pull main，每次结束工作后**至少**进行git commit，如果完成了可以git commit后进行git push 到自己的分支，之后**需要上github**来request pull到main branch

所有vue组件的命名必须多余两个单词且每个单词开头必须大写：

​	**错误示范**：登录组件，取名为~~Login.vue~~

​	**正确示范**：登录组件，取名为UserLogin.vue

vue组件存在**不同层级**：layout，page，component，请大家根据自己所写vue组件层次，在**对应目录下**创建vue文件。为了方便**代码的复用**，各位可以对一个功能进行细分，拆到不同的组件中。例如，登录页面中，短信、学号、邮箱登录样式基本一致，可以创建一个组件叫做LoginCardComponent.Vue，切换到不同模式下，只需要修改inputbox中相应的文字即可。

在写不同页面时，请注意为自己写的部分**创一个文件夹**，例如写登录页面，会需要有UserLogin.Vue和UserRegister.Vue组件，这时候，你需要在pages目录下创建一个叫做LoginPages的目录，来存这两个文件。

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
10月3日 超时一天未完成
#### 作业页面（李伟浩）：
- ass-list 展示所有作业，但是可以有分类，一个卡片下面是个人作业，一个卡片是下面是group work，支持点击去到ass-info，教师允许发布作业，教师支持查看布置的所有作业（设计ui的时候条例清晰） 
- ass-info 展示作业具体信息，允许在当前页面提交信息，支持在线浏览，在提交后支持修改，同时教师允许在该页面打分 （汤玉磊完成PDF版本）  
10月7日 
#### 打分页面（黄硕）：
- grade 查看自己作业的分数，以及最终总分
- grade-list教师支持查看各种分类的得分（按照个人，按照小组），排序也支持按照字母升序/降序，按照分数  
10月10日 
## 后端：springboot+mybatis-plus+postgresql
### springboot：

1. 包名及其意义：
    - config：装在一些配置类的文件，如CROS等
    - controller：控制器，提供restful服务，将各种服务整合在流程中
    - entities：实体，对应数据库中的表
    - mapper：将实体映射到数据库的表中
    - service：服务类，提供某种具体的服务
    - util：效用类，包装一些可复用的代码块，如cookie的设置与检查等
### Controller接口

所有请求地址名字都暂定，后端可以为了统一修改请求地址

#### 登录

##### /users/login/{user}/{password}

**效果：** 用于利用账号和密码或者验证码进行登录

**具体要求：**

1. 需要对user和password进行验证，支持多种方式判定，例如user既可以代表学号，也可以代表邮箱和手机号，只要一个验证通过就返回true
2. 在验证通过之后需要将秘钥或者其他的注入到cookies里

##### /users/reg/{user}/{password}

**效果：** 用于利用手机（还没好）和学号进行注册

**具体要求：**

1. 暂时没什么要求，能检验学号，手机号有咩有冲突就好

#### 主页面

##### /projects/{page}/{pagesize}

**效果：** 给出一个返回指定数量的project类的list ，有cookies
**具体要求：**   

1. 需要支持分页，我会给定变量page和pagesize来限制返回的数量，但当请求不提供page和pagesize时，需要返回全部的project。  
2. project类中至少应该包含project的自增id和project的名字，  
3. ~~用户的id利用cookies查询，现在由于登录的cookies还没写好，请在后端先写死一个id来做样例~~

**用途：** 该接口用于主页上的展示
