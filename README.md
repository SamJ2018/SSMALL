# 森马逊 -分布式电商解决方案By Sam
<a href="#t1" alt>一、项目架构 
  <ol>
    <li>
        <a href="#">平台介绍</a>
    </li>
    <li>
        <a href="#">技术选型</a>
    </li>
    <li>
        <a href="#">环境搭建</a>
    </li>
  </ol>
 <a href="#t1" alt>二、使用技术介绍
  <ol>
     <li>
       <a href="#"> maven vs <del>gradle</del></a>
    </li>
     <li>
       <a href="#"> dubbox vs <del>dubbo</del></a>
    </li>
       <li>
       <a href="#">zookeeper</a>
    </li>
    <li>
       <a href="#"> spirng、mybatis、spirngMVC vs springBoot</a>
    </li>
    <li>
        <a href="#"> mysql、redis 、mongoDB</a>
    </li>
    <li>
      <a href="#"> solr vs <del>Elasticsearch</del></a>
    </li>
     <li>
       <a href="#"> AngularJs vs <del>vue</del></a>
    </li> 
     <li>
       <a href="#"> Spring Security vs <del>shrio</del></a>
    </li> 
     <li>
        <a href="#"> fastDFS && ngix</a>
    </li>
      <li>
        <a href="#"> ActiveMQ</a>
    </li>
     <li>
      <a href="#"> Git vs <del>SVN</del></a>
    </li>
     <li>
      <a href="#"> static page generation</del></a>
    </li>
    <li>
      <a href="#"> single sign on</del></a>
    </li>
  </ol>
   <a href="#t1">三、prospect and conclusion</a><br>
   <strong>注：被划线的是本项目没有使用到的技术，列举出来是方便做对比</strong>

  ### <div id="t1">1、平台介绍与流程<sup>①</sup></div>
  本商城主要分为三个部分：运营商后台、商家后台和网站前台。类似与天*、京*商城。商家进行注册后，经过在运营商后台审核成功后入驻。
  商家可以添加商品，同样需要在运营商后台审核后才能上架，上架后可修改商品（同样需要审核）。在后台数据准备好后最终在前台展示。后台管理页面采用<a href="https://adminlte.io/" target="_blank">AdminTEL</a>模板,这是一款开源的基于bootstrap的页面静态模板，只需稍加修改即可使用。

  #### 1.1、 后台功能
  ![商家入驻](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%AE%B6%E5%85%A5%E9%A9%BB.png)

首先在商家注册页面填写必要信息，为了后期扩充，所有字段均为必填字段。提交申请后进入商家管理后台：

![商家管理后台](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E8%BF%90%E8%90%A5%E5%95%86%E7%99%BB%E9%99%86%E9%A1%B5%E9%9D%A2.png)

（注意：这里的账号密码通过springsecurity管理，可在ssmallmanagerweb下的reouces/spring-security.xml中配置，并没有另外生成注册页面）

![商家审核](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%AE%B6%E5%AE%A1%E6%A0%B8.png)

在商家审核页面就可以看到刚才入驻的商家，点击详情可以查看详情并选择审核通过、关闭、不通过

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%AE%B6%E5%AE%A1%E6%A0%B8%E8%AF%A6%E6%83%85.png)

只有审核通过的商家才可以登录商家管理页面并添加一些商品，先记住这一点稍后可以看到。接下来是商品的管理，因为未来数据可能过多，为了减轻数据库访问压力，商品管理分为五大模块：品牌管理、规格管理、模板管理、分类管理、商品审核，并对应于数据库中的五张表。

品牌管理-新建

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%BE%AE%E6%98%9F.png)

规格管理-新建

![规格管理](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E8%A7%84%E6%A0%BC%E7%AE%A1%E7%90%86.png)

模板管理-新建(注意：每一层都与上一层有关联 新建的顺序不能颠倒)

![模板管理](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%A8%A1%E6%9D%BF%E7%AE%A1%E7%90%86.png)

模板分类（依次点击下一级 点击面包屑可以回退 最终找到笔记本）

![模板分类](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%A8%A1%E6%9D%BF%E5%88%86%E7%B1%BB1.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%A8%A1%E6%9D%BF%E5%88%86%E7%B1%BB2.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%A8%A1%E6%9D%BF%E5%88%86%E7%B1%BB3.png)

修改模板ID为46（可在模板管理页面查看，前面操作生成的，为自增主键）

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%A8%A1%E6%9D%BFID2.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%93%81%E5%88%86%E7%B1%BB2.png)

至此商品管理录入完成，进入商家后台进行具体商品的添加。

添加商品基本信息

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%93%81%E5%BD%95%E5%85%A5.png)

可批量添加图片

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%89%B9%E9%87%8F%E6%B7%BB%E5%8A%A0%E5%9B%BE%E7%89%87.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E8%A7%84%E6%A0%BC.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E8%A7%84%E6%A0%BC3.png)

保存成功

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E4%BF%9D%E5%AD%98%E6%88%90%E5%8A%9F.png)

接下来返回到运营商管理后台将商品审核了就大功告成了

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%95%86%E5%93%81%E5%AE%A1%E6%A0%B8%E6%88%90%E5%8A%9F.png)

前台广告添加

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%B9%BF%E5%91%8A1.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%B9%BF%E5%91%8A3.png)

至此后台管理的主要功能结束，还有很多细节和BUG有待后续处理。

### 1.2前台功能

前往首页

![](screenshot\首页.png)

搜索页面

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%89%8B%E6%9C%BA1.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%89%8B%E6%9C%BA2.png)

找到刚才添加的那件商品

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%BE%AE%E6%98%9F2.png)

查看详情，选择规格，添加购物车

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E5%BE%AE%E6%98%9F3.png)

这时需要登录or注册，填写手机号，收到短信后填写验证码 注册成功(阿里大于短信平台)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E7%99%BB%E5%BD%95.png)

注册后登录，就可以进行结算

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E7%BB%93%E7%AE%971.png)

添加收获地址-微信付款

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E6%94%B6%E8%8E%B7%E5%9C%B0%E5%9D%80.png)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/pay.png)

秒杀界面

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E7%A7%92%E6%9D%80%E7%95%8C%E9%9D%A2.png)

详情 (使用spring task进行任务调度，右边的读秒结束后将商品下架)

![](https://github.com/SamJ2018/SSMALL/blob/master/screenshot/%E7%A7%92%E6%9D%80%E8%AF%A6%E6%83%852.png)

抢购完成后的流程与前面普通购买相同，至此所有主要功能都展示完毕。



