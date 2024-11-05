현대오토에버 모빌리티 스쿨 [영화리뷰 플랫폼]
===

# 💻 프로젝트 정보

## 팀원 소개
<table>
<thead>
<tr>
<th align="center"><strong>박준민</strong></th>
<th align="center"><strong>오정환</strong></th>
<th align="center"><strong>임동연</strong></th>
<th align="center"><strong>고채린</strong></th>
<th align="center"><strong>이유정</strong></th>
</tr>
</thead>
<tbody>
<tr>
<td align="center"><a href="https://github.com/pjm2571"><img src="https://avatars.githubusercontent.com/u/97939207?v=4" height="150" width="150" style="max-width: 100%;"> <br> @pjm2571</a></td>
<td align="center"><a href="https://github.com/OhJeongHwan1"><img src="https://avatars.githubusercontent.com/u/108731616?v=4" height="150" width="150" style="max-width: 100%;"> <br> @OhJeongHwan1</a></td>
<td align="center"><a href="https://github.com/yeon-dong"><img src="https://avatars.githubusercontent.com/u/80156654?v=4" height="150" width="150" style="max-width: 100%;"> <br> @yeon-dong</a></td>
<td align="center"><a href="https://github.com/chaelin2"><img src="https://avatars.githubusercontent.com/u/109078051?v=4" height="150" width="150" style="max-width: 100%;"> <br> @chaelin2</a></td>
<td align="center"><a href="https://github.com/LYJ22"><img src="https://avatars.githubusercontent.com/u/79090053?v=4" height="150" width="150" style="max-width: 100%;"> <br> @LYJ22</a></td>
</tr>
</tbody>
</table>


## 사용자 인증/인가 flow
```mermaid
flowchart TD
    A[요청 시작] --> B[Authorization 헤더에서 JWT 토큰 추출]
    B --> C{JWT 토큰이 있는가?}
    C -- 예 --> D{JWT 토큰이 유효한가?}
    C -- 아니오 --> G[인증 실패 - 요청 거부]
    D -- 예 --> E[Username 추출 후 UserDetails 로드]
    D -- 아니오 --> G[인증 실패 - 요청 거부]
    E --> F[SecurityContext에 Authentication 설정]
    F --> H[요청 처리]
    G --> H[요청 처리]
```
