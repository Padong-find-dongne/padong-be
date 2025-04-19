# 1. 경량 Alpine 기반 JDK 17 이미지 사용
FROM openjdk:17-jdk-alpine

# 2. 환경 변수로 JAR 파일 이름 정의
ENV APP_NAME=padong.jar

# 3. 작업 디렉토리 설정
WORKDIR /app

# 4. jar 복사 (Gradle 빌드 결과물)
COPY build/libs/*.jar $APP_NAME

# 5. 실행 명령어 (컨테이너 시작 시 실행)
ENTRYPOINT ["java", "-jar", "/app/padong.jar"]