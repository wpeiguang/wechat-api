package io.github.biezhi.wechat;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.api.request.HttpRequest;
import io.github.biezhi.wechat.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HelloBot extends WeChatBot {

    boolean first = true;
    List<String> msgList = Arrays.asList("就知道你会说：", "就知道你又这样说：", "来自有道的翻译：");

    public HelloBot(Config config) {
        super(config);
    }

    HttpRequest httpRequest = new HttpRequest(null, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36 Maxthon/5.2.4.3000", null);

    @Bind(msgType = MsgType.TEXT)
    public void handleText(WeChatMessage message) {
//        String response = "";
//        if(first){
//            response = "秒回有没有";
//            first = false;
//        }else {
//            response = msgList.get((int) (Math.random()*3))+message.getText();
//        }

//        log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
        if(message.getName().equals("疯疯臭臭")) {
            log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
            String param = "key=6ad8b4d96861f17d68270216c880d5e3&info="+message.getText();
            String response = httpRequest.sendGet("http://www.tuling123.com/openapi/api", param);
            JSONObject resJson = new JSONObject(response);
            if( resJson.getDouble("code") == 100000){
                this.sendMsg(message.getFromUserName(), resJson.getString("text"));
            }

//                String msg = "";
//                for(int i = 0; i < 10; i++){
//                    msg = msg+"哈";
//                    this.sendMsg(message.getFromUserName(), msg);
//                }
//                this.sendMsg(message.getFromUserName(), response);
//
        }
    }

    public static void main(String[] args) {
        new HelloBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }

}
