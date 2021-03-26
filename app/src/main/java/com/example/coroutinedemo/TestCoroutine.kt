package com.example.coroutinedemo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * desc:
 *
 * @author biaowen.yu
 * @date 2021/3/25 11:00
 *
 **/
class TestCoroutine {


}


fun main(){
    // 穿件一个新的协程
    GlobalScope.launch {
        delay(1000)
        println("World!")
    }

    // 这边并发不会等待，启动协程需要时间，此刻协程还未分配到cpu时间
    print("Hello,")
    // 普通线程阻塞
//    Thread.sleep(2000)
    // kotlin 阻塞，这句代码阻塞了主线程
    runBlocking {
        delay(2000)
    }


}