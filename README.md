# toyProject-advertisingPlatform

Email: b635032_@daum.net

이 프로젝트는 Spring 프레임워크와 JPA 기술 스택을 연마하기 위해 제작된 토이 프로젝트이다.

이커머스 업체가 상품을 광고할 때 필요한 비즈니스 서비스와 API를 임의로 가정하여 구현하였다.

## 1. 환경설정
+ Java 8
+ Spring Boot Version 2.6.8
+ Gradle
+ H2 Database
+ JPA
+ Swagger
+ Web
+ Lombok
+ Validation
+ Spring Batch


## 2. 다이어그램 및 엔티티 설명

![ER다이어그램](https://user-images.githubusercontent.com/62477958/174471125-101f7a93-6b06-409d-af4e-be1e4906e662.png)

### 2.1 Company

Company는 Item(상품), Advertisement(광고), 그리고 Contract(계약)을 관리하는 주체이다.

**Company: (companyId, companyName, serialNumber, telNumber, address)**


### 2.2 Item

Item은 Company를 통해서만 생성될 수 있는 엔티티이다. Item과 Company는 N:1 관계를 갖는다.

**Item: (itemId, itemName, price, stock, companyId(fk))**

### 2.3 Contract

Contract는 Company를 통해서만 생성될 수 있는 엔티티이다. Contract와 Company는 N:1 관계를 갖는다.

Company의 Contract들은 서로 계약기간이 중복될 수 없다.

**Contract: (contractId, startDate, endDate, companyId(fk))**

### 2.4 Advertisement

Advertisement는 Company가 등록한 Item을 광고한다는 정보를 갖는 엔티티이다. Advertisement 생성 시 2가지 조건이 필요하다.

+ 1. Company는 Contract가 존재한다.
+ 2. 광고되는 Item은 Company가 등록한 것이다.

**Advertisement: (advertisementId, biddingPrice, companyId(fk), ItemId(fk))**

### 2.5 AdvertisementBilling

AdvertisementBilling은 고객이 광고를 클릭할 때 발생하는 정보를 저장하는 엔티티이다. 

AdvertisementBilling과 Advertisement는 N:1 관계이다.

**AdvertisementBilling: (advertisementBillingId, clickTime, biddingPrice, advertisementId(fk))**

## 3. 주요 비즈니스 서비스

### 3.1 Company

#### 3.1.1 Company 등록

#### 3.1.2 Item 등록

#### 3.1.3 Item 조회

#### 3.1.4 Contract 등록

***

### 3.2 Advertisement

#### 3.2.1 Advertisement 등록

광고하는 Item과 Company가 서로 연관되었는지 확인이 필요하다.

#### 3.2.2 Advertisement 전시

등록된 전체 광고 중 재고(stock)가 존재하고, 입찰가격(biddingPrice)이 높은 상위 3개를 Json 형태로 반환한다.

Json Response에 Company와 Item에 대한 정보를 추가하기 위해 Fetch Join이 필요하며,

재고가 존재하는 상위 광고 3개를 조회하기 위해, JPQL에 적절한 Where와 Order By를 추가해야한다. 

***

### 3.3 AdvertisementBilling

#### 3.3.1 AdvertisementBilling 저장

#### 3.3.2 AdvertisementBilling 집계

저장된 AdvertisementBilling을 일별 집계한다.

집계를 위해서는 AdvertisementBilling의 (광고ID, 입찰가격, 광고클릭시간), Advertisement의 (광고 ID), Company의 (업체ID, 업체명), Item의 (상품ID, 상품명)이 필요하다.

AdvertisementBilling와 Advertisement는 광고ID를 통해 조인할 수 있고,

Advertisement와 Company는 업체ID를 통해 조인할 수 있고,

Advertisement와 Item은 상품ID를 통해 조인할 수 있다.

적절히 조인하여 원하는 Attribute를 가져와 일별 집계 데이터를 생성할 수 있다.





