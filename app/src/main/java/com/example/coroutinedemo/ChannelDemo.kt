package com.example.coroutinedemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * desc:通道
延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输。 通道提供了一种在流中传输值的方法。
 *
 * @author biaowen.yu
 * @date 2021/3/26 17:51
 *
 **/

fun main()= runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x * x)
    }

    repeat(5){ println(channel.receive())}
    print("Done!!")
}