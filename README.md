# 프로젝트 설정 README

### 프로젝트 환경

- JDK v17
- Springboot v3.2.0
- Mysql v8.0.35
- Gradle v8.5
- 개발 Idle은 VsCode로 통일

### vscode extension

- springboot 관련 extension 찾아서 설치하셈..

### vscode launch.json

- 아래 json을 launch.json에 추가하고, 개발 시 Spring Boot-TravelApplication으로 로컬실행
  {
  "type": "java",
  "name": "Spring Boot-TravelApplication",
  "request": "launch",
  "mainClass": "travel.plan.TravelApplication",
  "projectName": "trvbe",
  "args": "--debug=false --spring.profiles.active=local ",
  "vmArgs": " -Dspring.profiles.active=local ",
  // "envFile": "${workspaceFolder}/.env.local"
  },

### 형상관리 branch 전략

- git branch는 개발계 main으로 사용. 기능 구현시 기능 별 feature/{기능}\_{버전} 생성하여 개발하고, main에 merge한다.

### swagger ui path (api list)

- url : {{domain}}/api-docs

### 공통응답 목업 적용

{
code: String,
message: String,
body: List<?> or Map<?> or String
}

- 위 형식으로 공통응답부 사용
- Controller 혹은 ServiceImpl에서 응답 리턴 시, 아래와 같이 사용
  return ApiResult.getHashMap(ApiStatus.{{응답상태}});
- body에 들어갈 데이터타입 추가 필요 시 ApiResult.java에 메서드추가하시면 됩니다.
