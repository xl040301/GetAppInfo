# GetAppInfo
根据客户需求，获取手机上所有APP相关信息，包括包名，安装路径，文件权限，权限级别，SharedUserId等：<br>
由于系统开机时PMS会扫描手机上所有APP信息并解析，最终保存在/data/system/packages.xml中，<br>
xml中信息很多，那么怎么操作才能获取客户所需的数据呢，总不能去单独去扫描系统APP，然后作处理，<br>
或者单独查看XML文件，然后一个一个去拷贝，那样得弄到什么时候。不过系统已经为我们作了许多工作，<br>
这里只需要针对packags.xml进行处理即可。这里的思路是将xml中数据通过pullParser保存<br>
到所创建的表中，然后将各个表中数据通过视图方式集合呈现出客户所需要的最终数据。<br>
<div align=center><img width="800" height="600" src="https://github.com/xl040301/GetAppInfo/blob/master/screenshot/shot.png"/></div>
