    /* 
     * Copyright 2009 Cedric Priscal 
     * 
     * Licensed under the Apache License, Version 2.0 (the "License"); 
     * you may not use this file except in compliance with the License. 
     * You may obtain a copy of the License at 
     * 
     * http://www.apache.org/licenses/LICENSE-2.0 
     * 
     * Unless required by applicable law or agreed to in writing, software 
     * distributed under the License is distributed on an "AS IS" BASIS, 
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
     * See the License for the specific language governing permissions and 
     * limitations under the License. 
     */  


#include <stdio.h>
#include <stdlib.h>
#include <linux/rtc.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <time.h>

#include <jni.h>
#include "com_mitac_vibrator_RTC.h"

    #include "android/log.h"  
    static const char *TAG = "SerialPort";  
    #define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)  
    #define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)  
    #define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)  


    /*
     * Class:     com_mitac_vibrator_RTC
     * Method:    getTime
     * Signature: ()V
     */
JNIEXPORT void JNICALL Java_com_mitac_vibrator_RTC_getTime(JNIEnv * env, jobject thiz)
{
        //jclass SerialPortClass = env->GetObjectClass(thiz);  

        int fd, retval;
        struct rtc_time rtc_tm;
        time_t timep;
        struct tm *p;

        fd = open("/dev/rtc0", O_RDONLY);
        if (fd == -1) {
                perror("/dev/rtc0");
                LOGD("fail to open /dev/rtc0");
                exit(errno);
        }

        /* Read the RTC time/date */
        retval = ioctl(fd, RTC_RD_TIME, &rtc_tm);
        if (retval == -1) {
                perror("ioctl");
                LOGD("ioctl: RTC_RD_TIME failure");
                close(fd);
                exit(errno);
        }
        close(fd);
/*
        fprintf(stderr, "RTC date/time: %d/%d/%d %02d:%02d:%02d\n",
                rtc_tm.tm_mday, rtc_tm.tm_mon + 1, rtc_tm.tm_year + 1900,
                rtc_tm.tm_hour, rtc_tm.tm_min, rtc_tm.tm_sec);

        time(&timep);
        p = gmtime(&timep);
        fprintf(stderr, "OS date/time(UTC): %d/%d/%d %02d:%02d:%02d\n",
                p->tm_mday, p->tm_mon + 1, p->tm_year + 1900,
                p->tm_hour, p->tm_min, p->tm_sec);
        
        p = localtime(&timep);
        fprintf(stderr, "OS date/time(Local): %d/%d/%d %02d:%02d:%02d\n",
                p->tm_mday, p->tm_mon + 1, p->tm_year + 1900,
                p->tm_hour, p->tm_min, p->tm_sec);
*/      
}
