
### :snippets: ../../target/generated-snippets
#### 스니펫(API 요청, 응답 등의 정보 토막)생성 경로
[snippets](../../target/generated-snippets)

## 아래는 작성 예시
[admin_categories.adoc](admin/category/admin_categories.adoc) 

:categories: {snippets}/admin/categories <sup id="a1">[1](#footnote_1)</sup>  
[categories](../../target/generated-snippets/admin/categories)

=== 메뉴에 사용할 카테고리 계층 조회 <sup id="a2">[2](#footnote_2)</sup>  
:adoc: {categories}/test_RetrieveCategoryStep  <sup id="a3">[3](#footnote_3)</sup>  
[adoc](../../target/generated-snippets/admin/categories/test_RetrieveCategoryStep)  
include::{adoc}/curl-request.adoc[]  
include::{adoc}/http-request.adoc[]  
include::{adoc}/http-response.adoc[]  
include::{adoc}/httpie-request.adoc[]  
include::{adoc}/request-body.adoc[]  
include::{adoc}/response-body.adoc[]  

=== 카테고리 생성  
:adoc: {categories}/testCreateCategory_ifCreateRequestIncludeNullOrBlank_throwsValidationFailedException  
[adoc](../../target/generated-snippets/admin/categories/testCreateCategory_ifCreateRequestIncludeNullOrBlank_throwsValidationFailedException)  
include::{adoc}/curl-request.adoc[]  
include::{adoc}/http-request.adoc[]  
include::{adoc}/http-response.adoc[]  
include::{adoc}/httpie-request.adoc[]  
include::{adoc}/request-body.adoc[]  
include::{adoc}/response-body.adoc[]  

<a id="footnote_1">1</a>: 테스트 클래스 폴더   [↩](#a1)
<a id="footnote_2">2</a>: 테스트 메소드의 간략한 설명  [↩](#a2)  
<a id="footnote_3">3</a>: 테스트 메소드 별로 생성되는 snippets 경로 지정 [↩](#a3)
    
    :categories: {snippets}/admin/categories ≒ categories = snippets + "/admin/categories"
    :adoc: {categories}/test_RetrieveCategoryStep  ≒ adoc = categories + "/test_RetrieveCategoryStep"
    정도로 이해할 수 있음. 위와 같은 방법으로 adoc을 재정의 하는 방식으로 include 를 보다 편하게 수행할 수 있다.


## 각 도메인별 adoc 작성 후

[index.adoc](index.adoc) 파일에 해당하는 파일을
include::admin_categories.adoc[] 와 같이 추가하면 적용