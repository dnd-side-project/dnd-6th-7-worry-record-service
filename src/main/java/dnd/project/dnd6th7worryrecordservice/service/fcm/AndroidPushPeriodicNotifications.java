package dnd.project.dnd6th7worryrecordservice.service.fcm;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import dnd.project.dnd6th7worryrecordservice.service.WorryService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class AndroidPushPeriodicNotifications {
    public final UserService userService;
    public final WorryService worryService;

    public String PeriodicNotificationJson() throws JSONException {

        List<User> allUser = userService.findAllUser();
        List<String> worryExpiryUserDeviceTokenList = new ArrayList<>();    //만료된 걱정이 있는 유저의 DeviceToken List

        LocalDate today = LocalDate.now();
        LocalDateTime afterDate = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0, 0);
        LocalDateTime beforeDate = afterDate.plusDays(1);

        // 만료된 걱정이 있는 유저의 deviceToken을 List에 저장
        for (User user : allUser) {
            List<Worry> worryList = worryService.findWorryByUser(user);
            for (Worry worry : worryList) {
                if (worry.getWorryExpiryDate().isAfter(afterDate) && worry.getWorryExpiryDate().isBefore(beforeDate)) {
                    worryService.turnWorryLockState(worry.getWorryId());
                    if (user.getDeviceToken() != null && user.getDeviceToken() != "") {
                        System.out.println("디바이스 토큰 add");
                        worryExpiryUserDeviceTokenList.add(user.getDeviceToken());
                        break;
                    }
                }
            }
        }

        if(worryExpiryUserDeviceTokenList.isEmpty())
            return null;
        else{
            return returnPushMessage(worryExpiryUserDeviceTokenList);
        }


    }

    //pushMessage 생성하여 return
    private String returnPushMessage(List<String> worryExpiryUserDeviceTokenList) {
        JSONObject body = new JSONObject();
        JSONArray array = new JSONArray();
        for (String token : worryExpiryUserDeviceTokenList) {
            array.put(token);
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();

        try {
            String pushMessage = "보관하신 걱정이 만료되었어요!";
            String titleMessage = "걱정 후기를 남겨주세요";

            pushMessage = URLEncoder.encode(pushMessage, "UTF-8");
            titleMessage = URLEncoder.encode(titleMessage, "UTF-8");
            notification.put("title", titleMessage);
            notification.put("body", pushMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
