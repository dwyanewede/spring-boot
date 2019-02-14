package com.sxs.spring.web.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @ClassName: FluxDemo
 * @Description: web reactive mono and flux
 * @Author: sxs
 * @CreateDate: 2019/2/14 16:42
 * @Version: 1.0
 */
public class FluxDemo {

    public static void main(String[] args) {

        print("开始执行...");
        // 1.单线程同步执行
//        Flux.just("A","B","C").subscribe(FluxDemo::print);
        // 2.线程池切换
//        Flux.just("A","B","C")
//                .map(value -> "+" + value)
//                .publishOn(Schedulers.elastic())
//                .subscribe(FluxDemo::print);
        // 3.背压操作
//        Flux.just("A","B","C")
//                .subscribe(FluxDemo::print, // 消费数据
//                            FluxDemo::print, // 异常处理
//                        () -> {
//                    // 完成回调
//                            print("操作完成...");
//                        },
//                        subscription -> {
////                    subscription.cancel(); //取消请求完成操作处理动作
//                    subscription.request(Integer.MAX_VALUE);//接受请求元素的数量
//                }
//                );
        // 4.自定义Subscriber
        Flux.just("A","B","C")
                .subscribe(new Subscriber<String>() {
                    private Subscription subscription;
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        if ("B".equals(s)){
                            throw new RuntimeException("自定义异常");
                        }
                        print(s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        print(throwable);
                    }

                    @Override
                    public void onComplete() {
                        print("操作完成...");
                    }
                });


    }

    private static void print(Object obj){
        String name = Thread.currentThread().getName();
        System.out.println("【 线程：" + name + " 】" + obj);
    }
}
