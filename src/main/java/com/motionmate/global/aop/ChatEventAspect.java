package com.motionmate.global.aop;
import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.service.redis.RedisPublisher;
import com.motionmate.service.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ChatEventAspect {


    private final RedisPublisher redisPublisher;

    @Pointcut("execution(* com.motionmate.service.chat.ChatRoomService.enterRoom(..)) || " +
            "execution(* com.motionmate.service.chat.ChatRoomService.disconnectFromRoom(..)) || " +
            "execution(* com.motionmate.service.chat.ChatRoomService.exitRoom(..))")
    private void enterOrDisconnectOrExitMethods() {}


    @Around("enterOrDisconnectOrExitMethods()")
    public void handleChatOnlineUserEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("<<<<>>>>입장메서드 실행해서 AOP실행");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName=signature.getMethod().getName();
        //String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Long roomId=Long.valueOf(String.valueOf(args[0]));
        String nickname=String.valueOf(args[1]);

        for (Object arg : args) {
            System.out.println("@@@arg: " + arg);
        }

        joinPoint.proceed();
        String content="님이 채팅방을 나갔습니다.";
        ChatMessage.MessageType messageType=ChatMessage.MessageType.LEAVE;
        if (methodName.equals("enterRoom")) {
            content = "님이 채팅방에 입장하였습니다!";
            messageType = ChatMessage.MessageType.ENTER;
        } else if (methodName.equals("exitRoom")) {
            content = "님이 채팅방에서 탈퇴하였습니다.";
            messageType = ChatMessage.MessageType.EXIT;
        }
        redisPublisher.publish(ChatMessageRequestDto.builder()
                        .type(messageType)
                        .message(nickname+content)
                        .chatRoomId(roomId)
                        .senderNickname(nickname)
                .build());

    }

}
