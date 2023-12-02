package travel.cmn;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class LogAspect {

    /**
     * 클래스 진입 로그
     * @param jp
     */
    @Before(value = "execution(* credos.com..*.*Controller(..)) or execution(* credos..*.*ServiceImpl.*(..))")
    public void startLog(JoinPoint jp) {

        log.debug("========== " + jp.getSignature().getDeclaringType().getName() + "." + jp.getSignature().getName()
                + " ==========");
    }
}