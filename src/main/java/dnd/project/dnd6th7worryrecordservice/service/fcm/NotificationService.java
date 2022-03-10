//package dnd.project.dnd6th7worryrecordservice.service.fcm;
//
//import dnd.project.dnd6th7worryrecordservice.service.UserService;
//import dnd.project.dnd6th7worryrecordservice.service.WorryService;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//
//@RequiredArgsConstructor
//@Component
//public class NotificationService {
//    private final UserService userService;
//    private final WorryService worryService;
//    private final AndroidPushNotificationsService androidPushNotificationsService;
//    Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Scheduled(cron = "0 0 8 * * *")        //cron = 초 분 시 일 월 요일
//    public void Notificate() throws JSONException, InterruptedException {
//        System.out.println("notification");
//        AndroidPushPeriodicNotifications androidPushPeriodicNotifications = new AndroidPushPeriodicNotifications(userService, worryService);
//        String notifications = androidPushPeriodicNotifications.PeriodicNotificationJson();
//
//        HttpEntity<String> request = new HttpEntity<>(notifications);
//
//        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
//        CompletableFuture.allOf(pushNotification).join();
//
//
//        try {
//            String firebaseResponse = pushNotification.get();
//        } catch (InterruptedException e) {
//            logger.debug("got interrupted!");
//            throw new InterruptedException();
//        } catch (ExecutionException e) {
//            logger.debug("execution error!");
//        }
//    }
//}
