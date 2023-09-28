# CS309-OOAD-ProjectHelper
## 前端: vue+quasar+
### 页面
大页面，例如登录页面，主页面等,目前计划设计如下页面，英文表示组件名字： 

#### 登录部分：
- login 用于登录，提供多种登录方式，短信/邮件/密码，在该页面中提供切换登录方式的选项 
- register 用于注册，界面风格要求与login保持一致，用户可以提供短信（可选），必须提供学号，名字和密码，学号需要检验是否符合南科大学号规范。 
效果如下图：
![登录效果图](image/login效果图.jpg)
#### 主要页面: 
- info 两个用途：1. 用于展示个人信息，至少需要展示名字，学号，权限组（学生/教师/助教等，可扩展），手机号（可选），允许修改除了学号和姓名外的所有信息
- main 主页面，类似sakai的风格，提供各种project和个人信息的入口

#### 组管理页面：
- groups 展示所有的group列表，允许点击进入对应的group页面，教师允许编辑信息（划分小组，create multiple groups based on group size, deadline, number and other information.等，具体看project要求）。
- group 展示group成员，技术栈及leader等信息，学生在该页面只允许浏览、加入和退出，教师允许编辑信息（分配组员，设置leader等，具体看project要求），

#### 作业页面：
- ass-list 展示所有作业，但是可以有分类，一个卡片下面是个人作业，一个卡片是下面是group work，支持点击去到ass-info，教师允许发布作业，教师支持查看布置的所有作业（设计ui的时候条例清晰）
- ass-info 展示作业具体信息，允许在当前页面提交信息，支持在线浏览，在提交后支持修改，同时教师允许在该页面打分 

#### 打分页面
- grade 查看自己作业的分数，以及最终总分
- grade-list教师支持查看各种分类的得分（按照个人，按照小组），排序也支持按照字母升序/降序，按照分数
  
## 后端：springboot+mybatis-plus+postgresql
### springboot：

1. 包名及其意义：
    - config：装在一些配置类的文件，如CROS等
    - controller：控制器，提供restful服务，将各种服务整合在流程中
    - entities：实体，对应数据库中的表
    - mapper：将实体映射到数据库的表中
    - service：服务类，提供某种具体的服务
    - util：效用类，包装一些可复用的代码块，如cookie的设置与检查等
