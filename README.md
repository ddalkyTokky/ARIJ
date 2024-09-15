# AJIR

# [PPT 자료 보기](https://github.com/AJIR-ARIJ/ARIJ/blob/dev0.0.0/src/main/resources/PPT.pdf)
# [시연 영상 보기](https://www.youtube.com/watch?v=byN59cJxvIg)

---
## 1. 와이어 프레임

[와이어 프레임 상세 보기](https://docs.google.com/spreadsheets/d/1UaHRtKKUmxIfd28su0GRdlhPRpmZt7UbsHaCK5YWFFU/edit?gid=0#gid=0)

![](https://github.com/AJIR-ARIJ/ARIJ/blob/dev0.0.0/src/main/resources/img/%EC%99%80%EC%9D%B4%EC%96%B4%20%ED%94%84%EB%A0%88%EC%9E%84.png)


---
## 2-1. DBMS ERD

![](https://github.com/AJIR-ARIJ/ARIJ/blob/dev0.0.0/src/main/resources/img/ERD.png)

---

## 2-2. DBMS DDL

<details>
<summary> DDL </summary>
<div markdown="1">

```
CREATE TYPE pr AS ENUM ('EMERGENCY', 'URGENT', 'NORMAL');
CREATE TYPE ro AS ENUM ('USER', 'LEADER', 'ADMIN');
CREATE TYPE ws AS ENUM ('TODO', 'INPROGRESS', 'DONE');

CREATE TABLE issue (
 id BIGSERIAL PRIMARY KEY NOT NULL,
 member_id BIGINT NOT NULL,
 team_id BIGINT NOT NULL,
 title VARCHAR(200) NOT NULL,
 content VARCHAR(1000) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 deleted_at TIMESTAMP,
 deleted BOOLEAN NOT NULL,
 priority pr NOT NULL,
 working_status ws NOT NULL
);

CREATE TABLE member (
 id BIGSERIAL PRIMARY KEY NOT NULL,
 team_id BIGINT NOT NULL,
 role ro NOT NULL,
 email VARCHAR(100) NOT NULL UNIQUE,
 pw1 VARCHAR(64) NOT NULL,
 pw2 VARCHAR(64) NOT NULL,
 pw3 VARCHAR(64) NOT NULL,
 nickname VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE team (
 id BIGSERIAL PRIMARY KEY NOT NULL,
 name VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE comment (
 id BIGSERIAL PRIMARY KEY NOT NULL,
 issue_id BIGINT NOT NULL,
 member_id BIGINT NOT NULL,
 content VARCHAR(1000) NOT NULL,
 created_at TIMESTAMP NOT NULL
);

ALTER TABLE comment ADD CONSTRAINT FK_issue_TO_comment_1 FOREIGN KEY (
 issue_id
)
REFERENCES issue (
 id
)
ON DELETE CASCADE;

ALTER TABLE comment ADD CONSTRAINT FK_member_TO_comment_1 FOREIGN KEY (
 member_id
)
REFERENCES member (
 id
);

ALTER TABLE issue ADD CONSTRAINT FK_member_TO_issue_1 FOREIGN KEY (
 member_id
)
REFERENCES member (
 id
);

ALTER TABLE issue ADD CONSTRAINT FK_team_TO_issue_1 FOREIGN KEY (
 team_id
)
REFERENCES team (
 id
);

ALTER TABLE member ADD CONSTRAINT FK_team_TO_member_1 FOREIGN KEY (
 team_id
)
REFERENCES team (
 id
);
```

</div>
</details>

---
## 3. API 명세서

[API 명세서 상세 보기](https://docs.google.com/spreadsheets/d/1UaHRtKKUmxIfd28su0GRdlhPRpmZt7UbsHaCK5YWFFU/edit?usp=sharing )

![](https://github.com/AJIR-ARIJ/ARIJ/blob/dev0.0.0/src/main/resources/img/API%20%EB%AA%85%EC%84%B8%EC%84%9C.png)
