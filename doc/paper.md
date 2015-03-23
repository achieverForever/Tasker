## 广东外语外贸大学
## 本科生毕业论文（设计）

### 论文题目：基于条件触发的Android自动任务执行App

作者姓名：郭伟铖，王俊胜
作者学号：20111003828，20111003810
指导教师：马文华
年级专业：2011级计算机科学与技术
所在学院：思科信息学院


提交日期：2015年6月


### 摘要

在移动互联网的时代，android平台早已经普及开来。人类正在前所未有的享受着科技所带来的前所未有的便利。而随着各种移动设备以及物联网的普及，人类可以让软件实现更加智能的管理各种设备。比如在晚上休息时不想被人打扰，可以自动的屏蔽手机上Email、短信、电话的接受，而等到明天早上才自动提醒用户；又比如当你打开Instagram分享照片时，你可能想把所分享的照片同时上传到你的Dropbox同步盘上。
本文就基于条件触发的Android自动化任务执行App进行设计和研究。本文首先介绍分析了当前基于情景等条件触发的app研究现状。然后介绍可以触发任务的情景，例如获取到用户的位置就应该调用相关的位置服务（LBS），设计好相关的情景是这个系统成功的关键。然后在介绍运用的Android开发技术，
论文给出了Android应用Tasker的实例。该实例实现了几种常用的自动化情景，以及提供接口给用户自定义情景任务。
**关键词**：*Android* *条件触发*，*自动任务*，*LBS*，
###Abstract
In the mobile Internet era, android platform already gaining in popularity. Mankind is enjoying unprecedented technology brought unprecedented convenience. With a variety of mobile devices and the popularity of things that humans can make software more intelligent management of various devices. For example, do not want to be disturbed during the night to rest, the phone can automatically shield Email, SMS, telephone acceptance, and wait until tomorrow morning to automatically alert users; another example, when you open Instagram to share photos, you might want to have shared uploading photos to your Dropbox sync disk.
In this paper, based on the conditions to trigger automated tasks Android App for the design and execution of research. This paper describes the current study analyzes the status quo scenario-based app trigger conditions. Then introduce scenarios can trigger tasks, such as access to the user's location should call the relevant location-based services (LBS), good design is the key to this scenario related to the system's success. Then introduce the use of Android development technology,
Paper gives an example of Tasker's Android application. This example implements several common automation scenarios, as well as providing an interface to a user-defined profile task.
**Key Words:** *Android app*, *automated tasks*, *LBS*

### 目录

**摘要**
*Abstract*

**第一章  绪论**
1.1 。。。
1.2 。。。

**第二章  基本模型与原理？**
2.1 。。。
2.2 。。。

**第三章  程序实现**
3.1 。。。
3.2 。。。

**第四章  总结与展望**
4.1 。。。
4.2 。。。

**参考文献**

**致谢**


### 第一章  绪论
介绍下项目背景，竞品分析，现实需求，应用场景，bla，bla，bla

在移动互联时代，智能手机作为继PC后性能最强的移动运算平台，已经成为了人们生活中必不可少的一部分。当今软硬件发展迅速，更新迭代周期短，智能手机的性能越来越强大，使得人们以前必须在PC上完成的操作如今在移动端即可轻松完成。从收发邮件，查询天气，发微信、微博，网购，查地图，看电影，买机票，我们能想到的各种场景，都已经在智能手机上实现了，而且用户体验比线下更好，更方便。正是由于这些原因，人们花在手机上的时间比以往都要多，可以说，现代人们的生活和工作都已经离不开智能手机了。但是智能手机给人们带来便利的同时也带来了很多操作上的麻烦。例如，为了修改某个系统设置，常常需要用户进入到层级很深的设置里面。这些操作是有规律的，每次手工去修改不仅浪费时间，也让智能手机的使用门槛变高，降低了用户体检。另外，我们在日常生活中经常会有这样的现实需求：如果我到了公司/学校，自动静音；如果我在开车，自动发短信回复来电等等。如果能提供这样的一个工具，用户可以自定义触发的条件，并能选择满足条件后会执行什么动作，那将会大大增强智能手机的可用性。

通过前期调研，我们发现市场上主要有Locale和Tasker两款与我们的构思类似的产品。Locale比较有特色的是其结合了人工智能的地理位置服务以及可扩展的插件机制。其定位机制由于缺乏资料无从考究，但可以了解到的是Locale的定位服务综合利用了GPS，Wi-Fi，移动网络等数据源去确定用户当前的位置，号称能瞬间定位，并且能把电量消耗降到最少。经过对Locale插件开发者指南的研究，我们发现Locale的插件机制主要是通过Android系统的广播实现的。Locale通过发送类型如Edit，Query，Fire的广播并将数据存放在EXTRA_BUNDLE中与插件进行数据交互，定时检测插件Condition是否满足，若满足则通过发送Fire类型的广播唤起对应的插件执行后续的Action。通过这种方式，Google应用商店已经出现了数百款Locale的第三方插件，极大的增强了Locale的功能性和实用性。


另一款同类型的产品Tasker，提供的Action无所不包，从来电、电量、位置到通知被点击等应有尽有，可以说只有想不到，没有做不到，但同时也会造成应用异常之难用，部分选项更是要求用户具备一定的计算机知识，提高了用户的使用门槛。


我们在构思这款App的时候，决定在功能性和实用性两者间，把程序的实用性放在第一位。因为这个App的初衷是让智能手机变得更智能，如果要以复杂的操作和高昂的学习成本为代价，则会偏离了初衷，人为的制造了另一种“不智能”。
通过调研和从用户收集需求，我们列举了以下几个App应该具备的功能：
	
1. 到家或者公司自动开wifi
2. 连到特定的wifi，减低音量，减低亮度
3. 连上HDMI，把亮度调到最低
4. 某些应用延长关闭屏幕时间
5. 插上耳机/蓝牙调整音量
6. 电量低于10%，关闭wifi的数据/降低音量/亮度最低/去振动/关蓝牙gps等
7. 蓝牙15分钟没连上自动关闭
8. 忘带手机，发一条信息，触发a.所有短信转发到gmail b.来电回短信说手机没带并发送手机号到gmail
9. 开某些软件关闭转屏

从以上几个需求看来，App应该具备修改系统的各项设置的能力，并且能定时的检测系统的状态，能在后台常驻系统。





### 第二章  基本模型与原理
经过分析，我们建立了以下几个Model表示App的数据结构，分别是Condition，Action，Scene和Event。其中Event用于在EventBus中传递消息，携带了事件类型及其参数。Condition是对一个条件的抽象，包含条件的状态，参数等信息。Action是各种操作的抽象表示，泛指一个可以执行的操作，例如修改系统某项设置，发短信，播放音乐等等。Scene表示情景，一个情景是由若干个Condition和若干个Action组成的。当且仅当所有Condition都满足的时候，该情景的Action才会执行。下面详细讨论每个Model的定义：
 
所有事件都继承自Event，这使得可以在一个中心化的位置统一处理所有事件，并能根据需求实现事件的分发逻辑。针对不同类型的事件，我们将eventCode这个字段作为事件类型的标识。由于涉及到事件类型的转换，所以在进行事件的比较和转换前，我们加上了对这个字段的校验，只有eventCode相同才能通过校验，否则会抛异常。
