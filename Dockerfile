#基础镜像
FROM openjdk:17-jdk-alpine

#安装字体
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --update ttf-dejavu fontconfig \
    && rm -rf /var/cache/apk/* \
    && mkfontscale && mkfontdir && fc-cache

#添加文件
ADD target/ProjectHelper-0.0.1-SNAPSHOT.jar /usr/local
RUN chmod u+x /usr/local/ProjectHelper-0.0.1-SNAPSHOT.jar

#设置时区
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

#开放端口
EXPOSE 8443

#启动命令
ENTRYPOINT ["java","-jar","/usr/local/ProjectHelper-0.0.1-SNAPSHOT.jar"]

