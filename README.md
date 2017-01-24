# ddy-dianmai-manage
这是基于shiro,springMVC的前后端分离的权限管理系统，接口以restful风格,用户只需要更改 
1.applicationContext-config-druid.xml 
2.applicationContext-config-shiro.xml 中的shiroFilter url对应的过滤器filter，用户也可以自己增加过滤器 
3.resources.properties 数据库连接 
4.log4j.xml 日志路径 
5.运行shiro.sql 
6.另外pom.xml中的Gson是我自己本地修改过的jar，用户可以从网络上下载，替换掉
7.前端需要把登录返回的sid存于cookie中,每次请求携带此sid,shiro会有机制从请求中获取到sid

特此声明: 
    此项目绝大部分逻辑都是根据 https://github.com/dingxuefei/shiro (使用jsp) 项目改造成前后端分离，
    另外参考 http://jinnianshilongnian.iteye.com/blog/2018398 对session进行了改造
