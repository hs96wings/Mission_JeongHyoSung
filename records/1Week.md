# NWeek_JeongHyoSung.md

## Title: [1Week] 정효성

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감상대 삭제
  - [x] 등록되지 않은 호감상대는 삭제할 수 없다 
  - [x] 삭제버튼을 누르면 호감 상대가 삭제되어야 한다
  - [x] 삭제하기 전 소유권이 본인에게 있는지 체크
  - [x] 삭제 후 다시 호감목록 페이지로 돌아온다
- [ ] 구글 로그인
  - [ ] OAuth2로 구현

### 1주차 미션 요약

---

**[접근 방법]**

- 테스트 코드를 만들기 위해 WHEN은 t001에서, THEN은 t003에서 가져왔다
```Java
// WHEN
// t001에서 가져온 코드를
ResultActions resultActions = mvc
        .perform(get("/likeablePerson/add"))
        .andDo(print());
// t006으로 바꿔주었다
ResultActions resultActions = mvc
        .perfome(get("/likeablePerson/delete/1"))
        .andDo(print());

// THEN
// t003에서 가져온 코드를
resultActions
  .andExpect(handler().handlerType(LikeablePersonController.class))
  .andExpect(handler().methodName("add"))
  .andExpect(status().is3xxRedirection());
// t006으로 바꿔주었다
resultActions
  .andExpect(handler().handlerType(LikeablePersonController.class))
  .andExpect(handler().methodName("delete"))
  .andExpect(status().is3xxRedirection());
```
삭제하고 다시 목록으로 돌아가야하므로 3xxRedirection를 사용했다

- 소유자 체크
```java
// LikeablePersonService.java에
// findById를 만들어 삭제할 호감대상을 가져올 수 있게 한다  
public LikeablePerson findById(long id) {
    return likeablePersonRepository.findById(id).orElse(null);
}

// LikeablePersonController.java
// 소유자가 아니라면
if (likeablePerson.getFromInstaMember() != rq.getMember().getInstaMember()) {
    return rq.redirectWithMsg("/likeablePerson/list", "소유자가 아닙니다");
}
```
likeablePerson.getFromInstaMember()과 rq.getMember().getInstaMember()를 비교해서
두 값이 다르면 소유자가 아니므로 에러메시지를 쓰고 리턴시킨다

- 없는 데이터 체크
findById를 만들어 줄 때 값이 없으면 orElse(null)로 null 값을 반환해주었으니
```java
// 없는 데이터라면
if (likeablePerson == null) {
    return rq.redirectWithMsg("/likeablePerson/list", "유효하지 않는 데이터입니다");
}
```
likeablePerson이 null값이라면 에러메시지를 주고 리턴시킨다

- 호감상대 삭제
```java
// LikeablePersonController.java 에선 createRsData를 쓰고 Service에 삭제 요청을 한다
RsData<LikeablePerson> createRsData = likeablePersonService.delete(likeablePerson);
// LikeablePersonService.java
// Transactional은 select SQL이 아니므로 꼭 붙여주어야 한다
@Transactional
public RsData<LikeablePerson> delete(LikeablePerson likeablePerson) {
    likeablePersonRepository.delete(likeablePerson);

    return RsData.of("S-1", "삭제되었습니다");
}
```
@Transactional은 delete가 select SQL이 아니기 때문에 꼭 붙여주어야 한다
Repository에 삭제요청을 하고 성공 메시지를 반환해준다

**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.

  **참고: [Refactoring]**

    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.