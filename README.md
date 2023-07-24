# thing-order

## 설명

thing-order 프로젝트는 키덜트 이커머스 서비스 띵고의 주문 API를 Spring Boot와 JPA로 구현한 프로젝트 입니다.
이 프로젝트를 진행하게 된 계기는 아래와 같습니다.

1. 기존 띵고의 API 서버는 싱글 모듈의 단점을 해소.
2. Node.js 기반 웹 프레임워크인 Nest.js를 Spring Boot 포팅하는 것을 시뮬레이션 하기 위함.
3. 테스트 코드를 추가하여 API 서버의 안정성을 더하기 위함.
4. Java, Spring Boot 그리로 JPA를 학습하기 위함.

## 사용 기술 및 환경

- Java
- Spring Boot
- JPA
- PostgreSQL
- Gradle

## 실행 방법

> 띵고 서비스의 개발 DB 접근을 막기 위해 jasypt를 이용해 application.yml의 DB 정보를 암호화했습니다.
> jasypt_password를 모를 경우에는 해당 프로젝트를 실행할 수 없습니다.

```shell
$ ./gradlew build -Pjasypt.encryptor.password='jasypt_password'
$ java -jar ./web/build/libs/web-1.0.jar --jasypt.encryptor.password='jasypt_password'
```

## 프로젝트 디렉토리 구조

```text
.
├── README.md
├── build
│   ├── libs
│   └── tmp
├── build.gradle
├── common
│   ├── build
│   ├── build.gradle
│   ├── out
│   └── src
├── gradle
│   └── wrapper
├── gradlew
├── gradlew.bat
├── settings.gradle
└── web
    ├── build
    ├── build.gradle
    ├── out
    └── src
```

## ERD

[ERD 링크]()
![image](https://github.com/hwibaski/java-problem-solving/assets/85930725/ed2e66f9-016e-4b04-a629-c4230527e9a0)
