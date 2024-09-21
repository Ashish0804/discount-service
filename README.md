# Monk Commerce Discount Service

This service provides REST endpoints to manage `Coupons` and evaluate a cart against all possible coupons at runtime.

### Assumptions
* This service assumes the cart and product entities are managed by their own separate domain services. This domain of this service is limited to managing the coupon domain entity, product and cart are treated as foreign entities.
* This service assumes that the AutN and AuthZ are done at the gateway layer.
* This service assumes that you can only apply 1 Coupon. For example there coupons for product A and B exist and the cart contains both A and B, then only one of them can be selected.

### Use Cases
1. **[Implemented]** Creation of a new Coupon.
2. **[Implemented]** Creation of multiple new Coupons.
3. **[Implemented]** Getting all the coupons.
4. **[Implemented]** Getting a coupon with id.
5. **[Implemented]** Deleting a coupon with id.
6. **[Implemented]** Getting all applicable coupons for a cart.
7. **[Implemented]** Applying a specific coupon to a cart.
8. **[Not Implemented]** PUT method (*deprioritized in favour of POST*)
9. **[Not Implemented]** User details (first time?, loyal?, milestone?) based discounts.
10. **[Not Implemented]** Payment Type based discounts.
11. **[Not Implemented]** Seasonal discounts.
12. **[Not Implemented]** Usage Limit or Expiration on coupons.
13. **[Not Implemented]** Analytics.
14. **[Not Implemented]** Notifications on coupons.
15. **[Not Implemented]** Concurrency Handling for usage count.
16. **[Not Implemented]** Decoupling objects internally with the request objects.
17. **[Not Implemented]** Free product discount.
18. **[Not Implemented]** Pagination to APIs.

### Examples
#### GET /coupons
Response:
```
[
    {
        "id": 4254458416752905633,
        "type": "PRODUCT",
        "discount": 0.5,
        "productId": 1
    },
    {
        "id": 8989409639447348072,
        "type": "CART_VALUE",
        "discount": 0.5,
        "minCartValue": 1000.0
    },
    {
        "id": 3058698387227953112,
        "type": "BUY_X_GET_Y",
        "requiredProductIds": [
            1,
            2
        ],
        "requiredProducts": 2,
        "freeProductIds": [
            3,
            4
        ],
        "freeProducts": 1
    }
]
```

#### GET /coupon/{id}
Response (Coupon Exists):
```
{
    "id": 4254458416752905633,
    "type": "PRODUCT",
    "discount": 0.5,
    "productId": 1
}
```

Response (Coupon Doesn't Exist):
```
400: Bad Request
```

#### POST /coupon
Request: 
```
{
    "type": "PRODUCT",
    "discount": 0.5,
    "productId": 12
}
```

Response (Coupon Id):
```
3695691894844706074
```

Request (invalid schema):
```
{
    "type": "PRODUCT",
    "discount": -1.0,
    "productId": 12
}
```

Response:
```
{
    "timestamp": "2024-09-21T09:31:20.404+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/v1/coupon"
}
```

#### POST /coupons

Request:
```
[
    {
    "type": "PRODUCT",
    "discount": 0.5,
    "productId": 12
},
{
    "type": "PRODUCT",
    "discount": 0.2,
    "productId": 1
}
]
```

Response:
```
[
    1261786151684425124,
    5223562988408424163
]
```

#### DELETE /coupon/{id}
Response:
```
{
    "id": 5223562988408424163,
    "type": "PRODUCT",
    "discount": 0.2,
    "productId": 1
}
```

Response (Coupon doesn't exist):
```
400: Bad Request
```

#### POST /evaluate
Request:
```
{
    "cart": {
    "items": [
        {"id": 1, "value": 10.0, "quantity": 10},
        {"id": 2, "value": 100.0, "quantity": 20}
    ]
}
}
```

Response:
```
{
    "1926902968298981064": 1050.0,
    "6649925179862827746": 50.0
}
```

#### POST /apply/{id}
Request: `0.0.0.0:8080/v1/apply/1926902968298981064`
```
{
    "cart": {
    "items": [
        {"id": 1, "value": 10.0, "quantity": 10},
        {"id": 2, "value": 100.0, "quantity": 20}
    ]
}
}
```

Response:
```
1050.0
```

Request (Coupon doesn't exist): `0.0.0.0:8080/v1/apply/1926`
```
{
    "cart": {
    "items": [
        {"id": 1, "value": 10.0, "quantity": 10},
        {"id": 2, "value": 100.0, "quantity": 20}
    ]
}
}
```

Response:
```
400: Bad Request
```


### Enhancements

#### Coupon Expiration and count limit
The Coupon can be further enhanced by storing an expiration date and a count limit. 
This can be done by adding to the entity 2 new fields: 
```java
DateTime expirationDate;
Long usageCount;
```
Then at the time of applying a coupon, we just have to check:
```
if (!(usageCount < 0) || (DateTime.now() > expirationDate)) {
    throw new ExpiredCouponException("Coupon is no longer valid");
        }
```

#### Using a persistence layer
Due to time constraints, I have not implemented a persistence layer yet. I'm storing the data in memory using a hashmap.
The Dao is written against an interface and can be easily implemented to use an actual DB. 
My DB of choice would be a NoSql DB as the coupon structure is not same. 
Given Monk is already using AWS, I'd implement the persistence layer with DynamoDb with coupon id as partition key.

#### Using proper HTTP error codes for custom exceptions
Due to time constraints, I am always returning a Bad Request in case of any errors. This can be enhanced by returning contextual error messages with proper error codes.

#### Integration Tests
Due to time constraints, I have not written integration tests for the APIs.

#### Giving Detailed cart state for the coupon
Currently, I'm just returning the raw discount value due to time constraints. 
This can be made more contextual by returning the discount applied on each product. 
I feel the current implementation is a good starting point and meets the functional requirement of computing discount and saving customers money.

#### Adding analytics and notifications for coupons
This service can be broken into 2 services: one to manage the coupon entity (responsible for providing CRUD APIs, analytics into coupon usage patterns and notifications) and another to evaluate a transaction for the discount. 
