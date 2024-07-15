package ru.develgame.playground.vchat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;

/**
 * Class for update all events depends by time
 *
 * createTimer(long initialDuration, long intervalDuration, Serializable info)
 * base delay between 2 timer event - 100ms
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MainTimer {
    @Resource
    private TimerService timerService;

    @PostConstruct
    public void initialize() {
        timerService.createTimer(1000, 100, "Interval");
    }

    @Timeout
    public void updatePersonTimeout(Timer timer) {

    }
}
