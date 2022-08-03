# jpa pageable

This module is to demonstrate how spring data's `Pageable` works. It works as I expected.

You specify the page number (starting from 0) and the page size you need using,
```java
Pageable pageable = PageRequest.of(pageNumber, size);
```

and pass it to the DAO method. That's it. Spring data takes care of rest of it. In addition to the results, spring
data also returns `totalPages` and `numberOfElements` which tells you how many pages are there and what is the
total number of elements for the query. 