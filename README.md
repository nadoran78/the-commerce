The Commerce Toy project
----
### 기본세팅
  - DB : H2 데이터베이스(In Memory)
    - 적용배경 : 별도의 설정없이 즉시 사용하기 위해 In Memory DB를 사용
    - 사용방법(저장된 데이터 확인방법)
      1) 어플리케이션 구동 후 localhost:8080/h2-console 접속
      2) 아래 그림과 같이 입력 후 Connect 클릭(username: sa, password: 없음)

    ![img.png](doc%2Fimage%2Fimg.png)
  - CI 적용: main, develop 브랜치 push 및 pull request 작성 시 빌드 및 테스트
  - swagger 적용: api 명세 확인 목적
    - 어플리케이션 구동 후 http://localhost:8080/swagger-ui/index.html 접속하여 api 명세서 확인
  - 예외 처리: CustomException 을 별도로 만들어 GlobalExceptionHandler 적용

### 회원가입(/api/user/join)
  - POST 방식으로 Request Body에 파라미터 입력하여 요청(회워id, 비밀번호, 닉네임, 이름, 전화번호, 이메일)
  - 요청 파라미터가 형식에 맞지 않을 경우 예외 발생
    - 회원id: 영문과 숫자로 구성된 2~10자
    - 비밀번호: 영문 대 소문자, 숫자, 특수문자를 함께 사용한 8~16자
    - 닉네임: 숫자, 특수문자를 제외한 6~10자
    - 이름: 숫자, 특수문자를 제외한 2~10자
    - 전화번호: 휴대폰 번호 형식
    - 이메일: 이메일 형식
  - 기존 가입한 회원 id일 경우 예외 발생
  - DB에 저장 후 201 응답코드 반환

### 회원 목록 조회(/api/user/list)
  - GET 및 쿼리스트링 방식으로 요청(페이징 처리)
  - 가입일 순/ 이름 순 정렬하여 반환
  - 페이지 형식으로 반환(id, 닉네임, 이름, 전화번호, 이메일 마스킹 처리하여 반환)

### 회원 정보 수정(/api/user/{로그인 아이디})
  - PATCH 방식으로 Request Body에 수정할 파라미터만 입력하여 요청
  - 로그인 아이디를 입력받아 회원 기가입 여부 확인(미가입 시 예외 발생)
  - 로그인 아이디와 입력받은 파라미터를 통해 본인 회원정보 수정
  - 수정한 회원정보 전체 반환(회원id, 닉네임, 이름, 전화번호, 이메일)