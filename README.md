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
  
  ### <div id="t1">1、平台介绍<sup>①</sup></div>
  本商城主要分为三个部分：运营商后台、商家后台和网站前台。类似与天*、京*商城。商家进行注册后，经过在运营商后台审核成功后入驻。
  商家可以添加商品，同样需要在运营商后台审核后才能上架，上架后可修改商品（同样需要审核）。在后台数据准备好后最终在前台展示。
  #### 1.1、 商家后台
  页面采用<a href="https://adminlte.io/" target="_blank">AdminTEL</a>模板,这是一款开源的基于bootstrap的主题，只需稍加修改即可使用。
  
