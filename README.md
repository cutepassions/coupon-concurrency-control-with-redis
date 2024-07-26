# 쿠폰 발급 동시성 제어 (Redis)
분산 환경에서 동시성을 제어하기 위해 Redis를 사용한 코드입니다.

![아키텍처](image.png)

위 이미지와 같은 아키텍처로 구성되어 있습니다.  
사용을 위해서는 Eureka Service > gateway > Redis Distributed Lock 순으로 실행하면 됩니다.

실행을 위해서 Redis와 Mysql이 설치되어 있어야 합니다.


<!-- 관련 포스트는 [여기](https://velog.io/@cutepassions/JPA%EB%A1%9C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4%ED%95%98%EA%B8%B0)에서 확인할 수 있습니다. -->
