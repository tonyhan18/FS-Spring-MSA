package dev.practice.order.domain.partner;

import dev.practice.order.common.exception.InvalidParamException;
import dev.practice.order.common.util.TokenGenerator;
import dev.practice.order.domain.AbstractEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Slf4j //로그 -> ?
@Getter // 엔티티에서 setter을 추가하는 건 문제가 많아진다
// -> Getter만 동작하기 위해서는 생성과정에서 초기화가 무조건 들어오게 하면 된다.
@Entity // 엔티티 선언 -> ?
@NoArgsConstructor // Spring JPA를 사용하기 위해서 기본생성자가 필요하다.
@Table(name = "partners") // DB에 정의한 테이블명의 명시적 선언
public class Partner extends AbstractEntity {
    /*
     * 가급적 각각에 맞는 annotation을 선언해 주는 것이 좋다.
     */

    private static final String PREFIX_PARTNER = "ptn_";

    @Id // ?
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ?
    private Long id;
    private String partnerToken; // 시스템의 안정적인 운영을 위한 pk와 동일 수준의 token
    private String partnerName;
    private String businessNo;
    private String email;


    @Enumerated(EnumType.STRING) // DB선언시 enable, disable 영어 자체가 저장됨.
    private Status status;

    @Getter // 속성값이 추가 되었다면 이걸 추가
    @RequiredArgsConstructor
    public enum Status {
        ENABLE("활성화"), DISABLE("비활성화"); //의미가 읽히도록 선언
        private final String description;
    }


    @Builder // 빌더패턴을 사용해서 생성
    public Partner(String partnerName, String businessNo, String email) {
        // 입력 예외 처리 null이 들어오는 경우 error 발생
        if (StringUtils.isEmpty(partnerName)) throw new InvalidParamException("empty partnerName");
        if (StringUtils.isEmpty(businessNo)) throw new InvalidParamException("empty businessNo");
        if (StringUtils.isEmpty(email)) throw new InvalidParamException("empty email");

        this.partnerToken = TokenGenerator.randomCharacterWithPrefix(PREFIX_PARTNER);
        this.partnerName = partnerName;
        this.businessNo = businessNo;
        this.email = email;
        this.status = Status.ENABLE;
    }

    public void enable() {
        this.status = Status.ENABLE;
    }

    public void disable() {
        this.status = Status.DISABLE;
    }
}
