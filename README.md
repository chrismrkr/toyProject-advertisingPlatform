# toyProject-advertisingPlatform

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

Company: (companyId, companyName, serialNumber, telNumber, address)


### 2.2 Item

Item은 Company를 통해서만 생성될 수 있는 엔티티이다. Item과 Company는 N:1 관계를 갖는다.

Item: (itemId, itemName, price, stock, companyId(fk))

### 2.3 Contract

Contract는 Company를 통해서만 생성될 수 있는 엔티티이다. Contract와 Company는 N:1 관계를 갖는다.

Company의 Contract들은 서로 계약기간이 중복될 수 없다.

Contract: (contractId, startDate, endDate, companyId(fk))

### 2.4 Advertisement

Advertisement는 Company가 등록한 Item을 광고한다는 정보를 갖는 엔티티이다. Advertisement 생성 시 2가지 조건이 필요하다.

+ 1. Company는 Contract가 존재한다.
+ 2. 광고되는 Item은 Company가 등록한 것이다.

Advertisement: (advertisementId, biddingPrice, companyId(fk), ItemId(fk))

### 2.5 AdvertisementBilling

AdvertisementBilling은 고객이 광고를 클릭할 때 발생하는 정보를 저장하는 엔티티이다. 

AdvertisementBilling과 Advertisement는 N:1 관계이다.

Advertisementbilling: (advertisementBillingId, clickTime, biddingPrice, advertisementId(fk))
